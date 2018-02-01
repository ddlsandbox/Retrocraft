package com.retrocraft.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTileEntityOrientable<TE extends TileEntity> extends BlockTileEntity<TE>
{

  private static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public BlockTileEntityOrientable(Material material, String name, PropertyDirection facing)
  {
    super(material, name);
    
    //FACING = facing;
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
      EntityLivingBase player, ItemStack stack)
  {

    world.setBlockState(pos, state.withProperty(FACING,
          player.getHorizontalFacing().getOpposite()), 2);

    super.onBlockPlacedBy(world, pos, state, player, stack);
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return this.getDefaultState().withProperty(FACING,
        EnumFacing.getHorizontal(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
      return state.getValue(FACING).getHorizontalIndex();
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
