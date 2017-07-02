package com.retrocraft.recipe;

import java.util.Hashtable;
import java.util.List;

import com.retrocraft.recipe.RetrocraftRecipes.SearchCase;
import com.retrocraft.util.StackUtil;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GrinderRecipeRegistry
{

  private static final int OUTPUT_COUNT = 2;
  private static final Hashtable<Item, ItemStack> RECIPES = new Hashtable<Item, ItemStack>();

  public static void addRecipe(ItemStack input, ItemStack output)
  {
    RECIPES.put(input.getItem(), output);
    System.out.println(
        "[RETROCRAFT] Added grinder recipe for " + input.getDisplayName() + " --> " + output.getDisplayName());
  }

  public static boolean addRecipe(List<ItemStack> inputs,
      List<ItemStack> outputs)
  {
    boolean hasWorkedOnce = false;
    for (ItemStack input : inputs)
    {
      if (StackUtil.isValid(input) && !existRecipeForInput(input))
      {
        for (ItemStack output : outputs)
        {
          if (StackUtil.isValid(output))
          {
            ItemStack outputCopy = output.copy();
            outputCopy = StackUtil.setStackSize(outputCopy, OUTPUT_COUNT);

            addRecipe(input, outputCopy);
            hasWorkedOnce = true;
          }
        }
      }
    }
    return hasWorkedOnce;
  }

  public static ItemStack getOutput(ItemStack input)
  {
    return RECIPES.get(input.getItem());
  }

  public static boolean existRecipeForInput(ItemStack input)
  {
    return RECIPES.containsKey(input.getItem());
  }

  public static void registerSearchCase(SearchCase theCase)
  {
    for (String ore : OreDictionary.getOreNames())
    {
      if (ore.length() > theCase.inputPrefix.length())
      {
        if (ore.substring(0, theCase.inputPrefix.length())
            .equals(theCase.inputPrefix))
        {
          String output = theCase.resultPrefix
              + ore.substring(theCase.inputPrefix.length());
          addRecipe(OreDictionary.getOres(ore, false),
                    OreDictionary.getOres(output, false));
        }
      }
    }
  }

}