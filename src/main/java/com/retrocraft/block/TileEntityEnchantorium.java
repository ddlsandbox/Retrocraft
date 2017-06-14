package com.retrocraft.block;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.enchantorium.EnchantHelper;
import com.retrocraft.network.PacketRequestUpdateEnchantorium;
import com.retrocraft.network.PacketUpdateEnchantorium;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityEnchantorium extends TileEntity {

	public long lastChangeTime;
	
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				RetroCraft.network.sendToAllAround(
					new PacketUpdateEnchantorium (
						TileEntityEnchantorium.this), 
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), 
						pos.getX(), pos.getY(), pos.getZ(), 64));
			}
		}
	};
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
	}
    
	@Override
	public void onLoad() {
		if (world.isRemote) {
			RetroCraft.network.sendToServer(new PacketRequestUpdateEnchantorium(this));
		}
	}
	
    public final void sendUpdate(){
    	
    	System.out.println("[RETROCRAFT] Send update MSG");
    	RetroCraft.network.sendToServer(new PacketUpdateEnchantorium(this));
    	System.out.println("[RETROCRAFT] Sent update MSG");
//        if(this.world != null && !this.world.isRemote){
//            NBTTagCompound compound = new NBTTagCompound();
//            //this.writeSyncableNBT(compound, NBTType.SYNC);
//            this.writeToNBT(compound);
//
//            NBTTagCompound data = new NBTTagCompound();
//            data.setTag("Data", compound);
//            data.setInteger("X", this.pos.getX());
//            data.setInteger("Y", this.pos.getY());
//            data.setInteger("Z", this.pos.getZ());
//            PacketHandler.theNetwork.sendToAllAround(
//            		new PacketClientToServer(
//            				data, 
//            				PacketHandler.TILE_ENTITY_HANDLER), 
//            				new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), 
//            				this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64));
//        }
    }
    
    public void enchantCurrent(HashMap<Integer, Integer> map, 
			HashMap<Integer, Integer> levels)
	{
    	System.out.println("[RETROCRAFT] Container Enchant!");
    	final ItemStack itemStack = inventory.getStackInSlot(0);
    	EnchantHelper.setEnchantments(map, itemStack, levels);
	}

}