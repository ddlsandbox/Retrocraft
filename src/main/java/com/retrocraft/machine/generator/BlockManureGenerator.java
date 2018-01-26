package com.retrocraft.machine.generator;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.block.BlockTileEntityOrientable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockManureGenerator extends BlockTileEntityOrientable<TileManureGenerator>
{
  
  public BlockManureGenerator(String name)
  {
    super(Material.ROCK, name, BlockHorizontal.FACING);
    this.setTickRandomly(true);
  }

  @Override
  @Deprecated
  public boolean isOpaqueCube(IBlockState state)
  {
    return false;
  }

  @Override
  @Deprecated
  public boolean isFullCube(IBlockState state)
  {
    return false;
  }

  @Override
  public BlockManureGenerator setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public Class<TileManureGenerator> getTileEntityClass()
  {
    return TileManureGenerator.class;
  }

  @Override
  public TileManureGenerator createTileEntity(World world, IBlockState state)
  {
    return new TileManureGenerator();
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos,
      IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side,
      float hitX, float hitY, float hitZ)
  {
    if (worldIn.isRemote)
      return true;

    playerIn.openGui(RetroCraft.instance, ModGuiHandler.MANUREGENERATOR, worldIn,
        pos.getX(), pos.getY(), pos.getZ());
    return true;
  }
}
