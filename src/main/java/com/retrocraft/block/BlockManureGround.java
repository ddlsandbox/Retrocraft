package com.retrocraft.block;

import java.util.Random;

import com.retrocraft.item.ModItems;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockManureGround extends BlockBase
{
  private final Item drops = ModItems.manure;
  private final int least_quantity = 1;
  private final int most_quantity = 2;

  public BlockManureGround(Material materialIn, String name)
  {
    super(materialIn, name, "shovel", 0, 0.4F, 2.0F, SoundType.GROUND);
  }

  @Override
  public Item getItemDropped(IBlockState state, Random random, int fortune)
  {
    return drops;
  }

  @Override
  public int quantityDropped(Random random)
  {
    if (this.least_quantity >= this.most_quantity)
      return this.least_quantity;
    return this.least_quantity + random.nextInt(this.most_quantity - this.least_quantity);
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random)
  {
    if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getDefaultState(), random, fortune))
    {
      int i = random.nextInt(fortune + 2) - 1;
      if (i < 0)
      {
        i = 0;
      }
      return this.quantityDropped(random) * (i + 1);
    } 
    else
    {
      return this.quantityDropped(random);
    }
  }
}
