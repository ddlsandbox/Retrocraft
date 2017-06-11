package com.retrocraft.block.enchantorium;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.TileEntityEnchantorium;
import com.retrocraft.network.PacketUpdateEnchantorium;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnchantorium extends Container {

	private static final int MAINSLOT_X = 36;
	private static final int MAINSLOT_Z = 17;
	private static final int INVENTORY_X = 43;
	private static final int INVENTORY_Z = 91;
	private static final int HOTBAR_X = 43;
	private static final int HOTBAR_Z = 149;

	private Map<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
	private final TileEntityEnchantorium enchantorium;

	private IInventory last_inventory;
	
	public ContainerEnchantorium(InventoryPlayer playerInv, final TileEntityEnchantorium enchantorium) {

		System.out.println("[RETROCRAFT] Created enchantorium container");

		this.enchantorium = enchantorium;

		IItemHandler inventory = enchantorium.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
				EnumFacing.NORTH);

		addSlotToContainer(new SlotItemHandler(inventory, 0, MAINSLOT_X, MAINSLOT_Z) {

			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValid(ItemStack par1ItemStack) {

				return par1ItemStack.isItemEnchantable() || par1ItemStack.isItemEnchanted();
			}

			@Override
			public void onSlotChanged() {
				enchantorium.markDirty();
			}
		});

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, INVENTORY_X + j * 18, INVENTORY_Z + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			addSlotToContainer(new Slot(playerInv, k, /* item */
					HOTBAR_X + k * 18, /* x */
					HOTBAR_Z)); /* z */
		}
	}

	public Map<Integer, Integer> getEnchantments() {

		return enchantments;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		System.out.println("[RETROCRAFT] Craftchange");
		super.onCraftMatrixChanged(par1IInventory);

		last_inventory = par1IInventory;
		// enchantorium.markDirty();
		readItems();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		readItems();

		return itemstack;
	}

	private void addEnchantsFor(ItemStack itemStack, HashMap<Integer, Integer> temp) {

		for (int i : EnchantHelper.ALL_ENCHANTMENTS) {
			final Enchantment obj = Enchantment.getEnchantmentByID(i);
			addEnchantFor(itemStack, temp, obj);
		}
	}

	private void addEnchantFor(ItemStack itemStack, HashMap<Integer, Integer> temp, Enchantment obj) {

		if (obj != null && obj.canApplyAtEnchantingTable(itemStack)) {
			temp.put(Enchantment.getEnchantmentID(obj), 0);
		}
	}

	/**
	 * Will read the enchantments on the items and ones the can be added to the
	 * items
	 */
	private void readItems() {

		System.out.println("[RETROCRAFT] Read enchantorium items");

		Slot slot = inventorySlots.get(0);
		ItemStack itemStack = slot.getStack();

		final HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
		final HashMap<Integer, Integer> temp2 = new LinkedHashMap<Integer, Integer>();

		if (itemStack != null) {
			if (EnchantHelper.isItemEnchantable(itemStack)) {
				addEnchantsFor(itemStack, temp);
			}
			// else if (EnchantHelper.isItemEnchanted(itemStack)
			// && EnchantHelper.isNewItemEnchantable(itemStack.getItem())) {
			// temp.putAll(EnchantmentHelper.getEnchantments(itemStack));
			//
			// for (final Enchantment obj : Enchantment.enchantmentsList) {
			// if (obj == null)
			// continue;
			//
			// boolean add = true;
			// for (final Integer enc : temp.keySet()) {
			//
			// final Enchantment enchantment = Utilities.getEnchantment(enc);
			// if (enchantment == null)
			// continue;
			//
			// if (!EnchantHelper.isEnchantmentsCompatible(enchantment, obj)) {
			// add = false;
			// }
			// }
			// if (add) {
			// addEnchantFor(itemStack, temp2, obj);
			// }
			// }
			// temp.putAll(temp2);
			// }

			if (enchantments != temp) {
				enchantments = temp;
			}
		} else {
			enchantments = temp;
		}
	}

	public boolean canPurchase(EntityPlayer player, int cost) {

		if (player.capabilities.isCreativeMode) {
			return true;
		}

		if (player.experienceLevel < cost) {
			player.sendMessage(new TextComponentString("Not enough levels. Required " + cost));
			return false;
		}
		return true;
	}

	public void enchant (EntityPlayer player, 
					HashMap<Integer, Integer> map, 
					HashMap<Integer, Integer> levels, 
					int cost) throws Exception {
        
        final ItemStack itemstack = inventorySlots.get(0).getStack();
        final HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
        int serverCost = 0;
        
        if (itemstack == null)
            return;
            
        for (final Integer enchantId : map.keySet()) {
            final Integer level = map.get(enchantId);
            final Integer startingLevel = enchantments.get(enchantId);
            Enchantment enchantment = Enchantment.getEnchantmentByID(enchantId);
            if (level > startingLevel)
                serverCost += enchantmentCost(enchantment, level, startingLevel);
            else if (level < startingLevel)
                serverCost += disenchantmentCost(enchantment, level, startingLevel);
        }
        
        if (cost != serverCost) {
            throw new Exception("Cost is different on client and server");
        }
        
        for (final Integer enchantId : enchantments.keySet()) {
            final Integer level = enchantments.get(enchantId);
            
            if (level != 0) {
                if (!map.containsKey(enchantId)) {
                    map.put(enchantId, level);
                }
            }
        }
        
        for (final Integer enchantId : map.keySet()) {
            final Integer level = map.get(enchantId);
            
            if (level == 0)
                temp.put(enchantId, level);
                
        }
        for (Integer object : temp.keySet()) {
            map.remove(object);
        }
        
        if (canPurchase(player, serverCost)) {
            ItemStack itemStack = EnchantHelper.setEnchantments(map, itemstack, levels);
            final Slot slot = inventorySlots.get(0);

//            slot.putStack(ItemStack.EMPTY);
            slot.putStack(itemStack);
            
          //slot.onSlotChanged();
            //slot.onTake(player, itemStack1);
            
            if (!player.capabilities.isCreativeMode)
                player.addExperienceLevel(-cost);
            
//            
//            EntityItem item = new EntityItem(enchantorium.getWorld(), 
//            		enchantorium.getPos().getX(), enchantorium.getPos().getY(), enchantorium.getPos().getZ(), 
//            		itemStack.copy());
//    		enchantorium.getWorld().spawnEntity(item);
        }
        
        readItems();

        enchantorium.enchantCurrent(map, levels);
        
        enchantorium.markDirty();
                
        onCraftMatrixChanged(last_inventory);

        enchantorium.sendUpdate();
        
        //enchantorium.onLoad();
        
}

	public int enchantmentCost(Enchantment enchantment, int enchantmentLevel, Integer level) {

		final double costFactor = 1.0;
		final ItemStack itemStack = inventorySlots.get(0).getStack();
		if (itemStack == null)
			return 0;

		final int maxLevel = enchantment.getMaxLevel();

		if (enchantmentLevel > maxLevel) {
			return 0;
		}

		final int averageCost = (enchantment.getMinEnchantability(enchantmentLevel)
				+ enchantment.getMaxEnchantability(enchantmentLevel)) / 2;
		int enchantability = itemStack.getItem().getItemEnchantability();

		if (enchantability < 1)
			enchantability = 1;

		int adjustedCost = (int) (averageCost * (enchantmentLevel - level + maxLevel)
				/ ((double) maxLevel * enchantability));

//		int temp = (int) (adjustedCost * (60 / (bookCases() + 1)));
//		temp /= 20;
//		if (temp > adjustedCost) {
//			adjustedCost = temp;
//		}

		adjustedCost *= (costFactor / 3D);
		if (enchantability > 1) {
			adjustedCost *= Math.log(enchantability) / 2;
		} else {
			adjustedCost /= 10;
		}

		return Math.max(1, adjustedCost);
	}

	public int disenchantmentCost(Enchantment enchantment, int enchantmentLevel, Integer level) {

		final ItemStack itemStack = inventorySlots.get(0).getStack();
		final double costFactor = 1.0;

		if (itemStack == null)
			return 0;

		final int maxLevel = enchantment.getMaxLevel();

		if (enchantmentLevel > maxLevel)
			return 0;

		final int averageCost = (enchantment.getMinEnchantability(level) + enchantment.getMaxEnchantability(level)) / 2;
		int enchantability = itemStack.getItem().getItemEnchantability(itemStack);

		if (enchantability <= 1)
			enchantability = 10;

		int adjustedCost = (int) (averageCost * (enchantmentLevel - level - maxLevel)
				/ ((double) maxLevel * enchantability));

//		int temp = (int) (adjustedCost * (60 / (bookCases() + 1)));
//		temp /= 20;
//		if (temp > adjustedCost) {
//			adjustedCost = temp;
//		}

		adjustedCost *= (costFactor / 4D);

		if (enchantability > 1)
			adjustedCost *= Math.log(enchantability) / 2;
		else
			adjustedCost /= 10;

		final int enchantmentCost = enchantmentCost(enchantment, level - 1, enchantmentLevel);

		return Math.min(adjustedCost, -enchantmentCost);
	}

	public int repairCostMax() {

		final double repairFactor = 1.5;
		final ItemStack itemStack = inventorySlots.get(0).getStack();
		if (itemStack == null)
			return 0;

		if (!itemStack.isItemEnchanted() || !itemStack.isItemDamaged())
			return 0;

		int cost = 0;

		final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

		for (final Enchantment enchantment : enchantments.keySet()) {
			final Integer enchantmentLevel = enchantments.get(enchantment);

			cost += enchantmentCost(enchantment, enchantmentLevel, 0);
		}

		final int maxDamage = itemStack.getMaxDamage();
		final int displayDamage = itemStack.getItemDamage();
		int enchantability = itemStack.getItem().getItemEnchantability(itemStack);

		if (enchantability <= 1) {
			enchantability = 10;
		}

		final double percentDamage = 1 - (maxDamage - displayDamage) / (double) maxDamage;

		double totalCost = (percentDamage * cost) / enchantability;

		totalCost *= 2 * repairFactor;

		return (int) Math.max(1, totalCost);
	}
}