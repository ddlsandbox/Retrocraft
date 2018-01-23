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

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
  {
    ItemStack copyOfSourceStack = StackUtil.getNull();
    Slot slot = inventorySlots.get(sourceSlotIndex);

    if (slot != null && slot.getHasStack())
    {
      ItemStack sourceStack = slot.getStack();
      copyOfSourceStack = sourceStack.copy();

      if (isVanillaSlot(sourceSlotIndex))
      {
        if (!this.mergeItemStack(sourceStack, customFirstSlotIndex,
            customFirstSlotIndex + 1, false))
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
        slot.putStack(StackUtil.getNull());
      }

      if (sourceStack.getCount() == copyOfSourceStack.getCount())
      {
        return StackUtil.getNull();
      }

      slot.onTake(player, sourceStack);
    }

    return copyOfSourceStack;
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
        player.addExperienceLevel(-serverCost);
      }
    }

    enchanter.enchantCurrent(map);
    readItems();
    enchanter.markDirty();

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
  }

  public int disenchantmentCost(Enchantment enchantment, int enchantmentLevel,
      Integer level)
  {

    return -enchantmentCost(enchantment, level, enchantmentLevel) + 1;
  }

  /* Client Synchronization */

  @Override
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();
  }

}
