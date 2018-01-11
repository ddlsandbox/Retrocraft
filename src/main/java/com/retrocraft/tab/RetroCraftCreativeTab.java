package com.retrocraft.tab;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RetroCraftCreativeTab extends CreativeTabs {

	public RetroCraftCreativeTab() {
			super(RetroCraft.modId);
		}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.magicalCore);
	}

}
