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
				new ItemStack(ModItems.mechanicalCore), "MMM", "MDM", "MMM",
				'M', ModItems.ingotManolium,
				'D', Items.DIAMOND);

		GameRegistry.addShapedRecipe(
				new ItemStack(ModItems.magicalCore), "OOO", "ODO", "OOO",
				'O', ModItems.gemOctirion,
				'E', Items.EMERALD);

		GameRegistry.addShapedRecipe(
				new ItemStack(ModBlocks.pedestalManolium), "OOO", " M ", " S ",
				'O', ModItems.ingotManolium,
				'M', ModItems.mechanicalCore,
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
          new ItemStack(ModBlocks.blockWaystone), "SSS", "CWC", "OOO",
          'S', Blocks.STONEBRICK,
					'C', ModItems.magicalCore,
          'W', Items.ENDER_EYE,
          'O', Blocks.OBSIDIAN);
		}

		/* Crusher */
		GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreOctirion", false), OreDictionary.getOres("gemOctirion", false));
		GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreNickel", false), OreDictionary.getOres("dustNickel", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreIron", false), OreDictionary.getOres("dustIron", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreGold", false), OreDictionary.getOres("dustGold", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreLapis", false), OreDictionary.getOres("gemLapis", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreCoal", false), OreDictionary.getOres("coal", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("cobblestone", false), OreDictionary.getOres("sand", false));
    GrinderRecipeRegistry.addRecipe(OreDictionary.getOres("oreRedstone", false), OreDictionary.getOres("dustRedstone", false));
    GrinderRecipeRegistry.addRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT));
    GrinderRecipeRegistry.addRecipe(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 4));

    GrinderRecipeRegistry.registerSearchCase(new SearchCase("denseore",  8, "dust"));
    GrinderRecipeRegistry.registerSearchCase(new SearchCase("gem",       1, "dust"));
    GrinderRecipeRegistry.registerSearchCase(new SearchCase("ingot",     1, "dust"));
    GrinderRecipeRegistry.registerSearchCase(new SearchCase("oreNether", 6, "dust"));
    GrinderRecipeRegistry.registerSearchCase(new SearchCase("orePoor",   4, "nugget"));
    GrinderRecipeRegistry.registerSearchCase(new SearchCase("ore",       2, "dust"));
    GrinderRecipeRegistry.registerSearchCase(new SearchCase("ore",       2, "gem"));

    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustManolite", false), OreDictionary.getOres("ingotManolium", false));
    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustManolazium", false), OreDictionary.getOres("ingotManolazium", false));
    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustGold", false), OreDictionary.getOres("ingotGold", false));
    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustIron", false), OreDictionary.getOres("ingotIron", false));
    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustDiamond", false), OreDictionary.getOres("gemDiamond", false));
    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustEmerald", false), OreDictionary.getOres("gemEmerald", false));
    SmelterRecipeRegistry.addRecipe(OreDictionary.getOres("dustLapis", false), OreDictionary.getOres("gemLapis", false));

    SmelterRecipeRegistry.registerSearchCase(new SearchCase("dust", 1, "ingot"));
    SmelterRecipeRegistry.registerSearchCase(new SearchCase("dust", 1, "gem"));
	}

  public static class SearchCase
  {

    final String inputPrefix;
    final int    resultAmount;
    final String resultPrefix;

    public SearchCase(String inputPrefix, int resultAmount)
    {
      this(inputPrefix, resultAmount, "dust");
    }

    public SearchCase(String inputPrefix, int resultAmount, String resultPrefix)
    {
      this.inputPrefix = inputPrefix;
      this.resultAmount = resultAmount;
      this.resultPrefix = resultPrefix;
    }
  }
}
