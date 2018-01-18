package com.retrocraft.network;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTeleportEffect implements IMessage
{

  private BlockPos pos;

  public PacketTeleportEffect()
  {
  }

  public PacketTeleportEffect(BlockPos pos)
  {
    this.pos = pos;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    pos = BlockPos.fromLong(buf.readLong());
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    buf.writeLong(pos.toLong());
  }

  public BlockPos getPos()
  {
    return pos;
  }

  public static class Handler
      implements IMessageHandler<PacketTeleportEffect, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final PacketTeleportEffect message,
        MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          Minecraft mc = Minecraft.getMinecraft();
          mc.ingameGUI.getBossOverlay().clearBossInfos();
          RetroCraft.proxy.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL,
              message.getPos(), 1f);
          for (int i = 0; i < 128; i++)
          {
            mc.world.spawnParticle(EnumParticleTypes.PORTAL,
                message.getPos().getX()
                    + (mc.world.rand.nextDouble() - 0.5) * 3,
                message.getPos().getY() + mc.world.rand.nextDouble() * 3,
                message.getPos().getZ()
                    + (mc.world.rand.nextDouble() - 0.5) * 3,
                (mc.world.rand.nextDouble() - 0.5) * 2,
                -mc.world.rand.nextDouble(),
                (mc.world.rand.nextDouble() - 0.5) * 2);
          }
        }
      });
      return null;
    }
  }
}