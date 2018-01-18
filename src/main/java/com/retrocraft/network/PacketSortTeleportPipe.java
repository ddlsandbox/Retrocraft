package com.retrocraft.network;

import javax.annotation.Nullable;

import com.retrocraft.machine.teleportpipe.PlayerTeleportData;
import com.retrocraft.machine.teleportpipe.TeleportEntry;
import com.retrocraft.machine.teleportpipe.TeleportManager;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSortTeleportPipe implements IMessage
{

  private int index;
  private int otherIndex;

  public PacketSortTeleportPipe()
  {
  }

  public PacketSortTeleportPipe(int index, int otherIndex)
  {
    this.index = index;
    this.otherIndex = otherIndex;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    index = buf.readByte();
    otherIndex = buf.readByte();
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    buf.writeByte(index);
    buf.writeByte(otherIndex);
  }

  public int getIndex()
  {
    return index;
  }

  public int getOtherIndex()
  {
    return otherIndex;
  }

  public static class Handler
      implements IMessageHandler<PacketSortTeleportPipe, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final PacketSortTeleportPipe message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          PlayerTeleportData teleportPipeData = PlayerTeleportData
              .fromPlayer(ctx.getServerHandler().player);
          TeleportEntry[] entries = teleportPipeData.getTeleportPipes();
          int index = message.getIndex();
          int otherIndex = message.getOtherIndex();
          if (index < 0 || index >= entries.length || otherIndex < 0
              || otherIndex >= entries.length)
          {
            return;
          }
          TeleportEntry swap = entries[index];
          entries[index] = entries[otherIndex];
          entries[otherIndex] = swap;
          teleportPipeData.store(ctx.getServerHandler().player);
          TeleportManager
              .sendPlayerTeleportPipes(ctx.getServerHandler().player);
        }
      });
      return null;
    }
  }

}