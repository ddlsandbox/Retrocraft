package com.retrocraft.client;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.retrocraft.entity.teleportpipe.TeleportEntry;

public class ClientTeleportPipes
{

  private static final Map<String, TeleportEntry> knownTeleportPipes = Maps
      .newHashMap();

  public static void setKnownTeleportPipes(TeleportEntry[] entries)
  {
    knownTeleportPipes.clear();
    for (TeleportEntry entry : entries)
    {
      knownTeleportPipes.put(entry.getName(), entry);
    }
  }

  @Nullable
  public static TeleportEntry getKnownTeleportPipe(String name)
  {
    return knownTeleportPipes.get(name);
  }

  public static TeleportEntry[] getKnownTeleportPipes()
  {
    return knownTeleportPipes.values()
        .toArray(new TeleportEntry[knownTeleportPipes.size()]);
  }
}