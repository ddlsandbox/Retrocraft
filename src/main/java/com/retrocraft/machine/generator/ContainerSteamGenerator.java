package com.retrocraft.machine.generator;

import com.retrocraft.common.ContainerBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerSteamGenerator  extends ContainerBase
{

  // Stores the tile entity instance for later use
  private TileSteamGenerator tileGenerator;

  public static final int INPUT_SLOTS_COUNT = 1;

  private static final int INPUT_SLOT_NUMBER = 0;

  public ContainerSteamGenerator(InventoryPlayer invPlayer, TileSteamGenerator tileGenerator)
  {
    super(true, false);
    this.tileGenerator = tileGenerator;
    this.guiInventoryPosX = 8;
    this.guiInventoryPosY = 97;
    this.guiHotbarPosX    = 8;
    this.guiHotbarPosY    = 155;

    addVanillaSlots(invPlayer);

    final int INPUT_SLOTS_XPOS = 80;
    final int INPUT_SLOTS_YPOS = 43;
    addSlotToContainer(new SlotFuel(tileGenerator,
        TileSteamGenerator.INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));
  }

  // Checks each tick to make sure the player is still able to access the
  // inventory and if not closes the gui
  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return tileGenerator.isUsableByPlayer(player);
  }

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
      if (TileSteamGenerator.isItemValidForFuelSlot(sourceStack))
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

  @Override
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();
    if (!tileGenerator.getStackInSlot(INPUT_SLOT_NUMBER)
        .isEmpty())
      for (IContainerListener listener : this.listeners)
      {
        // System.out.println("[RETROCRAFT] Container: Notify listeners!");
      }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void updateProgressBar(int id, int data)
  {
    tileGenerator.setField(id, data);
  }

  // SlotSmeltableInput is a slot for input items
  public class SlotFuel extends Slot
  {
    public SlotFuel(IInventory inventoryIn, int index, int xPosition,
        int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    // if this function returns false, the player won't be able to insert the
    // given item into this slot
    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileSteamGenerator.isItemValidForFuelSlot(stack);
    }
  }
}
