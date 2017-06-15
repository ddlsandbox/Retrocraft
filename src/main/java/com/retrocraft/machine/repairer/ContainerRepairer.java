package com.retrocraft.machine.repairer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * ContainerSmelting is used to link the client side gui to the server side inventory and it is where
 * you add the slots holding items. It is also used to send server side data such as progress bars to the client
 * for use in guis
 */
public class ContainerRepairer extends Container {

	// Stores the tile entity instance for later use
	private TileRepairer tileInventoryFurnace;

	// These store cache values, used by the server to only update the client side tile entity when values have changed
	private int [] cachedFields;

	// must assign a slot index to each of the slots used by the GUI.
	// For this container, we can see the furnace fuel, input, and output slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container using addSlotToContainer(), it automatically increases the slotIndex, which means

	public static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { 
			EntityEquipmentSlot.HEAD, 
			EntityEquipmentSlot.CHEST, 
			EntityEquipmentSlot.LEGS, 
			EntityEquipmentSlot.FEET, 
			EntityEquipmentSlot.OFFHAND };
	
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int PLAYER_EQUIPMENT_SLOT_COUNT = 5;
		
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + 
												  PLAYER_INVENTORY_SLOT_COUNT + 
												  PLAYER_EQUIPMENT_SLOT_COUNT;

	// slots for armor
	private static final int FIRST_ARMOR_SLOT_NUMBER = PLAYER_INVENTORY_SLOT_COUNT + HOTBAR_SLOT_COUNT;
	
	public static final int INPUT_SLOTS_COUNT = 1;

	// slot index is the unique index for all slots in this container i.e. 
	//   0 - 35 for invPlayer 
	//  36 - 40 for equipment
	//       41 for current slot
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int FIRST_INPUT_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	public ContainerRepairer(InventoryPlayer invPlayer, TileRepairer tileRepairer) {
		this.tileInventoryFurnace = tileRepairer;

		final int SLOT_X_SPACING = 18;
		final int SLOT_Y_SPACING = 18;
		final int HOTBAR_XPOS = 37;
		final int HOTBAR_YPOS = 107;
		// Add the players hotbar to the gui - the [xpos, ypos] location of each item
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlotToContainer(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}

		// Add the rest of the players inventory to the gui
		final int PLAYER_INVENTORY_XPOS = 37;
		final int PLAYER_INVENTORY_YPOS = 47;
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlotToContainer(new Slot(invPlayer, slotNumber,  xpos, ypos));
			}
		}

		// Add the equipment inventory to the gui
		final int PLAYER_EQUIPMENT_XPOS = 7;
		final int PLAYER_EQUIPMENT_YPOS = 24;
		for (int k = 0; k<PLAYER_EQUIPMENT_SLOT_COUNT; k++) {
            final int armorType = k;
            final int slotNumber = k<4?(FIRST_ARMOR_SLOT_NUMBER + 3 - k):40;
            addSlotToContainer(new Slot(
            		invPlayer, 
            		slotNumber, 
            		PLAYER_EQUIPMENT_XPOS, 
            		PLAYER_EQUIPMENT_YPOS + k * 19) {
                @Override
                public int getSlotStackLimit () {
                    
                    return 1;
                }
                
                @Override
                public boolean isItemValid (ItemStack par1ItemStack) {
                	EntityEquipmentSlot entityEquipmentSlot = VALID_EQUIPMENT_SLOTS[armorType];                    
                    Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
                    return item != null && item.isValidArmor(par1ItemStack, entityEquipmentSlot , invPlayer.player);
                }
            });
        }

		final int INPUT_SLOTS_XPOS = 37;
		final int INPUT_SLOTS_YPOS = 17;
		addSlotToContainer(
				new SlotRepairableInput(
						tileRepairer, 
						TileRepairer.INPUT_SLOT_NUMBER, 
						INPUT_SLOTS_XPOS, 
						INPUT_SLOTS_YPOS));
	}

	public boolean repairItem()
	{
		boolean repairOK = tileInventoryFurnace.repairItem(); 
		tileInventoryFurnace.update();
		tileInventoryFurnace.markDirty();
		return repairOK;
	}
	// Checks each tick to make sure the player is still able to access the inventory and if not closes the gui
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileInventoryFurnace.isUsableByPlayer(player);
	}

	// This is where you specify what happens when a player shift clicks a slot in the gui
	//  (when you shift click a slot in the TileEntity Inventory, it moves it to the first available position in the hotbar and/or
	//    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
	//    position in the TileEntity inventory - either input or fuel as appropriate for the item you clicked)
	// At the very least you must override this and return EMPTY_ITEM or the game will crash when the player shift clicks a slot
	// returns EMPTY_ITEM if the source slot is empty, or if none of the source slot items could be moved.
	//   otherwise, returns a copy of the source stack
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
	{
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && 
			sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			
			// This is a vanilla container slot
			// If the stack is repairable try to merge merge the stack into the input slots
			if (TileRepairer.isItemValidForInputSlot(sourceStack)){  //isEmptyItem
                if(!this.mergeItemStack(sourceStack, 
                						FIRST_INPUT_SLOT_INDEX, //TileRepairer.INPUT_SLOT_NUMBER, 
                						FIRST_INPUT_SLOT_INDEX+1, //TileRepairer.INPUT_SLOT_NUMBER+1, 
                						false)) {
                    return ItemStack.EMPTY;
                }
                //
			} else {
				return ItemStack.EMPTY;  //EMPTY_ITEM;
			}
		} else if(sourceSlotIndex == FIRST_INPUT_SLOT_INDEX){
            if(!this.mergeItemStack(sourceStack, 
            		VANILLA_FIRST_SLOT_INDEX, 
            		VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, 
            		false)){
                return ItemStack.EMPTY;
            }
        } else {
			System.err.print("[RETROCRAFT] Error: Invalid slotIndex:" + sourceSlotIndex);
			return ItemStack.EMPTY;  //EMPTY_ITEM;
		}

		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {  //getStackSize()
			sourceSlot.putStack(ItemStack.EMPTY);  // Empty Item
		} else {
			sourceSlot.onSlotChanged();
		}

		sourceSlot.onTake(player, sourceStack);  // onPickupFromSlot()
		return copyOfSourceStack;
	}

	/* Client Synchronization */

	// This is where you check if any values have changed and if so send an update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match the ID in sendProgressBarUpdate with the value in
	//   updateProgressBar()
	// The progress bar values are restricted to shorts.  If you have a larger value (eg int), it's not a good idea to try and split it
	//   up into two shorts because the progress bar values are sent independently, and unless you add synchronisation logic at the
	//   receiving side, your int value will be wrong until the second short arrives.  Use a custom packet instead.
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!tileInventoryFurnace.getStackInSlot(TileRepairer.INPUT_SLOT_NUMBER).isEmpty())
			for (IContainerListener listener : this.listeners) {
				//System.out.println("[RETROCRAFT] Container: Notify listeners!");
			}
	}

	// Called when a progress bar update is received from the server. The two values (id and data) are the same two
	// values given to sendProgressBarUpdate.  In this case we are using fields so we just pass them to the tileEntity.
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileInventoryFurnace.setField(id, data);
	}

	// SlotSmeltableInput is a slot for input items
	public class SlotRepairableInput extends Slot {
		public SlotRepairableInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return TileRepairer.isItemValidForInputSlot(stack);
		}
	}
}
