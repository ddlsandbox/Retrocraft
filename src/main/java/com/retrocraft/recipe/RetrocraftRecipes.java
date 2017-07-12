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

		GameRegistry.addShapedRecipe(
        new ItemStack(ModBlocks.blockManolium), "III", "III", "III",
        'I', ModItems.ingotManolium);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModBlocks.blockManolazium), "III", "III", "III",
        'I', ModItems.ingotManolazium);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModBlocks.blockOctirion), "III", "III", "III",
        'I', ModItems.gemOctirion);
		
		GameRegistry.addShapelessRecipe(
        new ItemStack(ModItems.ingotManolium, 9),
        new Object[] { ModBlocks.blockManolium });
		GameRegistry.addShapelessRecipe(
        new ItemStack(ModItems.ingotManolazium, 9),
        new Object[] { ModBlocks.blockManolazium });
		GameRegistry.addShapelessRecipe(
        new ItemStack(ModItems.gemOctirion, 9),
        new Object[] { ModBlocks.blockOctirion });
		
		GameRegistry.addShapedRecipe(
        new ItemStack(ModBlocks.blockMachineChasis), "ISI", "SSS", "ISI",
        'I', ModItems.ingotManolium,
        'S', Blocks.STONE);
		
		GameRegistry.addShapelessRecipe(
				new ItemStack(ModItems.dustManolazium),
				new Object[] { ModItems.dustManolite, Items.DIAMOND, Items.REDSTONE, ModItems.gemOctirion });
		
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.stoneHammer), "III", " S ", " S ",
        'I', Blocks.STONE,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.ironHammer), "III", " S ", " S ",
        'I', Blocks.IRON_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.goldHammer), "III", " S ", " S ",
        'I', Blocks.GOLD_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.diamondHammer), "III", " S ", " S ",
        'I', Blocks.DIAMOND_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumHammer), "III", "ISI", " S ",
        'I', ModItems.ingotManolium,
        'S', Items.STICK);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumHammer), "III", "ISI", " S ",
        'I', ModItems.ingotManolazium,
        'S', Items.STICK);
		
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.stoneExcavator), "I", "S", "S",
        'I', Blocks.STONE,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.ironExcavator), "I", "S", "S",
        'I', Blocks.IRON_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.goldExcavator), "I", "S", "S",
        'I', Blocks.GOLD_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.diamondExcavator), "I", "S", "S",
        'I', Blocks.DIAMOND_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumExcavator), " II", " SI", "S  ",
        'I', ModItems.ingotManolium,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumExcavator), " II", " SI", "S  ",
        'I', ModItems.ingotManolazium,
        'S', Items.STICK);
		
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.stoneStreamAxe), "II", "IS", " S",
        'I', Blocks.STONE,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.ironStreamAxe), "II", "IS", " S",
        'I', Blocks.IRON_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.goldStreamAxe), "II", "IS", " S",
        'I', Blocks.GOLD_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.diamondStreamAxe), "II", "IS", " S",
        'I', Blocks.DIAMOND_BLOCK,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumStreamAxe), "III", "IIS", "  S",
        'I', ModItems.ingotManolium,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumStreamAxe), "III", "IIS", "  S",
        'I', ModItems.ingotManolazium,
        'S', Items.STICK);
		
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumETool), "ABC", " S ", " S ",
        'A', ModItems.manoliumExcavator,
        'B', ModItems.manoliumHammer,
        'C', ModItems.manoliumStreamAxe,
        'S', Items.STICK);
		GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumETool), "ABC", " S ", " S ",
        'A', ModItems.manolaziumExcavator,
        'B', ModItems.manolaziumHammer,
        'C', ModItems.manolaziumStreamAxe,
        'S', Items.STICK);
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ModItems.manoliumSword), "I", "I", "S",
				'I', ModItems.ingotManolium,
				'S', Items.STICK);
		
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumHead), "III", "I I",
        'I', ModItems.ingotManolium);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumChest), "I I", "III", "III",
        'I', ModItems.ingotManolium);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumLegs), "III", "I I", "I I",
        'I', ModItems.ingotManolium);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manoliumFeet), "I I", "I I",
        'I', ModItems.ingotManolium);
    
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumHead), "IMI", "M M",
        'M', ModItems.magicalCore,
        'I', ModItems.ingotManolazium);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumChest), "I I", "MMM", "III",
        'M', ModItems.magicalCore,
        'I', ModItems.ingotManolazium);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumLegs), "MIM", "I I", "I I",
        'M', ModItems.magicalCore,
        'I', ModItems.ingotManolazium);
    GameRegistry.addShapedRecipe(
        new ItemStack(ModItems.manolaziumFeet), "M M", "I I",
        'M', ModItems.magicalCore,
        'I', ModItems.ingotManolazium);
    
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

		GameRegistry.addShapedRecipe(
        new ItemStack(ModBlocks.blockRepairer), "OAO", " M ", " C ",
        'O', ModItems.gemOctirion,
        'A', Blocks.ANVIL,
        'M', ModBlocks.blockMachineChasis,
        'C', ModItems.magicalCore);
		
		if(!RetroCraft.getConfig().creativeModeOnly) {
      GameRegistry.addShapedRecipe(
          new ItemStack(ModBlocks.blockWaystone), "SSS", "WCW", "OOO",
          'S', Blocks.STONEBRICK,
					'C', ModItems.magicalCore,
          'W', Items.ENDER_PEARL,
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
