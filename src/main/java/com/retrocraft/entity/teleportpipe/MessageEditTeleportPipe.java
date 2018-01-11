package com.retrocraft.entity.teleportpipe;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEditTeleportPipe implements IMessage
{

  private BlockPos pos;
  private String   name;
  private boolean  isGlobal;

  public MessageEditTeleportPipe()
  {
  }

  public MessageEditTeleportPipe(BlockPos pos, String name, boolean isGlobal)
  {
    this.pos = pos;
    this.name = name;
    this.isGlobal = isGlobal;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    pos = BlockPos.fromLong(buf.readLong());
    name = ByteBufUtils.readUTF8String(buf);
    isGlobal = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    buf.writeLong(pos.toLong());
    ByteBufUtils.writeUTF8String(buf, name);
    buf.writeBoolean(isGlobal);
  }

  public BlockPos getPos()
  {
    return pos;
  }

  public String getName()
  {
    return name;
  }

  public boolean isGlobal()
  {
    return isGlobal;
  }

  public static class Handler
      implements IMessageHandler<MessageEditTeleportPipe, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageEditTeleportPipe message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          EntityPlayer entityPlayer = ctx.getServerHandler().player;
          if (RetroCraft.getConfig().creativeModeOnly
              && !entityPlayer.capabilities.isCreativeMode)
          {
            return;
          }
          World world = entityPlayer.getEntityWorld();
          BlockPos pos = message.getPos();
          if (entityPlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 10)
          {
            return;
          }
          GlobalTeleportPipe globalWaystones = GlobalTeleportPipe
              .get(ctx.getServerHandler().player.world);
          TileEntity tileEntity = world.getTileEntity(pos);
          if (tileEntity instanceof TileTeleportPipe)
          {
            TileTeleportPipe tileWaystone = (TileTeleportPipe) tileEntity;
            if (globalWaystones
                .getGlobalWaystone(tileWaystone.getWaystoneName()) != null
                && !ctx
                    .getServerHandler().player.capabilities.isCreativeMode)
            {
              return;
            }
            if (RetroCraft.getConfig().restrictRenameToOwner
                && !tileWaystone.isOwner(ctx.getServerHandler().player))
            {
              ctx.getServerHandler().player.sendMessage(
                  new TextComponentTranslation("retrocraft:notTheOwner"));
              return;
            }
            if (globalWaystones.getGlobalWaystone(message.getName()) != null
                && !ctx
                    .getServerHandler().player.capabilities.isCreativeMode)
            {
              ctx.getServerHandler().player.sendMessage(
                  new TextComponentTranslation("retrocraft:nameOccupied",
                      message.getName()));
              return;
            }
            TeleportEntry oldWaystone = new TeleportEntry(tileWaystone);
            globalWaystones.removeGlobalWaystone(oldWaystone);

            tileWaystone.setWaystoneName(message.getName());

            TeleportEntry newWaystone = new TeleportEntry(tileWaystone);

            if (message.isGlobal() && ctx
                .getServerHandler().player.capabilities.isCreativeMode)
            {
              tileWaystone.setGlobal(true);
              newWaystone.setGlobal(true);
              globalWaystones.addGlobalWaystone(newWaystone);
              for (Object obj : FMLCommonHandler.instance()
                  .getMinecraftServerInstance().getPlayerList().getPlayers())
              {
                TeleportManager.sendPlayerWaystones((EntityPlayer) obj);
              }
            }
          }

        }
      });
      return null;
    }
  }
}