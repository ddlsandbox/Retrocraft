package com.retrocraft.network;

import com.retrocraft.block.TileEntityEnchantorium;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateEnchantorium implements IMessage {

	private BlockPos pos;
	private ItemStack stack;

	public PacketUpdateEnchantorium(BlockPos pos, ItemStack stack) {
		this.pos = pos;
		this.stack = stack;
//		this.lastChangeTime = lastChangeTime;
	}
	
	public PacketUpdateEnchantorium(TileEntityEnchantorium te) {
		this(te.getPos(), te.inventory.getStackInSlot(0));//, te.lastChangeTime);
	}
	
	public PacketUpdateEnchantorium() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack);
//		buf.writeLong(lastChangeTime);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		stack = ByteBufUtils.readItemStack(buf);
//		lastChangeTime = buf.readLong();
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateEnchantorium, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateEnchantorium message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityEnchantorium te = (TileEntityEnchantorium)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.inventory.setStackInSlot(0, message.stack);
				System.out.println("[RETROCRAFT MSG] update");
//				te.lastChangeTime = message.lastChangeTime;
			});
			return null;
		}
	
	}
}