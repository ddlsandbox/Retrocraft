package com.retrocraft.item;

import com.retrocraft.util.ItemUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolSmelter extends ItemBase
{

  public ToolSmelter(String name)
  {
    super(name);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9,
      float par10)
  {
    ItemStack result = ItemUtil.getSmeltingResultForItem(new ItemStack(world.getBlockState(pos).getBlock()));
    
    if (result.isEmpty())
      return EnumActionResult.FAIL;
    
    IBlockState newState = Block.getBlockFromItem(result.getItem()).getDefaultState();
    
    world.setBlockState(pos, newState);
    
    if (!world.isRemote && newState.getBlock() == Blocks.AIR)
    {
      EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), result);
      world.spawnEntity(item);
    }

    return EnumActionResult.SUCCESS;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack)
  {
    return EnumRarity.EPIC;
  }
}
