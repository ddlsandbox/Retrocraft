package com.retrocraft.machine.enchanter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnchanter extends Container
{

  private static final int MAINSLOT_X  = 36;
  private static final int MAINSLOT_Y  = 17;
  private static final int INVENTORY_X = 43;
  private static final int INVENTORY_Y = 91;
  private static final int EQUIPMENT_X = 6;
  private static final int EQUIPMENT_Y = 23;
  private static final int HOTBAR_X    = 43;
  private static final int HOTBAR_Y    = 149;

  private static final int SLOT_X_SPACING = 18;
  private static final int SLOT_Y_SPACING = 18;

  private static final int                  EQUIPMENT_SLOTS[]     =
  { 39, 38, 37, 36, 40 };
  public static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]
  { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
      EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET,
      EntityEquipmentSlot.OFFHAND };

  private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
  private final TileEntityEnchanter enchanter;

  public ContainerEnchanter(InventoryPlayer playerInv,
      final TileEntityEnchanter enchanter)
  {

    this.enchanter = enchanter;

    IItemHandler inventory = enchanter.getCapability(
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

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

    for (int i = 0; i < 3; i++)
    {
      for (int j = 0; j < 9; j++)
      {
        addSlotToContainer(
            new Slot(playerInv, j + i * 9 + 9, INVENTORY_X + j * SLOT_X_SPACING,
                INVENTORY_Y + i * SLOT_Y_SPACING));
      }
    }

    for (int k = 0; k < 5; k++)
    {
      final int armorType = k;
      final int slotNumber = EQUIPMENT_SLOTS[k];
      addSlotToContainer(
          new Slot(playerInv, slotNumber, EQUIPMENT_X, EQUIPMENT_Y + k * 19)
          {
            @Override
            public int getSlotStackLimit()
            {

              return 1;
            }

            @Override
            public boolean isItemValid(ItemStack par1ItemStack)
            {
              EntityEquipmentSlot entityEquipmentSlot = VALID_EQUIPMENT_SLOTS[armorType];
              Item item = (par1ItemStack == null ? null
                  : par1ItemStack.getItem());
              return item != null && item.isValidArmor(par1ItemStack,
                  entityEquipmentSlot, playerInv.player);
            }
          });
    }

    for (int k = 0; k < 9; k++)
    {
      addSlotToContainer(new Slot(playerInv, k, /* item */
          HOTBAR_X + k * SLOT_X_SPACING, /* x */
          HOTBAR_Y)); /* y */
    }
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
  public void onContainerClosed(EntityPlayer p_onContainerClosed_1_)
  {
    // TODO Auto-generated method stub
    super.onContainerClosed(p_onContainerClosed_1_);
    System.out.println("[RETROCRAFT] CONTAINER CLOSED");
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
  public ItemStack transferStackInSlot(EntityPlayer player, int index)
  {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(index);

    if (slot != null && slot.getHasStack())
    {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      int containerSlots = inventorySlots.size()
          - player.inventory.mainInventory.size();

      if (index < containerSlots)
      {
        if (!this.mergeItemStack(itemstack1, containerSlots,
            inventorySlots.size(), true))
        {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false))
      {
        return ItemStack.EMPTY;
      }

      if (itemstack1.getCount() == 0)
      {
        slot.putStack(ItemStack.EMPTY);
      }
      // else
      // {
      // slot.onSlotChanged();
      // }

      if (itemstack1.getCount() == itemstack.getCount())
      {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, itemstack1);
    }

    return itemstack;
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

    Slot slot = inventorySlots.get(0);
    ItemStack itemStack = slot.getStack();

    final HashMap<Enchantment, Integer> temp = new LinkedHashMap<Enchantment, Integer>();
    // final HashMap<Integer, Integer> temp2 = new LinkedHashMap<Integer,
    // Integer>();

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

    final ItemStack itemstack = inventorySlots.get(0).getStack();
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
        enchantmentDataList
            .add(new EnchantmentData(enchantment, map.get(enchantment)));

      if (!player.capabilities.isCreativeMode)
        if (serverCost < 0)
          player.addExperience(-serverCost);
        else
          player.addExperienceLevel(
              -EnchantHelper.getLevelsFromExperience(serverCost));
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

    final double costFactor = 1.0;
    final ItemStack itemStack = inventorySlots.get(0).getStack();
    if (itemStack == null)
      return 0;

    final int maxLevel = enchantment.getMaxLevel();

    if (enchantmentLevel > maxLevel)
    {
      return 0;
    }

    final int averageCost = (enchantment.getMinEnchantability(enchantmentLevel)
        + enchantment.getMaxEnchantability(enchantmentLevel)) / 2;
    int enchantability = itemStack.getItem().getItemEnchantability();

    if (enchantability < 1)
      enchantability = 1;

    int adjustedCost = (int) (averageCost
        * (enchantmentLevel - level + maxLevel)
        / ((double) maxLevel * enchantability));

    adjustedCost *= (costFactor / 3D);
    if (enchantability > 1)
    {
      adjustedCost *= Math.log(enchantability) / 2;
    } else
    {
      adjustedCost /= 10;
    }

    return Math.max(1, adjustedCost);
  }

  public int disenchantmentCost(Enchantment enchantment, int enchantmentLevel,
      Integer level)
  {

    final ItemStack itemStack = inventorySlots.get(0).getStack();
    final double costFactor = 1.0;

    if (itemStack == null)
      return 0;

    final int maxLevel = enchantment.getMaxLevel();

    if (enchantmentLevel > maxLevel)
      return 0;

    final int averageCost = (enchantment.getMinEnchantability(level)
        + enchantment.getMaxEnchantability(level)) / 2;
    int enchantability = itemStack.getItem().getItemEnchantability(itemStack);

    if (enchantability <= 1)
      enchantability = 10;

    int adjustedCost = (int) (averageCost
        * (enchantmentLevel - level - maxLevel)
        / ((double) maxLevel * enchantability));

    // int temp = (int) (adjustedCost * (60 / (bookCases() + 1)));
    // temp /= 20;
    // if (temp > adjustedCost) {
    // adjustedCost = temp;
    // }

    adjustedCost *= (costFactor / 4D);

    if (enchantability > 1)
      adjustedCost *= Math.log(enchantability) / 2;
    else
      adjustedCost /= 10;

    final int enchantmentCost = enchantmentCost(enchantment, level - 1,
        enchantmentLevel);

    return Math.min(adjustedCost, -enchantmentCost);
  }

  public int repairCostMax()
  {

    final double repairFactor = 1.5;
    final ItemStack itemStack = inventorySlots.get(0).getStack();
    if (itemStack == null)
      return 0;

    if (!itemStack.isItemEnchanted() || !itemStack.isItemDamaged())
      return 0;

    int cost = 0;

    final Map<Enchantment, Integer> enchantments = EnchantmentHelper
        .getEnchantments(itemStack);

    for (final Enchantment enchantment : enchantments.keySet())
    {
      final Integer enchantmentLevel = enchantments.get(enchantment);

      cost += enchantmentCost(enchantment, enchantmentLevel, 0);
    }

    final int maxDamage = itemStack.getMaxDamage();
    final int displayDamage = itemStack.getItemDamage();
    int enchantability = itemStack.getItem().getItemEnchantability(itemStack);

    if (enchantability <= 1)
    {
      enchantability = 10;
    }

    final double percentDamage = 1
        - (maxDamage - displayDamage) / (double) maxDamage;

    double totalCost = (percentDamage * cost) / enchantability;

    totalCost *= 2 * repairFactor;

    return (int) Math.max(1, totalCost);
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
