package com.retrocraft.entity.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.client.ClientWaystones;
import com.retrocraft.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTeleportPipes implements IMessage
{

  private TeleportEntry[] entries;
  private long            lastFreeWarp;
  private long            lastWarpStoneUse;

  public MessageTeleportPipes()
  {
  }

  public MessageTeleportPipes(TeleportEntry[] entries, long lastFreeWarp,
                          long lastWarpStoneUse)
  {
    this.entries = entries;
    this.lastFreeWarp = lastFreeWarp;
    this.lastWarpStoneUse = lastWarpStoneUse;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    entries = new TeleportEntry[buf.readByte()];
    for (int i = 0; i < entries.length; i++)
    {
      entries[i] = TeleportEntry.read(buf);
    }
    lastFreeWarp = buf.readLong();
    lastWarpStoneUse = buf.readLong();
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    buf.writeByte(entries.length);
    for (TeleportEntry entry : entries)
    {
      entry.write(buf);
    }
    buf.writeLong(lastFreeWarp);
    buf.writeLong(lastWarpStoneUse);
  }

  public TeleportEntry[] getEntries()
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

  public static class Handler implements IMessageHandler<MessageTeleportPipes, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageTeleportPipes message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          ClientWaystones.setKnownWaystones(message.getEntries());
          PlayerTeleportHelper.store(
              FMLClientHandler.instance().getClientPlayerEntity(),
              message.getEntries(), message.getLastFreeWarp(),
              message.getLastWarpStoneUse());
        }
      });
      return null;
    }
  }
}