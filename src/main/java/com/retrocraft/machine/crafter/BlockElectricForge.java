package com.retrocraft.machine.crafter;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.block.BlockBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockElectricForge extends BlockBase
{

  public BlockElectricForge(String name)
  {
    super(Material.ROCK, name);
  }
  
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (!world.isRemote) {
      if (!player.isSneaking()) {
        player.openGui(RetroCraft.instance,
              ModGuiHandler.ELECTRIC_FORGE,
              world,
              pos.getX(), pos.getY(), pos.getZ());
      }
    }
    return true;
  }
  
  @Override
  public BlockElectricForge setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }
}
