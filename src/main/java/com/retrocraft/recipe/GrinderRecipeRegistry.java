package com.retrocraft.recipe;

import java.util.Hashtable;
import java.util.List;

import com.retrocraft.recipe.RetrocraftRecipes.SearchCase;
import com.retrocraft.util.ItemUtil;
import com.retrocraft.util.StackUtil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GrinderRecipeRegistry
{

  private static final int OUTPUT_COUNT = 2;
  private static final Hashtable<ItemStack, ItemStack> RECIPES = new Hashtable<ItemStack, ItemStack>();

  public static void addRecipe(ItemStack input, ItemStack output)
  {
    RECIPES.put(input, output);
    System.out.println(
        "[RETROCRAFT] Added grinder recipe for " + input.getDisplayName() + " --> " + output.getDisplayName());
  }

  public static boolean addRecipe(List<ItemStack> inputs,
      List<ItemStack> outputs)
  {
    boolean hasWorkedOnce = false;
    for (ItemStack input : inputs)
    {
      if (StackUtil.isNotNull(input) && !existRecipeForInput(input))
      {
        for (ItemStack output : outputs)
        {
          if (StackUtil.isNotNull(output))
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
    // TODO: Not so efficient, but it works. Use a wrapper object
    //       and redefine equals to .areItemsEqual;
    for (ItemStack key : RECIPES.keySet())
    {
      if (ItemUtil.areItemsEqual(input, key))
        return RECIPES.get(key);
    }
    return null;
  }

  public static boolean existRecipeForInput(ItemStack input)
  {
    for (ItemStack key : RECIPES.keySet())
    {
      if (ItemUtil.areItemsEqual(input, key))
        return true;
    }
    return false;
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