package com.retrocraft.block.pedestal;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.network.PacketRequestUpdatePedestal;
import com.retrocraft.network.PacketUpdatePedestal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityPedestal extends TileEntity {

	public enum PedestalMode { STATIC, ROTATING, DANCING };
	
	public long lastChangeTime;
	public PedestalMode mode = PedestalMode.ROTATING;
	
	public PedestalMode getMode()
	{
		return mode;
	}
	
	public void switchMode()
	{
		switch (mode)
		{
		case STATIC:
			mode = PedestalMode.ROTATING; break;
		case ROTATING:
			mode = PedestalMode.DANCING; break;
		case DANCING:
			mode = PedestalMode.STATIC; break;
		}
		RetroCraft.network.sendToAllAround(
				new PacketUpdatePedestal(
					TileEntityPedestal.this), 
				new NetworkRegistry.TargetPoint(world.provider.getDimension(), 
					pos.getX(), pos.getY(), pos.getZ(), 64));
	}
	
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				RetroCraft.network.sendToAllAround(
					new PacketUpdatePedestal(
						TileEntityPedestal.this), 
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), 
						pos.getX(), pos.getY(), pos.getZ(), 64));
			}
			TileEntityPedestal.this.markDirty();
		}
	};
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
	  super.writeToNBT(compound);
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setLong("lastChangeTime", lastChangeTime);
		compound.setInteger("mode", mode.ordinal());
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	  super.readFromNBT(compound);
	  inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		lastChangeTime = compound.getLong("lastChangeTime");
		mode = PedestalMode.values()[compound.getInteger("mode")];
	}
	
  @Override
  public NBTTagCompound getUpdateTag() {
      return writeToNBT(new NBTTagCompound());
  }
  
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
	  if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
  }
  return super.getCapability(capability, facing);
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			RetroCraft.network.sendToServer(new PacketRequestUpdatePedestal(this));
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
	}
}