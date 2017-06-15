package com.retrocraft.machine.enchanter;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
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
        return itemStack.getItem().getItemEnchantability(itemStack) > 0 
        	&& (itemStack.getItem() == Items.BOOK || 
        		itemStack.getItem() == Items.ENCHANTED_BOOK || itemStack.isItemEnchantable() ||
        		itemStack.isItemEnchanted());
    }
    
    /**
     * Calculates how many experience points are it would take to get to the specified level.
     *
     * @param level The level to calculate for.
     * @return int The amount of experience points required to go from level 0 to the specified
     *         level.
     */
    public static int getExperienceFromLevel (int level) {

        return (int) (level < 16 ? 17 * level : level > 15 && level < 31 ? 1.5f * (level * level) - 29.5f * level + 360 : 3.5f * (level * level) - 151.5f * level + 2220);
    }

    
    /**
     * Calculate the amount of experience to go from one level to another.
     *
     * @param startingLevel The level you are currently at.
     * @param destinationLevel The level you want to go to.
     * @return int The amount of experience points needed to go from the startingLevel to the
     *         destinationLevel.
     */
    public static int getExperienceToLevel (int startingLevel, int destinationLevel) {

        return getExperienceFromLevel(destinationLevel) - getExperienceFromLevel(startingLevel);
    }

    /**
     * Calculates the amount of levels that an amount of EXP is equal to. This is a pretty
     * costly method due to the level curve.
     *
     * @param exp The amount of EXP to solve for.
     * @return int The amount of levels that can be created by the amount of exp.
     */
    public static int getLevelsFromExperience (int exp) {

        int currentLevel = 0;

        float levelCap = getExperienceToLevel(currentLevel, currentLevel + 1);

        int currentExp = exp;

        while (currentExp >= levelCap) {

            currentExp -= levelCap;
            currentLevel += 1;
            levelCap = getExperienceToLevel(currentLevel, currentLevel + 1);
        }

        return currentLevel;
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
    
    public static ItemStack setEnchantments (List<EnchantmentData> enchantmentData, 
    										 ItemStack itemStack) {

        final NBTTagList nbttaglist = new NBTTagList();

        for (final EnchantmentData data : enchantmentData) {

            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setShort("id", (short) Enchantment.getEnchantmentID(data.enchantmentobj));
            nbttagcompound.setInteger("lvl", data.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound);
        }

        if (nbttaglist.tagCount() > 0)
            itemStack.setTagInfo("ench", nbttaglist);

        else if (itemStack.hasTagCompound())
            itemStack.getTagCompound().removeTag("ench");

        return itemStack;
    }
}
