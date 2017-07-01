package com.retrocraft.recipe;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.retrocraft.util.StackUtil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GrinderRecipeRegistry
{

  public static final int OUTPUT_COUNT = 2;

  public static final Hashtable<ItemStack, ItemStack> RECIPES      = new Hashtable<ItemStack, ItemStack>();
  public static final ArrayList<SearchCase>           SEARCH_CASES = new ArrayList<SearchCase>();

  public static void addRecipe(ItemStack input, ItemStack output)
  {
    RECIPES.put(input, output);
  }

  public static boolean addRecipe(List<ItemStack> inputs,
      List<ItemStack> outputs)
  {
    boolean hasWorkedOnce = false;
    for (ItemStack input : inputs)
    {
      if (StackUtil.isValid(input) && existRecipeForInput(input))
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
    return RECIPES.get(input);
  }

  public static boolean existRecipeForInput(ItemStack input)
  {
    return RECIPES.containsKey(input);
  }

  public static void registerSearchCase(SearchCase theCase)
  {
    ArrayList<String> oresNoResult = new ArrayList<String>();

    for (String ore : OreDictionary.getOreNames())
    {
      if (ore.length() > theCase.theCase.length())
      {
        if (ore.substring(0, theCase.theCase.length()).equals(theCase.theCase))
        {
          String output = theCase.resultPreString
              + ore.substring(theCase.theCase.length());

          if (!addRecipe(OreDictionary.getOres(ore, false),
              OreDictionary.getOres(output, false)))
          {
            if (!oresNoResult.contains(ore))
            {
              oresNoResult.add(ore);
            }
          }
        }
      }
    }
  }
  
  public static class SearchCase
  {

    final String theCase;
    final int    resultAmount;
    final String resultPreString;

    public SearchCase(String theCase, int resultAmount)
    {
      this(theCase, resultAmount, "dust");
    }

    public SearchCase(String theCase, int resultAmount, String resultPreString)
    {
      this.theCase = theCase;
      this.resultAmount = resultAmount;
      this.resultPreString = resultPreString;
    }
  }
}