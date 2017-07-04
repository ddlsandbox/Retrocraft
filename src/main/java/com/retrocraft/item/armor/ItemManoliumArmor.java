package com.retrocraft.item.armor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.retrocraft.item.ModItems;

public class ItemManoliumArmor extends ItemArmor {

	public ItemManoliumArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, int renderIndex, int armorType) {
		super(par2EnumArmorMaterial, renderIndex, EntityEquipmentSlot.values()[armorType]);
	}

	@Override
	 public ItemManoliumArmor setCreativeTab(CreativeTabs tab) {
	 super.setCreativeTab(tab);
	 return this;
	 }
	
	/**
	 * Return an item rarity from EnumRarity
	 */    
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String layer) {
		{
			return ARMOR_DIR + "manoilumarmor_2.png";
		}
		return ARMOR_DIR + "manoilumarmor_1.png";
	}
	
//    /**
//     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
//     */
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
//    	ItemStack istack = new ItemStack(par1, 1, 0);
//    	switch (this.armorType) {
//    	case 0:
//    		istack.addEnchantment(Enchantment.projectileProtection, 2);
//            break;	
//    	case 1:
//    		istack.addEnchantment(Enchantment.blastProtection, 2);
//            break;	
//    	case 2:
//    		istack.addEnchantment(Enchantment.fireProtection, 2);
//            break;	
//    	case 3:
//    		istack.addEnchantment(Enchantment.featherFalling, 2);
//            break;	
//    	}
//    	par3List.add(istack);
//    }

    
    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
    	// repair with steeleaf ingots
        return par2ItemStack.getItem() == ModItems.ingotManolium ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
}
