package com.retrocraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemManure extends ItemBase
{

  public ItemManure(String name)
  {
    super(name);
  }

  @Override
  public ItemManure setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }
  
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9,
      float par10)
  {
    ItemStack stack = player.getHeldItem(hand);
    if (ItemDye.applyBonemeal(stack, world, pos, player, hand))
    {
      if (!world.isRemote)
      {
        world.playEvent(2005, pos, 0);
      }
      return EnumActionResult.SUCCESS;
    }
    return super.onItemUse(player, world, pos, hand, side, par8, par9, par10);
  }

  @Override
  public EnumRarity getRarity(ItemStack stack)
  {
    return EnumRarity.COMMON;
  }
}