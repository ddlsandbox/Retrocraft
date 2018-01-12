package com.retrocraft.item.backpack;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

  public void updateBackpack(ItemStack stack, BackpackInfo backpackInfo)
  {
    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());

    stack.getTagCompound().setTag("packInfo", backpackInfo.serializeNBT());
    if (backpackInfo.getInventory() != null)
    {
      NBTTagList invTag = new NBTTagList();
      for (int i = 0; i < backpackInfo.getInventory().getSlots(); i++)
        invTag.appendTag(backpackInfo.getInventory().getStackInSlot(i).serializeNBT());

      stack.getTagCompound().setTag("packInv", invTag);
    }
  }

  // @Override
  // @SideOnly(Side.CLIENT)
  // public void registerIcons(IconRegister iconRegister)
  // {
  // this.itemIcon = iconRegister.registerIcon("inventoryitemmod:" +
  // this.getUnlocalizedName().substring(5));
  // }
}
