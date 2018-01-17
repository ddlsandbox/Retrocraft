package com.retrocraft.block;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockBase extends Block implements ItemModelProvider{

	protected String name;
	
	public BlockBase(Material materialIn, String name) 
	{
	  this(materialIn, name, "pickaxe", 0, 1.5F, 10.0F, SoundType.STONE);
	}
	
	public BlockBase(Material materialIn, String name, String harvestTool, int harvestLevel, float hardness, float resistance, SoundType soundType) 
	{
	  super(materialIn);
	  this.name = name;
	 
	  setUnlocalizedName(name);
    setRegistryName(name);

	  this.setHarvestLevel(harvestTool, harvestLevel);
    this.setHardness(hardness);
    this.setResistance(resistance);
    this.setSoundType(soundType);
	}
	
	@Override
	public void registerItemModel(Item itemBlock) {
		RetroCraft.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	 @Override
	 public BlockBase setCreativeTab(CreativeTabs tab) {
		 super.setCreativeTab(tab);
		 return this;
	 }

	
}