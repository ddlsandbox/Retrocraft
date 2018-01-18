package com.retrocraft.machine.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.network.PacketTeleportEffect;
import com.retrocraft.network.PacketTeleportPipes;

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

  public static void sendPlayerTeleportPipes(EntityPlayer player)
  {
    if (player instanceof EntityPlayerMP)
    {
      PlayerTeleportData teleportPipeData = PlayerTeleportData.fromPlayer(player);
      RetroCraft.network.sendTo(new PacketTeleportPipes(
          teleportPipeData.getTeleportPipes(), teleportPipeData.getLastFreeWarp(),
          teleportPipeData.getLastWarpStoneUse()), (EntityPlayerMP) player);
    }
  }

  public static void addPlayerTeleportPipe(EntityPlayer player,
      TeleportEntry teleportPipe)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper
        .getOrCreateTeleportPipesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.TELEPORT_LIST, Constants.NBT.TAG_COMPOUND);
    tagList.appendTag(teleportPipe.writeToNBT());
    tagCompound.setTag(PlayerTeleportHelper.TELEPORT_LIST, tagList);
  }

  public static boolean removePlayerTeleportPipe(EntityPlayer player,
      TeleportEntry teleportPipe)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper.getTeleportPipesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.TELEPORT_LIST, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tagList.tagCount(); i++)
    {
      NBTTagCompound entryCompound = tagList.getCompoundTagAt(i);
      if (TeleportEntry.read(entryCompound).equals(teleportPipe))
      {
        tagList.removeTag(i);
        return true;
      }
    }
    return false;
  }

  public static boolean checkAndUpdateTeleportPipe(EntityPlayer player,
      TeleportEntry teleportPipe)
  {
    NBTTagCompound tagCompound = PlayerTeleportHelper.getTeleportPipesTag(player);
    NBTTagList tagList = tagCompound.getTagList(
        PlayerTeleportHelper.TELEPORT_LIST, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < tagList.tagCount(); i++)
    {
      NBTTagCompound entryCompound = tagList.getCompoundTagAt(i);
      if (TeleportEntry.read(entryCompound).equals(teleportPipe))
      {
        TileTeleportPipe tileEntity = getTeleportPipeInWorld(teleportPipe);
        if (tileEntity != null)
        {
          if (!entryCompound.getString("Name")
              .equals(tileEntity.getTeleportPipeName()))
          {
            entryCompound.setString("Name", tileEntity.getTeleportPipeName());
            sendPlayerTeleportPipes(player);
          }
          return true;
        } else
        {
          if (teleportPipe.isGlobal())
          {
            GlobalTeleportPipe.get(player.world).removeGlobalTeleportPipe(teleportPipe);
          }
          removePlayerTeleportPipe(player, teleportPipe);
          sendPlayerTeleportPipes(player);
        }
        return false;
      }
    }
    return false;
  }

  @Nullable
  public static TileTeleportPipe getTeleportPipeInWorld(TeleportEntry teleportPipe)
  {
    World targetWorld = DimensionManager.getWorld(teleportPipe.getDimensionId());
    if (targetWorld == null)
    {
      DimensionManager.initDimension(teleportPipe.getDimensionId());
      targetWorld = DimensionManager.getWorld(teleportPipe.getDimensionId());
    }
    if (targetWorld != null)
    {
      TileEntity tileEntity = targetWorld.getTileEntity(teleportPipe.getPos());
      if (tileEntity instanceof TileTeleportPipe)
      {
        return (TileTeleportPipe) tileEntity;
      }
    }
    return null;
  }

  public static boolean teleportToTeleportPipe(EntityPlayer player,
      TeleportEntry teleportPipe)
  {
    if (!checkAndUpdateTeleportPipe(player, teleportPipe))
    {
      TextComponentTranslation chatComponent = new TextComponentTranslation(
          "retrocraft:teleportPipeBroken");
      chatComponent.getStyle().setColor(TextFormatting.RED);
      player.sendMessage(chatComponent);
      return false;
    }
    World targetWorld = DimensionManager.getWorld(teleportPipe.getDimensionId());
//    EnumFacing facing = targetWorld.getBlockState(teleportPipe.getPos())
//        .getValue(BlockTeleportPipe.FACING);
    BlockPos targetPos = teleportPipe.getPos().offset(EnumFacing.UP);
    boolean dimensionWarp = teleportPipe
        .getDimensionId() != player.getEntityWorld().provider.getDimension();

    sendTeleportEffect(player.world, new BlockPos(player));
    if (dimensionWarp)
    {
      MinecraftServer server = player.world.getMinecraftServer();
      if (server != null)
      {
        server.getPlayerList().transferPlayerToDimension(
            (EntityPlayerMP) player, teleportPipe.getDimensionId(),
            new TeleporterPipe((WorldServer) player.world));
      }
    }
//    player.rotationYaw = getRotationYaw(facing);
    player.setPositionAndUpdate(targetPos.getX() + 0.5, targetPos.getY() + 0.5,
        targetPos.getZ() + 0.5);
    sendTeleportEffect(player.world, targetPos);
    return true;
  }

  public static void sendTeleportEffect(World world, BlockPos pos)
  {
    RetroCraft.network.sendToAllAround(new PacketTeleportEffect(pos),
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