package com.retrocraft.item;

import com.retrocraft.item.tool.ToolEverything;
import com.retrocraft.item.tool.ToolExcavator;
import com.retrocraft.item.tool.ToolStreamAxe;
import com.retrocraft.item.tool.ToolHammer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	/* dust */
	@GameRegistry.ObjectHolder("retrocraft:dust_manolite")
	public static ItemOre dustManolite;
	@GameRegistry.ObjectHolder("retrocraft:dust_manolazium")
	public static ItemOre dustManolazium;
	@GameRegistry.ObjectHolder("retrocraft:gem_octirion")
	public static ItemOre gemOctirion;

	/* ingot */
	@GameRegistry.ObjectHolder("retrocraft:ingot_manolium")
	public static ItemOre ingotManolium;
	@GameRegistry.ObjectHolder("retrocraft:ingot_manolazium")
	public static ItemOre ingotManolazium;

	@GameRegistry.ObjectHolder("retrocraft:mechanical_core")
	public static ItemOre mechanicalCore;
	@GameRegistry.ObjectHolder("retrocraft:magical_core")
	public static ItemOre magicalCore;
//
//	public static ItemSword manoliumSword;
//
//	/* hammer */
	  @GameRegistry.ObjectHolder("retrocraft:hammer_iron")
  	public static ToolHammer ironHammer;
  	@GameRegistry.ObjectHolder("retrocraft:hammer_diamond")
  	public static ToolHammer diamondHammer;
//	  @GameRegistry.ObjectHolder("retrocraft:hammer_manolium")
//  	public static ToolHammer manoliumHammer;

	@GameRegistry.ObjectHolder("retrocraft:excavator_stone")
	public static ToolExcavator stoneExcavator;
	@GameRegistry.ObjectHolder("retrocraft:excavator_iron")
	public static ToolExcavator ironExcavator;
	@GameRegistry.ObjectHolder("retrocraft:excavator_gold")
	public static ToolExcavator goldExcavator;
	@GameRegistry.ObjectHolder("retrocraft:excavator_diamond")
	public static ToolExcavator diamondExcavator;
	@GameRegistry.ObjectHolder("retrocraft:excavator_manolium")
	public static ToolExcavator manoliumExcavator;
	@GameRegistry.ObjectHolder("retrocraft:excavator_manolazium")
	public static ToolExcavator manolaziumExcavator;

	@GameRegistry.ObjectHolder("retrocraft:streamaxe_iron")
	public static ToolStreamAxe ironStreamAxe;
	@GameRegistry.ObjectHolder("retrocraft:streamaxe_diamond")
	public static ToolStreamAxe diamondStreamAxe;
//	@GameRegistry.ObjectHolder("retrocraft:streamaxe_manolium")
//	public static ToolStreamAxe manoliumStreamAxe;

	@GameRegistry.ObjectHolder("retrocraft:etool_manolium")
	public static ToolEverything manoliumETool;
	@GameRegistry.ObjectHolder("retrocraft:etool_manolazium")
	public static ToolEverything manolaziumETool;
//
//	public static ItemSword manolaziumSword;
//
	@GameRegistry.ObjectHolder("retrocraft:wooden_bucket")
	public static ItemWoodenBucket woodenBucket;
//	public static ItemWoodenBucket woodenWaterBucket;
	@GameRegistry.ObjectHolder("retrocraft:wooden_bucket_milk")
	public static ItemWoodenMilkBucket woodenMilkBucket;
//
//	public static ItemManoliumArmor manoliumHead;
//	public static ItemManoliumArmor manolaziumHead;
//	public static ItemManoliumArmor manoliumChest;
//	public static ItemManoliumArmor manolaziumChest;
//	public static ItemManoliumArmor manoliumLegs;
//	public static ItemManoliumArmor manolaziumLegs;
//	public static ItemManoliumArmor manoliumFeet;
//	public static ItemManoliumArmor manolaziumFeet;
	
	public static void init() {
//		manoliumHead = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "head_manolium", 0,
//				EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab));
//		manolaziumHead = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "head_manolazium", 0,
//				EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab));
//
//		manoliumChest = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "chest_manolium", 0,
//				EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab));
//		manolaziumChest = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "chest_manolazium", 0,
//				EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab));
//
//		manoliumLegs = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "legs_manolium", 0,
//				EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab));
//		manolaziumLegs = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "legs_manolazium", 0,
//				EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab));
//
//		manoliumFeet = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "feet_manolium", 0,
//				EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab));
//		manolaziumFeet = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "feet_manolazium", 0,
//				EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab));
//
//		manoliumSword = register(new ItemSword(RetroCraft.manoliumToolMaterial, "sword_manolium")
//				.setCreativeTab(RetroCraft.creativeTab));
//
//		stoneHammer = register(
//				new ToolHammer(ToolMaterial.STONE, "hammer_stone").setCreativeTab(RetroCraft.creativeTab));
//		ironHammer = register(new ToolHammer(ToolMaterial.IRON, "hammer_iron").setCreativeTab(RetroCraft.creativeTab));
//		goldHammer = register(new ToolHammer(ToolMaterial.GOLD, "hammer_gold").setCreativeTab(RetroCraft.creativeTab));
//		diamondHammer = register(
//				new ToolHammer(ToolMaterial.DIAMOND, "hammer_diamond").setCreativeTab(RetroCraft.creativeTab));
//		manoliumHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_manolium")
//				.setCreativeTab(RetroCraft.creativeTab));
//		manolaziumHammer = register(new ToolHammer(RetroCraft.manolaziumToolMaterial, "hammer_manolazium")
//				.setCreativeTab(RetroCraft.creativeTab));
//
//		stoneExcavator = register(
//				new ToolExcavator(ToolMaterial.STONE, "excavator_stone").setCreativeTab(RetroCraft.creativeTab));
//		ironExcavator = register(
//				new ToolExcavator(ToolMaterial.IRON, "excavator_iron").setCreativeTab(RetroCraft.creativeTab));
//		goldExcavator = register(
//				new ToolExcavator(ToolMaterial.GOLD, "excavator_gold").setCreativeTab(RetroCraft.creativeTab));
//		diamondExcavator = register(
//				new ToolExcavator(ToolMaterial.DIAMOND, "excavator_diamond").setCreativeTab(RetroCraft.creativeTab));
//		manoliumExcavator = register(new ToolExcavator(RetroCraft.manoliumToolMaterial, "excavator_manolium")
//				.setCreativeTab(RetroCraft.creativeTab));
//		manolaziumExcavator = register(new ToolExcavator(RetroCraft.manolaziumToolMaterial, "excavator_manolazium")
//				.setCreativeTab(RetroCraft.creativeTab));
//
//		stoneStreamAxe = register(
//				new ToolStreamAxe(ToolMaterial.STONE, "streamaxe_stone").setCreativeTab(RetroCraft.creativeTab));
//		ironStreamAxe = register(
//				new ToolStreamAxe(ToolMaterial.IRON, "streamaxe_iron").setCreativeTab(RetroCraft.creativeTab));
//		goldStreamAxe = register(
//				new ToolStreamAxe(ToolMaterial.GOLD, "streamaxe_gold").setCreativeTab(RetroCraft.creativeTab));
//		diamondStreamAxe = register(
//				new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_diamond").setCreativeTab(RetroCraft.creativeTab));
//		// why it fails with manolium?
//		manoliumStreamAxe = register(
//				new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolium").setCreativeTab(RetroCraft.creativeTab));
//		manolaziumStreamAxe = register(
//				new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolazium").setCreativeTab(RetroCraft.creativeTab));
//
//		manoliumETool = register(new ToolEverything(RetroCraft.manoliumToolMaterial, "etool_manolium")
//				.setCreativeTab(RetroCraft.creativeTab));
//		manolaziumETool = register(new ToolEverything(RetroCraft.manolaziumToolMaterial, "etool_manolazium")
//				.setCreativeTab(RetroCraft.creativeTab));
//
//		manolaziumSword = register(new ItemSword(RetroCraft.manolaziumToolMaterial, "sword_manolazium")
//				.setCreativeTab(RetroCraft.creativeTab));

//		woodenBucket = register(new ItemWoodenBucket("wooden_bucket", Blocks.AIR).setCreativeTab(RetroCraft.creativeTab));
//		woodenWaterBucket = register(new ItemWoodenBucket("wooden_bucket_water", Blocks.FLOWING_WATER).setCreativeTab(RetroCraft.creativeTab));
//    
//		//woodenBucket = register(new ItemWoodenBucket("wooden_bucket").setCreativeTab(RetroCraft.creativeTab));
//		woodenMilkBucket = register(new ItemWoodenMilkBucket("wooden_bucket_milk").setCreativeTab(RetroCraft.creativeTab));
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
		
		ModelLoader.setCustomModelResourceLocation(woodenBucket, 0, new ModelResourceLocation(woodenBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(woodenMilkBucket, 0, new ModelResourceLocation(woodenMilkBucket.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(stoneExcavator, 0, new ModelResourceLocation(stoneExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironExcavator, 0, new ModelResourceLocation(ironExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldExcavator, 0, new ModelResourceLocation(goldExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondExcavator, 0, new ModelResourceLocation(diamondExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumExcavator, 0, new ModelResourceLocation(manoliumExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumExcavator, 0, new ModelResourceLocation(manolaziumExcavator.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ironStreamAxe, 0, new ModelResourceLocation(ironStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondStreamAxe, 0, new ModelResourceLocation(diamondStreamAxe.getRegistryName(), "inventory"));
//		ModelLoader.setCustomModelResourceLocation(manoliumStreamAxe, 0, new ModelResourceLocation(manoliumStreamAxe.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(ironHammer, 0, new ModelResourceLocation(ironHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondHammer, 0, new ModelResourceLocation(diamondHammer.getRegistryName(), "inventory"));
//		ModelLoader.setCustomModelResourceLocation(manoliumHammer, 0, new ModelResourceLocation(manoliumHammer.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(manoliumETool, 0, new ModelResourceLocation(manoliumETool.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumETool, 0, new ModelResourceLocation(manolaziumETool.getRegistryName(), "inventory"));
    }
}
