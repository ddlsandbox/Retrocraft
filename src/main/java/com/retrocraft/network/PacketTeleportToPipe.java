package com.retrocraft.network;

import javax.annotation.Nullable;

import com.retrocraft.machine.teleportpipe.TeleportEntry;
import com.retrocraft.machine.teleportpipe.TeleportManager;
import com.retrocraft.util.UsefulFunctions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTeleportToPipe implements IMessage
{

  private TeleportEntry teleportPipe;
  private EnumHand      hand;
  private TeleportEntry fromTeleportPipe;

  public PacketTeleportToPipe()
  {
  }

  public PacketTeleportToPipe(TeleportEntry teleportPipe, EnumHand hand,
                                   @Nullable TeleportEntry fromTeleportPipe)
  {
    this.teleportPipe = teleportPipe;
    this.hand = hand;
    this.fromTeleportPipe = fromTeleportPipe;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    teleportPipe = TeleportEntry.read(buf);
    hand = EnumHand.MAIN_HAND;
    fromTeleportPipe = TeleportEntry.read(buf);
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    teleportPipe.write(buf);
    fromTeleportPipe.write(buf);
  }

  public TeleportEntry getTeleportPipe()
  {
    return teleportPipe;
  }

  @Nullable
  public TeleportEntry getFromTeleportPipe()
  {
    return fromTeleportPipe;
  }

  public EnumHand getHand()
  {
    return hand;
  }

  public static class Handler
      implements IMessageHandler<PacketTeleportToPipe, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final PacketTeleportToPipe message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          EntityPlayer player = ctx.getServerHandler().player;
          int xpLevelCost = UsefulFunctions.teleportXpCost(player, message.getTeleportPipe().getPos());

          if (player.experienceLevel < xpLevelCost)
          {
            return;
          }
          TeleportEntry fromTeleportPipe = message.getFromTeleportPipe();
          if (fromTeleportPipe == null
              || TeleportManager.getTeleportPipeInWorld(fromTeleportPipe) == null)
          {
            return;
          }

          if (TeleportManager.teleportToTeleportPipe(
              ctx.getServerHandler().player, message.getTeleportPipe()))
          {
            player.addExperienceLevel(-xpLevelCost);
          }

          TeleportManager
              .sendPlayerTeleportPipes(ctx.getServerHandler().player);
        }
      });
      return null;
    }
  }

}