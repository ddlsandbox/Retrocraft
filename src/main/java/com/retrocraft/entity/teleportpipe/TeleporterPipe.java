package com.retrocraft.entity.teleportpipe;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterPipe extends Teleporter
{
  public TeleporterPipe(WorldServer world)
  {
    super(world);
  }

  @Override
  public void placeInPortal(Entity entity, float rotationYaw)
  {
  }
}