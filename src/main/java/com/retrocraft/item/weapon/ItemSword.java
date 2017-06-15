package com.retrocraft.item.weapon;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSword extends net.minecraft.item.ItemSword implements ItemModelProvider {

	private String name;

	public ItemSword(ToolMaterial material, String name) {
		super(material);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}
	
	@Override
	public void registerItemModel(Item item) {
		RetroCraft.proxy.registerItemRenderer(this, 0, name);
	}
	
	 @Override
	 public ItemSword setCreativeTab(CreativeTabs tab) {
	 super.setCreativeTab(tab);
	 return this;
	 }

}