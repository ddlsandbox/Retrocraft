package com.retrocraft.item;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.armor.ItemManoliumArmor;
import com.retrocraft.item.backpack.ItemBackpack;
import com.retrocraft.item.replacer.ToolReplacer;
import com.retrocraft.item.tool.ItemWoodenBucket;
import com.retrocraft.item.tool.ToolEverything;
import com.retrocraft.item.tool.ToolExcavator;
import com.retrocraft.item.tool.ToolHammer;
import com.retrocraft.item.tool.ToolStreamAxe;
import com.retrocraft.item.weapon.ItemSword;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

  /* dust */
  
  public static ItemOre dustManolite;
  public static ItemOre dustManolazium;
  public static ItemOre gemOctirion;

  /* ingot */
  
  public static ItemOre ingotManolium;
  public static ItemOre ingotManolazium;

  /* tools */

  public static ToolHammer stoneHammer;
  public static ToolHammer ironHammer;
  public static ToolHammer goldHammer;
  public static ToolHammer diamondHammer;
  public static ToolHammer manoliumHammer;
  public static ToolHammer manolaziumHammer;

  public static ToolExcavator stoneExcavator;
  public static ToolExcavator ironExcavator;
  public static ToolExcavator goldExcavator;
  public static ToolExcavator diamondExcavator;
  public static ToolExcavator manoliumExcavator;
  public static ToolExcavator manolaziumExcavator;

  public static ToolStreamAxe stoneStreamAxe;
  public static ToolStreamAxe ironStreamAxe;
  public static ToolStreamAxe goldStreamAxe;
  public static ToolStreamAxe diamondStreamAxe;
  public static ToolStreamAxe manoliumStreamAxe;
  public static ToolStreamAxe manolaziumStreamAxe;

  public static ToolEverything manoliumETool;
  public static ToolEverything manolaziumETool;

  /* armor */
  
  public static ItemManoliumArmor manoliumHead;
  public static ItemManoliumArmor manolaziumHead;
  public static ItemManoliumArmor manoliumChest;
  public static ItemManoliumArmor manolaziumChest;
  public static ItemManoliumArmor manoliumLegs;
  public static ItemManoliumArmor manolaziumLegs;
  public static ItemManoliumArmor manoliumFeet;
  public static ItemManoliumArmor manolaziumFeet;

  public static ToolSmelter toolSmelter;
  public static ToolReplacer toolReplacer;
  
  /* weapons */
  
  public static ItemSword manoliumSword;
  public static ItemSword manolaziumSword;
  public static ItemSword octirionSword;

  /* misc */
  
  public static ItemOre mechanicalCore;
  public static ItemOre magicalCore;
  public static ItemManure manure;
  public static ItemBackpack backpack;
  public static ItemWoodenBucket woodenBucket;
  public static ItemWoodenBucket woodenWaterBucket;
  public static ItemWoodenMilkBucket woodenMilkBucket;
	
	
	public static void init() {
/* dust */
    
    dustManolite = new ItemOre("dust_manolite", "dustManolite").setCreativeTab(RetroCraft.creativeTab);
    dustManolazium = new ItemOre("dust_manolazium", "dustManolazium").setCreativeTab(RetroCraft.creativeTab);
    gemOctirion = new ItemOre("gem_octirion", "gemOctirion").setCreativeTab(RetroCraft.creativeTab);

    /* ingot */
    
    ingotManolium = new ItemOre("ingot_manolium", "ingotManolium").setCreativeTab(RetroCraft.creativeTab);
    ingotManolazium = new ItemOre("ingot_manolazium", "ingotManolazium").setCreativeTab(RetroCraft.creativeTab);

    /* tools */
    
    stoneExcavator = new ToolExcavator(ToolMaterial.STONE, "excavator_stone").setCreativeTab(RetroCraft.creativeTab);
    ironExcavator = new ToolExcavator(ToolMaterial.IRON, "excavator_iron").setCreativeTab(RetroCraft.creativeTab);
    goldExcavator = new ToolExcavator(ToolMaterial.GOLD, "excavator_gold").setCreativeTab(RetroCraft.creativeTab);
    diamondExcavator = new ToolExcavator(ToolMaterial.DIAMOND, "excavator_diamond").setCreativeTab(RetroCraft.creativeTab);
    manoliumExcavator = new ToolExcavator(RetrocraftMaterials.manoliumToolMaterial, "excavator_manolium").setCreativeTab(RetroCraft.creativeTab);
    manolaziumExcavator = new ToolExcavator(RetrocraftMaterials.manolaziumToolMaterial, "excavator_manolazium").setCreativeTab(RetroCraft.creativeTab);
    
    stoneStreamAxe = new ToolStreamAxe(ToolMaterial.STONE, "streamaxe_stone").setCreativeTab(RetroCraft.creativeTab);
    ironStreamAxe = new ToolStreamAxe(ToolMaterial.IRON, "streamaxe_iron").setCreativeTab(RetroCraft.creativeTab);
    goldStreamAxe = new ToolStreamAxe(ToolMaterial.GOLD, "streamaxe_gold").setCreativeTab(RetroCraft.creativeTab);
    diamondStreamAxe = new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_diamond").setCreativeTab(RetroCraft.creativeTab);
    manoliumStreamAxe = new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolium").setCreativeTab(RetroCraft.creativeTab);
    manolaziumStreamAxe = new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolazium").setCreativeTab(RetroCraft.creativeTab);

    stoneHammer = new ToolHammer(ToolMaterial.STONE, "hammer_stone").setCreativeTab(RetroCraft.creativeTab);
    ironHammer = new ToolHammer(ToolMaterial.IRON, "hammer_iron").setCreativeTab(RetroCraft.creativeTab);
    goldHammer = new ToolHammer(ToolMaterial.GOLD, "hammer_gold").setCreativeTab(RetroCraft.creativeTab);
    diamondHammer = new ToolHammer(ToolMaterial.DIAMOND, "hammer_diamond").setCreativeTab(RetroCraft.creativeTab);
    manoliumHammer = new ToolHammer(RetrocraftMaterials.manoliumToolMaterial, "hammer_manolium").setCreativeTab(RetroCraft.creativeTab);
    manolaziumHammer = new ToolHammer(RetrocraftMaterials.manolaziumToolMaterial, "hammer_manolazium").setCreativeTab(RetroCraft.creativeTab);

    toolSmelter = new ToolSmelter("tool_smelter").setCreativeTab(RetroCraft.creativeTab);
    toolReplacer = new ToolReplacer("tool_replacer").setCreativeTab(RetroCraft.creativeTab);
    
    manoliumETool = new ToolEverything(RetrocraftMaterials.manoliumToolMaterial, "etool_manolium").setCreativeTab(RetroCraft.creativeTab);
    manolaziumETool = new ToolEverything(RetrocraftMaterials.manolaziumToolMaterial, "etool_manolazium").setCreativeTab(RetroCraft.creativeTab);
    
    /* armor */
    
    manoliumHead = new ItemManoliumArmor(RetrocraftMaterials.manoliumArmorMaterial, "head_manolium", 0,
        EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab);
    manolaziumHead = new ItemManoliumArmor(RetrocraftMaterials.manolaziumArmorMaterial, "head_manolazium", 0,
        EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab);

    manoliumChest = new ItemManoliumArmor(RetrocraftMaterials.manoliumArmorMaterial, "chest_manolium", 0,
        EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab);
    manolaziumChest = new ItemManoliumArmor(RetrocraftMaterials.manolaziumArmorMaterial, "chest_manolazium", 0,
        EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab);

    manoliumLegs = new ItemManoliumArmor(RetrocraftMaterials.manoliumArmorMaterial, "legs_manolium", 0,
        EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab);
    manolaziumLegs = new ItemManoliumArmor(RetrocraftMaterials.manolaziumArmorMaterial, "legs_manolazium", 0,
        EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab);

    manoliumFeet = new ItemManoliumArmor(RetrocraftMaterials.manoliumArmorMaterial, "feet_manolium", 0,
        EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab);
    manolaziumFeet = new ItemManoliumArmor(RetrocraftMaterials.manolaziumArmorMaterial, "feet_manolazium", 0,
        EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab);
    
    /* weapons */
    
    manoliumSword = new ItemSword(RetrocraftMaterials.manoliumToolMaterial, "sword_manolium").setCreativeTab(RetroCraft.creativeTab);
    manolaziumSword = new ItemSword(RetrocraftMaterials.manolaziumToolMaterial, "sword_manolazium").setCreativeTab(RetroCraft.creativeTab);
    octirionSword = new ItemSword(RetrocraftMaterials.octirionToolMaterial, "sword_octirion").setCreativeTab(RetroCraft.creativeTab);

    /* misc */
    
    manure = new ItemManure("manure").setCreativeTab(RetroCraft.creativeTab);
    mechanicalCore = new ItemOre("mechanical_core", "mechanicalCore").setCreativeTab(RetroCraft.creativeTab);
    magicalCore = new ItemOre("magical_core", "magicalCore").setCreativeTab(RetroCraft.creativeTab);
    woodenBucket = new ItemWoodenBucket("wooden_bucket", Blocks.AIR).setCreativeTab(RetroCraft.creativeTab);
    woodenWaterBucket = new ItemWoodenBucket("wooden_bucket_water", Blocks.FLOWING_WATER).setCreativeTab(RetroCraft.creativeTab);
    woodenMilkBucket = new ItemWoodenMilkBucket("wooden_bucket_milk").setCreativeTab(RetroCraft.creativeTab);
    backpack = new ItemBackpack("backpack").setCreativeTab(RetroCraft.creativeTab);
	}

	@SideOnly(Side.CLIENT)
  public static void initModels() {
		dustManolite.initModel();
		dustManolazium.initModel();

		ingotManolium.initModel();
		ingotManolazium.initModel();
		gemOctirion.initModel();
		
		mechanicalCore.initModel();
		magicalCore.initModel();
		
		manure.initModel();
		
		ModelLoader.setCustomModelResourceLocation(woodenBucket, 0, new ModelResourceLocation(woodenBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(woodenWaterBucket, 0, new ModelResourceLocation(woodenWaterBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(woodenMilkBucket, 0, new ModelResourceLocation(woodenMilkBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(backpack, 0, new ModelResourceLocation(backpack.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(stoneExcavator, 0, new ModelResourceLocation(stoneExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironExcavator, 0, new ModelResourceLocation(ironExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldExcavator, 0, new ModelResourceLocation(goldExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondExcavator, 0, new ModelResourceLocation(diamondExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumExcavator, 0, new ModelResourceLocation(manoliumExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumExcavator, 0, new ModelResourceLocation(manolaziumExcavator.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(stoneStreamAxe, 0, new ModelResourceLocation(stoneStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironStreamAxe, 0, new ModelResourceLocation(ironStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldStreamAxe, 0, new ModelResourceLocation(goldStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondStreamAxe, 0, new ModelResourceLocation(diamondStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumStreamAxe, 0, new ModelResourceLocation(manoliumStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumStreamAxe, 0, new ModelResourceLocation(manolaziumStreamAxe.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(stoneHammer, 0, new ModelResourceLocation(stoneHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironHammer, 0, new ModelResourceLocation(ironHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldHammer, 0, new ModelResourceLocation(goldHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondHammer, 0, new ModelResourceLocation(diamondHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumHammer, 0, new ModelResourceLocation(manoliumHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumHammer, 0, new ModelResourceLocation(manolaziumHammer.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(manoliumETool, 0, new ModelResourceLocation(manoliumETool.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumETool, 0, new ModelResourceLocation(manolaziumETool.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(manoliumHead, 0, new ModelResourceLocation(manoliumHead.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumHead, 0, new ModelResourceLocation(manolaziumHead.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumChest, 0, new ModelResourceLocation(manoliumChest.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumChest, 0, new ModelResourceLocation(manolaziumChest.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumLegs, 0, new ModelResourceLocation(manoliumLegs.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumLegs, 0, new ModelResourceLocation(manolaziumLegs.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumFeet, 0, new ModelResourceLocation(manoliumFeet.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumFeet, 0, new ModelResourceLocation(manolaziumFeet.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(toolSmelter, 0, new ModelResourceLocation(toolSmelter.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(toolReplacer, 0, new ModelResourceLocation(toolReplacer.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(manoliumSword, 0, new ModelResourceLocation(manoliumSword.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumSword, 0, new ModelResourceLocation(manolaziumSword.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(octirionSword, 0, new ModelResourceLocation(octirionSword.getRegistryName(), "inventory"));
    }
}
