package com.retrocraft.tile;

import javax.annotation.Nullable;

import com.retrocraft.util.ItemStackHandlerCustom;
import com.retrocraft.util.StackUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumSkyBlock;

public abstract class TileEntityInventory extends TileEntityBase implements ISidedInventory, ITickable
{
  private final String name;
  
  protected final int inputSlotsCount;
  protected final int outputSlotsCount;
  protected final int totalSlotsCount;
  
  protected final int firstInputSlot, lastInputSlot;
  protected final int firstOutputSlot, lastOutputSlot;

  protected int[] slotsTop, slotsBottom, slotsNorth, slotsEast, slotsSouth, slotsWest;
  
  private ItemStackHandlerCustom slots;
  
  public TileEntityInventory(String name, int inputSlots, int outputSlots)
  {
    this.name = name;
    
    this.inputSlotsCount = inputSlots;
    this.outputSlotsCount = outputSlots;
    this.totalSlotsCount = inputSlotsCount + outputSlotsCount;
    
    this.firstInputSlot = 0;
    this.lastInputSlot = inputSlotsCount - 1;
    this.firstOutputSlot = inputSlotsCount;
    this.lastOutputSlot = firstOutputSlot + outputSlotsCount - 1;
    
    this.slots = this.getSlots();
    this.setSlotSides();

    clear();
  }
  
  protected abstract ItemStackHandlerCustom getSlots();
  
  /* set slotsTop, bottom, North, East, South, West */
  protected abstract void setSlotSides();
  
  @Override
  public void openInventory(EntityPlayer player) { }
  
  @Override
  public void closeInventory(EntityPlayer arg0) { }
  
  @Override
  public void clear()
  {
    for (int i=0; i<totalSlotsCount; ++i)
      slots.setStackInSlot(i, StackUtil.getNull());
  }

  @Override
  public boolean isEmpty()
  {
    for (int i=0; i<totalSlotsCount; ++i)
      if (StackUtil.isNotNull(slots.getStackInSlot(i)))
          return false;
    return true;
  }
  
  /**
   * Removes some of the units from stack in the given slot, and returns as a separate stack
   * @param slotIndex the slot number to remove the items from
   * @param count the number of units to remove
   * @return a new stack containing the units removed from the slot
   */
  @Override
  public ItemStack decrStackSize(int index, int count)
  {
    ItemStack itemStackInSlot = getStackInSlot(index);
    if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;  //isEmpty(), EMPTY_ITEM

    ItemStack itemStackRemoved;
    if (itemStackInSlot.getCount() <= count) { //getStackSize
      itemStackRemoved = itemStackInSlot;
      setInventorySlotContents(index, ItemStack.EMPTY); // EMPTY_ITEM
    } else {
      itemStackRemoved = itemStackInSlot.splitStack(count);
      if (itemStackInSlot.getCount() == 0) { //getStackSize
        setInventorySlotContents(index, ItemStack.EMPTY); //EMPTY_ITEM
      }
    }
    markDirty();
    return itemStackRemoved;
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public int getSizeInventory()
  {
    return slots.getSlots();
  }

  @Override
  public ItemStack getStackInSlot(int index)
  {
    return slots.getStackInSlot(index);
  }
  
  public void setStackInSlot(int index, ItemStack stack)
  {
    slots.setStackInSlot(index, stack);
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack)
  {
    return true;
  }

  @Override
  public boolean isUsableByPlayer(EntityPlayer player)
  {
    if (this.world.getTileEntity(this.pos) != this) 
      return false;
    final double X_CENTRE_OFFSET = 0.5;
    final double Y_CENTRE_OFFSET = 0.5;
    final double Z_CENTRE_OFFSET = 0.5;
    final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
    return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack itemStack = getStackInSlot(index);
    if (!itemStack.isEmpty()) setInventorySlotContents(index, ItemStack.EMPTY);
    return itemStack;
  }

  @Override
  public int getFieldCount()
  {
    return 0;
  }
  
  @Override
  public int getField(int index)
  {
    return 0;
  }
  
  @Override
  public void setField(int index, int value) { }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack)
  {
    slots.setStackInSlot(index, stack);
    if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) 
    {
      stack.setCount(getInventoryStackLimit());
    }
    markDirty(); 
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public boolean hasCustomName()
  {
    return false;
  }
  
  @Nullable
  @Override
  public ITextComponent getDisplayName() {
    return this.hasCustomName() 
        ? new TextComponentString(this.getName()) 
        : new TextComponentTranslation(this.getName());
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing facing)
  {
    return slots.canExtract(stack, index);
  }

  @Override
  public boolean canInsertItem(int index, ItemStack stack, EnumFacing facing)
  {
    return slots.canInsert(stack, index);
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    switch(side)
    {
    case DOWN:
      return slotsBottom;
    case UP:
      return slotsTop;
    case NORTH:
      return slotsNorth;
    case EAST:
      return slotsEast;
    case SOUTH:
      return slotsSouth;
    case WEST:
      return slotsWest;
    default:
      return null;
    }
  }

  /* NBT */
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
  {
    super.writeToNBT(parentNBTTagCompound);

    NBTTagList dataForAllSlots = new NBTTagList();
    for (int i = 0; i < totalSlotsCount; ++i) {
      if (!this.getStackInSlot(i).isEmpty()) 
      {
        NBTTagCompound dataForThisSlot = new NBTTagCompound();
        dataForThisSlot.setByte("Slot", (byte) i);
        this.getStackInSlot(i).writeToNBT(dataForThisSlot);
        dataForAllSlots.appendTag(dataForThisSlot);
      }
    }
    parentNBTTagCompound.setTag("Items", dataForAllSlots);
    return parentNBTTagCompound;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtTagCompound)
  {
    super.readFromNBT(nbtTagCompound);
    
    final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
    
    NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

    for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
      NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
      byte slotNumber = dataForOneSlot.getByte("Slot");
      if (slotNumber >= 0 && slotNumber < totalSlotsCount) {
        slots.setStackInSlot(slotNumber, new ItemStack(dataForOneSlot));
      }
    }
  }
  

  // This method is called every tick to update the tile entity
  // It runs both on the server and the client.
  @Override
  public void updateEntity() {
    // The block update (for renderer) is only required on client side, but the lighting is required on both, since
    //    the client needs it for rendering and the server needs it for crop growth etc
    super.updateEntity();
    
    if (!isEmpty()) {
      if (world.isRemote) {
        IBlockState iblockstate = this.world.getBlockState(pos);
        final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
        world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
      }
      world.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }
  }
}
