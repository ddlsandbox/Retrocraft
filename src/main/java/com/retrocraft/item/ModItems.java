package com.retrocraft.item;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.tool.ItemSword;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	/* ingotts */
	public static ItemOre ingotCopper;
	public static ItemOre ingotManolium;
	public static ItemOre ingotManolazium;
	
	public static ItemSword manoliumSword;
	public static ItemSword manolaziumSword;
	
	public static void init() {
		ingotCopper = register(new ItemOre("ingot_copper", "ingotCopper").setCreativeTab(RetroCraft.creativeTab));
		ingotManolium = register(new ItemOre("ingot_manolium", "ingotManolium").setCreativeTab(RetroCraft.creativeTab));
		ingotManolazium = register(new ItemOre("ingot_manolazium", "ingotManolazium").setCreativeTab(RetroCraft.creativeTab));
		
		manoliumSword = register(new ItemSword(RetroCraft.manoliumToolMaterial, "sword_manolium").setCreativeTab(RetroCraft.creativeTab));
		manolaziumSword = register(new ItemSword(RetroCraft.manolaziumToolMaterial, "sword_manolazium").setCreativeTab(RetroCraft.creativeTab));
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