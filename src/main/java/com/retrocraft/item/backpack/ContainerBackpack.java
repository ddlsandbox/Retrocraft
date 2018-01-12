package com.retrocraft.item.backpack;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBackpack extends Container
{
  private final ItemStack backpackStack;
  private final BackpackInfo backpackInfo;
  private int blocked = -1;
  
  public ContainerBackpack(ItemStack backpackStack, InventoryPlayer inventoryPlayer, EnumHand hand) {
    this.backpackStack = backpackStack;
    BackpackInfo backpackInfo = BackpackInfo.fromStack(backpackStack);
    IItemHandler itemHandler = backpackInfo.getInventory();

    this.backpackInfo = backpackInfo;
    setupSlots(inventoryPlayer, itemHandler, hand);
}
  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer player) {
      return true;
  }

  @Override
  public void putStackInSlot(int slotID, ItemStack stack) {
      super.putStackInSlot(slotID, stack);
  }

  @Nonnull
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
      Slot slot = this.getSlot(slotIndex);

      if (!slot.canTakeStack(player))
          return slot.getStack();

      if (slotIndex == blocked)
          return ItemStack.EMPTY;

      if (!slot.getHasStack())
          return ItemStack.EMPTY;

      ItemStack stack = slot.getStack();
      ItemStack newStack = stack.copy();

      if (slotIndex < BackpackInfo.BACKPACK_SIZE) {
          if (!this.mergeItemStack(stack, BackpackInfo.BACKPACK_SIZE, this.inventorySlots.size(), true))
              return ItemStack.EMPTY;
          slot.onSlotChanged();
      } else if (!this.mergeItemStack(stack, 0, BackpackInfo.BACKPACK_SIZE, false))
          return ItemStack.EMPTY;

      if (stack.isEmpty())
          slot.putStack(ItemStack.EMPTY);
      else
          slot.onSlotChanged();

      return slot.onTake(player, newStack);
  }

  @Nonnull
  @Override
  public ItemStack slotClick(int slotId, int button, ClickType flag, EntityPlayer player) {
      if (slotId < 0 || slotId > inventorySlots.size())
          return super.slotClick(slotId, button, flag, player);

      Slot slot = inventorySlots.get(slotId);
      if (!canTake(slotId, slot, button, player, flag))
          return slot.getStack();

      return super.slotClick(slotId, button, flag, player);
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);
//      if (!(backpackStack.getItem() instanceof IBackpack)) {
//          RetroCraft.LOGGER.debug("Attempted to close backpack on non-IBackpack item {}. Changes will not persist.");
//          return;
//      }
      ((ItemBackpack) backpackStack.getItem()).updateBackpack(backpackStack, backpackInfo);
  }

  // Helper

  public boolean canTake(int slotId, Slot slot, int button, EntityPlayer player, ClickType clickType) {
      if (slotId == blocked)
          return false;

      if (clickType == ClickType.SWAP) {
          int hotbarId = BackpackInfo.BACKPACK_SIZE + 27 + button; // Backpack slots + main inventory + hotbar id
          if (hotbarId == blocked)
              return false;
      }

      if (slot.getStack().getItem() instanceof ItemBackpack)
          return false;

      return true;
  }

  /**
   * The localized display name of the backpack.
   *
   * @return - The name as a String
   */
  @Nonnull
  public String getName() {
      return backpackStack.getDisplayName();
  }

  /**
   * Gets the {@link BackpackSize} of the backpack.
   *
   * @return - The BackpackSize
   */
  public int getBackpackSize() {
      return BackpackInfo.BACKPACK_SIZE;
  }

  // Setup slots
  // All credit for code below here goes to copygirl (comments/javadocs/precondition checks mine though)

  /**
   * Sets up the backpack's slots, delegating where necessary.
   *
   * @param inventoryPlayer - The player's inventory
   * @param itemHandler     - The IItemHandler of the backpack
   */
  private void setupSlots(InventoryPlayer inventoryPlayer, IItemHandler itemHandler, EnumHand hand) {
      setupBackpackSlots(itemHandler);
      setupPlayerSlots(inventoryPlayer, hand);
  }

  /**
   * Sets up the slots for the backpack specifically.
   *
   * @param itemHandler - The {@link IItemHandler} for the backpack.
   */
  private void setupBackpackSlots(IItemHandler itemHandler) {
      int xOffset = 1 + getContainerInvXOffset();
      int yOffset = 1 + getBorderTop();
      for (int y = 0; y < BackpackInfo.BACKPACK_ROWS; y++, yOffset += 18)
          for (int x = 0; x < BackpackInfo.BACKPACK_COLS; x++)
              addSlotToContainer(new SlotItemHandler(itemHandler, x + y * BackpackInfo.BACKPACK_COLS, xOffset + x * 18, yOffset));
  }

  /**
   * Sets up the slots for the player specifically.
   *
   * @param inventoryPlayer - the {@link InventoryPlayer} for the player.
   */
  private void setupPlayerSlots(InventoryPlayer inventoryPlayer, EnumHand hand) {
      int xOffset = 1 + getPlayerInvXOffset();
      int yOffset = 1 + getBorderTop() + getContainerInvHeight() + getBufferInventory();

      //Inventory
      for (int y = 0; y < 3; y++, yOffset += 18)
          for (int x = 0; x < 9; x++)
              addSlotToContainer(new Slot(inventoryPlayer, x + y * 9 + 9, xOffset + x * 18, yOffset));

      //Hotbar
      yOffset += getBufferHotbar();
      for (int x = 0; x < 9; x++) {
          Slot slot = addSlotToContainer(new Slot(inventoryPlayer, x, xOffset + x * 18, yOffset) {
              @Override
              public boolean canTakeStack(final EntityPlayer playerIn) {
                  return slotNumber != blocked;
              }
          });
          if (x == inventoryPlayer.currentItem && hand == EnumHand.MAIN_HAND)
              blocked = slot.slotNumber;
      }
  }

  // GUI/slot setup helpers

  /**
   * Returns the size of the top border in pixels.
   */
  public int getBorderTop() {
      return 17;
  }

  /**
   * Returns the size of the side border in pixels.
   */
  public int getBorderSide() {
      return 7;
  }

  /**
   * Returns the size of the bottom border in pixels.
   */
  public int getBorderBottom() {
      return 7;
  }

  /**
   * Returns the space between container and player inventory in pixels.
   */
  public int getBufferInventory() {
      return 13;
  }

  /**
   * Returns the space between player inventory and hotbar in pixels.
   */
  public int getBufferHotbar() {
      return 4;
  }

  /**
   * Returns the size of the maximum number of columns possible.
   */
  public int getMaxColumns() {
      return BackpackInfo.BACKPACK_COLS;
  }

  /**
   * Returns the size of the maximum number of rows possible.
   */
  public int getMaxRows() {
      return BackpackInfo.BACKPACK_ROWS;
  }

  /**
   * Returns the total width of the container in pixels.
   */
  public int getWidth() {
      return Math.max(BackpackInfo.BACKPACK_COLS, 9) * 18 + getBorderSide() * 2;
  }

  /**
   * Returns the total height of the container in pixels.
   */
  public int getHeight() {
      return getBorderTop() + (BackpackInfo.BACKPACK_ROWS * 18) +
              getBufferInventory() + (4 * 18) +
              getBufferHotbar() + getBorderBottom();
  }

  /**
   * Returns the size of the container's width, only the inventory/slots, not the border, in pixels.
   */
  public int getContainerInvWidth() {
      return BackpackInfo.BACKPACK_COLS * 18;
  }

  /**
   * Returns the size of the container's height, only the inventory/slots, not the border, in pixels.
   */
  public int getContainerInvHeight() {
      return BackpackInfo.BACKPACK_ROWS * 18;
  }

  /**
   * Returns the size of the x offset for the backpack container in pixels.
   */
  public int getContainerInvXOffset() {
      return getBorderSide() + Math.max(0, (getPlayerInvWidth() - getContainerInvWidth()) / 2);
  }

  /**
   * Returns the size of the x offset for the player's inventory in pixels.
   */
  public int getPlayerInvXOffset() {
      return getBorderSide() + Math.max(0, (getContainerInvWidth() - getPlayerInvWidth()) / 2);
  }

  /**
   * Returns the size of the player's inventory width, not including the borders, in pixels.
   */
  public int getPlayerInvWidth() {
      return 9 * 18;
  }

  /**
   * Returns the size of the player's inventory height, including the hotbar, in pixels.
   */
  public int getPlayerInvHeight() {
      return 4 * 18 + getBufferHotbar();
  }

  public int getRowSize() {
      return Math.max(BackpackInfo.BACKPACK_ROWS, 9);
  }
}
