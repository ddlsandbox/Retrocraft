package com.retrocraft.machine.generator;

import com.retrocraft.RetroCraftConfig;
import com.retrocraft.RetroCraftGlobals;
import com.retrocraft.api.energy.IEnergyProvider;
import com.retrocraft.item.ModItems;
import com.retrocraft.machine.IEnergyDisplay;
import com.retrocraft.tile.CustomEnergyStorage;
import com.retrocraft.tile.TileEntityInventory;
import com.retrocraft.util.ItemStackHandlerCustom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileManureGenerator extends TileEntityInventory
    implements IEnergyProvider, ITickable, IEnergyDisplay
{
  private static final int CAPACITY   = RetroCraftGlobals.manureGeneratorNrgCapacity;
  private static final int THROUGHPUT = RetroCraftGlobals.manureGeneratorNrgThroughput;
  private static final int EFFICIENCY = RetroCraftGlobals.manureGeneratorEfficiency;

  private static final int FIELD_BURN_TIME_REMAINING    = 0;
  private static final int FIELD_BURN_INITIAL_VALUE     = 1;
  private static final int FIELD_CURRENT_ENERGY_PRODUCE = 2;
  private static final int FIELD_COUNT                  = 3;

  public static final int INPUT_SLOT_NUMBER = 0;
  
  public int        burnTimeRemaining;
  private int       burnTimeInitialValue;
  private int       currentEnergyProduce;
  private boolean   cachedBurningState = false;

  private int lastEnergy;
  private int lastBurnTime;
  private int lastEnergyProduce;
  private int lastMaxBurnTime;

  protected CustomEnergyStorage storage;
  protected EnumFacing[]        outputSides;
  protected EnumFacing[]        inputSides;

  public TileManureGenerator()
  {
    super("tile.manuregenerator.name", 1, 0);

    this.storage = new CustomEnergyStorage(CAPACITY, 0, THROUGHPUT);
    
    this.currentEnergyProduce = EFFICIENCY;
    this.outputSides = EnumFacing.values();
    this.inputSides = EnumFacing.HORIZONTALS;

    clear();
  }
  
  protected ItemStackHandlerCustom getSlots()
  {
    return new ItemStackHandlerCustom(totalSlotsCount){
      @Override
      public boolean canInsert(ItemStack stack, int slot){
          return TileManureGenerator.this.isItemValidForSlot(slot, stack);
      }

      @Override
      public boolean canExtract(ItemStack stack, int slot){
          return false;
      }

      @Override
      public int getSlotLimit(int slot){
          return 64;
      }

      @Override
      protected void onContentsChanged(int slot){
          super.onContentsChanged(slot);
          TileManureGenerator.this.markDirty();
      }
    };
  }
  
  protected void setSlotSides()
  {
    slotsTop = slotsBottom =
    slotsNorth = slotsEast = slotsSouth = slotsWest = new int[] {0};
  }
  
  @SideOnly(Side.CLIENT)
  public int getEnergyScaled(int i)
  {
    return this.storage.getEnergyStored() * i
        / this.storage.getMaxEnergyStored();
  }

  @SideOnly(Side.CLIENT)
  public int getBurningScaled(int i)
  {
    return this.burnTimeRemaining * i / this.burnTimeInitialValue;
  }

  public double fractionOfFuelRemaining()
  {
    if (burnTimeInitialValue <= 0)
      return 0;
    double fraction = burnTimeRemaining / (double) burnTimeInitialValue;
    return MathHelper.clamp(fraction, 0.0, 1.0);
  }

  public int secondsOfFuelRemaining()
  {
    if (burnTimeRemaining <= 0)
      return 0;
    return burnTimeRemaining / 20; // 20 ticks per second
  }

  /**
   * decreases the burn time, checks if burnTimeRemaining = 0 and tries to
   * consume a new piece of fuel if one is available
   * 
   * @return true, if the fuel slot is burning
   */
  private boolean burnFuel()
  {
    ItemStack itemStack = getStackInSlot(INPUT_SLOT_NUMBER);
    boolean inventoryChanged = false;
    
    if (burnTimeRemaining > 0)
    {
      --burnTimeRemaining;
    }

    if (burnTimeRemaining == 0)
    {
      if (!itemStack.isEmpty() && getItemBurnTime(itemStack) > 0)
      {
        burnTimeRemaining = burnTimeInitialValue = getItemBurnTime(itemStack);
        itemStack.shrink(1);
        inventoryChanged = true;

        if (itemStack.getCount() == 0)
        {
          itemStack = ItemStack.EMPTY;
        }
      }
    }

    if (inventoryChanged)
      markDirty();
    
    return burnTimeRemaining > 0;
  }

  public static short getItemBurnTime(ItemStack stack)
  {
    Item item = stack.getItem();
    int burntime = item == ModItems.manure ? 1600 : 0;

    return (short) MathHelper.clamp(burntime, 0, Short.MAX_VALUE);
  }

  @Override
  public void updateEntity()
  {
    super.updateEntity();
    ItemStack itemStack = getStackInSlot(INPUT_SLOT_NUMBER);
    
    /* update block (working/notworking) */
    if (cachedBurningState == itemStack.isEmpty())
    {
      cachedBurningState = !itemStack.isEmpty();
      if (world.isRemote)
      {
        IBlockState iblockstate = this.world.getBlockState(pos);
        final int FLAGS = 3; // I'm not sure what these flags do, exactly.
        world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
      }
      world.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }

    if (!this.world.isRemote)
    {
      boolean flag = burnFuel();

      if (flag)
      {
        this.storage.receiveEnergyInternal(this.currentEnergyProduce, false);
      }

      if ((this.storage.getEnergyStored() != this.lastEnergy
          || this.lastBurnTime != this.burnTimeRemaining
          || this.lastEnergyProduce != this.currentEnergyProduce
          || this.lastMaxBurnTime != this.burnTimeInitialValue)
          && sendUpdateWithInterval())
      {
        this.markDirty();
        this.lastEnergy = this.storage.getEnergyStored();
        this.lastBurnTime = this.burnTimeRemaining;
        this.lastEnergyProduce = this.currentEnergyProduce;
        this.lastMaxBurnTime = this.burnTimeInitialValue;
      }
    }
    else
    {
      if (burnTimeRemaining > 0 && burnTimeRemaining != lastBurnTime)
      {
        lastBurnTime = burnTimeRemaining;
        
        //RetroCraft.proxy.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, pos, 0.5f);
        if (!RetroCraftConfig.disableParticles)
        {
          world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                pos.getX() + 0.5,
                pos.getY() + 1, //  + (world.rand.nextDouble()) * 2
                pos.getZ() + 0.5, 0, .05, 0);
        }
      }
    }
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing)
  {
    if (capability == CapabilityEnergy.ENERGY
        || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      return true;

    return super.hasCapability(capability, facing);
  }

  static public boolean isItemValidForFuelSlot(ItemStack itemStack)
  {
    return getItemBurnTime(itemStack) > 0;
  }
  
  @Override
  public void writeSyncableNBT(NBTTagCompound compound, NBTType type)
  {
    if (type != NBTType.SAVE_BLOCK)
    {
      compound.setInteger("BurnTime", this.burnTimeRemaining);
      compound.setInteger("MaxBurnTime", this.burnTimeInitialValue);
      compound.setInteger("CurrentEnergy", this.currentEnergyProduce);
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
      this.burnTimeInitialValue = compound.getInteger("MaxBurnTime");
      this.currentEnergyProduce = compound.getInteger("CurrentEnergy");
    }
    this.storage.readFromNBT(compound);
    super.readSyncableNBT(compound, type);
  }

  @Override
  public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate)
  {
    return storage.extractEnergy(maxExtract, simulate);
  }

  @Override
  public int getEnergyStored(EnumFacing facing)
  {
    return storage.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored(EnumFacing facing)
  {
    return storage.getMaxEnergyStored();
  }

  @Override
  public boolean canConnectEnergy(EnumFacing arg0)
  {
    return true;
  }

  @Override
  public IEnergyStorage getEnergyStorage(EnumFacing facing)
  {
    return storage;
  }

  @Override
  public int getThroughputOut(EnumFacing facing)
  {
    return storage.getMaxExtract();
  }

  public EnumFacing[] getOutputSides()
  {
    return outputSides;
  }

  @Override
  public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
  {
    if (slotIndex == INPUT_SLOT_NUMBER)
      return TileManureGenerator.isItemValidForFuelSlot(itemStack);
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
    case FIELD_BURN_INITIAL_VALUE:
      return burnTimeInitialValue;
    case FIELD_BURN_TIME_REMAINING:
      return burnTimeRemaining;
    case FIELD_CURRENT_ENERGY_PRODUCE:
      return currentEnergyProduce;
    }
    return 0;
  }

  @Override
  public void setField(int fieldId, int value)
  {
    switch (fieldId)
    {
    case FIELD_BURN_INITIAL_VALUE:
      burnTimeInitialValue = value;
      break;
    case FIELD_BURN_TIME_REMAINING:
      burnTimeRemaining = value;
      break;
    case FIELD_CURRENT_ENERGY_PRODUCE:
      currentEnergyProduce = value;
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

}
