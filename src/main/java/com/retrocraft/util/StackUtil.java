package com.retrocraft.util;

import net.minecraft.item.ItemStack;

public final class StackUtil
{
  public static ItemStack validateCopy(ItemStack stack)
  {
    if (isValid(stack))
    {
      return stack.copy();
    } else
    {
      return getNull();
    }
  }

  public static ItemStack validateCheck(ItemStack stack)
  {
    if (isValid(stack))
    {
      return stack;
    } else
    {
      return getNull();
    }
  }

  public static boolean isValid(ItemStack stack)
  {
    return stack != null && !ItemStack.areItemStacksEqual(stack, getNull())
        && stack.getCount() > 0 && stack.getItem() != null;
  }

  public static ItemStack getNull()
  {
    return ItemStack.EMPTY;
  }

  public static int getStackSize(ItemStack stack)
  {
    if (!isValid(stack))
    {
      return 0;
    } else
    {
      return stack.getCount();
    }
  }

  public static ItemStack setStackSize(ItemStack stack, int size)
  {
    return setStackSize(stack, size, false);
  }

  public static ItemStack setStackSize(ItemStack stack, int size,
      boolean containerOnEmpty)
  {
    if (size <= 0)
    {
      if (isValid(stack) && containerOnEmpty)
      {
        return stack.getItem().getContainerItem(stack);
      } else
      {
        return getNull();
      }
    }
    stack.setCount(size);
    return stack;
  }

  public static ItemStack addStackSize(ItemStack stack, int size)
  {
    return addStackSize(stack, size, false);
  }

  public static ItemStack addStackSize(ItemStack stack, int size,
      boolean containerOnEmpty)
  {
    return setStackSize(stack, getStackSize(stack) + size, containerOnEmpty);
  }
}
