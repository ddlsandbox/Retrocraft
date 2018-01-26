package com.retrocraft.machine.smelter;

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

public class BlockSmelter extends BlockTileEntityOrientable<TileSmelter>
{
  
  public BlockSmelter(String name)
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
  public BlockSmelter setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public Class<TileSmelter> getTileEntityClass()
  {
    return TileSmelter.class;
  }

  @Override
  public TileSmelter createTileEntity(World world, IBlockState state)
  {
    return new TileSmelter();
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos,
      IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side,
      float hitX, float hitY, float hitZ)
  {
    if (worldIn.isRemote)
      return true;

    playerIn.openGui(RetroCraft.instance, ModGuiHandler.ORESMELTER, worldIn,
        pos.getX(), pos.getY(), pos.getZ());
    return true;
  }
}
