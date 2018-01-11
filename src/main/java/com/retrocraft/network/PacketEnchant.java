package com.retrocraft.network;

import java.util.HashMap;
import java.util.Map.Entry;

import com.retrocraft.machine.enchanter.ContainerEnchanter;
import com.retrocraft.machine.repairer.ContainerRepairer;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEnchant implements IMessage {
	
	private HashMap<Enchantment, Integer> map;
//	private HashMap<Integer, Integer> levels;
	private int cost;
	
	public PacketEnchant() {
	}

	public PacketEnchant(HashMap<Enchantment, Integer> map, int cost) {
		this.map = map;
		this.cost = cost;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.cost);
		buf.writeInt(this.map.size());

		for (final Entry<Enchantment, Integer> entry : this.map.entrySet()) {

			ByteBufUtils.writeUTF8String(buf, entry.getKey().getRegistryName().toString());
			buf.writeInt(entry.getValue());
		}

	}

	@Override
	public void fromBytes(ByteBuf buf) {
        this.cost = buf.readInt();

        this.map = new HashMap<>();
        final int size = buf.readInt();

        for (int index = 0; index < size; index++)
            this.map.put(Enchantment.getEnchantmentByLocation(
            				ByteBufUtils.readUTF8String(buf)), 
            			 buf.readInt());
	}

	// The params of the IMessageHandler are <REQ, REPLY>
	// This means that the first param is the packet you are receiving, and the
	// second is the packet you are returning.
	// The returned packet can be used as a "response" from a sent packet.
	public static class Handler implements IMessageHandler<PacketEnchant, IMessage> {
		// Do note that the default constructor is required, but implicitly
		// defined in this case

		@Override
		public IMessage onMessage(PacketEnchant message, MessageContext ctx) {
			// This is the player the packet was sent to the server from
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

            if (serverPlayer.openContainer instanceof ContainerEnchanter) {

            	try {
	                ((ContainerEnchanter) serverPlayer.openContainer).enchant(serverPlayer, message.map, message.cost);
	//                updateItemStack(
	//                		serverPlayer, message.map, message.cost);
	                serverPlayer.openContainer.detectAndSendChanges();
            	} 
            	catch (Exception e)
            	{
            		System.out.println("[RETROCRAFT] Enchant handler exception");
            	}
            }

			// No response packet
			return null;
		}
	}
}
