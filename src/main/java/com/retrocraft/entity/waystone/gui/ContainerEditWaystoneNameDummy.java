package com.retrocraft.entity.waystone.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerEditWaystoneNameDummy extends Container
{
  @Override
  public boolean canInteractWith(EntityPlayer playerIn)
  {
    return true;
  }
}