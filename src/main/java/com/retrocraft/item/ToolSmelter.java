package com.retrocraft.item;

import com.retrocraft.util.ItemUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolSmelter extends ItemBase
{
  private int maxDamage = 100;
  
  public ToolSmelter(String name)
  {
    super(name);
    
    this.setMaxStackSize(1);
    this.setMaxDamage(maxDamage);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9,
      float par10)
  {
    ItemStack heldStack = player.getHeldItem(hand);
    int currentDamage = heldStack.getItemDamage();
    
    ItemStack source = Item.getItemFromBlock(world.getBlockState(pos).getBlock()).getDefaultInstance();
    ItemStack result = ItemUtil.getSmeltingResultForItem(source);
    
    if (currentDamage >= maxDamage || result.isEmpty())
      return EnumActionResult.FAIL;
    
    IBlockState newState = Block.getBlockFromItem(result.getItem()).getDefaultState();
    
    world.setBlockState(pos, newState);
    
    if (!world.isRemote && newState.getBlock() == Blocks.AIR)
    {
      EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), result.copy());
      item.setPickupDelay(10);
      world.spawnEntity(item);
    }

    heldStack.setItemDamage(currentDamage + 1);
    return EnumActionResult.SUCCESS;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack)
  {
    return EnumRarity.EPIC;
  }
}
