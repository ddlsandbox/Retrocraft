package com.retrocraft.item.replacer;

import com.retrocraft.util.ItemStackHandlerCustom;
import com.retrocraft.util.StackUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerReplacer extends Container
{

  final int INPUT_SLOTS_XPOS = 80;
  final int INPUT_SLOTS_YPOS = 43;
  
  private final ItemStackHandlerCustom bagInventory;
  private final InventoryPlayer inventory;
  private final ItemStack sack;

  
  public ContainerReplacer(ItemStack sack, InventoryPlayer inventory)
  {
    this.inventory = inventory;
    this.bagInventory = new ItemStackHandlerCustom(1)
    {
      @Override
      public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
      {
        return super.insertItem(slot, stack, simulate);
      };
    };
    this.sack = sack;
    this.addSlotToContainer(
        new AllSlot(
            this.bagInventory, 
            0, 
            INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS)
    {
      @Override
      public boolean isItemValid(ItemStack stack)
      {
        return true;
      }
    });

    /* inventory */
    for (int i = 0; i < 3; i++)
    {
      for (int j = 0; j < 9; j++)
      {
        this.addSlotToContainer(
            new Slot(
                inventory, 
                j + i * 9 + 9, 
                8 + j * 18, 
                97 + i * 18));
      }
    }
    
    /* hotbar */
    for (int i = 0; i < 9; i++)
    {
      if (i == inventory.currentItem)
      {
        this.addSlotToContainer(
            new SlotImmovable(inventory, i, 8 + i * 18, 97+62));
      } else
      {
        this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 97+62));
      }
    }

    ItemStack stack = inventory.getCurrentItem();
    if (StackUtil.isValid(stack) && stack.getItem() instanceof ToolReplacer)
    {
      NBTTagCompound compound = inventory.getCurrentItem().getTagCompound();
      if (compound != null)
      {
        if (this.bagInventory != null && this.bagInventory.getSlots() > 0)
        {
          NBTTagList tagList = compound.getTagList("Items", 10);
          for (int i = 0; i < this.bagInventory.getSlots(); i++)
          {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            this.bagInventory.setStackInSlot(i,
                tagCompound != null && tagCompound.hasKey("id") ? new ItemStack(tagCompound) : StackUtil.getNull());
          }
        }
      }
    }
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot)
  {
    int inventoryStart = this.bagInventory.getSlots();
    int inventoryEnd = inventoryStart + 26;
    int hotbarStart = inventoryEnd + 1;
    int hotbarEnd = hotbarStart + 8;

    Slot theSlot = this.inventorySlots.get(slot);

    if (theSlot != null && theSlot.getHasStack())
    {
      ItemStack newStack = theSlot.getStack();
      ItemStack currentStack = newStack.copy();

      // Other Slots in Inventory excluded
      if (slot >= inventoryStart)
      {
        if (!this.mergeItemStack(newStack, 0, 1, false))
        {
          if (slot >= inventoryStart && slot <= inventoryEnd)
          {
            if (!this.mergeItemStack(newStack, hotbarStart, hotbarEnd + 1, false))
            {
              return StackUtil.getNull();
            }
          } else if (slot >= inventoryEnd + 1 && slot < hotbarEnd + 1
              && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd + 1, false))
          {
            return StackUtil.getNull();
          }
        }
        //
      }
      else if (!this.mergeItemStack(newStack, inventoryStart, hotbarEnd + 1, false))
      {
        return StackUtil.getNull();
      }

      if (!StackUtil.isValid(newStack))
      {
        theSlot.putStack(StackUtil.getNull());
      } else
      {
        theSlot.onSlotChanged();
      }

      if (StackUtil.getStackSize(newStack) == StackUtil.getStackSize(currentStack))
      {
        return StackUtil.getNull();
      }
      theSlot.onTake(player, newStack);

      return currentStack;
    }
    return StackUtil.getNull();
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
  {
    if (clickTypeIn == ClickType.SWAP && dragType == this.inventory.currentItem)
    {
      return ItemStack.EMPTY;
    } 
    else
    {
      return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
  }

  @Override
  public void onContainerClosed(EntityPlayer player)
  {
    ItemStack stack = this.inventory.getCurrentItem();
    if (StackUtil.isValid(stack) && stack.getItem() instanceof ToolReplacer)
    {
      NBTTagCompound compound = this.inventory.getCurrentItem().getTagCompound();
      if (compound == null)
      {
        compound = new NBTTagCompound();
      }

      if (this.bagInventory != null && this.bagInventory.getSlots() > 0)
      {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < this.bagInventory.getSlots(); i++)
        {
          ItemStack slot = this.bagInventory.getStackInSlot(i);
          NBTTagCompound tagCompound = new NBTTagCompound();
          if (StackUtil.isValid(slot))
          {
            slot.writeToNBT(tagCompound);
          }
          tagList.appendTag(tagCompound);
        }
        compound.setTag("Items", tagList);
      }

      stack.setTagCompound(compound);
    }
    super.onContainerClosed(player);
  }

  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return !sack.isEmpty() && player.getHeldItemMainhand() == sack;
  }

  public class AllSlot extends SlotItemHandler
  {
    private final ItemStackHandlerCustom handler;

    public AllSlot(ItemStackHandlerCustom handler, int index, int xPosition, int yPosition)
    {
      super(handler, index, xPosition, yPosition);
      this.handler = handler;
    }

    @Override
    public ItemStack getStack()
    {
      return this.handler.getStackInSlot(this.getSlotIndex());
    }

    @Override
    public void putStack(ItemStack stack)
    {
      this.handler.setStackInSlot(this.getSlotIndex(), stack);
      this.onSlotChanged();
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
      ItemStack maxAdd = stack.copy();
      maxAdd.setCount(stack.getMaxStackSize());
      ItemStack currentStack = this.handler.getStackInSlot(this.getSlotIndex());
      this.handler.setStackInSlot(this.getSlotIndex(), ItemStack.EMPTY);
      ItemStack remainder = this.handler.insertItemInternal(this.getSlotIndex(), maxAdd, true);
      this.handler.setStackInSlot(this.getSlotIndex(), currentStack);
      return stack.getMaxStackSize() - remainder.getCount();
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
      return !this.handler.extractItemInternal(this.getSlotIndex(), 1, true).isEmpty();
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
      return this.handler.extractItemInternal(this.getSlotIndex(), amount, false);
    }
  }

  public class SlotImmovable extends Slot
  {

    public SlotImmovable(IInventory inventory, int id, int x, int y)
    {
      super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
      return false;
    }

    @Override
    public void putStack(ItemStack stack)
    {

    }

    @Override
    public ItemStack decrStackSize(int i)
    {
      return ItemStack.EMPTY;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player)
    {
      return false;
    }
  }
}