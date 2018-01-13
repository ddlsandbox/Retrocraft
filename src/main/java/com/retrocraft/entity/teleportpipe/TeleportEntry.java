package com.retrocraft.entity.teleportpipe;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class TeleportEntry
{

  private final String   name;
  private final int      dimensionId;
  private final BlockPos pos;
  private boolean        isGlobal;

  public TeleportEntry(String name, int dimensionId, BlockPos pos,
                       boolean isGlobal)
  {
    this.name = name;
    this.dimensionId = dimensionId;
    this.pos = pos;
    this.isGlobal = isGlobal;
  }

  public TeleportEntry(TileTeleportPipe tileTeleportPipe)
  {
    this.name = tileTeleportPipe.getTeleportPipeName();
    this.dimensionId = tileTeleportPipe.getWorld().provider.getDimension();
    this.pos = tileTeleportPipe.getPos();
    this.isGlobal = tileTeleportPipe.isGlobal();
  }

  public String getName()
  {
    return name;
  }

  public int getDimensionId()
  {
    return dimensionId;
  }

  public BlockPos getPos()
  {
    return pos;
  }

  public boolean isGlobal()
  {
    return isGlobal;
  }

  public void setGlobal(boolean isGlobal)
  {
    this.isGlobal = isGlobal;
  }

  public static TeleportEntry read(ByteBuf buf)
  {
    return new TeleportEntry(ByteBufUtils.readUTF8String(buf), buf.readInt(),
        BlockPos.fromLong(buf.readLong()), buf.readBoolean());
  }

  public static TeleportEntry read(NBTTagCompound tagCompound)
  {
    return new TeleportEntry(tagCompound.getString("Name"),
        tagCompound.getInteger("Dimension"),
        BlockPos.fromLong(tagCompound.getLong("Position")),
        tagCompound.getBoolean("IsGlobal"));
  }

  public void write(ByteBuf buf)
  {
    ByteBufUtils.writeUTF8String(buf, name);
    buf.writeInt(dimensionId);
    buf.writeLong(pos.toLong());
    buf.writeBoolean(isGlobal);
  }

  public NBTTagCompound writeToNBT()
  {
    NBTTagCompound tagCompound = new NBTTagCompound();
    tagCompound.setString("Name", name);
    tagCompound.setInteger("Dimension", dimensionId);
    tagCompound.setLong("Position", pos.toLong());
    tagCompound.setBoolean("IsGlobal", isGlobal);
    return tagCompound;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    TeleportEntry that = (TeleportEntry) o;
    return dimensionId == that.dimensionId && pos.equals(that.pos)
        && isGlobal == that.isGlobal;
  }

  @Override
  public int hashCode()
  {
    int result = dimensionId;
    result = 31 * result + pos.hashCode();
    return result;
  }

}