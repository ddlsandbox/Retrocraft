package com.retrocraft.recipe;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.item.ModItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RetrocraftRecipes 
{
	public static void init() 
	{
		GameRegistry.addSmelting(ModItems.dustManolite, 
		                         new ItemStack(ModItems.ingotManolium), 0.7f);
		GameRegistry.addSmelting(ModItems.dustManolazium, 
                             new ItemStack(ModItems.ingotManolazium), 0.7f);
		
		GameRegistry.addSmelting(ModBlocks.oreManolite, 
		                         new ItemStack(ModItems.ingotManolium), 0.7f);

		GameRegistry.addShapelessRecipe(
				new ItemStack(ModItems.dustManolazium), 
				new Object[] { ModItems.dustManolite, Items.EMERALD, Items.DIAMOND, Items.REDSTONE });
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ModItems.dustManolazium), "E", "M", "E", 
				'M', ModItems.dustManolite, 
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
		
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.woodenBucket), "W W", "WSW", 
        'W', Blocks.PLANKS, 
        'S', Blocks.WOODEN_SLAB);
		
		if(!RetroCraft.getConfig().creativeModeOnly) {
      GameRegistry.addShapedRecipe(
          new ItemStack(ModBlocks.blockWaystone), " S ", "SWS", "OOO", 
          'S', Blocks.STONEBRICK, 
          'W', Items.ENDER_EYE, 
          'O', Blocks.OBSIDIAN);
		}
		
		/* Crusher */
		GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreNickel", false), OreDictionary.getOres("dustNickel", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreIron", false), OreDictionary.getOres("dustIron", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreGold", false), OreDictionary.getOres("dustGold", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreLapis", false), OreDictionary.getOres("gemLapis", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreCoal", false), OreDictionary.getOres("coal", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("cobblestone", false), OreDictionary.getOres("sand", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreRedstone", false), OreDictionary.getOres("dustRedstone", false));
    GrinderRecipeRegistry.addRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT));
    GrinderRecipeRegistry.addRecipe(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 4));
    
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("oreNether", 6));
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("orePoor", 4, "nugget"));
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("denseore", 8));
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("gem", 1));
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("ingot", 1));
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("ore", 2));
    GrinderRecipeRegistry.SEARCH_CASES.add(new GrinderRecipeRegistry.SearchCase("ore", 2, "gem")); //If no dust is found for certain ores, make gems directly
	}
}
