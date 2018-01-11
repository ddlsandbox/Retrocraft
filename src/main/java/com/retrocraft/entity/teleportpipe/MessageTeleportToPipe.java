package com.retrocraft.entity.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraftGlobals;
import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
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

  private TeleportEntry waystone;
  private EnumHand      hand;
  private TeleportEntry fromWaystone;

  public MessageTeleportToPipe()
  {
  }

  public MessageTeleportToPipe(TeleportEntry waystone, EnumHand hand,
                                   @Nullable TeleportEntry fromWaystone)
  {
    this.waystone = waystone;
    this.hand = hand;
    this.fromWaystone = fromWaystone;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    waystone = TeleportEntry.read(buf);
    hand = EnumHand.MAIN_HAND;
    fromWaystone = TeleportEntry.read(buf);
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    waystone.write(buf);
    fromWaystone.write(buf);
  }

  public TeleportEntry getWaystone()
  {
    return waystone;
  }

  @Nullable
  public TeleportEntry getFromWaystone()
  {
    return fromWaystone;
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
          EntityPlayer player = ctx.getServerHandler().playerEntity;
          int dist = (int) Math.sqrt(
              player.getDistanceSqToCenter(message.getWaystone().getPos()));
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
          TeleportEntry fromWaystone = message.getFromWaystone();
          if (fromWaystone == null
              || TeleportManager.getWaystoneInWorld(fromWaystone) == null)
          {
            return;
          }

          if (TeleportManager.teleportToWaystone(
              ctx.getServerHandler().playerEntity, message.getWaystone()))
          {
            player.addExperienceLevel(-xpLevelCost);
          }

          TeleportManager
              .sendPlayerWaystones(ctx.getServerHandler().playerEntity);
        }
      });
      return null;
    }
  }

}