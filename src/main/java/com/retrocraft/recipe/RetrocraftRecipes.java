package com.retrocraft.recipe;

import com.retrocraft.block.ModBlocks;
import com.retrocraft.item.ModItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RetrocraftRecipes {
	public static void init() {
		GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper), 0.7f);
		GameRegistry.addSmelting(ModBlocks.oreManolite, new ItemStack(ModItems.ingotManolium), 0.7f);

		GameRegistry.addShapelessRecipe(
				new ItemStack(ModItems.ingotManolazium), 
				new Object[] { ModItems.ingotManolium, Items.EMERALD, Items.DIAMOND, Items.REDSTONE });
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ModItems.ingotManolazium), "E", "M", "E", 
				'M', ModItems.ingotManolium, 
				'E', Items.EMERALD);
		 
		GameRegistry.addShapedRecipe(
				new ItemStack(Items.DIAMOND_PICKAXE), "III", " S ", " S ", 
				'I', ModItems.ingotManolium, 
				'S', Items.STICK);
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ModItems.manoliumSword), "I", "I", "S", 
				'I', ModItems.ingotManolium, 
				'S', Items.STICK);
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ModBlocks.pedestalManolium), "OOO", " S ", " S ", 
				'O', ModItems.ingotManolium, 
				'S', Blocks.STONEBRICK);
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ModItems.manolaziumSword), "I", "I", "S", 
				'I', ModItems.ingotManolazium, 
				'S', Items.STICK);
	}
}
