package com.retrocraft.entity.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTeleportToPipe implements IMessage
{

  private TeleportEntry teleportPipe;
  private EnumHand      hand;
  private TeleportEntry fromTeleportPipe;

  public MessageTeleportToPipe()
  {
  }

  public MessageTeleportToPipe(TeleportEntry teleportPipe, EnumHand hand,
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
      implements IMessageHandler<MessageTeleportToPipe, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageTeleportToPipe message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          EntityPlayer player = ctx.getServerHandler().player;
          int dist = (int) Math.sqrt(
              player.getDistanceSqToCenter(message.getTeleportPipe().getPos()));
          int xpLevelCost = 0;
          if (!player.capabilities.isCreativeMode)
            xpLevelCost = RetroCraft.getConfig().blocksPerXPLevel > 0
                ? MathHelper.clamp(
                    dist / RetroCraft.getConfig().blocksPerXPLevel, 0, 3)
                : 0;

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