package com.retrocraft.entity.teleportpipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class PlayerTeleportData
{

  private final TeleportEntry[] entries;
  private final long            lastFreeWarp;
  private final long            lastWarpStoneUse;

  public PlayerTeleportData(TeleportEntry[] entries, long lastFreeWarp,
                            long lastWarpStoneUse)
  {
    this.entries = entries;
    this.lastFreeWarp = lastFreeWarp;
    this.lastWarpStoneUse = lastWarpStoneUse;
  }

  public TeleportEntry[] getWaystones()
  {
    return entries;
  }

  public long getLastFreeWarp()
  {
    return lastFreeWarp;
  }

  public long getLastWarpStoneUse()
  {
    return lastWarpStoneUse;
  }

  public void store(EntityPlayerMP player)
  {
    PlayerTeleportHelper.store(player, entries, lastFreeWarp, lastWarpStoneUse);
  }

  public static PlayerTeleportData fromPlayer(EntityPlayer player)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper.getWaystonesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.WAYSTONE_LIST, Constants.NBT.TAG_COMPOUND);
    TeleportEntry[] entries = new TeleportEntry[tagList.tagCount()];
    for (int i = 0; i < entries.length; i++)
    {
      entries[i] = TeleportEntry.read(tagList.getCompoundTagAt(i));
    }
    long lastFreeWarp = tagCompound
        .getLong(PlayerTeleportHelper.LAST_FREE_WARP);
    long lastWarpStoneUse = tagCompound
        .getLong(PlayerTeleportHelper.LAST_WARP_STONE_USE);
    return new PlayerTeleportData(entries, lastFreeWarp, lastWarpStoneUse);
  }

}