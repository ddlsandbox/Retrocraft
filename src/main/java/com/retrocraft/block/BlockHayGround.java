package com.retrocraft.block;

import com.retrocraft.RetroCraftGlobals;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHayGround extends BlockBase
{

  public BlockHayGround(Material materialIn, String name)
  {
    super(materialIn, name, "shovel", 0, 0.4F, 2.0F, SoundType.GROUND);
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity)
  {
    super.onEntityWalk(world, pos, entity);
    if (entity instanceof EntityCow || entity instanceof EntityPig || entity instanceof EntityChicken)
    {
      if (world.rand.nextDouble() < RetroCraftGlobals.manureProb)
        world.setBlockState(pos, ModBlocks.blockManureGround.getDefaultState());
    }
  }
}
