package com.retrocraft.item.armor;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftGlobals;
import com.retrocraft.item.ItemModelProvider;
import com.retrocraft.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemManoliumArmor extends ItemArmor implements ItemModelProvider
{

  private String                        name;

  public ItemManoliumArmor(ItemArmor.ArmorMaterial armorMaterial,
                           String name, int renderIndex, EntityEquipmentSlot armorType)
  {
    super(armorMaterial, renderIndex, armorType);

    setRegistryName(name);
    setUnlocalizedName(name);
    this.name = name;
  }

  @Override
  public ItemManoliumArmor setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  /**
   * Return an item rarity from EnumRarity
   */
  @Override
  public EnumRarity getRarity(ItemStack par1ItemStack)
  {
    return EnumRarity.UNCOMMON;
  }

  /**
   * Return whether this item is repairable in an anvil.
   */
  @Override
  public boolean getIsRepairable(ItemStack par1ItemStack,
      ItemStack par2ItemStack)
  {
    // repair with steeleaf ingots
    return par2ItemStack.getItem() == ModItems.ingotManolium ? true
        : super.getIsRepairable(par1ItemStack, par2ItemStack);
  }

  @Override
  public void registerItemModel(Item item)
  {
    RetroCraft.proxy.registerItemRenderer(this, 0, name);
  }
  
  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
  {
    super.onArmorTick(world, player, itemStack);
    
    if (itemStack.getItem() == ModItems.manolaziumHead)
      player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 12 * RetroCraftGlobals.TICKS_PER_SECOND, 0, false, false));
    else if (itemStack.getItem() == ModItems.manolaziumChest)
    {
      player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 12 * RetroCraftGlobals.TICKS_PER_SECOND, 0, false, false));
    }
    else if (itemStack.getItem() == ModItems.manolaziumFeet)
    {
      player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 4 * RetroCraftGlobals.TICKS_PER_SECOND, 0, false, false));
      player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 4 * RetroCraftGlobals.TICKS_PER_SECOND, 1, false, false));
    }
  }
}
