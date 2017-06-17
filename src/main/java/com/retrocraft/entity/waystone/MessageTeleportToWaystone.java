package com.retrocraft.entity.waystone;

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

public class MessageTeleportToWaystone implements IMessage
{

  private WaystoneEntry waystone;
  private EnumHand      hand;
  private WaystoneEntry fromWaystone;

  public MessageTeleportToWaystone()
  {
  }

  public MessageTeleportToWaystone(WaystoneEntry waystone, EnumHand hand,
                                   @Nullable WaystoneEntry fromWaystone)
  {
    this.waystone = waystone;
    this.hand = hand;
    this.fromWaystone = fromWaystone;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    waystone = WaystoneEntry.read(buf);
    hand = EnumHand.MAIN_HAND;
    fromWaystone = WaystoneEntry.read(buf);
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    waystone.write(buf);
    fromWaystone.write(buf);
  }

  public WaystoneEntry getWaystone()
  {
    return waystone;
  }

  @Nullable
  public WaystoneEntry getFromWaystone()
  {
    return fromWaystone;
  }

  public EnumHand getHand()
  {
    return hand;
  }

  public static class Handler
      implements IMessageHandler<MessageTeleportToWaystone, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageTeleportToWaystone message,
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
          int xpLevelCost = RetroCraft.getConfig().blocksPerXPLevel > 0
              ? MathHelper.clamp(dist / RetroCraft.getConfig().blocksPerXPLevel,
                  0, 3)
              : 0;

          if (player.experienceLevel < xpLevelCost)
          {
            return;
          }
          WaystoneEntry fromWaystone = message.getFromWaystone();
          if (fromWaystone == null
              || WaystoneManager.getWaystoneInWorld(fromWaystone) == null)
          {
            return;
          }

          if (WaystoneManager.teleportToWaystone(
              ctx.getServerHandler().playerEntity, message.getWaystone()))
          {
            player.removeExperienceLevel(xpLevelCost);
          }

          WaystoneManager
              .sendPlayerWaystones(ctx.getServerHandler().playerEntity);
        }
      });
      return null;
    }
  }

}