package com.retrocraft.item.tool;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ToolEverything extends ToolBase implements ItemModelProvider {

	private String name;

	private static Material[] materials = { 
			Material.ANVIL, Material.GLASS, Material.ICE, Material.IRON,
			Material.PACKED_ICE, Material.PISTON, Material.ROCK,
			Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW,
			Material.CRAFTED_SNOW, Material.CLAY, 
			Material.WOOD };
	
	public ToolEverything(ToolMaterial material, String name) {
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
	public ToolEverything setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);

		return this;
	}
}