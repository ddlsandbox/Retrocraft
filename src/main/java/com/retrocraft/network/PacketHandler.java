package com.retrocraft.network;

import java.util.ArrayList;
import java.util.List;

import com.retrocraft.RetroCraft;
import com.retrocraft.tile.TileEntityBase;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
  public static final List<IDataHandler> DATA_HANDLERS = new ArrayList<IDataHandler>();
  public static final IDataHandler TILE_ENTITY_HANDLER = new IDataHandler() {
    @Override
    @SideOnly(Side.CLIENT)
    public void handleData(NBTTagCompound compound, MessageContext context) {
      World world = Minecraft.getMinecraft().world;
      if (world != null) {
        TileEntity tile = world.getTileEntity(
            new BlockPos(compound.getInteger("X"), compound.getInteger("Y"), compound.getInteger("Z")));
        if (tile instanceof TileEntityBase) {
          ((TileEntityBase) tile).readSyncableNBT(compound.getCompoundTag("Data"),
              TileEntityBase.NBTType.SYNC);
        }
      }
    }
  };
  
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
    DATA_HANDLERS.add(TILE_ENTITY_HANDLER);
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