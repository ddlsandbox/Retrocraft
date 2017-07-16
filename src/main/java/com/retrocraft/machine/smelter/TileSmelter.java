package com.retrocraft.machine.smelter;

import com.retrocraft.RetroCraftGlobals;
import com.retrocraft.machine.IEnergyDisplay;
import com.retrocraft.recipe.SmelterRecipeRegistry;
import com.retrocraft.tile.CustomEnergyStorage;
import com.retrocraft.tile.TileInventoryBase;
import com.retrocraft.util.StackUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileSmelter extends TileInventoryBase
    implements IEnergyStorage, ITickable, IEnergyDisplay
{
  private static final int CAPACITY   = RetroCraftGlobals.oreSmelterCapacity;
  private static final int THROUGHPUT = 1000;

  private static final int ENERGY_USE = RetroCraftGlobals.oreSmelterEnergyUsed;
  private static final int BURN_TIME  = RetroCraftGlobals.oreSmelterBurnTime;

  private static final int FIELD_BURN_TIME_REMAINING = 0;
  private static final int FIELD_COUNT               = 1;

  public static final int INPUT_SLOT_NUMBER  = 0;
  public static final int OUTPUT_SLOT_NUMBER = 1;

  public int burnTimeRemaining;
  private int lastEnergy;
  private int lastBurnTime;

  protected CustomEnergyStorage storage;
  protected EnumFacing[]        inputSides;

  public TileSmelter()
  {
    super("tile.oregrinder.name", 2);

    this.storage = new CustomEnergyStorage(CAPACITY, THROUGHPUT, THROUGHPUT);

    this.inputSides = new EnumFacing[]
    { EnumFacing.NORTH };

    clear();
  }

  @SideOnly(Side.CLIENT)
  public int getEnergyScaled(int i)
  {
    return this.storage.getEnergyStored() * i
        / this.storage.getMaxEnergyStored();
  }

  @SideOnly(Side.CLIENT)
  public int getBurnTimeScaled(int i)
  {
    return this.burnTimeRemaining * i / BURN_TIME;
  }
  
  public boolean canSmelt(int theInput, int theOutput)
  {
    if (StackUtil.isValid(getStackInSlot(theInput)))
    {
      ItemStack outputOne = SmelterRecipeRegistry
          .getOutput(getStackInSlot(theInput));

      if (StackUtil.isValid(outputOne))
      {
        return (!StackUtil.isValid(getStackInSlot(theOutput))
            || (getStackInSlot(theOutput).isItemEqual(outputOne)
                && StackUtil.getStackSize(
                    getStackInSlot(theOutput)) <= getStackInSlot(theOutput)
                        .getMaxStackSize() - StackUtil.getStackSize(outputOne)));
      }
    }
    return false;
  }

  public void finishSmelting(int theInput, int theOutput)
  {
    ItemStack outputOne = SmelterRecipeRegistry
        .getOutput(getStackInSlot(theInput));
    if (StackUtil.isValid(outputOne))
    {
      if (!StackUtil.isValid(getStackInSlot(theOutput)))
      {
        setStackInSlot(theOutput, outputOne.copy());
      } else if (getStackInSlot(theOutput).getItem() == outputOne.getItem())
      {
        setStackInSlot(theOutput, StackUtil.addStackSize(
            getStackInSlot(theOutput), StackUtil.getStackSize(outputOne)));
      }
    }

    setStackInSlot(theInput,
        StackUtil.addStackSize(getStackInSlot(theInput), -1));
  }
  
  @Override
  public void updateEntity()
  {
    super.updateEntity();

    boolean canSmelt = this.canSmelt(INPUT_SLOT_NUMBER, OUTPUT_SLOT_NUMBER);

    boolean shouldPlaySound = false;

    if (canSmelt)
    {
      if (this.storage.getEnergyStored() >= ENERGY_USE)
      {
        if (this.burnTimeRemaining % 20 == 0)
        {
          shouldPlaySound = true;
        }
        this.burnTimeRemaining--;
        if (this.burnTimeRemaining <= 0)
        {
          this.finishSmelting(INPUT_SLOT_NUMBER, OUTPUT_SLOT_NUMBER);
          this.burnTimeRemaining = BURN_TIME;
        }
        this.storage.extractEnergyInternal(ENERGY_USE, false);
      }
    } 
    else
    {
      this.burnTimeRemaining = BURN_TIME;
    }

    if (!this.world.isRemote)
    {
      if ((this.storage.getEnergyStored() != this.lastEnergy
          || this.lastBurnTime != this.burnTimeRemaining)
          && sendUpdateWithInterval())
      {
        this.markDirty();
        this.lastEnergy = this.storage.getEnergyStored();
        this.lastBurnTime = this.burnTimeRemaining;
      }
    }

    if (shouldPlaySound)
    {
//      RetroCraft.proxy.playSound(SoundEvents.WEATHER_RAIN, pos, 0.5f);
      
      // this.world.playSound(null, this.getPos().getX(), this.getPos().getY(),
      // this.getPos().getZ(), SoundHandler.crusher, SoundCategory.BLOCKS,
      // 0.025F, 1.0F);
    }
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing)
  {
    if (capability == CapabilityEnergy.ENERGY)
      return true;

    return super.hasCapability(capability, facing);
  }

  static public boolean isItemValidForInputSlot(ItemStack itemStack)
  {
    return SmelterRecipeRegistry.existRecipeForInput(itemStack);
  }

  @Override
  public void writeSyncableNBT(NBTTagCompound compound, NBTType type)
  {
    if (type != NBTType.SAVE_BLOCK)
    {
      compound.setInteger("BurnTime", this.burnTimeRemaining);
    }
    this.storage.writeToNBT(compound);
    super.writeSyncableNBT(compound, type);
  }

  @Override
  public void readSyncableNBT(NBTTagCompound compound, NBTType type)
  {
    if (type != NBTType.SAVE_BLOCK)
    {
      this.burnTimeRemaining = compound.getInteger("BurnTime");
    }
    this.storage.readFromNBT(compound);
    super.readSyncableNBT(compound, type);
  }

  @Override
  public IEnergyStorage getEnergyStorage(EnumFacing facing)
  {
    return storage;
  }

  @Override
  public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
  {
    if (slotIndex == INPUT_SLOT_NUMBER)
      return SmelterRecipeRegistry.existRecipeForInput(itemStack);
    return false;
  }

  @Override
  public int getFieldCount()
  {
    return FIELD_COUNT;
  }

  @Override
  public int getField(int fieldId)
  {
    switch (fieldId)
    {
    case FIELD_BURN_TIME_REMAINING:
      return burnTimeRemaining;
    }
    return 0;
  }

  @Override
  public void setField(int fieldId, int value)
  {
    switch (fieldId)
    {
    case FIELD_BURN_TIME_REMAINING:
      burnTimeRemaining = value;
      break;
    }
  }

  @Override
  public CustomEnergyStorage getEnergyStorage()
  {
    return storage;
  }

  @Override
  public boolean needsHoldShift()
  {
    return false;
  }

  @Override
  public boolean canExtract()
  {
    return false;
  }

  @Override
  public boolean canReceive()
  {
    return true;
  }

  @Override
  public int extractEnergy(int arg0, boolean arg1)
  {
    return 0;
  }

  @Override
  public int getEnergyStored()
  {
    return storage.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored()
  {
    return storage.getMaxEnergyStored();
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate)
  {
    return this.storage.receiveEnergy(maxReceive, simulate);
  }

}
