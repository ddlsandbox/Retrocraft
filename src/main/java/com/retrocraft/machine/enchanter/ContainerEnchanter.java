package com.retrocraft.machine.enchanter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.retrocraft.RetroCraft;
import com.retrocraft.common.ContainerBase;
import com.retrocraft.util.StackUtil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnchanter extends ContainerBase
{

  private static final int MAINSLOT_X = 36;
  private static final int MAINSLOT_Y = 17;

  private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
  private final TileEntityEnchanter enchanter;

  public ContainerEnchanter(InventoryPlayer playerInv,
                            final TileEntityEnchanter enchanter)
  {
    super(true, true);

    guiHotbarPosX = 43;
    guiHotbarPosY = 194;
    guiInventoryPosX = 43;
    guiInventoryPosY = 136;
    guiEquipmentPosX = 7;
    guiEquipmentPosY = 24;

    this.enchanter = enchanter;

    IItemHandler inventory = enchanter.getCapability(
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

    addVanillaSlots(playerInv);

    addSlotToContainer( /* custom slot */
        new SlotItemHandler(inventory, 0, MAINSLOT_X, MAINSLOT_Y)
        {

          @Override
          public int getSlotStackLimit()
          {
            return 1;
          }

          @Override
          public boolean isItemValid(ItemStack itemStack)
          {
            return itemStack.getItem() != Items.BOOK
                && (itemStack.isItemEnchantable()
                    || itemStack.isItemEnchanted());
          }

          @Override
          public void onSlotChanged()
          {
            readItems();
            enchanter.markDirty();
          }
        });
  }

  public Map<Enchantment, Integer> getEnchantments()
  {
    return enchantments;
  }

  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return true;
  }

  @Override
  public void onContainerClosed(EntityPlayer player)
  {
    // TODO Drop item?
    super.onContainerClosed(player);
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
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(sourceSlotIndex);

    if (slot != null && slot.getHasStack())
    {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      // int containerSlots = inventorySlots.size()
      // - player.inventory.mainInventory.size();

      if (isVanillaSlot(sourceSlotIndex))
      {
        if (!this.mergeItemStack(itemstack1, customFirstSlotIndex,
            customFirstSlotIndex + 1, false))
        {
          return ItemStack.EMPTY;
        }
      } else
      {
        if (!this.mergeItemStack(itemstack1, vanillaFirstSlotIndex,
            vanillaFirstSlotIndex + vanillaSlotCount, true))
        {
          return ItemStack.EMPTY;
        }
      }

      if (itemstack1.getCount() == 0)
      {
        slot.putStack(ItemStack.EMPTY);
      }

      if (itemstack1.getCount() == itemstack.getCount())
      {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, itemstack1);
    }

    return itemstack;
  }

  public ItemStack getItem()
  {
    return inventorySlots.get(customFirstSlotIndex).getStack();
  }

  /**
   * Calculates the amount of experience to give the player for disenchanting
   * their item.
   *
   * @param enchantment:
   *          The enchantment being removed.
   * @param enchantmentLevel:
   *          The new amount of levels for the enchantment effect.
   * @param existingLevel:
   *          The amount of levels for the enchantment effect before updating.
   * @return int: The amount of experience points to give the player.
   */
  public int getRebate(Enchantment enchantment, int enchantmentLevel,
      Integer existingLevel)
  {

    final ItemStack stack = inventorySlots.get(customFirstSlotIndex).getStack();

    if (!StackUtil.isValid(stack)
        && enchantmentLevel > enchantment.getMaxLevel())
      return 0;

    final int oldCost = (int) ((enchantment.getMaxEnchantability(existingLevel)
        - stack.getItem().getItemEnchantability(stack)) / 2
        * RetroCraft.getConfig().enchantmentCostFactor);
    final int newCost = (int) ((enchantment.getMaxEnchantability(
        enchantmentLevel) - stack.getItem().getItemEnchantability(stack)) / 2
        * RetroCraft.getConfig().enchantmentCostFactor);
    final int returnAmount = (oldCost - newCost) / 2;
    return -returnAmount;
    // -EnchantHelper
    // .getExperienceFromLevel(returnAmount > 0 ? returnAmount : 0);
  }

  private void addEnchantsFor(ItemStack itemStack,
      HashMap<Enchantment, Integer> temp)
  {

    for (int i : EnchantHelper.ALL_ENCHANTMENTS)
    {
      final Enchantment obj = Enchantment.getEnchantmentByID(i);
      addEnchantFor(itemStack, temp, obj);
    }
  }

  private void addEnchantFor(ItemStack itemStack,
      HashMap<Enchantment, Integer> temp, Enchantment obj)
  {
    if (obj != null && obj.canApplyAtEnchantingTable(itemStack))
    {
      temp.put(obj, EnchantmentHelper.getEnchantmentLevel(obj, itemStack));
    }
  }

  /**
   * Will read the enchantments on the items and ones the can be added to the
   * items
   */
  private void readItems()
  {

    Slot slot = inventorySlots.get(customFirstSlotIndex);
    ItemStack itemStack = slot.getStack();

    final HashMap<Enchantment, Integer> temp = new LinkedHashMap<Enchantment, Integer>();

    if (itemStack != null)
    {
      if (EnchantHelper.isItemEnchantable(itemStack))
      {
        addEnchantsFor(itemStack, temp);
      }

      if (enchantments != temp)
      {
        enchantments = temp;
      }
    } else
    {
      enchantments = temp;
    }
  }

  public boolean canPurchase(EntityPlayer player, int cost)
  {

    if (player.capabilities.isCreativeMode)
    {
      return true;
    }

    if (player.experienceLevel < cost)
    {
      player.sendMessage(
          new TextComponentString("Not enough levels. Required " + cost));
      return false;
    }
    return true;
  }

  public boolean enchant(EntityPlayer player, HashMap<Enchantment, Integer> map,
      int cost) throws Exception
  {

    final ItemStack itemstack = inventorySlots.get(customFirstSlotIndex)
        .getStack();
    final HashMap<Enchantment, Integer> temp = new HashMap<Enchantment, Integer>();
    int serverCost = 0;

    if (itemstack == null)
      return false;

    for (final Enchantment enchantment : map.keySet())
    {
      final Integer level = map.get(enchantment);
      final Integer startingLevel = enchantments.get(enchantment);
      if (level > startingLevel)
        serverCost += enchantmentCost(enchantment, level, startingLevel);
      else if (level < startingLevel)
        serverCost += disenchantmentCost(enchantment, level, startingLevel);
    }

    if (cost != serverCost)
    {
      throw new Exception("Cost is different on client and server " + cost
          + " vs " + serverCost);
    }

    for (final Enchantment enchantment : enchantments.keySet())
    {
      final Integer level = enchantments.get(enchantment);

      if (level != 0)
      {
        if (!map.containsKey(enchantment))
        {
          map.put(enchantment, level);
        }
      }
    }

    for (final Enchantment enchantment : map.keySet())
    {
      final Integer level = map.get(enchantment);

      if (level == 0)
        temp.put(enchantment, level);

    }
    for (Enchantment object : temp.keySet())
    {
      map.remove(object);
    }

    if (canPurchase(player, serverCost))
    {
      final List<EnchantmentData> enchantmentDataList = new ArrayList<>();

      for (final Enchantment enchantment : map.keySet())
      {
        enchantmentDataList
            .add(new EnchantmentData(enchantment, map.get(enchantment)));
      }

      if (!player.capabilities.isCreativeMode)
      {
        player.addExperience(-serverCost);
      }
    }

    enchanter.enchantCurrent(map);
    readItems();
    enchanter.markDirty();
    // enchanter.sendUpdate();
    return true;
  }

  public int enchantmentCost(Enchantment enchantment, int enchantmentLevel,
      Integer level)
  {

    final ItemStack itemStack = inventorySlots.get(customFirstSlotIndex)
        .getStack();
    if (itemStack == null)
      return 0;

    final int maxLevel = enchantment.getMaxLevel();

    if (enchantmentLevel > maxLevel)
    {
      return 0;
    }

    return EnchantHelper.calculateEnchantmentCost(enchantment,
        enchantmentLevel + level);
    // return EnchantHelper.getExperienceFromLevel(
    // EnchantHelper.calculateEnchantmentCost(enchantment, enchantmentLevel +
    // level));
  }

  public int disenchantmentCost(Enchantment enchantment, int enchantmentLevel,
      Integer level)
  {

    return -enchantmentCost(enchantment, level, enchantmentLevel) + 1;
    // final ItemStack itemStack = inventorySlots.get(customFirstSlotIndex)
    // .getStack();
    // final double costFactor = 1.0;
    //
    // if (itemStack == null)
    // return 0;
    //
    // final int maxLevel = enchantment.getMaxLevel();
    //
    // if (enchantmentLevel > maxLevel)
    // return 0;
    //
    // final int averageCost = (enchantment.getMinEnchantability(level)
    // + enchantment.getMaxEnchantability(level)) / 2;
    // int enchantability =
    // itemStack.getItem().getItemEnchantability(itemStack);
    //
    // if (enchantability <= 1)
    // enchantability = 10;
    //
    // int adjustedCost = (int) (averageCost
    // * (enchantmentLevel - level - maxLevel)
    // / ((double) maxLevel * enchantability));
    //
    // // int temp = (int) (adjustedCost * (60 / (bookCases() + 1)));
    // // temp /= 20;
    // // if (temp > adjustedCost) {
    // // adjustedCost = temp;
    // // }
    //
    // adjustedCost *= (costFactor / 4D);
    //
    // if (enchantability > 1)
    // adjustedCost *= Math.log(enchantability) / 2;
    // else
    // adjustedCost /= 10;
    //
    // final int enchantmentCost = enchantmentCost(enchantment, level - 1,
    // enchantmentLevel);
    //
    // return Math.min(adjustedCost, -enchantmentCost);
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

    // boolean allFieldsHaveChanged = false;
    // boolean fieldHasChanged [] = new
    // boolean[tileInventoryFurnace.getFieldCount()];
    // if (cachedFields == null) {
    // cachedFields = new int[tileInventoryFurnace.getFieldCount()];
    // allFieldsHaveChanged = true;
    // }
    // for (int i = 0; i < cachedFields.length; ++i) {
    // if (allFieldsHaveChanged || cachedFields[i] !=
    // tileInventoryFurnace.getField(i)) {
    // cachedFields[i] = tileInventoryFurnace.getField(i);
    // fieldHasChanged[i] = true;
    // }
    // }
    //
    // // go through the list of listeners (players using this container) and
    // update them if necessary
    // for (IContainerListener listener : this.listeners) {
    // for (int fieldID = 0; fieldID < tileInventoryFurnace.getFieldCount();
    // ++fieldID) {
    // if (fieldHasChanged[fieldID]) {
    // // Note that although sendProgressBarUpdate takes 2 ints on a server
    // these are truncated to shorts
    // listener.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
    // }
    // }
    // }
  }

}
