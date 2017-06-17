package com.retrocraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.retrocraft.block.pedestal.TileEntityPedestal;

import io.netty.buffer.ByteBuf;

public class PacketUpdatePedestal implements IMessage
{

  private BlockPos                        pos;
  private ItemStack                       stack;
  private long                            lastChangeTime;
  private TileEntityPedestal.PedestalMode mode;

  public PacketUpdatePedestal(BlockPos pos, ItemStack stack,
                              long lastChangeTime,
                              TileEntityPedestal.PedestalMode mode)
  {
    this.pos = pos;
    this.stack = stack;
    this.lastChangeTime = lastChangeTime;
    this.mode = mode;

  }

  public PacketUpdatePedestal(TileEntityPedestal te)
  {
    this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime,
        te.getMode());
  }

  public PacketUpdatePedestal()
  {
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    buf.writeLong(pos.toLong());
    ByteBufUtils.writeItemStack(buf, stack);
    buf.writeLong(lastChangeTime);
    buf.writeInt(mode.ordinal());
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    pos = BlockPos.fromLong(buf.readLong());
    stack = ByteBufUtils.readItemStack(buf);
    lastChangeTime = buf.readLong();
    mode = TileEntityPedestal.PedestalMode.values()[buf.readInt()];
  }

  public static class Handler
      implements IMessageHandler<PacketUpdatePedestal, IMessage>
  {

    @Override
    public IMessage onMessage(PacketUpdatePedestal message, MessageContext ctx)
    {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        TileEntityPedestal te = (TileEntityPedestal) Minecraft
            .getMinecraft().world.getTileEntity(message.pos);
        te.inventory.setStackInSlot(0, message.stack);
        te.lastChangeTime = message.lastChangeTime;
        te.mode = message.mode;
      });
      return null;
    }

  }
}
