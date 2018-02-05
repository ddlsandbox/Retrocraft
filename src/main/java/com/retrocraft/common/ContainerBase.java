package com.retrocraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container
{
  public static final int  EQUIPMENT_SLOT_NUMBERS[] =
  { 39, 38, 37, 36, 40 };

  public static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]
  { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
    EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET,
    EntityEquipmentSlot.OFFHAND };

  protected int guiSlotSpacingX  = 18;
  protected int guiSlotSpacingY  = 18;
  protected int guiHotbarPosX    = 37;
  protected int guiHotbarPosY    = 107;
  protected int guiInventoryPosX = 37;
  protected int guiInventoryPosY = 47;
  protected int guiEquipmentPosX = 7;
  protected int guiEquipmentPosY = 24;
  
  protected int hotbarSlotCount      = 9;
  protected int inventoryRowCount    = 3;
  protected int inventoryColumnCount = 9;
  protected int equipmentSlotCount   = 5;

  protected int inventorySlotCount;
  protected int vanillaSlotCount;
  protected int vanillaFirstSlotIndex = 0;
  protected int equipmentFirstSlotIndex = 0;

  protected int customFirstSlotIndex;

  protected boolean hasInventory = false;
  protected boolean hasEquipment = false;

  public ContainerBase(boolean hasInventory,
                       boolean hasEquipment)
  {
    if (!hasInventory)
    {
      hotbarSlotCount = 0;
      inventoryColumnCount = inventoryRowCount = 0;
    }
    if (!hasEquipment)
      equipmentSlotCount = 0;

    inventorySlotCount = inventoryColumnCount * inventoryRowCount;
    vanillaSlotCount = inventorySlotCount + hotbarSlotCount
        + equipmentSlotCount;

    equipmentFirstSlotIndex = vanillaFirstSlotIndex + inventorySlotCount + hotbarSlotCount;
    customFirstSlotIndex = vanillaFirstSlotIndex + vanillaSlotCount;
  }
  
  protected void addEquipmentSlots(InventoryPlayer invPlayer)
  {
    for (int k = 0; k < equipmentSlotCount; k++)
    {
      final int armorType = k;
      final int slotNumber = EQUIPMENT_SLOT_NUMBERS[k];
      addSlotToContainer(new Slot(invPlayer, slotNumber, guiEquipmentPosX,
          guiEquipmentPosY + k * guiSlotSpacingY)
      {
        @Override
        public int getSlotStackLimit()
        {

          return 1;
        }

        @Override
        public boolean isItemValid(ItemStack par1ItemStack)
        {
          EntityEquipmentSlot entityEquipmentSlot = VALID_EQUIPMENT_SLOTS[armorType];
          Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
          return item != null && item.isValidArmor(par1ItemStack,
              entityEquipmentSlot, invPlayer.player);
        }
      });
    }
  }
  
  protected void addHotbarSlots(InventoryPlayer invPlayer)
  {
    for (int x = 0; x < hotbarSlotCount; x++)
    {
      int slotNumber = x;
      addSlotToContainer(new Slot(invPlayer, slotNumber,
          guiHotbarPosX + guiSlotSpacingX * x, guiHotbarPosY));
    }
  }
  
  protected void addInventorySlots(InventoryPlayer invPlayer)
  {
    for (int y = 0; y < inventoryRowCount; y++)
    {
      for (int x = 0; x < inventoryColumnCount; x++)
      {
        int slotNumber = hotbarSlotCount + y * inventoryColumnCount + x;
        int xpos = guiInventoryPosX + x * guiSlotSpacingX;
        int ypos = guiInventoryPosY + y * guiSlotSpacingY;
        addSlotToContainer(new Slot(invPlayer, slotNumber, xpos, ypos));
      }
    }
  }
  
  protected void addVanillaSlots(InventoryPlayer invPlayer)
  {
    addHotbarSlots(invPlayer);
    
    addInventorySlots(invPlayer);
    
    addEquipmentSlots(invPlayer);
  }

  protected boolean isVanillaSlot(int slotIndex)
  {
    return slotIndex >= vanillaFirstSlotIndex &&
           slotIndex < vanillaFirstSlotIndex + vanillaSlotCount;
  }
  
  @Override
  public boolean canInteractWith(EntityPlayer arg0)
  {
    return true;
  }
  
}
