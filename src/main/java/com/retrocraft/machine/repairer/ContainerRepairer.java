package com.retrocraft.machine.repairer;

import com.retrocraft.common.ContainerBase;
import com.retrocraft.util.StackUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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

  private static final int INPUT_SLOTS_XPOS  = 80;
  private static final int INPUT_SLOTS_YPOS  = 14;
  private static final int OUTPUT_SLOTS_XPOS = 80;
  private static final int OUTPUT_SLOTS_YPOS = 58;

  private TileRepairer tileInventoryFurnace;

  public static final int INPUT_SLOTS_COUNT = 1;
  public static final int OUTPUT_SLOTS_COUNT = 1;

  private static final int INPUT_SLOT_NUMBER = 0;
  private static final int OUTPUT_SLOT_NUMBER = 1;

  public ContainerRepairer(InventoryPlayer invPlayer, TileRepairer tileRepairer)
  {
    super(true, true);
    this.tileInventoryFurnace = tileRepairer;
    this.guiInventoryPosX = 8;
    this.guiInventoryPosY = 104;
    this.guiHotbarPosX    = 8;
    this.guiHotbarPosY    = 162;
    this.guiEquipmentPosX = 45;
    this.guiEquipmentPosY = 8;
    
    addVanillaSlots(invPlayer);

    addSlotToContainer(new SlotRepairableInput(tileRepairer, INPUT_SLOT_NUMBER, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS));

    addSlotToContainer(
        new SlotRepairableOutput(tileRepairer, OUTPUT_SLOT_NUMBER, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS));
  }

  public boolean repairItem()
  {
    boolean repairOK = tileInventoryFurnace.repairItem() > -1;
    tileInventoryFurnace.update();
    tileInventoryFurnace.markDirty();
    return repairOK;
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
      return StackUtil.getNull();
    ItemStack sourceStack = sourceSlot.getStack();
    ItemStack copyOfSourceStack = sourceStack.copy();

    if (isVanillaSlot(sourceSlotIndex))
    {

      if (getSlot(INPUT_SLOT_NUMBER).isItemValid(sourceStack))
      {
        if (!this.mergeItemStack(sourceStack, customFirstSlotIndex,
            customFirstSlotIndex + 1,
            false))
        {
          return StackUtil.getNull();
        }
      }
      else
      {
        return StackUtil.getNull();
      }
    }
    else
    {
      /* test equipment slots first */
      if (!this.mergeItemStack(sourceStack, equipmentFirstSlotIndex, equipmentFirstSlotIndex + equipmentSlotCount, true) &&
          !this.mergeItemStack(sourceStack, vanillaFirstSlotIndex, vanillaFirstSlotIndex + vanillaSlotCount, false))
      {
        return StackUtil.getNull();
      }
    }
    
    if (sourceStack.getCount() == 0)
    {
      sourceSlot.putStack(StackUtil.getNull());
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
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void updateProgressBar(int id, int data)
  {
    tileInventoryFurnace.setField(id, data);
  }

  public class SlotRepairableInput extends Slot
  {
    public SlotRepairableInput(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return stack.getItemDamage() > 0;
    }
  }

  public class SlotRepairableOutput extends Slot
  {
    public SlotRepairableOutput(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return false;
    }
  }
}
