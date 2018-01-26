package com.retrocraft.machine.grinder;

import com.retrocraft.common.ContainerBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerOreGrinder  extends ContainerBase
{
  private static final int INPUT_SLOTS_XPOS  = 80;
  private static final int INPUT_SLOTS_YPOS  = 14;
  private static final int OUTPUT_SLOTS_XPOS = 80;
  private static final int OUTPUT_SLOTS_YPOS = 58;
  
  private TileOreGrinder tileGrinder;

  public static final int INPUT_SLOTS_COUNT = 1;
  public static final int OUTPUT_SLOTS_COUNT = 1;

  private static final int INPUT_SLOT_NUMBER = 0;
  private static final int OUTPUT_SLOT_NUMBER = 1;
  
  public ContainerOreGrinder(InventoryPlayer invPlayer, TileOreGrinder tileGrinder)
  {
    super(true, false);
    this.tileGrinder = tileGrinder;
    this.guiInventoryPosX = 8;
    this.guiInventoryPosY = 97;
    this.guiHotbarPosX    = 8;
    this.guiHotbarPosY    = 155;

    addVanillaSlots(invPlayer);

    
    addSlotToContainer(new SlotInput(tileGrinder,
        INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));
    
    addSlotToContainer(new SlotInput(tileGrinder,
        OUTPUT_SLOT_NUMBER, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS));
  }

  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return tileGrinder.isUsableByPlayer(player);
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
      if (TileOreGrinder.isItemValidForInputSlot(sourceStack))
      {
        if (!this.mergeItemStack(sourceStack, customFirstSlotIndex,
            customFirstSlotIndex + 1,
            false))
        {
          return ItemStack.EMPTY;
        }
      } 
      else
      {
        return ItemStack.EMPTY;
      }
    } 
    else if (sourceSlotIndex >= customFirstSlotIndex)
    {
      if (!this.mergeItemStack(sourceStack, vanillaFirstSlotIndex,
          vanillaFirstSlotIndex + vanillaSlotCount, false))
      {
        return ItemStack.EMPTY;
      }
    } 
    else
    {
      System.err
          .print("[RETROCRAFT] Error: Invalid slotIndex:" + sourceSlotIndex);
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
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void updateProgressBar(int id, int data)
  {
    tileGrinder.setField(id, data);
  }

  public class SlotInput extends Slot
  {
    public SlotInput(IInventory inventoryIn, int index, int xPosition,
        int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileOreGrinder.isItemValidForInputSlot(stack);
    }
  }
  
  public class SlotOutput extends Slot {
    
    public SlotOutput(EntityPlayer player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
      return false;
    }
  }
}
