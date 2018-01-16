package com.retrocraft.machine.repairer;

import javax.annotation.Nullable;

import com.retrocraft.util.ItemStackHandlerCustom;
import com.retrocraft.util.StackUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumSkyBlock;

public class TileRepairer extends TileEntity implements ISidedInventory, ITickable {
	// Create and initialize the itemStacks variable that will store store the itemStacks
	public static final int INPUT_SLOTS_COUNT = 1;
	public static final int OUTPUT_SLOTS_COUNT = 1;
	public static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	public static final int INPUT_SLOT_NUMBER = 0;
	public static final int OUTPUT_SLOT_NUMBER = 1;

  private static final int[] SLOTS_TOP = new int[] {0};
  private static final int[] SLOTS_BOTTOM = new int[] {1};
  private static final int[] SLOTS_SIDES = new int[] {0,1};
  
	private ItemStackHandlerCustom slots;

	public TileRepairer()
	{
	  this.slots = new ItemStackHandlerCustom(TOTAL_SLOTS_COUNT){
      @Override
      public boolean canInsert(ItemStack stack, int slot){
          return TileRepairer.this.isItemValidForSlot(slot, stack);
      }

      @Override
      public boolean canExtract(ItemStack stack, int slot){
          return slot == OUTPUT_SLOT_NUMBER;
      }

      @Override
      public int getSlotLimit(int slot){
          return 1;
      }

      @Override
      protected void onContentsChanged(int slot){
          super.onContentsChanged(slot);
          TileRepairer.this.markDirty();
      }
  };
		clear();
	}

	// This method is called every tick to update the tile entity
	// It runs both on the server and the client.
	@Override
	public void update() {
		// The block update (for renderer) is only required on client side, but the lighting is required on both, since
		//    the client needs it for rendering and the server needs it for crop growth etc
		if (StackUtil.isValid(this.slots.getStackInSlot(INPUT_SLOT_NUMBER)) 
		    || StackUtil.isValid(this.slots.getStackInSlot(OUTPUT_SLOT_NUMBER))) {
			if (world.isRemote) {
				IBlockState iblockstate = this.world.getBlockState(pos);
				final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
				world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
			}
			world.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}
	}

	public boolean repairItem()
	{
		final ItemStack inputStack  = slots.getStackInSlot(INPUT_SLOT_NUMBER);
		final ItemStack outputStack = slots.getStackInSlot(OUTPUT_SLOT_NUMBER);
		if (inputStack.isEmpty() || !outputStack.isEmpty())
			return false;
		
		inputStack.setItemDamage(0);
		slots.setStackInSlot(OUTPUT_SLOT_NUMBER, inputStack.copy());
		slots.setStackInSlot(INPUT_SLOT_NUMBER, StackUtil.getNull());
		
		markDirty();
		return true;
	}

	// Gets the number of slots in the inventory
	@Override
	public int getSizeInventory() {
		return slots.getSlots();
	}

	// returns true if all of the slots in the inventory are empty
	@Override
	public boolean isEmpty()
	{
	  return slots.getStackInSlot(INPUT_SLOT_NUMBER).isEmpty()
	      && slots.getStackInSlot(OUTPUT_SLOT_NUMBER).isEmpty();
	}

	// Gets the stack in the given slot
	@Override
	public ItemStack getStackInSlot(int i) {
		return slots.getStackInSlot(i);
	}

	/**
	 * Removes some of the units from itemstack in the given slot, and returns as a separate itemstack
	 * @param slotIndex the slot number to remove the items from
	 * @param count the number of units to remove
	 * @return a new itemstack containing the units removed from the slot
	 */
	@Override
	public ItemStack decrStackSize(int slotIndex, int count) {
		ItemStack itemStackInSlot = getStackInSlot(slotIndex);
		if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;  //isEmpty(), EMPTY_ITEM

		ItemStack itemStackRemoved;
		if (itemStackInSlot.getCount() <= count) { //getStackSize
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(slotIndex, ItemStack.EMPTY); // EMPTY_ITEM
		} else {
			itemStackRemoved = itemStackInSlot.splitStack(count);
			if (itemStackInSlot.getCount() == 0) { //getStackSize
				setInventorySlotContents(slotIndex, ItemStack.EMPTY); //EMPTY_ITEM
			}
		}
		markDirty();
		return itemStackRemoved;
	}

	// overwrites the stack in the given slotIndex with the given stack
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
	  slots.setStackInSlot(slotIndex, itemstack);
		if (!itemstack.isEmpty() && itemstack.getCount() > getInventoryStackLimit()) {  // isEmpty();  getStackSize()
			itemstack.setCount(getInventoryStackLimit());  //setStackSize()
		}
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	static public boolean isItemValidForInputSlot(ItemStack itemStack)
	{
		return itemStack.getItemDamage() > 0;
	}
	
	static public boolean isItemValidForOutputSlot(ItemStack itemStack)
  {
    return false;
  }

	//------------------------------

	// This is where you save any data that you don't want to lose when the tile entity unloads
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

		NBTTagList dataForAllSlots = new NBTTagList();
		for (int i = 0; i < slots.getSlots(); ++i) {
			if (!this.getStackInSlot(i).isEmpty()) {  //isEmpty()
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(dataForThisSlot);
				dataForAllSlots.appendTag(dataForThisSlot);
			}
		}
		parentNBTTagCompound.setTag("Items", dataForAllSlots);
		return parentNBTTagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location
		final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
		NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			byte slotNumber = dataForOneSlot.getByte("Slot");
			if (slotNumber >= 0 && slotNumber < slots.getSlots()) {
				slots.setStackInSlot(slotNumber, new ItemStack(dataForOneSlot));
			}
		}
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

  @Override
  public NBTTagCompound getUpdateTag()
  {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
    return nbtTagCompound;
  }

  @Override
  public void handleUpdateTag(NBTTagCompound tag)
  {
    this.readFromNBT(tag);
  }
  //------------------------

	// set all slots to empty
	@Override
	public void clear() {
	  slots.setStackInSlot(INPUT_SLOT_NUMBER, StackUtil.getNull());
	  slots.setStackInSlot(OUTPUT_SLOT_NUMBER, StackUtil.getNull());
	}

	// will add a key for this container to the lang file so we can name it in the GUI
	@Override
	public String getName() {
		return "tile.repairer.name";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	// standard code to look up what the human-readable name is
  @Nullable
  @Override
  public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	// -----------------------------------------------------------------------------------------------------------
	// The following methods are not needed for this example but are part of IInventory so they must be implemented

	// Unused unless your container specifically uses it.
	// Return true if the given stack is allowed to go in the given slot
	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
		return itemStack.getItemDamage() > 0;
	}

	/**
	 * This method removes the entire contents of the given slot and returns it.
	 * Used by containers such as crafting tables which return any items in their slots when you close the GUI
	 * @param slotIndex
	 * @return
	 */
	@Override
	public ItemStack removeStackFromSlot(int slotIndex) {
		ItemStack itemStack = getStackInSlot(slotIndex);
		if (!itemStack.isEmpty()) setInventorySlotContents(slotIndex, ItemStack.EMPTY);  //isEmpty();  EMPTY_ITEM
		return itemStack;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
  {
    return slots.canExtract(stack, index);
  }

  @Override
  public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
  {
    return this.isItemValidForSlot(index, stack);
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
  }

}
