package com.retrocraft.entity.teleportpipe.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerEditTeleportNameDummy extends Container
{
  @Override
  public boolean canInteractWith(EntityPlayer playerIn)
  {
    return true;
  }
}