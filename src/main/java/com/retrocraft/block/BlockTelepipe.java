package com.retrocraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTelepipe extends BlockBase
{
  public static final PropertyBool UP = PropertyBool.create("up");
  public static final PropertyBool DOWN = PropertyBool.create("down");
  public static final PropertyBool NORTH = PropertyBool.create("north");
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  public static final PropertyBool EAST = PropertyBool.create("east");
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public BlockTelepipe(String name)
  {
    super(Material.ROCK, name);

    setHardness(3f);
    setResistance(5f);

    this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(DOWN, false)
        .withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false)
        .withProperty(WEST, false));
  }

  @Override
  public BlockTelepipe setCreativeTab(CreativeTabs tab)
  {
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
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
  {
    boolean flagN = this.canConnectTo(worldIn, pos.north(), false);
    boolean flagE = this.canConnectTo(worldIn, pos.east(), false);
    boolean flagS = this.canConnectTo(worldIn, pos.south(), false);
    boolean flagW = this.canConnectTo(worldIn, pos.west(), false);
    boolean flagD = this.canConnectTo(worldIn, pos.down(), false);
    boolean flagU = this.canConnectTo(worldIn, pos.up(), true);
    return state
        .withProperty(UP, flagU)
        .withProperty(DOWN, flagD)
        .withProperty(NORTH, flagN)
        .withProperty(EAST, flagE).withProperty(SOUTH, flagS).withProperty(WEST, flagW);
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
      float hitZ, int meta, EntityLivingBase base)
  {
    return this.getStateFromMeta(side.ordinal());
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return this.getDefaultState();
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return 0;
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, UP, DOWN, NORTH, EAST, WEST,
        SOUTH);
  }

  public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, boolean up)
  {
    IBlockState state = worldIn.getBlockState(pos);
    Block block = state.getBlock();
    return block == this || (up && block == ModBlocks.blockTeleportPipe);
  }
}
