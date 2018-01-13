package com.retrocraft.entity.teleportpipe;

import java.util.Random;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.block.BlockTileEntity;
import com.retrocraft.client.ClientTeleportPipes;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTeleportPipe extends BlockTileEntity<TileTeleportPipe>
{

  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockTeleportPipe(String name)
  {
    super(Material.ROCK, name);

//    setRegistryName(RetroCraft.modId, "teleportPipe");
//    setUnlocalizedName(getRegistryName().toString());
    setHardness(5f);
    setResistance(2000f);
  }

  @Override
  public BlockTeleportPipe setCreativeTab(CreativeTabs tab) {
    super.setCreativeTab(tab);
    return this;
  }
  
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
      new ModelResourceLocation(getRegistryName(), "inventory"));
  }
  
  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    int meta = state.getValue(FACING).getIndex();
    return meta;
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    EnumFacing facing = EnumFacing.getFront(meta & 7);
    if (facing.getAxis() == EnumFacing.Axis.Y)
    {
      facing = EnumFacing.NORTH;
    }
    return getDefaultState().withProperty(FACING, facing);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state)
  {
    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state)
  {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getPlayerRelativeBlockHardness(IBlockState state,
      EntityPlayer player, World world, BlockPos pos)
  {
    if (RetroCraft.getConfig().creativeModeOnly
        && !player.capabilities.isCreativeMode)
    {
      return -1f;
    }
    TileTeleportPipe tileTeleportPipe = (TileTeleportPipe) getTileEntity(world, pos);
    if (tileTeleportPipe != null && tileTeleportPipe.isGlobal()
        && !player.capabilities.isCreativeMode)
    {
      return -1f;
    }
    return super.getPlayerRelativeBlockHardness(state, player, world, pos);
  }

//  @Override
//  public boolean canPlaceBlockAt(World world, BlockPos pos)
//  {
//    Block blockBelow = world.getBlockState(pos.down()).getBlock();
//    if (blockBelow == this)
//    {
//      return false;
//    }
//    Block blockAbove = world.getBlockState(pos.up(2)).getBlock();
//    return blockAbove != this && 
//        super.canPlaceBlockAt(world, pos) && 
//        world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up());
//  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
      EntityLivingBase placer, ItemStack stack)
  {
    EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
    if (facing.getAxis() == EnumFacing.Axis.Y)
    {
      facing = EnumFacing.NORTH;
    }
    
    world.setBlockState(pos, this.getDefaultState().withProperty(FACING, facing));
    
    if (!world.isRemote && placer instanceof EntityPlayer
        && (!RetroCraft.getConfig().creativeModeOnly
            || ((EntityPlayer) placer).capabilities.isCreativeMode))
    {
      TileTeleportPipe tileTeleportPipe = (TileTeleportPipe) getTileEntity(world, pos);
      if (tileTeleportPipe != null)
      {
        tileTeleportPipe.setOwner((EntityPlayer) placer);
        ((EntityPlayer) placer).openGui(RetroCraft.instance,
            ModGuiHandler.TELEPORT, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
  }

//  @Override
//  public void breakBlock(World world, BlockPos pos, IBlockState state)
//  {
//    TileTeleportPipe tileTeleportPipe = getTileTeleportPipe(world, pos);
//    if (tileTeleportPipe != null && tileTeleportPipe.isGlobal())
//    {
//      GlobalTeleportPipes.get(world)
//          .removeGlobalTeleportPipe(new TeleportPipeEntry(tileTeleportPipe));
//    }
//    super.breakBlock(world, pos, state);
//  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
      EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX,
      float hitY, float hitZ)
  {
    if (player.isSneaking() && (player.capabilities.isCreativeMode
        || !RetroCraft.getConfig().creativeModeOnly))
    {
      if (!world.isRemote)
      {
        TileTeleportPipe tileTeleportPipe = getTileEntity(world, pos);
        if (tileTeleportPipe == null)
        {
          return true;
        }
        if (RetroCraft.getConfig().restrictRenameToOwner
            && !tileTeleportPipe.isOwner(player))
        {
          player.sendStatusMessage(
              new TextComponentTranslation("retrocraft:notTheOwner"), true);
          return true;
        }
        if (tileTeleportPipe.isGlobal() && !player.capabilities.isCreativeMode)
        {
          player.sendStatusMessage(
              new TextComponentTranslation("retrocraft:creativeRequired"),
              true);
          return true;
        }
        player.openGui(RetroCraft.instance, ModGuiHandler.TELEPORT, world,
            pos.getX(), pos.getY(), pos.getZ());
      }
      return true;
    }
    TileTeleportPipe tileTeleportPipe = getTileEntity(world, pos);
    if (tileTeleportPipe == null)
    {
      return true;
    }
    TeleportEntry knownTeleportPipe = world.isRemote
        ? ClientTeleportPipes.getKnownTeleportPipe(tileTeleportPipe.getTeleportPipeName())
        : null;
    if (knownTeleportPipe != null)
    {
      RetroCraft.proxy.openTeleportPipeSelection(EnumHand.MAIN_HAND, knownTeleportPipe);
    } else if (!world.isRemote)
    {
      TeleportEntry teleportPipe = new TeleportEntry(tileTeleportPipe);
      if (!TeleportManager.checkAndUpdateTeleportPipe(player, teleportPipe))
      {
        TextComponentString nameComponent = new TextComponentString(
            tileTeleportPipe.getTeleportPipeName());
        nameComponent.getStyle().setColor(TextFormatting.WHITE);
        TextComponentTranslation chatComponent = new TextComponentTranslation(
            "retrocraft:activatedTeleportPipe", nameComponent);
        chatComponent.getStyle().setColor(TextFormatting.YELLOW);
        player.sendMessage(chatComponent);
      }

      TeleportManager.removePlayerTeleportPipe(player, teleportPipe);
      TeleportManager.addPlayerTeleportPipe(player, teleportPipe);
      TeleportManager.sendPlayerTeleportPipes(player);
    } else
    {
      RetroCraft.proxy.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, pos, 1f);
      for (int i = 0; i < 32; i++)
      {
        world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
            pos.getX() + 0.5 + (world.rand.nextDouble() - 0.5) * 2,
            pos.getY() + 3,
            pos.getZ() + 0.5 + (world.rand.nextDouble() - 0.5) * 2, 0, -5, 0);
        world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
            pos.getX() + 0.5 + (world.rand.nextDouble() - 0.5) * 2,
            pos.getY() + 4,
            pos.getZ() + 0.5 + (world.rand.nextDouble() - 0.5) * 2, 0, -5, 0);
      }
    }
    return true;
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos,
      Random rand)
  {
    if (!RetroCraftConfig.disableParticles && rand.nextFloat() < 0.75f)
    {
      TileTeleportPipe tileTeleportPipe = getTileEntity(world, pos);
      if (tileTeleportPipe == null)
      {
        return;
      }
      if (ClientTeleportPipes
          .getKnownTeleportPipe(tileTeleportPipe.getTeleportPipeName()) != null)
      {
        world.spawnParticle(EnumParticleTypes.PORTAL,
            pos.getX() + 0.5 + (rand.nextDouble() - 0.5) * 1.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5 + (rand.nextDouble() - 0.5) * 1.5, 0, 0, 0);
//        world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
//            pos.getX() + 0.5 + (rand.nextDouble() - 0.5) * 1.5,
//            pos.getY() + 0.5,
//            pos.getZ() + 0.5 + (rand.nextDouble() - 0.5) * 1.5, 0, 0, 0);
      }
    }
  }

  @Override
  public Class<TileTeleportPipe> getTileEntityClass()
  {
    return TileTeleportPipe.class;
  }

  @Override
  public TileTeleportPipe createTileEntity(World world, IBlockState state)
  {
    return new TileTeleportPipe();
  }
}