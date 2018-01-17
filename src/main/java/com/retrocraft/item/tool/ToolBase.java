package com.retrocraft.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ToolBase extends ItemPickaxe
{
  protected String name;
  protected Material[] materials;
  protected boolean isBasicMaterial;
  
  private static int mineRadius = 1, mineDepth = 0;

  public ToolBase(String name, ToolMaterial material, Material[] breakableMaterials)
  {
    super(material);
    this.name = name;
    this.materials = breakableMaterials;
    this.isBasicMaterial = 
        (material == ToolMaterial.WOOD
        || material == ToolMaterial.STONE
        || material == ToolMaterial.IRON
        || material == ToolMaterial.GOLD
        || material == ToolMaterial.DIAMOND);
    
    setRegistryName(name);
    setUnlocalizedName(name);
  }  

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos blockPos, EntityPlayer player)
  {
    IBlockState blockState = player.world.getBlockState(blockPos);
    RayTraceResult mop = ToolFunctions.raytraceFromEntity(player.world, player, false, 4.5d);
    if (mop == null)
      return super.onBlockStartBreak(stack, blockPos, player);
    EnumFacing sideHit = mop.sideHit;

    if (!ToolFunctions.isEffective(blockState.getMaterial(), materials))
      return super.onBlockStartBreak(stack, blockPos, player);

    int xDist, yDist, zDist;
    yDist = xDist = zDist = mineRadius;

    switch (sideHit)
    {
    case DOWN:
    case UP:
      yDist = mineDepth;
      break;
    case NORTH:
    case SOUTH:
      zDist = mineDepth;
      break;
    case WEST:
    case EAST:
      xDist = mineDepth;
      break;
    }
    if (player.isSneaking())
    {
      if (!super.onBlockStartBreak(stack, blockPos, player))
      {
      }
      // breakExtraBlock(player.world, blockPos, sideHit, player, blockPos);
    } else
    {
      for (int xPos = blockPos.getX() - xDist; xPos <= blockPos.getX() + xDist; xPos++)
        for (int yPos = blockPos.getY() - yDist; yPos <= blockPos.getY() + yDist; yPos++)
          for (int zPos = blockPos.getZ() - zDist; zPos <= blockPos.getZ() + zDist; zPos++)
          {
            if (xPos == blockPos.getX() && yPos == blockPos.getY() && zPos == blockPos.getZ())
              continue;
            BlockPos radPos = new BlockPos(xPos, yPos, zPos);
            if (!super.onBlockStartBreak(stack, radPos, player)
                && ToolFunctions.isEffective(player.world.getBlockState(blockPos).getMaterial(), materials))
            {
              ToolFunctions.breakExtraBlock(player.world, radPos, sideHit, player, blockPos);
            }
          }
    }
    return super.onBlockStartBreak(stack, blockPos, player);
  }
}