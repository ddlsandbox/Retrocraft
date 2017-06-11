package com.retrocraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOre extends ItemBase implements ItemOreDict {

	private String oreName;

	public ItemOre(String name, String oreName) {
		super(name);

		this.oreName = oreName;
	}

	@Override
	public void initOreDict() {
		OreDictionary.registerOre(oreName, this);
	}

	 @Override
	 public ItemOre setCreativeTab(CreativeTabs tab) {
	 super.setCreativeTab(tab);
	 return this;
	 }

}