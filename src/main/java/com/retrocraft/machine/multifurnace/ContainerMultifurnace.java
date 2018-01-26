package com.retrocraft.machine.multifurnace;

import com.retrocraft.common.ContainerBase;
import com.retrocraft.util.ItemUtil;

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
public class ContainerMultifurnace extends ContainerBase
{

  private TileMultifurnace tileInventoryFurnace;

  // These store cache values, used by the server to only update the client side
  // tile entity when values have changed
  private int[] cachedFields;

  public final int FUEL_SLOTS_COUNT = 4;
  public final int INPUT_SLOTS_COUNT = 5;
  public final int OUTPUT_SLOTS_COUNT = 5;
  public final int FURNACE_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

  private final int FIRST_FUEL_SLOT_NUMBER = 0;
  private final int FIRST_INPUT_SLOT_NUMBER = FIRST_FUEL_SLOT_NUMBER + FUEL_SLOTS_COUNT;
  private final int FIRST_OUTPUT_SLOT_NUMBER = FIRST_INPUT_SLOT_NUMBER + INPUT_SLOTS_COUNT;

  private int fuelFirstSlotIndex;
  private int inputFirstSlotIndex;
  private int outputFirstSlotIndex;

  public ContainerMultifurnace(InventoryPlayer invPlayer, TileMultifurnace tileInventoryFurnace)
  {
    super(true, false);

    guiInventoryPosX = 8;
    guiInventoryPosY = 125;
    guiHotbarPosX = 8;
    guiHotbarPosY = 183;

    this.tileInventoryFurnace = tileInventoryFurnace;

    fuelFirstSlotIndex = customFirstSlotIndex;
    inputFirstSlotIndex = fuelFirstSlotIndex + FUEL_SLOTS_COUNT;
    outputFirstSlotIndex = inputFirstSlotIndex + INPUT_SLOTS_COUNT;

    System.out.println("[RETROCRAFT] First Slot index = " + fuelFirstSlotIndex + " " + inputFirstSlotIndex + " "
        + outputFirstSlotIndex);

    addVanillaSlots(invPlayer);

    final int FUEL_SLOTS_XPOS = 53;
    final int FUEL_SLOTS_YPOS = 96;
    // Add the tile fuel slots
    for (int x = 0; x < FUEL_SLOTS_COUNT; x++)
    {
      int slotNumber = x + FIRST_FUEL_SLOT_NUMBER;
      addSlotToContainer(
          new SlotFuel(tileInventoryFurnace, slotNumber, FUEL_SLOTS_XPOS + guiSlotSpacingX * x, FUEL_SLOTS_YPOS));
    }

    final int INPUT_SLOTS_XPOS = 26;
    final int INPUT_SLOTS_YPOS = 24;
    // Add the tile input slots
    for (int y = 0; y < INPUT_SLOTS_COUNT; y++)
    {
      int slotNumber = y + FIRST_INPUT_SLOT_NUMBER;
      addSlotToContainer(new SlotSmeltableInput(tileInventoryFurnace, slotNumber, INPUT_SLOTS_XPOS,
          INPUT_SLOTS_YPOS + guiSlotSpacingY * y));
    }

    final int OUTPUT_SLOTS_XPOS = 134;
    final int OUTPUT_SLOTS_YPOS = 24;

    for (int y = 0; y < OUTPUT_SLOTS_COUNT; y++)
    {
      int slotNumber = y + FIRST_OUTPUT_SLOT_NUMBER;
      addSlotToContainer(
          new SlotOutput(tileInventoryFurnace, slotNumber, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS + guiSlotSpacingY * y));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return tileInventoryFurnace.isUsableByPlayer(player);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
  {
    Slot sourceSlot = (Slot) inventorySlots.get(sourceSlotIndex);
    if (sourceSlot == null || !sourceSlot.getHasStack())
      return ItemStack.EMPTY;
    ItemStack sourceStack = sourceSlot.getStack();
    ItemStack copyOfSourceStack = sourceStack.copy();

    if (isVanillaSlot(sourceSlotIndex))
    {
      if (!ItemUtil.getSmeltingResultForItem(sourceStack).isEmpty())
      {
        if (!mergeItemStack(sourceStack, inputFirstSlotIndex, inputFirstSlotIndex + INPUT_SLOTS_COUNT, false))
        {
          return ItemStack.EMPTY;
        }
      }
      else if (ItemUtil.getItemBurnTime(sourceStack) > 0)
      {
        if (!mergeItemStack(sourceStack, fuelFirstSlotIndex, fuelFirstSlotIndex + FUEL_SLOTS_COUNT, true))
        {
          return ItemStack.EMPTY;
        }
      }
      else
      {
        return ItemStack.EMPTY;
      }
    }
    else if (sourceSlotIndex >= fuelFirstSlotIndex && sourceSlotIndex < fuelFirstSlotIndex + FURNACE_SLOTS_COUNT)
    {
      if (!mergeItemStack(sourceStack, vanillaFirstSlotIndex, vanillaFirstSlotIndex + vanillaSlotCount, false))
      {
        return ItemStack.EMPTY;
      }
    }
    else
    {
      System.err.print("[RETROCRAFT] Error: Invalid slotIndex:" + sourceSlotIndex);
      return ItemStack.EMPTY;
    }

    if (sourceStack.getCount() == 0)
    {
      sourceSlot.putStack(ItemStack.EMPTY);
    }
    else
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

    boolean allFieldsHaveChanged = false;
    boolean fieldHasChanged[] = new boolean[tileInventoryFurnace.getFieldCount()];
    if (cachedFields == null)
    {
      cachedFields = new int[tileInventoryFurnace.getFieldCount()];
      allFieldsHaveChanged = true;
    }
    for (int i = 0; i < cachedFields.length; ++i)
    {
      if (allFieldsHaveChanged || cachedFields[i] != tileInventoryFurnace.getField(i))
      {
        cachedFields[i] = tileInventoryFurnace.getField(i);
        fieldHasChanged[i] = true;
      }
    }

    for (IContainerListener listener : this.listeners)
    {
      for (int fieldID = 0; fieldID < tileInventoryFurnace.getFieldCount(); ++fieldID)
      {
        if (fieldHasChanged[fieldID])
        {
          // Note that although sendProgressBarUpdate takes 2 ints on a server these are
          // truncated to shorts
          listener.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void updateProgressBar(int id, int data)
  {
    tileInventoryFurnace.setField(id, data);
  }

  public class SlotFuel extends Slot
  {
    public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileMultifurnace.isItemValidForFuelSlot(stack);
    }
  }

  public class SlotSmeltableInput extends Slot
  {
    public SlotSmeltableInput(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileMultifurnace.isItemValidForInputSlot(stack);
    }
  }

  public class SlotOutput extends Slot
  {
    public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileMultifurnace.isItemValidForOutputSlot(stack);
    }
  }
}
