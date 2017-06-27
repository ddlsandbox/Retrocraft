package com.retrocraft.api.energy;

import net.minecraft.util.EnumFacing;

public interface IEnergyProvider
{

  public int getEnergyStored(EnumFacing facing);
  public int getMaxEnergyStored(EnumFacing facing);
  public boolean canConnectEnergy(EnumFacing facing);
  public int extractEnergy(EnumFacing facing, int amount, boolean simulate);
  public int getThroughputOut(EnumFacing facing);
  public EnumFacing[] getOutputSides();
}
