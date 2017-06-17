package com.retrocraft.network;

import com.retrocraft.RetroCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHandler
{
  private static int ID = 0;

  public static int nextID()
  {
    return ID++;
  }

  public static void sendToAllAround(IMessage message, TileEntity te, int range)
  {
    BlockPos p = te.getPos();
    RetroCraft.network.sendToAllAround(message,
        new TargetPoint(te.getWorld().provider.getDimension(), p.getX(),
            p.getY(), p.getZ(), range));
  }

  public static void sendToAllAround(IMessage message, TileEntity te)
  {
    sendToAllAround(message, te, 64);
  }

  public static void sendTo(IMessage message, EntityPlayerMP player)
  {
    RetroCraft.network.sendTo(message, player);
  }

  public static void init(FMLInitializationEvent event)
  {

  }

  public static IThreadListener getThreadListener(MessageContext ctx)
  {
    return ctx.side == Side.SERVER
        ? (WorldServer) ctx.getServerHandler().playerEntity.world
        : getClientThreadListener();
  }

  @SideOnly(Side.CLIENT)
  public static IThreadListener getClientThreadListener()
  {
    return Minecraft.getMinecraft();
  }

}