package com.retrocraft.entity.waystone;

import javax.annotation.Nullable;

import com.retrocraft.client.ClientWaystones;
import com.retrocraft.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageWaystones implements IMessage
{

  private WaystoneEntry[] entries;
  private long            lastFreeWarp;
  private long            lastWarpStoneUse;

  public MessageWaystones()
  {
  }

  public MessageWaystones(WaystoneEntry[] entries, long lastFreeWarp,
                          long lastWarpStoneUse)
  {
    this.entries = entries;
    this.lastFreeWarp = lastFreeWarp;
    this.lastWarpStoneUse = lastWarpStoneUse;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    entries = new WaystoneEntry[buf.readByte()];
    for (int i = 0; i < entries.length; i++)
    {
      entries[i] = WaystoneEntry.read(buf);
    }
    lastFreeWarp = buf.readLong();
    lastWarpStoneUse = buf.readLong();
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    buf.writeByte(entries.length);
    for (WaystoneEntry entry : entries)
    {
      entry.write(buf);
    }
    buf.writeLong(lastFreeWarp);
    buf.writeLong(lastWarpStoneUse);
  }

  public WaystoneEntry[] getEntries()
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

  public static class Handler implements IMessageHandler<MessageWaystones, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageWaystones message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          ClientWaystones.setKnownWaystones(message.getEntries());
          PlayerWaystoneHelper.store(
              FMLClientHandler.instance().getClientPlayerEntity(),
              message.getEntries(), message.getLastFreeWarp(),
              message.getLastWarpStoneUse());
        }
      });
      return null;
    }
  }
}