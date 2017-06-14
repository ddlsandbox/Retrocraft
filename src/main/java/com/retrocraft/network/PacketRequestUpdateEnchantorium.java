package com.retrocraft.network;

import com.retrocraft.block.TileEntityEnchantorium;
import com.retrocraft.block.enchantorium.ContainerEnchantorium;
import com.retrocraft.block.repairer.ContainerRepairer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketRequestUpdateEnchantorium implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateEnchantorium(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateEnchantorium(TileEntityEnchantorium te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateEnchantorium() {
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
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateEnchantorium, PacketUpdateEnchantorium> {
		@Override
		public PacketUpdateEnchantorium onMessage(PacketRequestUpdateEnchantorium message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.dimension);
			TileEntityEnchantorium te = (TileEntityEnchantorium)world.getTileEntity(message.pos);
			EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
			serverPlayer.openContainer.detectAndSendChanges();
			
			 try {
//                 ((ContainerEnchantorium) serverPlayer.openContainer).enchant(serverPlayer, map, levels, cost);();
             }
             catch (final Exception e) {
             }
			 
			return null;
		}
	
	}

}