package com.retrocraft.item.backpack;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBackpack extends ItemBase
{

  public ItemBackpack(String name)
  {
    super(name);

    setMaxStackSize(1);
  }

  @Override
  public ItemBackpack setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }
  
  // Without this method, your inventory will NOT work!!!
  @Override
  public int getMaxItemUseDuration(ItemStack stack)
  {
    return 1; // return any value greater than zero
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
  {
    ItemStack held = player.getHeldItem(hand);
    if (!world.isRemote)
    {
      player.openGui(RetroCraft.instance, ModGuiHandler.BACKPACK, world, hand == EnumHand.OFF_HAND ? 1 : 0, 0, 0);
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, held);
  }
}
