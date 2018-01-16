package com.retrocraft.machine.repairer;

import com.retrocraft.tile.TileEntityInventory;
import com.retrocraft.util.ItemStackHandlerCustom;
import com.retrocraft.util.StackUtil;

import net.minecraft.item.ItemStack;

public class TileRepairer extends TileEntityInventory {
  
	// Create and initialize the itemStacks variable that will store store the itemStacks
	private static final int INPUT_SLOTS_COUNT = 1;
	private static final int OUTPUT_SLOTS_COUNT = 1;
	private static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	private static final int INPUT_SLOT_NUMBER = 0;
	private static final int OUTPUT_SLOT_NUMBER = 1;

	public TileRepairer()
	{
	  super("tile.repairer.name", INPUT_SLOTS_COUNT, OUTPUT_SLOTS_COUNT);
	}

	protected ItemStackHandlerCustom getSlots()
	{
	  return new ItemStackHandlerCustom(TOTAL_SLOTS_COUNT){
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
	}
	
	protected void setSlotSides()
	{
	  slotsTop = new int[] {0};
	  slotsBottom = new int[] {1};
	  slotsNorth = slotsEast = slotsSouth = slotsWest = new int[] {0,1};
	}

  //------------------------

	@Override
  public int getInventoryStackLimit() {
    return 1;
  }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == firstInputSlot && stack.getItemDamage() > 0;
	}

  /* custom methods */
  
  public boolean repairItem()
  {
    final ItemStack inputStack  = getStackInSlot(INPUT_SLOT_NUMBER);
    final ItemStack outputStack = getStackInSlot(OUTPUT_SLOT_NUMBER);
    if (inputStack.isEmpty() || !outputStack.isEmpty())
      return false;
    
    inputStack.setItemDamage(0);
    setStackInSlot(OUTPUT_SLOT_NUMBER, inputStack.copy());
    setStackInSlot(INPUT_SLOT_NUMBER, StackUtil.getNull());
    
    markDirty();
    return true;
  }
  
}
