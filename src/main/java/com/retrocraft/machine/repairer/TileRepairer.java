package com.retrocraft.machine.repairer;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.RetroCraftGlobals;
import com.retrocraft.machine.IEnergyDisplay;
import com.retrocraft.tile.CustomEnergyStorage;
import com.retrocraft.tile.TileEntityInventory;
import com.retrocraft.util.ItemStackHandlerCustom;
import com.retrocraft.util.StackUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileRepairer extends TileEntityInventory
  implements IEnergyStorage, ITickable, IEnergyDisplay {
  
	// Create and initialize the itemStacks variable that will store store the itemStacks
	private static final int INPUT_SLOTS_COUNT = 1;
	private static final int OUTPUT_SLOTS_COUNT = 1;
	private static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	private static final int INPUT_SLOT_NUMBER = 0;
	private static final int OUTPUT_SLOT_NUMBER = 1;

  private static final int CAPACITY   = RetroCraftGlobals.repairerCapacity;
  private static final int THROUGHPUT = RetroCraftGlobals.repairerThroughput;

  private static final int ENERGY_USE = RetroCraftGlobals.repairerEnergyUsed;
  public int REPAIR_TIME = 0;

  private static final int FIELD_REPAIR_TIME_REMAINING = 0;
  private static final int FIELD_COUNT               = 1;
  
  public int repairTimeRemaining;
  private int lastEnergy;
  private int lastRepairTime;

  protected CustomEnergyStorage storage;
  
	public TileRepairer()
	{
	  super("tile.repairer.name", INPUT_SLOTS_COUNT, OUTPUT_SLOTS_COUNT);
	  
	  this.storage = new CustomEnergyStorage(CAPACITY, THROUGHPUT, THROUGHPUT);
    this.repairTimeRemaining = 0;
	}

	protected ItemStackHandlerCustom getSlots()
	{
	  return new ItemStackHandlerCustom(TOTAL_SLOTS_COUNT){
      @Override
      public boolean canInsert(ItemStack stack, int slot){
          return TileRepairer.this.isItemValidForSlot(slot, stack);
      }

      @Override
      public boolean canExtract(ItemStack stack, int slot){
          return slot == OUTPUT_SLOT_NUMBER;
      }

      @Override
      public int getSlotLimit(int slot){
          return 1;
      }

      @Override
      protected void onContentsChanged(int slot){
          super.onContentsChanged(slot);
          ItemStack stack = getStackInSlot(slot);
          if (slot == INPUT_SLOT_NUMBER && StackUtil.isNotNull(stack))
          {
            TileRepairer.this.REPAIR_TIME = stack.getMaxDamage();
          }
          TileRepairer.this.markDirty();
      }
    };
	}
	
	protected void setSlotSides()
	{
	  slotsTop = new int[] {0};
	  slotsBottom = new int[] {1};
	  slotsNorth = slotsEast = slotsSouth = slotsWest = new int[] {0,1};
	}

  //------------------------

  @SideOnly(Side.CLIENT)
  public int getEnergyScaled(int i)
  {
    return this.storage.getEnergyStored() * i / this.storage.getMaxEnergyStored();
  }

  @SideOnly(Side.CLIENT)
  public int getRepairTimeScaled(int maxScale)
  {
    return this.repairTimeRemaining * maxScale / REPAIR_TIME;
  }

  public boolean canRepairOn()
  {
    return StackUtil.isValid(getStackInSlot(INPUT_SLOT_NUMBER))
        && getStackInSlot(OUTPUT_SLOT_NUMBER).isEmpty();
  }

  @Override
  public void updateEntity()
  {
    super.updateEntity();

    boolean repaired = false;

    boolean canRepair = this.canRepairOn();

    boolean shouldPlaySound = false;

    if (canRepair)
    {
      if (this.storage.getEnergyStored() >= ENERGY_USE)
      {
        if (this.repairTimeRemaining % 20 == 0)
        {
          shouldPlaySound = true;
        }
        this.repairTimeRemaining = repairItem();
        if (this.repairTimeRemaining <= 0)
        {
          repaired = true;
          this.repairTimeRemaining = REPAIR_TIME = 0;
        }
        this.storage.extractEnergyInternal(ENERGY_USE, false);
      }
    } 
    else
    {
      this.repairTimeRemaining = REPAIR_TIME = 0;
    }

    if (!this.world.isRemote)
    {
      if ((this.storage.getEnergyStored() != this.lastEnergy || this.lastRepairTime != this.repairTimeRemaining)
          && sendUpdateWithInterval())
      {
        this.markDirty();
        this.lastEnergy = this.storage.getEnergyStored();
        this.lastRepairTime = this.repairTimeRemaining;
      }
    } 
    else 
    {
      if (shouldPlaySound)
      {
        if (!RetroCraftConfig.disableParticles)
          world.spawnParticle(EnumParticleTypes.REDSTONE,
            pos.getX() + world.rand.nextDouble(),
            pos.getY() + 1,
            pos.getZ() + world.rand.nextDouble(), 
            0, 0.03, 0);
        
        RetroCraft.proxy.playSound(SoundEvents.WEATHER_RAIN_ABOVE, pos, 0.5f);
      }
  
      if (repaired)
      {
        RetroCraft.proxy.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, pos, 1f);
      }
    }
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing)
  {
    if (capability == CapabilityEnergy.ENERGY)
      return true;

    return super.hasCapability(capability, facing);
  }

	@Override
  public int getInventoryStackLimit() {
    return 1;
  }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == firstInputSlot && stack.getItemDamage() > 0;
	}

  /* custom methods */
  
  public int repairItem()
  {
    final ItemStack inputStack  = getStackInSlot(INPUT_SLOT_NUMBER);
    final ItemStack outputStack = getStackInSlot(OUTPUT_SLOT_NUMBER);
    if (inputStack.isEmpty() || inputStack.getItemDamage() == 0 || !outputStack.isEmpty())
      return -1;
    
    inputStack.setItemDamage(inputStack.getItemDamage() - 1);
    
    int itemDamage = inputStack.getItemDamage(); 
    if (itemDamage == 0)
    {
      setStackInSlot(OUTPUT_SLOT_NUMBER, inputStack.copy());
      setStackInSlot(INPUT_SLOT_NUMBER, StackUtil.getNull());
      markDirty();
    }
    
    return itemDamage;
  }
  
  @Override
  public void writeSyncableNBT(NBTTagCompound compound, NBTType type)
  {
    if (type != NBTType.SAVE_BLOCK)
    {
      compound.setInteger("RepairTime", this.repairTimeRemaining);
    }
    this.storage.writeToNBT(compound);
    super.writeSyncableNBT(compound, type);
  }

  @Override
  public void readSyncableNBT(NBTTagCompound compound, NBTType type)
  {
    if (type != NBTType.SAVE_BLOCK)
    {
      this.repairTimeRemaining = compound.getInteger("RepairTime");
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
  public int getFieldCount()
  {
    return FIELD_COUNT;
  }

  @Override
  public int getField(int fieldId)
  {
    switch (fieldId)
    {
    case FIELD_REPAIR_TIME_REMAINING:
      return repairTimeRemaining;
    }
    return 0;
  }

  @Override
  public void setField(int fieldId, int value)
  {
    switch (fieldId)
    {
    case FIELD_REPAIR_TIME_REMAINING:
      repairTimeRemaining = value;
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
