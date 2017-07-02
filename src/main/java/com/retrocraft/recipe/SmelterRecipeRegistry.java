package com.retrocraft.recipe;

import java.util.Hashtable;
import java.util.List;

import com.retrocraft.recipe.RetrocraftRecipes.SearchCase;
import com.retrocraft.util.StackUtil;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class SmelterRecipeRegistry
{
  public static class RecipeKey
  {
    private Item item;
    private int damage;
    
    public RecipeKey(Item item, int damage)
    {
      this.item = item;
      this.damage = damage;
    }
    
    public RecipeKey(ItemStack itemStack)
    {
      this.item = itemStack.getItem();
      this.damage = itemStack.getItemDamage();
    }
    
    @Override
    public boolean equals(Object obj)
    {
      RecipeKey key = (RecipeKey) obj;
      return this.item == key.item && this.damage == key.damage;
    }
    
    @Override
    public String toString()
    {
      return item.getRegistryName().toString() + damage;
    }
  }
  
  private static final int OUTPUT_COUNT = 1;
  private static final Hashtable<RecipeKey, ItemStack> RECIPES = new Hashtable<RecipeKey, ItemStack>();
  
  public static void addRecipe(ItemStack input, ItemStack output)
  {
    RECIPES.put(new RecipeKey(input), output);
    System.out.println(
        "[RETROCRAFT] Added smelter recipe for " + input.getDisplayName() + " --> " + output.getDisplayName());
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
    RecipeKey key = new RecipeKey(input);
    for (final RecipeKey item : RECIPES.keySet())
      if (item.equals(key))
        return RECIPES.get(item);
    return null;
  }

  public static boolean existRecipeForInput(ItemStack input)
  {
    RecipeKey key = new RecipeKey(input);
    for (final RecipeKey item : RECIPES.keySet())
      if (item.equals(key))
        return true;
    return false;//RECIPES.containsKey();
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