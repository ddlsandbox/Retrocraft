package com.retrocraft.machine.smelter;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.block.BlockTileEntity;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSmelter extends BlockTileEntity<TileSmelter>
{
  
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public BlockSmelter(String name)
  {
    super(Material.ROCK, name);
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
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
      EntityLivingBase player, ItemStack stack)
  {
    world.setBlockState(pos, state.withProperty(BlockHorizontal.FACING,
        player.getHorizontalFacing().getOpposite()), 2);

    super.onBlockPlacedBy(world, pos, state, player, stack);
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return this.getDefaultState().withProperty(BlockHorizontal.FACING,
        EnumFacing.getHorizontal(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, BlockHorizontal.FACING);
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot)
  {
    return state.withProperty(BlockHorizontal.FACING,
        rot.rotate(state.getValue(BlockHorizontal.FACING)));
  }

  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirror)
  {
    return this.withRotation(state,
        mirror.toRotation(state.getValue(BlockHorizontal.FACING)));
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
