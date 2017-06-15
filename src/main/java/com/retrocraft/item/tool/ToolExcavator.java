package com.retrocraft.item.tool;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ToolExcavator extends ToolBase implements ItemModelProvider {

	private String name;

	private static Material[] materials = { Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW,
			Material.CRAFTED_SNOW, Material.CLAY };
	
	public ToolExcavator(ToolMaterial material, String name) {
		super(material, materials);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}

	@Override
	public void registerItemModel(Item item) {
		RetroCraft.proxy.registerItemRenderer(this, 0, name);
	}
	
	@Override
	public ToolExcavator setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);

		return this;
	}
}