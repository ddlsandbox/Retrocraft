package com.retrocraft.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTelepipe extends BlockBase
{
  public static final PropertyDirection FACING = BlockDirectional.FACING;

  public BlockTelepipe(String name) {
    super(Material.ROCK, name);

    setHardness(3f);
    setResistance(5f);
  }

  @Override
  public BlockTelepipe setCreativeTab(CreativeTabs tab) {
    super.setCreativeTab(tab);
    return this;
  }
  
  @Override
  public boolean isOpaqueCube(IBlockState iBlockState)
  {
    return false;
  }

  @Override
  public boolean isFullCube(IBlockState iBlockState)
  {
    return false;
  }
  
  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ, int meta,
      EntityLivingBase base)
  {
    return this.getStateFromMeta(side.ordinal());
  }
  
  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return this.getDefaultState().withProperty(FACING, 
        EnumFacing.getFront(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return state.getValue(FACING).getIndex();
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot)
  {
    return state.withProperty(FACING,
        rot.rotate(state.getValue(FACING)));
  }

  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirror)
  {
    return this.withRotation(state,
        mirror.toRotation(state.getValue(FACING)));
  }
}
