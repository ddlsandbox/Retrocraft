package com.retrocraft.item;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.armor.ArmorMaterials;
import com.retrocraft.item.armor.ItemManoliumArmor;
import com.retrocraft.item.tool.ToolEverything;
import com.retrocraft.item.tool.ToolExcavator;
import com.retrocraft.item.tool.ToolHammer;
import com.retrocraft.item.tool.ToolStreamAxe;
import com.retrocraft.item.weapon.ItemSword;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	public static ItemSword manoliumSword;

	/* hammer */
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

	public static ItemSword manolaziumSword;

	public static ItemWoodenBucket woodenBucket;
	public static ItemWoodenBucket woodenWaterBucket;
	public static ItemWoodenMilkBucket woodenMilkBucket;

	public static ItemManoliumArmor manoliumHead;
	public static ItemManoliumArmor manolaziumHead;
	public static ItemManoliumArmor manoliumChest;
	public static ItemManoliumArmor manolaziumChest;
	public static ItemManoliumArmor manoliumLegs;
	public static ItemManoliumArmor manolaziumLegs;
	public static ItemManoliumArmor manoliumFeet;
	public static ItemManoliumArmor manolaziumFeet;

	public static void init() {
		dustManolite = register(new ItemOre("dust_manolite", "dustManolite").setCreativeTab(RetroCraft.creativeTab));
		dustManolazium = register(
				new ItemOre("dust_manolazium", "dustManolazium").setCreativeTab(RetroCraft.creativeTab));
		gemOctirion = register(new ItemOre("gem_octirion", "gemOctirion").setCreativeTab(RetroCraft.creativeTab));

		ingotManolium = register(new ItemOre("ingot_manolium", "ingotManolium").setCreativeTab(RetroCraft.creativeTab));
		ingotManolazium = register(
				new ItemOre("ingot_manolazium", "ingotManolazium").setCreativeTab(RetroCraft.creativeTab));

		mechanicalCore = register(
				new ItemOre("mechanical_core", "mechanicalCore").setCreativeTab(RetroCraft.creativeTab));
		magicalCore = register(new ItemOre("magical_core", "magicalCore").setCreativeTab(RetroCraft.creativeTab));

		manoliumHead = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "head_manolium", 0,
				EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab));
		manolaziumHead = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "head_manolazium", 0,
				EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab));

		manoliumChest = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "chest_manolium", 0,
				EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab));
		manolaziumChest = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "chest_manolazium", 0,
				EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab));

		manoliumLegs = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "legs_manolium", 0,
				EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab));
		manolaziumLegs = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "legs_manolazium", 0,
				EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab));

		manoliumFeet = register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "feet_manolium", 0,
				EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab));
		manolaziumFeet = register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "feet_manolazium", 0,
				EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab));

		manoliumSword = register(new ItemSword(RetroCraft.manoliumToolMaterial, "sword_manolium")
				.setCreativeTab(RetroCraft.creativeTab));

		stoneHammer = register(
				new ToolHammer(ToolMaterial.STONE, "hammer_stone").setCreativeTab(RetroCraft.creativeTab));
		ironHammer = register(new ToolHammer(ToolMaterial.IRON, "hammer_iron").setCreativeTab(RetroCraft.creativeTab));
		goldHammer = register(new ToolHammer(ToolMaterial.GOLD, "hammer_gold").setCreativeTab(RetroCraft.creativeTab));
		diamondHammer = register(
				new ToolHammer(ToolMaterial.DIAMOND, "hammer_diamond").setCreativeTab(RetroCraft.creativeTab));
		manoliumHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_manolium")
				.setCreativeTab(RetroCraft.creativeTab));
		manolaziumHammer = register(new ToolHammer(RetroCraft.manolaziumToolMaterial, "hammer_manolazium")
				.setCreativeTab(RetroCraft.creativeTab));

		stoneExcavator = register(
				new ToolExcavator(ToolMaterial.STONE, "excavator_stone").setCreativeTab(RetroCraft.creativeTab));
		ironExcavator = register(
				new ToolExcavator(ToolMaterial.IRON, "excavator_iron").setCreativeTab(RetroCraft.creativeTab));
		goldExcavator = register(
				new ToolExcavator(ToolMaterial.GOLD, "excavator_gold").setCreativeTab(RetroCraft.creativeTab));
		diamondExcavator = register(
				new ToolExcavator(ToolMaterial.DIAMOND, "excavator_diamond").setCreativeTab(RetroCraft.creativeTab));
		manoliumExcavator = register(new ToolExcavator(RetroCraft.manoliumToolMaterial, "excavator_manolium")
				.setCreativeTab(RetroCraft.creativeTab));
		manolaziumExcavator = register(new ToolExcavator(RetroCraft.manolaziumToolMaterial, "excavator_manolazium")
				.setCreativeTab(RetroCraft.creativeTab));

		stoneStreamAxe = register(
				new ToolStreamAxe(ToolMaterial.STONE, "streamaxe_stone").setCreativeTab(RetroCraft.creativeTab));
		ironStreamAxe = register(
				new ToolStreamAxe(ToolMaterial.IRON, "streamaxe_iron").setCreativeTab(RetroCraft.creativeTab));
		goldStreamAxe = register(
				new ToolStreamAxe(ToolMaterial.GOLD, "streamaxe_gold").setCreativeTab(RetroCraft.creativeTab));
		diamondStreamAxe = register(
				new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_diamond").setCreativeTab(RetroCraft.creativeTab));
		// why it fails with manolium?
		manoliumStreamAxe = register(
				new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolium").setCreativeTab(RetroCraft.creativeTab));
		manolaziumStreamAxe = register(
				new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolazium").setCreativeTab(RetroCraft.creativeTab));

		manoliumETool = register(new ToolEverything(RetroCraft.manoliumToolMaterial, "etool_manolium")
				.setCreativeTab(RetroCraft.creativeTab));
		manolaziumETool = register(new ToolEverything(RetroCraft.manolaziumToolMaterial, "etool_manolazium")
				.setCreativeTab(RetroCraft.creativeTab));

		manolaziumSword = register(new ItemSword(RetroCraft.manolaziumToolMaterial, "sword_manolazium")
				.setCreativeTab(RetroCraft.creativeTab));

		woodenBucket = register(new ItemWoodenBucket("wooden_bucket", Blocks.AIR).setCreativeTab(RetroCraft.creativeTab));
                woodenWaterBucket = register(new ItemWoodenBucket("wooden_bucket_water", Blocks.FLOWING_WATER).setCreativeTab(RetroCraft.creativeTab));
    
		//woodenBucket = register(new ItemWoodenBucket("wooden_bucket").setCreativeTab(RetroCraft.creativeTab));
		woodenMilkBucket = register(new ItemWoodenMilkBucket("wooden_bucket_milk").setCreativeTab(RetroCraft.creativeTab));
	}

	private static <T extends Item> T register(T item) {
		GameRegistry.register(item);

		if (item instanceof ItemModelProvider) {
			((ItemModelProvider) item).registerItemModel(item);
		}

		if (item instanceof ItemOreDict) {
			((ItemOreDict) item).initOreDict();
		}

		return item;
	}
}
