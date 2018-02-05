package com.retrocraft.item.backpack;

import com.retrocraft.common.ContainerBase;
import com.retrocraft.util.ItemStackHandlerCustom;
import com.retrocraft.util.StackUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBackpack extends ContainerBase
{

  private final ItemStackHandlerCustom bagInventory;
  private final InventoryPlayer inventory;
  private final ItemStack sack;

  private static final int BACKPACK_INV_HEIGHT = 126;
  private static final int BACKPACK_ROWS = 6;
  private static final int BACKPACK_COLS = 9;
  private static final int BACKPACK_SIZE = BACKPACK_ROWS * BACKPACK_COLS;
  
  public ContainerBackpack(ItemStack sack, InventoryPlayer inventory)
  {
    super(true, false);
    
    this.inventory = inventory;
    
    guiHotbarPosX = 8;
    guiHotbarPosY = BACKPACK_INV_HEIGHT+62;
    guiInventoryPosX = 8;
    guiInventoryPosY = BACKPACK_INV_HEIGHT+4;
    
    this.bagInventory = new ItemStackHandlerCustom(BACKPACK_SIZE)
    {
      @Override
      public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
      {
        return super.insertItem(slot, stack, simulate);
      };
    };
    this.sack = sack;
    
    addVanillaSlots(inventory);

    for (int i = 0; i < BACKPACK_ROWS; i++)
    {
      for (int j = 0; j < BACKPACK_COLS; j++)
      {
        this.addSlotToContainer(
            new AllSlot(
                this.bagInventory, 
                j + i * BACKPACK_COLS, 
                10 + j * 18, 
                10 + i * 18)
        {
          @Override
          public boolean isItemValid(ItemStack stack)
          {
            return true;
          }
        });
      }
    }
    
    ItemStack stack = inventory.getCurrentItem();
    if (StackUtil.isValid(stack) && stack.getItem() instanceof ItemBackpack)
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
  protected void addHotbarSlots(InventoryPlayer invPlayer)
  {
    for (int x = 0; x < hotbarSlotCount; x++)
    {
      int slotNumber = x;
      if (x == invPlayer.currentItem)
      {
        this.addSlotToContainer(
            new SlotImmovable(invPlayer, slotNumber,
                guiHotbarPosX + guiSlotSpacingX * x, guiHotbarPosY));
      } else
      {
        this.addSlotToContainer(new Slot(invPlayer, slotNumber,
            guiHotbarPosX + guiSlotSpacingX * x, guiHotbarPosY));
      }
    }
  }
  
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot)
  {
    Slot theSlot = inventorySlots.get(slot);
    
    int inventoryStart = this.bagInventory.getSlots();
    int inventoryEnd = inventoryStart + 26;
    int hotbarStart = inventoryEnd + 1;
    int hotbarEnd = hotbarStart + 8;

    if (theSlot != null && theSlot.getHasStack())
    {
      ItemStack sourceStack = theSlot.getStack();
      ItemStack copyOfSourceStack = sourceStack.copy();

      if (isVanillaSlot(slot))
      {
        /* try putting in backpack */
        if (!this.mergeItemStack(sourceStack, customFirstSlotIndex, 
            customFirstSlotIndex + BACKPACK_SIZE, false))
        {
          return StackUtil.getNull();
        }
      }
      else
      {
        if (!this.mergeItemStack(sourceStack, 
            vanillaFirstSlotIndex, vanillaFirstSlotIndex + vanillaSlotCount, false))
      {
        return StackUtil.getNull();
      }
      }

      if (!StackUtil.isValid(sourceStack))
      {
        theSlot.putStack(StackUtil.getNull());
      } else
      {
        theSlot.onSlotChanged();
      }

      if (StackUtil.getStackSize(sourceStack) == StackUtil.getStackSize(copyOfSourceStack))
      {
        return StackUtil.getNull();
      }
      theSlot.onTake(player, sourceStack);

      return copyOfSourceStack;
    }
    return StackUtil.getNull();
  }
  
  public ItemStack atransferStackInSlot(EntityPlayer player, int sourceSlotIndex)
  {
    ItemStack copyOfSourceStack = StackUtil.getNull();
    Slot slot = inventorySlots.get(sourceSlotIndex);

    if (slot != null && slot.getHasStack())
    {
      ItemStack sourceStack = slot.getStack();
      copyOfSourceStack = sourceStack.copy();

      if (isVanillaSlot(sourceSlotIndex))
      {
        if (!this.mergeItemStack(sourceStack, customFirstSlotIndex,
            customFirstSlotIndex + 1, false))
        {
          return StackUtil.getNull();
        }
      } 
      else
      {
        /* test equipment slots first */
        if (!this.mergeItemStack(sourceStack, equipmentFirstSlotIndex, equipmentFirstSlotIndex + equipmentSlotCount, true) &&
            !this.mergeItemStack(sourceStack, vanillaFirstSlotIndex, vanillaFirstSlotIndex + vanillaSlotCount, false))
        {
          return StackUtil.getNull();
        }
      }

      if (sourceStack.getCount() == 0)
      {
        slot.putStack(StackUtil.getNull());
      }

      if (sourceStack.getCount() == copyOfSourceStack.getCount())
      {
        return StackUtil.getNull();
      }

      slot.onTake(player, sourceStack);
    }

    return copyOfSourceStack;
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
    if (StackUtil.isValid(stack) && stack.getItem() instanceof ItemBackpack)
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