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

  private static final int INPUT_SLOTS_XPOS  = 80;
  private static final int INPUT_SLOTS_YPOS  = 14;
  private static final int OUTPUT_SLOTS_XPOS = 80;
  private static final int OUTPUT_SLOTS_YPOS = 58;

  // Stores the tile entity instance for later use
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
    this.guiInventoryPosY = 97;
    this.guiHotbarPosX    = 8;
    this.guiHotbarPosY    = 155;
    
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

  // Checks each tick to make sure the player is still able to access the
  // inventory and if not closes the gui
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

    // Check if the slot clicked is one of the vanilla container slots
    if (isVanillaSlot(sourceSlotIndex))
    {

      if (getSlot(INPUT_SLOT_NUMBER).isItemValid(sourceStack))
      {
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
    } else if (sourceSlotIndex == customFirstSlotIndex || sourceSlotIndex == customFirstSlotIndex + 1)
    {
      if (!this.mergeItemStack(sourceStack, vanillaFirstSlotIndex, vanillaFirstSlotIndex + vanillaSlotCount, false))
      {
        return ItemStack.EMPTY;
      }
    } else
    {
      System.err.print("[RETROCRAFT] Error: Invalid slotIndex:" + sourceSlotIndex);
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
    if (!tileInventoryFurnace.isEmpty())
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

  // public int repairCostMax()
  // {
  //
  // final double repairFactor = 1.5;
  // final ItemStack itemStack = inventorySlots.get(0).getStack();
  // if (itemStack == null)
  // return 0;
  //
  // if (!itemStack.isItemEnchanted() || !itemStack.isItemDamaged())
  // return 0;
  //
  // int cost = 0;
  //
  // final Map<Enchantment, Integer> enchantments = EnchantmentHelper
  // .getEnchantments(itemStack);
  //
  // for (final Enchantment enchantment : enchantments.keySet())
  // {
  // final Integer enchantmentLevel = enchantments.get(enchantment);
  //
  // cost += enchantmentCost(enchantment, enchantmentLevel, 0);
  // }
  //
  // final int maxDamage = itemStack.getMaxDamage();
  // final int displayDamage = itemStack.getItemDamage();
  // int enchantability = itemStack.getItem().getItemEnchantability(itemStack);
  //
  // if (enchantability <= 1)
  // {
  // enchantability = 10;
  // }
  //
  // final double percentDamage = 1
  // - (maxDamage - displayDamage) / (double) maxDamage;
  //
  // double totalCost = (percentDamage * cost) / enchantability;
  //
  // totalCost *= 2 * repairFactor;
  //
  // return (int) Math.max(1, totalCost);
  // }

  public class SlotRepairableInput extends Slot
  {
    public SlotRepairableInput(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

    // if this function returns false, the player won't be able to insert the
    // given item into this slot
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

    // if this function returns false, the player won't be able to insert the
    // given item into this slot
    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return false;
    }
  }
}
