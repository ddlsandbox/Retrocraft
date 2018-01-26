package com.retrocraft.machine.generator;

import com.retrocraft.common.ContainerBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerManureGenerator  extends ContainerBase
{

  private TileManureGenerator tileGenerator;

  public static final int INPUT_SLOTS_COUNT = 1;

  public ContainerManureGenerator(InventoryPlayer invPlayer, TileManureGenerator tileGenerator)
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
        TileManureGenerator.INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));
  }

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

    if (isVanillaSlot(sourceSlotIndex))
    {
      if (TileManureGenerator.isItemValidForFuelSlot(sourceStack))
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
    else if (sourceSlotIndex == customFirstSlotIndex)
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

    sourceSlot.onTake(player, sourceStack);
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
    tileGenerator.setField(id, data);
  }

  public class SlotFuel extends Slot
  {
    public SlotFuel(IInventory inventoryIn, int index, int xPosition,
        int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return TileManureGenerator.isItemValidForFuelSlot(stack);
    }
  }
}
