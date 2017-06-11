package com.retrocraft.block.enchantorium;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotEnchant extends SlotItemHandler {
    final ContainerEnchantorium container;
    
    public SlotEnchant(
    		ContainerEnchantorium container, 
    		IItemHandler tableInventory, 
    		int slotIndex, 
    		int xDisplayPosition, 
    		int yDisplayPosition) {
        
        super(tableInventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.container = container;
    }
    
    @Override
    public int getSlotStackLimit () {
        return 1;
    }
    
    @Override
    public boolean isItemValid (ItemStack par1ItemStack) {
        
        return par1ItemStack.isItemEnchantable() || 
        		par1ItemStack.isItemEnchanted();
    }
}