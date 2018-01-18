package com.retrocraft.item;

import com.retrocraft.RetroCraft;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public final class RetrocraftMaterials
{
  public static ArmorMaterial manoliumArmorMaterial;
  public static ArmorMaterial manolaziumArmorMaterial;
  public static ArmorMaterial octirionArmorMaterial;
  
  public static ToolMaterial manoliumToolMaterial;
  public static ToolMaterial manolaziumToolMaterial;
  public static ToolMaterial octirionToolMaterial;
  
  public static void init()
  {
    manoliumArmorMaterial = addArmorMaterial("AM_MANOLIUM",
        RetroCraft.modId + ":armor_manolium", 
        80, 
        new int[]{ 3, 5, 6, 3 }, 
        10, 
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 
        2.0F);
    
    manolaziumArmorMaterial = addArmorMaterial("AM_MANOLAZIUM",
        RetroCraft.modId + ":armor_manolazium",
        100, 
        new int[]{ 5, 7, 8, 5 }, 
        15, 
        SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 
        4.0F);
    
    octirionArmorMaterial = addArmorMaterial("AM_OCTIRION",
        RetroCraft.modId + ":armor_octirion",
        100, 
        new int[]{ 5, 7, 8, 5 }, 
        15, 
        SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 
        4.0F);
    
    manoliumToolMaterial = addToolMaterial("TM_MANOLIUM", 
        2,   /* harvest level */
        500, /* durability */
        8f,  /* efficiency */
        4f,  /* damage */
        20); /* enchantability */
    
    manolaziumToolMaterial = addToolMaterial("TM_MANOLAZIUM", 
        3,     /* harvest level */
        1500,  /* durability */
        10f,   /* efficiency */
        8f,    /* damage */
        30);   /* enchantability */
    
    octirionToolMaterial = addToolMaterial("TM_OCTIRION", 
        4,    /* harvest level */
        2000, /* durability */
        15f,  /* efficiency */
        12f,  /* damage */
        30);  /* enchantability */
  }

  private static ArmorMaterial addArmorMaterial(String enumName, String textureName,
      int durability, int[] reductionAmounts, int enchantability,
      SoundEvent soundOnEquip, float toughness)
  {
    return EnumHelper.addArmorMaterial(
        enumName, 
        textureName,
        durability, 
        reductionAmounts, 
        enchantability, 
        soundOnEquip, toughness);
  }
  
  private static ToolMaterial addToolMaterial(String enumName,
      int harvestLevel, int durability, float efficiency, float damage, 
      int enchantability)
  {
    return EnumHelper.addToolMaterial(
        enumName, 
        harvestLevel,
        durability, 
        efficiency,
        damage,
        enchantability);
  }
}