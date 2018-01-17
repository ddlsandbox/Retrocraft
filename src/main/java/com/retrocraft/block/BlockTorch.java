package com.retrocraft.block;

import java.util.Random;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorch extends BlockBase
{

  public static final PropertyDirection FACING = BlockDirectional.FACING;

  private static final int LIGHT_VALUE = 15;
  protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.3500000059604645D, 0.0D, 0.3500000059604645D, 0.6500000238418579D, 1D, 0.6500000238418579D);
  protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
  protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
  protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
  protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
  protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);

  public BlockTorch(String name)
  {
    super(Material.CIRCUITS, name);
    this.setTickRandomly(true);

    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(1.5F);
    this.setResistance(3.0F);
    this.setSoundType(SoundType.METAL);
  }

  @Override
  public BlockTorch setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
      switch ((EnumFacing)state.getValue(FACING))
      {
          case EAST:
              return TORCH_EAST_AABB;

          case WEST:
              return TORCH_WEST_AABB;

          case SOUTH:
              return TORCH_SOUTH_AABB;

          case NORTH:
              return TORCH_NORTH_AABB;
              
          case DOWN:
            return DOWN_AABB;
            
          default:
            return STANDING_AABB;
      }
  }

//  @Nullable
//  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
//  {
//      return NULL_AABB;
//  }
  
  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
  {
    return LIGHT_VALUE;
  }
  
  @Override
  public BlockRenderLayer getBlockLayer()
  {
    return BlockRenderLayer.CUTOUT;
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

  public void randomDisplayTick(IBlockState stateIn, World worldIn,
      BlockPos pos, Random rand)
  {
    EnumFacing enumfacing = (EnumFacing) stateIn.getValue(FACING);
    double d0 = (double) pos.getX() + 0.45D;
    double d1;
    double d2 = (double) pos.getZ() + 0.45D;

    if (enumfacing == EnumFacing.DOWN)
      d1 = (double) pos.getY();
    else
      d1 = (double) pos.getY() + 1.0D;
    
    if (enumfacing.getAxis().isHorizontal())
    {
      EnumFacing enumfacing1 = enumfacing.getOpposite();
      worldIn.spawnParticle(EnumParticleTypes.END_ROD,
          d0 + 0.27D * (double) enumfacing1.getFrontOffsetX(), d1 + 0.22D,
          d2 + 0.27D * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D,
          new int[0]);
    } else
    {
      worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0, d1, d2, 0.0D, 0.0D,
          0.0D, new int[0]);
    }
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
