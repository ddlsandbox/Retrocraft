package com.retrocraft.client;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.retrocraft.entity.teleportpipe.TeleportEntry;

public class ClientWaystones
{

  private static final Map<String, TeleportEntry> knownWaystones = Maps
      .newHashMap();

  public static void setKnownWaystones(TeleportEntry[] entries)
  {
    knownWaystones.clear();
    for (TeleportEntry entry : entries)
    {
      knownWaystones.put(entry.getName(), entry);
    }
  }

  @Nullable
  public static TeleportEntry getKnownWaystone(String name)
  {
    return knownWaystones.get(name);
  }

  public static TeleportEntry[] getKnownWaystones()
  {
    return knownWaystones.values()
        .toArray(new TeleportEntry[knownWaystones.size()]);
  }
}