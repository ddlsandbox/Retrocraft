package com.retrocraft.entity.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSortTeleportPipe implements IMessage
{

  private int index;
  private int otherIndex;

  public MessageSortTeleportPipe()
  {
  }

  public MessageSortTeleportPipe(int index, int otherIndex)
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
      implements IMessageHandler<MessageSortTeleportPipe, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageSortTeleportPipe message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          PlayerTeleportData waystoneData = PlayerTeleportData
              .fromPlayer(ctx.getServerHandler().player);
          TeleportEntry[] entries = waystoneData.getWaystones();
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
          waystoneData.store(ctx.getServerHandler().player);
          TeleportManager
              .sendPlayerWaystones(ctx.getServerHandler().player);
        }
      });
      return null;
    }
  }

}