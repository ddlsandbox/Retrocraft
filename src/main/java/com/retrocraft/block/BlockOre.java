package com.retrocraft.block;

import com.retrocraft.item.ItemOreDict;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockBase implements ItemOreDict {

	private String oreName;
	
	public BlockOre(String name, String oreName, int harvestLevel) {
	  this(name, oreName, harvestLevel, 3.0F, 5.0F);
	}
	
	public BlockOre(String name, String oreName, int harvestLevel, float hardness, float resistance)
	{
	  super(Material.ROCK, name, "pickaxe", harvestLevel, hardness, resistance, SoundType.STONE);
	  
	  this.oreName = oreName;
	}

	@Override
	public BlockOre setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public void initOreDict() {
		OreDictionary.registerOre(oreName, this);
	}

}