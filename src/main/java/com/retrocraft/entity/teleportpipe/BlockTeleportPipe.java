package com.retrocraft.entity.teleportpipe;

import java.util.Random;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.block.BlockTileEntity;
import com.retrocraft.client.ClientWaystones;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockTeleportPipe extends BlockTileEntity<TileTeleportPipe>
{

  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockTeleportPipe(String name)
  {
    super(Material.ROCK, name);

//    setRegistryName(RetroCraft.modId, "waystone");
//    setUnlocalizedName(getRegistryName().toString());
    setHardness(5f);
    setResistance(2000f);
  }

  @Override
  public BlockTeleportPipe setCreativeTab(CreativeTabs tab) {
    super.setCreativeTab(tab);
    return this;
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
    TileTeleportPipe tileWaystone = (TileTeleportPipe) getTileEntity(world, pos);
    if (tileWaystone != null && tileWaystone.isGlobal()
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
      TileTeleportPipe tileWaystone = (TileTeleportPipe) getTileEntity(world, pos);
      if (tileWaystone != null)
      {
        tileWaystone.setOwner((EntityPlayer) placer);
        ((EntityPlayer) placer).openGui(RetroCraft.instance,
            ModGuiHandler.WAYSTONE, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
  }

//  @Override
//  public void breakBlock(World world, BlockPos pos, IBlockState state)
//  {
//    TileWaystone tileWaystone = getTileWaystone(world, pos);
//    if (tileWaystone != null && tileWaystone.isGlobal())
//    {
//      GlobalWaystones.get(world)
//          .removeGlobalWaystone(new WaystoneEntry(tileWaystone));
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
        TileTeleportPipe tileWaystone = getTileEntity(world, pos);
        if (tileWaystone == null)
        {
          return true;
        }
        if (RetroCraft.getConfig().restrictRenameToOwner
            && !tileWaystone.isOwner(player))
        {
          player.sendStatusMessage(
              new TextComponentTranslation("retrocraft:notTheOwner"), true);
          return true;
        }
        if (tileWaystone.isGlobal() && !player.capabilities.isCreativeMode)
        {
          player.sendStatusMessage(
              new TextComponentTranslation("retrocraft:creativeRequired"),
              true);
          return true;
        }
        player.openGui(RetroCraft.instance, ModGuiHandler.WAYSTONE, world,
            pos.getX(), pos.getY(), pos.getZ());
      }
      return true;
    }
    TileTeleportPipe tileWaystone = getTileEntity(world, pos);
    if (tileWaystone == null)
    {
      return true;
    }
    TeleportEntry knownWaystone = world.isRemote
        ? ClientWaystones.getKnownWaystone(tileWaystone.getWaystoneName())
        : null;
    if (knownWaystone != null)
    {
      RetroCraft.proxy.openWaystoneSelection(EnumHand.MAIN_HAND, knownWaystone);
    } else if (!world.isRemote)
    {
      TeleportEntry waystone = new TeleportEntry(tileWaystone);
      if (!TeleportManager.checkAndUpdateWaystone(player, waystone))
      {
        TextComponentString nameComponent = new TextComponentString(
            tileWaystone.getWaystoneName());
        nameComponent.getStyle().setColor(TextFormatting.WHITE);
        TextComponentTranslation chatComponent = new TextComponentTranslation(
            "retrocraft:activatedWaystone", nameComponent);
        chatComponent.getStyle().setColor(TextFormatting.YELLOW);
        player.sendMessage(chatComponent);
      }

      TeleportManager.removePlayerWaystone(player, waystone);
      TeleportManager.addPlayerWaystone(player, waystone);
      TeleportManager.sendPlayerWaystones(player);
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
      TileTeleportPipe tileWaystone = getTileEntity(world, pos);
      if (tileWaystone == null)
      {
        return;
      }
      if (ClientWaystones
          .getKnownWaystone(tileWaystone.getWaystoneName()) != null)
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