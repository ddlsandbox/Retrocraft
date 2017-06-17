package com.retrocraft.entity.waystone;

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

public class MessageEditWaystone implements IMessage
{

  private BlockPos pos;
  private String   name;
  private boolean  isGlobal;

  public MessageEditWaystone()
  {
  }

  public MessageEditWaystone(BlockPos pos, String name, boolean isGlobal)
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
      implements IMessageHandler<MessageEditWaystone, IMessage>
  {
    @Override
    @Nullable
    public IMessage onMessage(final MessageEditWaystone message,
        final MessageContext ctx)
    {
      PacketHandler.getThreadListener(ctx).addScheduledTask(new Runnable()
      {
        @Override
        public void run()
        {
          EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
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
          GlobalWaystones globalWaystones = GlobalWaystones
              .get(ctx.getServerHandler().playerEntity.world);
          TileEntity tileEntity = world.getTileEntity(pos);
          if (tileEntity instanceof TileWaystone)
          {
            TileWaystone tileWaystone = (TileWaystone) tileEntity;
            if (globalWaystones
                .getGlobalWaystone(tileWaystone.getWaystoneName()) != null
                && !ctx
                    .getServerHandler().playerEntity.capabilities.isCreativeMode)
            {
              return;
            }
            if (RetroCraft.getConfig().restrictRenameToOwner
                && !tileWaystone.isOwner(ctx.getServerHandler().playerEntity))
            {
              ctx.getServerHandler().playerEntity.sendMessage(
                  new TextComponentTranslation("retrocraft:notTheOwner"));
              return;
            }
            if (globalWaystones.getGlobalWaystone(message.getName()) != null
                && !ctx
                    .getServerHandler().playerEntity.capabilities.isCreativeMode)
            {
              ctx.getServerHandler().playerEntity.sendMessage(
                  new TextComponentTranslation("retrocraft:nameOccupied",
                      message.getName()));
              return;
            }
            WaystoneEntry oldWaystone = new WaystoneEntry(tileWaystone);
            globalWaystones.removeGlobalWaystone(oldWaystone);

            tileWaystone.setWaystoneName(message.getName());

            WaystoneEntry newWaystone = new WaystoneEntry(tileWaystone);

            if (message.isGlobal() && ctx
                .getServerHandler().playerEntity.capabilities.isCreativeMode)
            {
              tileWaystone.setGlobal(true);
              newWaystone.setGlobal(true);
              globalWaystones.addGlobalWaystone(newWaystone);
              for (Object obj : FMLCommonHandler.instance()
                  .getMinecraftServerInstance().getPlayerList().getPlayers())
              {
                WaystoneManager.sendPlayerWaystones((EntityPlayer) obj);
              }
            }
          }

        }
      });
      return null;
    }
  }
}