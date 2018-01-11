package com.retrocraft.network;

import com.retrocraft.machine.enchanter.TileEntityEnchanter;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateEnchanter implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateEnchanter(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateEnchanter(TileEntityEnchanter te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateEnchanter() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateEnchanter, PacketUpdateEnchanter> {
		@Override
		public PacketUpdateEnchanter onMessage(PacketRequestUpdateEnchanter message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileEntityEnchanter te = (TileEntityEnchanter)world.getTileEntity(message.pos);
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			serverPlayer.openContainer.detectAndSendChanges();
			
			 try {
//                 ((ContainerEnchanter) serverPlayer.openContainer).enchant(serverPlayer, map, levels, cost);();
             }
             catch (final Exception e) {
             }
			 
			return null;
		}
	
	}

}