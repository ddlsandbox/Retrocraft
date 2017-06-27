package com.retrocraft.api.energy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public interface IFluidReceiver
{

  public FluidStack drain(FluidStack arg0, boolean arg1);
  public FluidStack drain(int arg0, boolean arg1);
  public int fill(FluidStack arg0, boolean arg1);
  public IFluidTankProperties[] getTankProperties();
  public ItemStack getContainer();

}
