package com.retrocraft.tile;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileInventoryBase extends TileEntityBase implements IInventory
{

  private String      name;
  protected ItemStack[] itemStacks;

  public TileInventoryBase(String name, int slotCount)
  {
    this.name = name;
    this.itemStacks = new ItemStack[slotCount];
    clear();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
  {
    super.writeToNBT(parentNBTTagCompound);

    NBTTagList dataForAllSlots = new NBTTagList();
    for (int i = 0; i < this.itemStacks.length; ++i) {
      if (!this.itemStacks[i].isEmpty()) {  //isEmpty()
        NBTTagCompound dataForThisSlot = new NBTTagCompound();
        dataForThisSlot.setByte("Slot", (byte) i);
        this.itemStacks[i].writeToNBT(dataForThisSlot);
        dataForAllSlots.appendTag(dataForThisSlot);
      }
    }
    parentNBTTagCompound.setTag("Items", dataForAllSlots);

    return parentNBTTagCompound;
  }
  
  @Override
  public void readFromNBT(NBTTagCompound nbtTagCompound)
  {
    super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location
    final byte NBT_TYPE_COMPOUND = 10; // See NBTBase.createNewByType() for a listing
    NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

    Arrays.fill(itemStacks, ItemStack.EMPTY);           // set all slots to empty EMPTY_ITEM
    for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
      NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
      byte slotNumber = dataForOneSlot.getByte("Slot");
      if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
        this.itemStacks[slotNumber] = new ItemStack(dataForOneSlot);
      }
    }
  }
  
  /* IInventory */
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

  @Override
  public void clear()
  {
    Arrays.fill(itemStacks, ItemStack.EMPTY);
  }

  @Override
  public void closeInventory(EntityPlayer arg0)
  {
  }

  @Override
  public ItemStack decrStackSize(int slotIndex, int count)
  {
    ItemStack itemStackInSlot = getStackInSlot(slotIndex);
    if (itemStackInSlot.isEmpty())
      return ItemStack.EMPTY; // isEmpty(), EMPTY_ITEM

    ItemStack itemStackRemoved;
    if (itemStackInSlot.getCount() <= count)
    { // getStackSize
      itemStackRemoved = itemStackInSlot;
      setInventorySlotContents(slotIndex, ItemStack.EMPTY); // EMPTY_ITEM
    } else
    {
      itemStackRemoved = itemStackInSlot.splitStack(count);
      if (itemStackInSlot.getCount() == 0)
      { // getStackSize
        setInventorySlotContents(slotIndex, ItemStack.EMPTY); // EMPTY_ITEM
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
    return itemStacks.length;
  }

  @Override
  public ItemStack getStackInSlot(int slotIndex)
  {
    return itemStacks[slotIndex];
  }

  @Override
  public boolean isEmpty()
  {
    for (ItemStack itemstack : itemStacks)
    {
      if (!itemstack.isEmpty())
      { // isEmpty()
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isUsableByPlayer(EntityPlayer player)
  {
    return true;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {
  }

  @Override
  public ItemStack removeStackFromSlot(int slotIndex)
  {
    ItemStack itemStack = getStackInSlot(slotIndex);
    if (!itemStack.isEmpty())
      setInventorySlotContents(slotIndex, ItemStack.EMPTY); // isEmpty();
                                                            // EMPTY_ITEM
    return itemStack;
  }

  @Override
  public void setInventorySlotContents(int slotIndex, ItemStack itemstack)
  {
    itemStacks[slotIndex] = itemstack;
    if (!itemstack.isEmpty() && itemstack.getCount() > getInventoryStackLimit())
    { // isEmpty(); getStackSize()
      itemstack.setCount(getInventoryStackLimit()); // setStackSize()
    }
    markDirty();
  }

}
