package com.retrocraft.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	 
	 @SideOnly(Side.CLIENT)
	 public void initModel() {
	     ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	 }

}