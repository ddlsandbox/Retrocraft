package com.retrocraft.item;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.tool.ToolEverything;
import com.retrocraft.item.tool.ToolExcavator;
import com.retrocraft.item.tool.ToolHammer;
import com.retrocraft.item.tool.ToolStreamAxe;
import com.retrocraft.item.weapon.ItemSword;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	/* ingotts */
	public static ItemOre ingotCopper;
	public static ItemOre ingotManolium;
	public static ItemOre ingotManolazium;
	
	public static ItemSword manoliumSword;
	
	/* hammer */
	public static ToolHammer stoneHammer;
	public static ToolHammer ironHammer;
	public static ToolHammer goldHammer;
	public static ToolHammer diamondHammer;
	public static ToolHammer emeraldHammer;
	public static ToolHammer manoliumHammer;
	
	public static ToolExcavator manoliumExcavator;
	public static ToolEverything manoliumETool;
	public static ToolStreamAxe manoliumStreamAxe;
	public static ItemSword manolaziumSword;
	
	public static WoodenBucket woodenBucket;
	public static WoodenBucket woodenWaterBucket;
	public static ItemWoodenMilkBucket woodenMilkBucket;
	
	public static void init() {
		ingotCopper = register(new ItemOre("ingot_copper", "ingotCopper").setCreativeTab(RetroCraft.creativeTab));
		ingotManolium = register(new ItemOre("ingot_manolium", "ingotManolium").setCreativeTab(RetroCraft.creativeTab));
		ingotManolazium = register(new ItemOre("ingot_manolazium", "ingotManolazium").setCreativeTab(RetroCraft.creativeTab));
		
		manoliumSword = register(new ItemSword(RetroCraft.manoliumToolMaterial, "sword_manolium").setCreativeTab(RetroCraft.creativeTab));
		
		stoneHammer = register(new ToolHammer(ToolMaterial.STONE, "hammer_stone").setCreativeTab(RetroCraft.creativeTab));
		ironHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_iron").setCreativeTab(RetroCraft.creativeTab));
		goldHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_gold").setCreativeTab(RetroCraft.creativeTab));
		diamondHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_diamond").setCreativeTab(RetroCraft.creativeTab));
		emeraldHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_emerald").setCreativeTab(RetroCraft.creativeTab));
		manoliumHammer = register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_manolium").setCreativeTab(RetroCraft.creativeTab));
		
		manoliumExcavator = register(new ToolExcavator(RetroCraft.manoliumToolMaterial, "excavator_manolium").setCreativeTab(RetroCraft.creativeTab));
		manoliumStreamAxe = register(new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolium").setCreativeTab(RetroCraft.creativeTab));
		manoliumETool = register(new ToolEverything(RetroCraft.manoliumToolMaterial, "etool_manolium").setCreativeTab(RetroCraft.creativeTab));
		manolaziumSword = register(new ItemSword(RetroCraft.manolaziumToolMaterial, "sword_manolazium").setCreativeTab(RetroCraft.creativeTab));
		
		woodenBucket = register(new WoodenBucket("wooden_bucket").setCreativeTab(RetroCraft.creativeTab));
		woodenMilkBucket = register(new ItemWoodenMilkBucket("wooden_bucket_milk").setCreativeTab(RetroCraft.creativeTab));
	}
	
	private static <T extends Item> T register(T item) {
		GameRegistry.register(item);
		
		if(item instanceof ItemModelProvider) {
			((ItemModelProvider)item).registerItemModel(item);
		}
		
		if (item instanceof ItemOreDict) {
			((ItemOreDict)item).initOreDict();
		}
		
		return item;
	}
}