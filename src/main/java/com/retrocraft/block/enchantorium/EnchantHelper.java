package com.retrocraft.block.enchantorium;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EnchantHelper {

   public static ArrayList<Integer> ALL_ENCHANTMENTS = new ArrayList<Integer>();
   
   static {
		for (int i=0; i<200; i++) {
			final Enchantment e = Enchantment.getEnchantmentByID(i);
			if (e != null)
			{
				ALL_ENCHANTMENTS.add(i);
			}
		}
   }
   
   public static boolean isEnchantmentsCompatible (Enchantment ench1, Enchantment ench2) {
        
        return ench1.func_191560_c(ench2);
    }
    
//    public static boolean isItemEnchanted (ItemStack itemStack) {
//        
//        return itemStack.hasTagCompound() 
//        		&& (itemStack.getItem() != Items.ENCHANTED_BOOK ? 
//        				itemStack.stackTagCompound.hasKey("ench") : 
//        				itemStack.stackTagCompound.hasKey("StoredEnchantments"));
//    }
    
    public static boolean isNewItemEnchantable (Item item) {
        if (item.equals(Items.ENCHANTED_BOOK)) {
            return isItemEnchantable(new ItemStack(Items.BOOK));
        }
        return isItemEnchantable(new ItemStack(item));
    }
    
    /**
     * A check to see if an ItemStack can be enchanted. For this to be true, the ItemStack must
     * have an enchantability grater than 0, and be a book, enchanted book, or an tool.
     *
     * @param itemStack The ItemStack to check.
     * @return boolean Whether or not the ItemStack is enchantable.
     */
    public static boolean isItemEnchantable (ItemStack itemStack) {
        return itemStack.getItem().getItemEnchantability(itemStack) > 0 && (itemStack.getItem() == Items.BOOK || itemStack.getItem() == Items.ENCHANTED_BOOK || itemStack.isItemEnchantable());
    }
    
    
    public static ItemStack setEnchantments (HashMap<Integer, Integer> map, ItemStack itemStack, HashMap<Integer, Integer> levels) {
        
        final NBTTagList nbttaglist = new NBTTagList();
        
        NBTTagList restrictions;
        
        restrictions = itemStack.hasTagCompound() ? itemStack.getTagCompound().getTagList("restrictions", 10) : new NBTTagList();
        
        for (final Integer o : map.keySet()) {
            final int i = o;
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setShort("id", (short) i);
            nbttagcompound.setShort("lvl", (short) map.get(i).intValue());
            nbttaglist.appendTag(nbttagcompound);
            
            int startLevel = levels.get(i) == null ? map.get(i) : levels.get(i);
            
            for (int y = startLevel; y <= map.get(i); y++) {
                if (containsEnchantment(restrictions, i, y)) {
                    continue;
                }
                
                NBTTagCompound compound = new NBTTagCompound();
                compound.setShort("id", (short) i);
                compound.setShort("lvl", (short) y);
                //compound.setString("player", player.getDisplayNameString());
                restrictions.appendTag(compound);
            }
        }
        
        if (itemStack.getItem() == Items.BOOK) {
            itemStack = new ItemStack(Items.ENCHANTED_BOOK);
            if (nbttaglist.tagCount() > 0) {
                itemStack.setTagInfo("StoredEnchantments", nbttaglist);
                itemStack.setTagInfo("restrictions", restrictions);
            }
            else if (itemStack.hasTagCompound()) {
                itemStack.getTagCompound().removeTag("StoredEnchantments");
//                itemStack.stackTagCompound = null;
                itemStack = new ItemStack(Items.BOOK);
            }
        }
        else if (nbttaglist.tagCount() > 0) {
            itemStack.setTagInfo("ench", nbttaglist);
            itemStack.setTagInfo("restrictions", restrictions);
        }
        else if (itemStack.hasTagCompound()) {
            itemStack.getTagCompound().removeTag("ench");
        }
        return itemStack;
    }
    
    public static boolean containsEnchantment (NBTTagList restrictions, int id, int y) {
        
        for (int k = 0; k < restrictions.tagCount(); k++) {
            NBTTagCompound tag = restrictions.getCompoundTagAt(k);
            if (tag.getShort("lvl") == y && tag.getShort("id") == id) {
                return true;
            }
        }
        return false;
    }
}
