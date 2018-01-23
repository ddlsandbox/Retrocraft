/*
 * This file ("ItemUtil.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2016 Ellpeck
 */
package com.retrocraft.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public final class ItemUtil
{

  public static Item getItemFromName(String name)
  {
    ResourceLocation resLoc = new ResourceLocation(name);
    if (Item.REGISTRY.containsKey(resLoc))
    {
      return Item.REGISTRY.getObject(resLoc);
    }
    return null;
  }

  public static int getPlaceAt(ItemStack[] array, ItemStack stack)
  {
    return getPlaceAt(Arrays.asList(array), stack);
  }

  /**
   * Returns true if array contains stack or if both contain null
   */
  public static boolean contains(ItemStack[] array, ItemStack stack)
  {
    return getPlaceAt(array, stack) != -1;
  }

  
  /**
   * Returns the place of stack in array, -1 if not present
   */
  public static int getPlaceAt(List<ItemStack> list, ItemStack stack)
  {
    if (list != null && list.size() > 0)
    {
      for (int i = 0; i < list.size(); i++)
      {
        if ((!StackUtil.isValid(stack) && !StackUtil.isValid(list.get(i)))
            || areItemsEqual(stack, list.get(i)))
        {
          return i;
        }
      }
    }
    return -1;
  }

  public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2)
  {
    return StackUtil.isValid(stack1) && StackUtil.isValid(stack2)
        && (stack1.isItemEqual(stack2));
  }

  /**
   * Returns true if list contains stack or if both contain null
   */
  public static boolean contains(List<ItemStack> list, ItemStack stack)
  {
    return !(list == null || list.isEmpty())
        && getPlaceAt(list, stack) != -1;
  }

  public static void addEnchantment(ItemStack stack, Enchantment e, int level)
  {
    if (!hasEnchantment(stack, e))
    {
      stack.addEnchantment(e, level);
    }
  }

  public static boolean hasEnchantment(ItemStack stack, Enchantment e)
  {
    NBTTagList ench = stack.getEnchantmentTagList();
    if (ench != null)
    {
      for (int i = 0; i < ench.tagCount(); i++)
      {
        short id = ench.getCompoundTagAt(i).getShort("id");
        if (id == Enchantment.getEnchantmentID(e))
        {
          return true;
        }
      }
    }
    return false;
  }

  public static void removeEnchantment(ItemStack stack, Enchantment e)
  {
    NBTTagList ench = stack.getEnchantmentTagList();
    if (ench != null)
    {
      for (int i = 0; i < ench.tagCount(); i++)
      {
        short id = ench.getCompoundTagAt(i).getShort("id");
        if (id == Enchantment.getEnchantmentID(e))
        {
          ench.removeTag(i);
        }
      }
      if (ench.hasNoTags() && stack.hasTagCompound())
      {
        stack.getTagCompound().removeTag("ench");
      }
    }
  }

  public static boolean canBeStacked(ItemStack stack1, ItemStack stack2)
  {
    return ItemStack.areItemsEqual(stack1, stack2)
        && ItemStack.areItemStackTagsEqual(stack1, stack2);
  }
  
  // returns the smelting result for the given stack. Returns null if the given
  // stack can not be smelted
  public static ItemStack getSmeltingResultForItem(ItemStack stack)
  {
    return FurnaceRecipes.instance().getSmeltingResult(stack);
  }

  // returns the number of ticks the given item will burn. Returns 0 if the given
  // item is not a valid fuel
  public static short getItemBurnTime(ItemStack stack)
  {
    int burntime = TileEntityFurnace.getItemBurnTime(stack); // just use the vanilla values
    return (short) MathHelper.clamp(burntime, 0, Short.MAX_VALUE);
  }
}