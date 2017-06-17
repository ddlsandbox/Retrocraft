package com.retrocraft.network;

import com.retrocraft.machine.repairer.ContainerRepairer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
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
		toSend = buf.readInt();
	}

	public static class Handler implements IMessageHandler<PacketRepairer, IMessage> {

		@Override
		public IMessage onMessage(PacketRepairer message, MessageContext ctx) {

			EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;

			int amount = message.toSend;

			if (serverPlayer.openContainer instanceof ContainerRepairer) {
				try {
					((ContainerRepairer) serverPlayer.openContainer).repairItem();
				} catch (final Exception e) {
				}
				serverPlayer.openContainer.detectAndSendChanges();
			}

			// No response packet
			return null;
		}
	}

}
