package com.retrocraft.network;

import com.retrocraft.machine.repairer.ContainerRepairer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRepairer implements IMessage {
		  // A default constructor is always required
	public PacketRepairer() {
	}

	private int toSend;

	public PacketRepairer(int toSend) {
		this.toSend = toSend;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(toSend);

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple
		// values, you must read in the same order you wrote.
		toSend = buf.readInt();
	}

	// The params of the IMessageHandler are <REQ, REPLY>
	// This means that the first param is the packet you are receiving, and the
	// second is the packet you are returning.
	// The returned packet can be used as a "response" from a sent packet.
	public static class Handler implements IMessageHandler<PacketRepairer, IMessage> {
		// Do note that the default constructor is required, but implicitly
		// defined in this case

		@Override
		public IMessage onMessage(PacketRepairer message, MessageContext ctx) {
			// This is the player the packet was sent to the server from
			EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
			// The value that was sent
			int amount = message.toSend;

			if (serverPlayer.openContainer instanceof ContainerRepairer) {
				try {
					((ContainerRepairer) serverPlayer.openContainer).repairItem();
				} catch (final Exception e) {
					// EnchantingPlus.log.info("Enchanting failed because: " +
					// e.getLocalizedMessage());
				}
				serverPlayer.openContainer.detectAndSendChanges();
			}

			// No response packet
			return null;
		}
	}

}
