package com.retrocraft.network;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfig implements IMessage {

  private RetroCraftConfig config;

  public PacketConfig() {
  }

  public PacketConfig(RetroCraftConfig config) {
    this.config = config;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    config = RetroCraftConfig.read(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    config.write(buf);
  }

  public RetroCraftConfig getConfig() {
    return config;
  }
  
  public static class Handler implements IMessageHandler<PacketConfig, IMessage> {
    
    @Override
    @Nullable
    public IMessage onMessage(PacketConfig message, MessageContext ctx) {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
        @Override
        public void run() {
          RetroCraft.instance.setConfig(message.getConfig());
        }
      });
      return null;
    }
    
  }
}