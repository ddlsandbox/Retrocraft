package com.retrocraft.machine.grinder;

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

public class BlockOreGrinder extends BlockTileEntityOrientable<TileOreGrinder>
{
  
  public BlockOreGrinder(String name)
  {
    super(Material.ROCK, name, BlockHorizontal.FACING);
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
  public BlockOreGrinder setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public Class<TileOreGrinder> getTileEntityClass()
  {
    return TileOreGrinder.class;
  }

  @Override
  public TileOreGrinder createTileEntity(World world, IBlockState state)
  {
    return new TileOreGrinder();
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos,
      IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side,
      float hitX, float hitY, float hitZ)
  {
    if (worldIn.isRemote)
      return true;

    playerIn.openGui(RetroCraft.instance, ModGuiHandler.OREGRINDER, worldIn,
        pos.getX(), pos.getY(), pos.getZ());
    return true;
  }
}
