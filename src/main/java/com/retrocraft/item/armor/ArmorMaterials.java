package com.retrocraft.item.armor;

import com.retrocraft.RetroCraft;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public final class ArmorMaterials
{

  public static ArmorMaterial armorMaterialEmerald;
  public static ArmorMaterial armorMaterialObsidian;
  public static ArmorMaterial armorMaterialQuartz;

  public static ArmorMaterial manoliumArmorMaterial;
  public static ArmorMaterial manolaziumArmorMaterial;

  public static void init()
  {
    System.out.println("[RETROCRAFT] Config: " + RetroCraft.getConfig().grinderXpPerItem + " " + RetroCraft.getConfig().smelterXpFactor);
    
//    armorMaterialEmerald = addArmorMaterial("armorMaterialEmerald",
//        RetroCraft.modId + ":armor_emerald", 50, new int[]
//        { 5, 8, 9, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
//    armorMaterialObsidian = addArmorMaterial("armorMaterialObsidian",
//        RetroCraft.modId + ":armor_obsidian", 120, new int[]
//        { 1, 3, 4, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
//    armorMaterialQuartz = addArmorMaterial("armorMaterialQuartz",
//        RetroCraft.modId + ":armor_quartz", 20, new int[]
//        { 3, 5, 6, 3 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
    manoliumArmorMaterial = addArmorMaterial("RETROCRAFT_MANOLIUM",
        RetroCraft.modId + ":armor_manolium", 80, new int[]
        { 3, 5, 6, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
    manolaziumArmorMaterial = addArmorMaterial("RETROCRAFT_MANOLAZIUM",
        RetroCraft.modId + ":armor_manolazium", 100, new int[]
        { 5, 7, 8, 5 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
  }

  private static ArmorMaterial addArmorMaterial(String enumName, String textureName,
      int durability, int[] reductionAmounts, int enchantability,
      SoundEvent soundOnEquip)
  {
    return EnumHelper.addArmorMaterial(
        enumName, 
        textureName,
        durability, 
        reductionAmounts, 
        enchantability, 
        soundOnEquip, 0F);
  }
}