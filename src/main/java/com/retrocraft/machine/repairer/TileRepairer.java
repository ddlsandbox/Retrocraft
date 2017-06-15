package com.retrocraft.machine.repairer;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumSkyBlock;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * TileInventorySmelting is an advanced sided inventory that works like a vanilla furnace except that it has 5 input and output slots,
 * 4 fuel slots and cooks at up to four times the speed.
 * The input slots are used sequentially rather than in parallel, i.e. the first slot cooks, then the second, then the third, etc
 * The fuel slots are used in parallel.  The more slots burning in parallel, the faster the cook time.
 * The code is heavily based on TileEntityFurnace.
 */
public class TileRepairer extends TileEntity implements IInventory, ITickable {
	// Create and initialize the itemStacks variable that will store store the itemStacks
	public static final int INPUT_SLOTS_COUNT = 1;
	public static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT;

	public static final int INPUT_SLOT_NUMBER = 0;

	private ItemStack[] itemStacks;

	public TileRepairer()
	{
		itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
		clear();
	}

	// This method is called every tick to update the tile entity, i.e.
	// - see if the fuel has run out, and if so turn the furnace "off" and slowly uncook the current item (if any)
	// - see if any of the items have finished smelting
	// It runs both on the server and the client.
	@Override
	public void update() {
		// The block update (for renderer) is only required on client side, but the lighting is required on both, since
		//    the client needs it for rendering and the server needs it for crop growth etc
		if (itemStacks[INPUT_SLOT_NUMBER].getCount() == 1) {
			if (world.isRemote) {
				IBlockState iblockstate = this.world.getBlockState(pos);
				final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
				world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
			}
			world.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}
	}


//public void repair (EntityPlayer player, int cost) throws Exception {
//        
//        final ItemStack itemStack = tableInventory.getStackInSlot(0);
//        
//        if (itemStack == null)
//            return;
//            
//        boolean flag = !itemStack.hasTagCompound() || !itemStack.getTagCompound().hasKey("charge");
//        
//        if ((!itemStack.isItemEnchanted() || cost == 0) && flag)
//            return;
//            
//        if (canPurchase(player, cost)) {
//            int maxCost = repairCostMax();
//            double percAmnt = cost / (double) maxCost;
//            
//            int remain = itemStack.getItemDamageForDisplay();
//            double newDamage = remain - remain * percAmnt;
//            newDamage = (newDamage <= 0) ? 0 : newDamage;
//            
//            itemStack.setItemDamage((int) newDamage);
//            if (!player.capabilities.isCreativeMode)
//                player.addExperienceLevel(-cost);
//                
//        }
//        
//        onCraftMatrixChanged(tableInventory);
//}

	/**
	 * checks that there is an item to be smelted in one of the input slots and that there is room for the result in the output slots
	 * If desired, performs the smelt
	 * @param performSmelt if true, perform the smelt.  if false, check whether smelting is possible, but don't change the inventory
	 * @return false if no items can be smelted, true otherwise
	 */
	public boolean repairItem()
	{
		final ItemStack inputStack = itemStacks[INPUT_SLOT_NUMBER];
		if (inputStack.isEmpty())
			return false;
		
		inputStack.setItemDamage(0);
//		itemStacks[FIRST_OUTPUT_SLOT] = inputStack.copy();
		
		markDirty();
		return true;
	}

	// Gets the number of slots in the inventory
	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

	// returns true if all of the slots in the inventory are empty
	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : itemStacks) {
			if (!itemstack.isEmpty()) {  // isEmpty()
				return false;
			}
		}

		return true;
	}

	// Gets the stack in the given slot
	@Override
	public ItemStack getStackInSlot(int i) {
		return itemStacks[i];
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
		itemStacks[slotIndex] = itemstack;
		if (!itemstack.isEmpty() && itemstack.getCount() > getInventoryStackLimit()) {  // isEmpty();  getStackSize()
			itemstack.setCount(getInventoryStackLimit());  //setStackSize()
		}
		markDirty();
	}

	// This is the maximum number if items allowed in each slot
	// This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
	// inserting items via the gui
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	// Return true if the given player is able to use this block. In this case it checks that
	// 1) the world tileentity hasn't been replaced in the meantime, and
	// 2) the player isn't too far away from the centre of the block
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	// Return true if the given stack is allowed to be inserted in the given slot
	// Unlike the vanilla furnace, we allow anything to be placed in the fuel slots
	static public boolean isItemValidForInputSlot(ItemStack itemStack)
	{
		return itemStack.getItemDamage() > 0;
	}

	//------------------------------

	// This is where you save any data that you don't want to lose when the tile entity unloads
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

//		// Save the stored item stacks

		// to use an analogy with Java, this code generates an array of hashmaps
		// The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
		//   as slot=1, id=2353, count=1, etc
		// Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.
		NBTTagList dataForAllSlots = new NBTTagList();
		for (int i = 0; i < this.itemStacks.length; ++i) {
			if (!this.itemStacks[i].isEmpty()) {  //isEmpty()
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setByte("Slot", (byte) i);
				this.itemStacks[i].writeToNBT(dataForThisSlot);
				dataForAllSlots.appendTag(dataForThisSlot);
			}
		}
		// the array of hashmaps is then inserted into the parent hashmap for the container
		parentNBTTagCompound.setTag("Items", dataForAllSlots);
		return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location
		final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
		NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

		Arrays.fill(itemStacks, ItemStack.EMPTY);           // set all slots to empty EMPTY_ITEM
		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			byte slotNumber = dataForOneSlot.getByte("Slot");
			if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
				this.itemStacks[slotNumber] = new ItemStack(dataForOneSlot);
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
  //------------------------

	// set all slots to empty
	@Override
	public void clear() {
		Arrays.fill(itemStacks, ItemStack.EMPTY);  //EMPTY_ITEM
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

//	// Fields are used to send non-inventory information from the server to interested clients
//	// The container code caches the fields and sends the client any fields which have changed.
//	// The field ID is limited to byte, and the field value is limited to short. (if you use more than this, they get cast down
//	//   in the network packets)
//	// If you need more than this, or shorts are too small, use a custom packet in your container instead.
//
//	private static final byte COOK_FIELD_ID = 0;
//	private static final byte FIRST_BURN_TIME_REMAINING_FIELD_ID = 1;
//	private static final byte FIRST_BURN_TIME_INITIAL_FIELD_ID = FIRST_BURN_TIME_REMAINING_FIELD_ID + (byte)FUEL_SLOTS_COUNT;
//	private static final byte NUMBER_OF_FIELDS = FIRST_BURN_TIME_INITIAL_FIELD_ID + (byte)FUEL_SLOTS_COUNT;
//
	@Override
	public int getField(int id) {
//		if (id == COOK_FIELD_ID) return cookTime;
//		if (id >= FIRST_BURN_TIME_REMAINING_FIELD_ID && id < FIRST_BURN_TIME_REMAINING_FIELD_ID + FUEL_SLOTS_COUNT) {
//			return burnTimeRemaining[id - FIRST_BURN_TIME_REMAINING_FIELD_ID];
//		}
//		if (id >= FIRST_BURN_TIME_INITIAL_FIELD_ID && id < FIRST_BURN_TIME_INITIAL_FIELD_ID + FUEL_SLOTS_COUNT) {
//			return burnTimeInitialValue[id - FIRST_BURN_TIME_INITIAL_FIELD_ID];
//		}
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
//		if (id == COOK_FIELD_ID) {
//			cookTime = (short)value;
//		} else if (id >= FIRST_BURN_TIME_REMAINING_FIELD_ID && id < FIRST_BURN_TIME_REMAINING_FIELD_ID + FUEL_SLOTS_COUNT) {
//			burnTimeRemaining[id - FIRST_BURN_TIME_REMAINING_FIELD_ID] = value;
//		} else if (id >= FIRST_BURN_TIME_INITIAL_FIELD_ID && id < FIRST_BURN_TIME_INITIAL_FIELD_ID + FUEL_SLOTS_COUNT) {
//			burnTimeInitialValue[id - FIRST_BURN_TIME_INITIAL_FIELD_ID] = value;
//		} else {
//			System.err.println("[RETROCRAFT] Error: Invalid field ID in TileInventorySmelting.setField:" + id);
//		}
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

}
