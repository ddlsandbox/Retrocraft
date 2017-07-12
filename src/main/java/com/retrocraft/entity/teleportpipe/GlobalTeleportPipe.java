package com.retrocraft.entity.teleportpipe;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.retrocraft.RetroCraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GlobalTeleportPipe extends WorldSavedData
{

  private static final String DATA_NAME     = RetroCraft.modId
      + "_GlobalWaystones";
  private static final String TAG_LIST_NAME = "GlobalWaystones";

  private final Map<String, TeleportEntry> globalWaystones = Maps.newHashMap();

  public GlobalTeleportPipe()
  {
    super(DATA_NAME);
  }

  public GlobalTeleportPipe(String name)
  {
    super(name);
  }

  public void addGlobalWaystone(TeleportEntry entry)
  {
    globalWaystones.put(entry.getName(), entry);
    markDirty();

    for (EntityPlayer player : FMLCommonHandler.instance()
        .getMinecraftServerInstance().getPlayerList().getPlayers())
    {
      TeleportManager.addPlayerWaystone(player, entry);
    }
  }

  public void removeGlobalWaystone(TeleportEntry entry)
  {
    globalWaystones.remove(entry.getName());
    markDirty();

    for (EntityPlayer player : FMLCommonHandler.instance()
        .getMinecraftServerInstance().getPlayerList().getPlayers())
    {
      TeleportManager.removePlayerWaystone(player, entry);
    }
  }

  public Collection<TeleportEntry> getGlobalWaystones()
  {
    return globalWaystones.values();
  }

  @Nullable
  public TeleportEntry getGlobalWaystone(String name)
  {
    return globalWaystones.get(name);
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound)
  {
    NBTTagList tagList = tagCompound.getTagList(TAG_LIST_NAME,
        Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tagList.tagCount(); i++)
    {
      TeleportEntry entry = TeleportEntry.read((NBTTagCompound) tagList.get(i));
      globalWaystones.put(entry.getName(), entry);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
  {
    NBTTagList tagList = new NBTTagList();
    for (TeleportEntry entry : globalWaystones.values())
    {
      tagList.appendTag(entry.writeToNBT());
    }
    tagCompound.setTag(TAG_LIST_NAME, tagList);
    return tagCompound;
  }

  public static GlobalTeleportPipe get(World world)
  {
    MapStorage storage = world.getMapStorage();
    if (storage != null)
    {
      GlobalTeleportPipe instance = (GlobalTeleportPipe) storage
          .getOrLoadData(GlobalTeleportPipe.class, DATA_NAME);
      if (instance == null)
      {
        instance = new GlobalTeleportPipe();
        storage.setData(DATA_NAME, instance);
      }
      return instance;
    }
    return new GlobalTeleportPipe();
  }

}