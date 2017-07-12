package com.retrocraft.item;

import javax.annotation.Nullable;

import com.retrocraft.FluidHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.recipe.RetrocraftRecipes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ItemWoodenBucket extends Item implements ItemModelProvider
{

  private String      name;
  private final Block isFull;

  public ItemWoodenBucket(String name, Block containedBlock)
  {
    this.setCreativeTab(CreativeTabs.MISC);
    this.setHasSubtypes(true);
    this.setMaxStackSize(1);
    this.setRegistryName(name);
    this.setUnlocalizedName(name);
    this.name = name;

    this.maxStackSize = 1;
    this.isFull = containedBlock;

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public ItemWoodenBucket setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public void registerItemModel(Item item)
  {
    RetroCraft.proxy.registerItemRenderer(this, 0, name);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack)
  {
    if (stack.getItemDamage() > 0
        && stack.getItemDamage() <= FluidHandler.NAMES.length)
    {
      return getUnlocalizedName() + "_"
          + FluidHandler.NAMES[stack.getItemDamage() - 1];
    } else
    {
      return getUnlocalizedName();
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn,
      EntityPlayer playerIn, EnumHand hand)
  {
    boolean flag = this.isFull == Blocks.AIR;

    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if (!worldIn.isRemote && itemStackIn != null)
    {
      RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);

      if (raytraceresult != null
          && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
      {
        BlockPos blockpos = raytraceresult.getBlockPos();

        if (!worldIn.isBlockModifiable(playerIn, blockpos))
        {
          return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
        } else if (flag)
        {
          if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit),
              raytraceresult.sideHit, itemStackIn))
          {
            return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
          } else
          {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            Material material = iblockstate.getMaterial();

            if (material == Material.WATER
                && ((Integer) iblockstate.getValue(BlockLiquid.LEVEL))
                    .intValue() == 0)
            {
              worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
              playerIn.addStat(StatList.getObjectUseStats(this));
              RetroCraft.proxy.playSound(SoundEvents.ITEM_BUCKET_FILL, playerIn.getPosition(), 1.0F);
              return ActionResult.newResult(EnumActionResult.SUCCESS,
                  this.fillBucket(itemStackIn, playerIn, ModItems.woodenWaterBucket));
            } else if (material == Material.LAVA
                && ((Integer) iblockstate.getValue(BlockLiquid.LEVEL))
                    .intValue() == 0)
            {
              RetroCraft.proxy.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, playerIn.getPosition(), 1.0F);
              playerIn.addStat(StatList.getObjectUseStats(this));
              return ActionResult.newResult(EnumActionResult.SUCCESS, 
                  ItemStack.EMPTY);
            } else
            {
              return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
            }
          }
        } else
        {
          boolean flag1 = worldIn.getBlockState(blockpos).getBlock()
              .isReplaceable(worldIn, blockpos);
          BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP
              ? blockpos : blockpos.offset(raytraceresult.sideHit);

          if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit,
              itemStackIn))
          {
            return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
          } else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1))
          {
            playerIn.addStat(StatList.getObjectUseStats(this));
            return !playerIn.capabilities.isCreativeMode
                ? ActionResult.newResult(EnumActionResult.SUCCESS,
                    new ItemStack(ModItems.woodenBucket))
                : ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
          } else
          {
            return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
          }
        }
      }
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
  }

  private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player,
      Item fullBucket)
  {
    int stackSize = emptyBuckets.getCount();
    if (player.capabilities.isCreativeMode)
    {
      return emptyBuckets;
    } else
    {
      emptyBuckets.setCount(stackSize - 1);
      if (emptyBuckets.getCount() <= 0)
      {
        return new ItemStack(fullBucket);
      } else
      {
        if (!player.inventory
            .addItemStackToInventory(new ItemStack(fullBucket)))
        {
          player.dropItem(new ItemStack(fullBucket), false);
        }

        return emptyBuckets;
      }
    }
  }

  public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer worldIn,
      World pos, BlockPos posIn)
  {
    if (this.isFull == Blocks.AIR)
    {
      return false;
    } else
    {
      IBlockState iblockstate = pos.getBlockState(posIn);
      Material material = iblockstate.getMaterial();
      boolean flag = !material.isSolid();
      boolean flag1 = iblockstate.getBlock().isReplaceable(pos, posIn);

      if (!pos.isAirBlock(posIn) && !flag && !flag1)
      {
        return false;
      } else
      {
        if (pos.provider.doesWaterVaporize()
            && this.isFull == Blocks.FLOWING_WATER)
        {
          int l = posIn.getX();
          int i = posIn.getY();
          int j = posIn.getZ();
          RetroCraft.proxy.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, posIn, 1f);

          for (int k = 0; k < 8; ++k)
          {
            pos.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                (double) l + Math.random(), (double) i + Math.random(),
                (double) j + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
          }
        } else
        {
          if (!pos.isRemote && (flag || flag1) && !material.isLiquid())
          {
            pos.destroyBlock(posIn, true);
          }

          SoundEvent soundevent = this.isFull == Blocks.FLOWING_LAVA
              ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA
              : SoundEvents.ITEM_BUCKET_EMPTY;
          RetroCraft.proxy.playSound(soundevent, posIn, 1.0f);
          pos.setBlockState(posIn, this.isFull.getDefaultState(), 11);
        }

        return true;
      }
    }
  }
}
