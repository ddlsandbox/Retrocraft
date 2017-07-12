package com.retrocraft.entity.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TeleportManager
{

  public static void sendPlayerWaystones(EntityPlayer player)
  {
    if (player instanceof EntityPlayerMP)
    {
      PlayerTeleportData waystoneData = PlayerTeleportData.fromPlayer(player);
      RetroCraft.network.sendTo(new MessageTeleportPipes(
          waystoneData.getWaystones(), waystoneData.getLastFreeWarp(),
          waystoneData.getLastWarpStoneUse()), (EntityPlayerMP) player);
    }
  }

  public static void addPlayerWaystone(EntityPlayer player,
      TeleportEntry waystone)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper
        .getOrCreateWaystonesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.WAYSTONE_LIST, Constants.NBT.TAG_COMPOUND);
    tagList.appendTag(waystone.writeToNBT());
    tagCompound.setTag(PlayerTeleportHelper.WAYSTONE_LIST, tagList);
  }

  public static boolean removePlayerWaystone(EntityPlayer player,
      TeleportEntry waystone)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper.getWaystonesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.WAYSTONE_LIST, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tagList.tagCount(); i++)
    {
      NBTTagCompound entryCompound = tagList.getCompoundTagAt(i);
      if (TeleportEntry.read(entryCompound).equals(waystone))
      {
        tagList.removeTag(i);
        return true;
      }
    }
    return false;
  }

  public static boolean checkAndUpdateWaystone(EntityPlayer player,
      TeleportEntry waystone)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper.getWaystonesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.WAYSTONE_LIST, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tagList.tagCount(); i++)
    {
      NBTTagCompound entryCompound = tagList.getCompoundTagAt(i);
      if (TeleportEntry.read(entryCompound).equals(waystone))
      {
        TileTeleportPipe tileEntity = getWaystoneInWorld(waystone);
        if (tileEntity != null)
        {
          if (!entryCompound.getString("Name")
              .equals(tileEntity.getWaystoneName()))
          {
            entryCompound.setString("Name", tileEntity.getWaystoneName());
            sendPlayerWaystones(player);
          }
          return true;
        } else
        {
          if (waystone.isGlobal())
          {
            GlobalTeleportPipe.get(player.world).removeGlobalWaystone(waystone);
          }
          removePlayerWaystone(player, waystone);
          sendPlayerWaystones(player);
        }
        return false;
      }
    }
    return false;
  }

  @Nullable
  public static TileTeleportPipe getWaystoneInWorld(TeleportEntry waystone)
  {
    World targetWorld = DimensionManager.getWorld(waystone.getDimensionId());
    if (targetWorld == null)
    {
      DimensionManager.initDimension(waystone.getDimensionId());
      targetWorld = DimensionManager.getWorld(waystone.getDimensionId());
    }
    if (targetWorld != null)
    {
      TileEntity tileEntity = targetWorld.getTileEntity(waystone.getPos());
      if (tileEntity instanceof TileTeleportPipe)
      {
        return (TileTeleportPipe) tileEntity;
      }
    }
    return null;
  }

  public static boolean teleportToWaystone(EntityPlayer player,
      TeleportEntry waystone)
  {
    if (!checkAndUpdateWaystone(player, waystone))
    {
      TextComponentTranslation chatComponent = new TextComponentTranslation(
          "retrocraft:waystoneBroken");
      chatComponent.getStyle().setColor(TextFormatting.RED);
      player.sendMessage(chatComponent);
      return false;
    }
    World targetWorld = DimensionManager.getWorld(waystone.getDimensionId());
    EnumFacing facing = targetWorld.getBlockState(waystone.getPos())
        .getValue(BlockTeleportPipe.FACING);
    BlockPos targetPos = waystone.getPos().offset(facing);
    boolean dimensionWarp = waystone
        .getDimensionId() != player.getEntityWorld().provider.getDimension();

    sendTeleportEffect(player.world, new BlockPos(player));
    if (dimensionWarp)
    {
      MinecraftServer server = player.world.getMinecraftServer();
      if (server != null)
      {
        server.getPlayerList().transferPlayerToDimension(
            (EntityPlayerMP) player, waystone.getDimensionId(),
            new TeleporterPipe((WorldServer) player.world));
      }
    }
    player.rotationYaw = getRotationYaw(facing);
    player.setPositionAndUpdate(targetPos.getX() + 0.5, targetPos.getY() + 0.5,
        targetPos.getZ() + 0.5);
    sendTeleportEffect(player.world, targetPos);
    return true;
  }

  public static void sendTeleportEffect(World world, BlockPos pos)
  {
    RetroCraft.network.sendToAllAround(new MessageTeleportEffect(pos),
        new NetworkRegistry.TargetPoint(world.provider.getDimension(),
            pos.getX(), pos.getY(), pos.getZ(), 64));
  }

  public static float getRotationYaw(EnumFacing facing)
  {
    switch (facing)
    {
    case NORTH:
      return 180f;
    case SOUTH:
      return 0f;
    case WEST:
      return 90f;
    case EAST:
      return -90f;
    default:
      break;
    }
    return 0f;
  }

}