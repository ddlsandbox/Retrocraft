package com.retrocraft.block.enchanter;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.retrocraft.block.enchantorium.EnchantHelper;
import com.retrocraft.network.PacketClientToServer;
import com.retrocraft.network.PacketHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityEnchanter extends TileEntity {

	public long lastChangeTime;

	public ItemStackHandler inventory = new ItemStackHandler(1) {
		// @Override
		// protected void onContentsChanged(int slot) {
		// 	if (!world.isRemote) {
		// 		lastChangeTime = world.getTotalWorldTime();
		// 		RetroCraft.network.sendToAllAround(
		// 			new PacketUpdateEnchanter (
		// 				TileEntityEnchanter.this),
		// 			new NetworkRegistry.TargetPoint(world.provider.getDimension(),
		// 				pos.getX(), pos.getY(), pos.getZ(), 64));
		// 	}
		// }
	};

	/* Capability Interface */
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagList dataForAllSlots = new NBTTagList();
		NBTTagList dataForSlot = new NBTTagList();
		NBTTagCompound dataForThisSlot = new NBTTagCompound();
		dataForThisSlot.setByte("Slot", (byte) 0);
		inventory.getStackInSlot(0).writeToNBT(dataForThisSlot);
		dataForAllSlots.appendTag(dataForThisSlot);
		compound.setTag("Items", dataForAllSlots);
		
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
		NBTTagList dataForAllSlots = compound.getTagList("Items", NBT_TYPE_COMPOUND);
		inventory.setStackInSlot(0, ItemStack.EMPTY);           // set slot to empty EMPTY_ITEM
		NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(0);
		byte slotNumber = dataForOneSlot.getByte("Slot");
		inventory.setStackInSlot(0, new ItemStack(dataForOneSlot));
	}



	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
	}

	@Override
	public void onLoad() {
		// if (world.isRemote) {
		// 	RetroCraft.network.sendToServer(new PacketRequestUpdateEnchanter(this));
		// }
	}

    public final void sendUpdate(){

    	// System.out.println("[RETROCRAFT] Send update MSG");
    	// RetroCraft.network.sendToServer(new PacketUpdateEnchanter(this));
    	// System.out.println("[RETROCRAFT] Sent update MSG");

        if(this.world != null && !this.world.isRemote){
            NBTTagCompound compound = new NBTTagCompound();
            //this.writeSyncableNBT(compound, NBTType.SYNC);
            this.writeToNBT(compound);

            NBTTagCompound data = new NBTTagCompound();
            data.setTag("Data", compound);
            data.setInteger("X", this.pos.getX());
            data.setInteger("Y", this.pos.getY());
            data.setInteger("Z", this.pos.getZ());
            PacketHandler.theNetwork.sendToAllAround(
            		new PacketClientToServer(
            				data,
            				PacketHandler.TILE_ENTITY_HANDLER),
            		new NetworkRegistry.TargetPoint(this.world.provider.getDimension(),
            				this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64));
        }
    }

    public void enchantCurrent(HashMap<Integer, Integer> map,
			HashMap<Integer, Integer> levels)
	{
    	System.out.println("[RETROCRAFT] Container Enchant!");
    	final ItemStack itemStack = inventory.getStackInSlot(0);
    	EnchantHelper.setEnchantments(map, itemStack, levels);
    	
    	markDirty();
	}
    
    
//	// When the world loads from disk, the server needs to send the TileEntity information to the client
//	//  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this
  @Override
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket()
  {
    NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
    final int METADATA = 0;
    return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
    handleUpdateTag(updateTagDescribingTileEntityState);
  }

  /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
     Warning - although our getUpdatePacket() uses this method, vanilla also calls it directly, so don't remove it.
   */
  @Override
  public NBTTagCompound getUpdateTag()
  {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
    return nbtTagCompound;
  }

  /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
   Warning - although our onDataPacket() uses this method, vanilla also calls it directly, so don't remove it.
 */
  @Override
  public void handleUpdateTag(NBTTagCompound tag)
  {
    this.readFromNBT(tag);
  }

}
