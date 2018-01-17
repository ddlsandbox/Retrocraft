package com.retrocraft.item.tool;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ToolHammer extends ToolBase implements ItemModelProvider {

	private static Material[] materials = { Material.ANVIL, Material.GLASS, Material.ICE, Material.IRON,
			Material.PACKED_ICE, Material.PISTON, Material.ROCK };
	
	public ToolHammer(ToolMaterial material, String name) {
		super(name, material, materials);
	}

	@Override
	public void registerItemModel(Item item) {
		RetroCraft.proxy.registerItemRenderer(this, 0, name);
	}
	  
	@Override
	public ToolHammer setCreativeTab(CreativeTabs tab) {
	  if (!isBasicMaterial || RetroCraft.getConfig().supportBasicMaterials)
	    super.setCreativeTab(tab);

		return this;
	}
}