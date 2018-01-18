package com.retrocraft.machine.teleportpipe;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class PlayerTeleportHelper
{

  public static final String TELEPORTS           = "TeleportPipes";
  public static final String TELEPORT_LIST       = "TeleportPipeList";
  public static final String LAST_FREE_WARP      = "LastFreeWarp";
  public static final String LAST_WARP_STONE_USE = "LastWarpStoneUse";

  public static NBTTagCompound getTeleportPipesTag(EntityPlayer player)
  {
    return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
        .getCompoundTag(TELEPORTS);
  }

  public static NBTTagCompound getOrCreateTeleportPipesTag(EntityPlayer player)
  {
    NBTTagCompound persistedTag = player.getEntityData()
        .getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    NBTTagCompound teleportPipesTag = persistedTag.getCompoundTag(TELEPORTS);
    persistedTag.setTag(TELEPORTS, teleportPipesTag);
    player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedTag);
    return teleportPipesTag;
  }

  public static void store(EntityPlayer player, TeleportEntry[] entries,
      long lastFreeWarp, long lastWarpStoneUse)
  {
    NBTTagCompound tagCompound = getOrCreateTeleportPipesTag(player);
    NBTTagList tagList = new NBTTagList();
    for (TeleportEntry entry : entries)
    {
      tagList.appendTag(entry.writeToNBT());
    }
    tagCompound.setTag(TELEPORT_LIST, tagList);
    tagCompound.setLong(LAST_FREE_WARP, lastFreeWarp);
    tagCompound.setLong(LAST_WARP_STONE_USE, lastWarpStoneUse);
  }

  public static boolean canUseWarpStone(EntityPlayer player)
  {
    return true;
  }

  public static void setLastFreeWarp(EntityPlayer player, long lastFreeWarp)
  {
    getOrCreateTeleportPipesTag(player).setLong(LAST_FREE_WARP, lastFreeWarp);
  }

  public static long getLastFreeWarp(EntityPlayer player)
  {
    return getTeleportPipesTag(player).getLong(LAST_FREE_WARP);
  }

  public static void setLastWarpStoneUse(EntityPlayer player,
      long lastWarpStone)
  {
    getOrCreateTeleportPipesTag(player).setLong(LAST_WARP_STONE_USE, lastWarpStone);
  }

  public static long getLastWarpStoneUse(EntityPlayer player)
  {
    return getTeleportPipesTag(player).getLong(LAST_WARP_STONE_USE);
  }

  @Nullable
  public static TeleportEntry getLastTeleportPipe(EntityPlayer player)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper.getTeleportPipesTag(player);
    NBTTagList tagList = tagCompound.getTagList(TELEPORT_LIST,
        Constants.NBT.TAG_COMPOUND);
    if (tagList.tagCount() > 0)
    {
      return TeleportEntry
          .read(tagList.getCompoundTagAt(tagList.tagCount() - 1));
    }
    return null;
  }

}