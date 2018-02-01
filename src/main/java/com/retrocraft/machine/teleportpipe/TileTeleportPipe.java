package com.retrocraft.machine.teleportpipe;

import java.util.UUID;

import javax.annotation.Nullable;

import com.retrocraft.util.NameGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/* from TeleportPipes (blay09) */

public class TileTeleportPipe extends TileEntity
{

  private String  teleportPipeName = "";
  private UUID    owner;
  private boolean isGlobal;

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
  {
    super.writeToNBT(tagCompound);
    tagCompound.setString("TeleportPipeName", teleportPipeName);
    if (owner != null)
    {
      tagCompound.setTag("Owner", NBTUtil.createUUIDTag(owner));
    }
    tagCompound.setBoolean("IsGlobal", isGlobal);
    return tagCompound;
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound)
  {
    super.readFromNBT(tagCompound);
    teleportPipeName = tagCompound.getString("TeleportPipeName");
    if (tagCompound.hasKey("Owner"))
    {
      owner = NBTUtil.getUUIDFromTag(tagCompound.getCompoundTag("Owner"));
    }
    isGlobal = tagCompound.getBoolean("IsGlobal");
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
  {
    super.onDataPacket(net, pkt);
    generateName();
    readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public NBTTagCompound getUpdateTag()
  {
    generateName();
    return writeToNBT(new NBTTagCompound());
  }

  private void generateName() {
    if(teleportPipeName.isEmpty()) {
      teleportPipeName = NameGenerator.getName(world.getBiome(pos), world.rand);
    }
  }
  
  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket()
  {
    return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
  }

  public String getTeleportPipeName()
  {
    return teleportPipeName;
  }

  public boolean isOwner(EntityPlayer player)
  {
    return owner == null || player.getGameProfile().getId().equals(owner)
        || player.capabilities.isCreativeMode;
  }

  public void setTeleportPipeName(String teleportPipeName)
  {
    this.teleportPipeName = teleportPipeName;
    IBlockState state = world.getBlockState(pos);
    world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state,
        state, 3);
    markDirty();
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState,
      IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox()
  {
    return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
        pos.getY() + 1, pos.getZ() + 1);
    // return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() +
    // 2,
    // pos.getY() + 1, pos.getZ() + 2);
  }

  public void setOwner(EntityPlayer owner)
  {
    this.owner = owner.getGameProfile().getId();
    markDirty();
  }

  public boolean isGlobal()
  {
    return isGlobal;
  }

  public void setGlobal(boolean isGlobal)
  {
    this.isGlobal = isGlobal;
    markDirty();
  }

}