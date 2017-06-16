package com.retrocraft.machine.repairer;

import com.retrocraft.common.ContainerBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: brandon3055 Date: 06/01/2015
 *
 * ContainerSmelting is used to link the client side gui to the server side
 * inventory and it is where you add the slots holding items. It is also used to
 * send server side data such as progress bars to the client for use in guis
 */
public class ContainerRepairer extends ContainerBase
{

  // Stores the tile entity instance for later use
  private TileRepairer tileInventoryFurnace;

  public static final int INPUT_SLOTS_COUNT = 1;

  public ContainerRepairer(InventoryPlayer invPlayer, TileRepairer tileRepairer)
  {
    super(true, true);
    this.tileInventoryFurnace = tileRepairer;

    addVanillaSlots(invPlayer);

    final int INPUT_SLOTS_XPOS = 37;
    final int INPUT_SLOTS_YPOS = 17;
    addSlotToContainer(new SlotRepairableInput(tileRepairer,
        TileRepairer.INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));
  }

  public boolean repairItem()
  {
    boolean repairOK = tileInventoryFurnace.repairItem();
    tileInventoryFurnace.update();
    tileInventoryFurnace.markDirty();
    return repairOK;
  }

  // Checks each tick to make sure the player is still able to access the
  // inventory and if not closes the gui
  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return tileInventoryFurnace.isUsableByPlayer(player);
  }

  // This is where you specify what happens when a player shift clicks a slot in
  // the gui
  // (when you shift click a slot in the TileEntity Inventory, it moves it to
  // the first available position in the hotbar and/or
  // player inventory. When you you shift-click a hotbar or player inventory
  // item, it moves it to the first available
  // position in the TileEntity inventory - either input or fuel as appropriate
  // for the item you clicked)
  // At the very least you must override this and return EMPTY_ITEM or the game
  // will crash when the player shift clicks a slot
  // returns EMPTY_ITEM if the source slot is empty, or if none of the source
  // slot items could be moved.
  // otherwise, returns a copy of the source stack
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
  {
    Slot sourceSlot = (Slot) inventorySlots.get(sourceSlotIndex);

    if (sourceSlot == null || !sourceSlot.getHasStack())
      return ItemStack.EMPTY; // EMPTY_ITEM
    ItemStack sourceStack = sourceSlot.getStack();
    ItemStack copyOfSourceStack = sourceStack.copy();

    // Check if the slot clicked is one of the vanilla container slots
    if (isVanillaSlot(sourceSlotIndex))
    {

      // This is a vanilla container slot
      // If the stack is repairable try to merge merge the stack into the input
      // slots
      if (TileRepairer.isItemValidForInputSlot(sourceStack))
      { // isEmptyItem
        if (!this.mergeItemStack(sourceStack, customFirstSlotIndex, // TileRepairer.INPUT_SLOT_NUMBER,
            customFirstSlotIndex + 1, // TileRepairer.INPUT_SLOT_NUMBER+1,
            false))
        {
          return ItemStack.EMPTY;
        }
        //
      } else
      {
        return ItemStack.EMPTY; // EMPTY_ITEM;
      }
    } else if (sourceSlotIndex == customFirstSlotIndex)
    {
      if (!this.mergeItemStack(sourceStack, vanillaFirstSlotIndex,
          vanillaFirstSlotIndex + vanillaSlotCount, false))
      {
        return ItemStack.EMPTY;
      }
    } else
    {
      System.err
          .print("[RETROCRAFT] Error: Invalid slotIndex:" + sourceSlotIndex);
      return ItemStack.EMPTY; // EMPTY_ITEM;
    }

    // If stack size == 0 (the entire stack was moved) set slot contents to null
    if (sourceStack.getCount() == 0)
    { // getStackSize()
      sourceSlot.putStack(ItemStack.EMPTY); // Empty Item
    } else
    {
      sourceSlot.onSlotChanged();
    }

    sourceSlot.onTake(player, sourceStack); // onPickupFromSlot()
    return copyOfSourceStack;
  }

  /* Client Synchronization */

  // This is where you check if any values have changed and if so send an update
  // to any clients accessing this container
  // The container itemstacks are tested in Container.detectAndSendChanges, so
  // we don't need to do that
  // We iterate through all of the TileEntity Fields to find any which have
  // changed, and send them.
  // You don't have to use fields if you don't wish to; just manually match the
  // ID in sendProgressBarUpdate with the value in
  // updateProgressBar()
  // The progress bar values are restricted to shorts. If you have a larger
  // value (eg int), it's not a good idea to try and split it
  // up into two shorts because the progress bar values are sent independently,
  // and unless you add synchronisation logic at the
  // receiving side, your int value will be wrong until the second short
  // arrives. Use a custom packet instead.
  @Override
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();
    if (!tileInventoryFurnace.getStackInSlot(TileRepairer.INPUT_SLOT_NUMBER)
        .isEmpty())
      for (IContainerListener listener : this.listeners)
      {
        // System.out.println("[RETROCRAFT] Container: Notify listeners!");
      }
  }

  // Called when a progress bar update is received from the server. The two
  // values (id and data) are the same two
  // values given to sendProgressBarUpdate. In this case we are using fields so
  // we just pass them to the tileEntity.
  @SideOnly(Side.CLIENT)
  @Override
  public void updateProgressBar(int id, int data)
  {
    tileInventoryFurnace.setField(id, data);
  }

  // SlotSmeltableInput is a slot for input items
  public class SlotRepairableInput extends Slot
  {
    public SlotRepairableInput(IInventory inventoryIn, int index, int xPosition,
        int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    // if this function returns false, the player won't be able to insert the
    // given item into this slot
    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileRepairer.isItemValidForInputSlot(stack);
    }
  }
}
