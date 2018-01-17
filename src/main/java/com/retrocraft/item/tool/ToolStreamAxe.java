package com.retrocraft.item.tool;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolStreamAxe extends ItemAxe implements ItemModelProvider {

	private String name;
	private boolean isBasicMaterial;

	public ToolStreamAxe(ToolMaterial material, String name) {
		super(material);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
		this.isBasicMaterial = 
		    (material == ToolMaterial.WOOD
		    || material == ToolMaterial.STONE
		    || material == ToolMaterial.IRON
		    || material == ToolMaterial.GOLD
		    || name.contains("diamond"));
	}

	@Override
	public void registerItemModel(Item item) {
		RetroCraft.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public ToolStreamAxe setCreativeTab(CreativeTabs tab) {
	  if (!isBasicMaterial || RetroCraft.getConfig().supportBasicMaterials)
	    super.setCreativeTab(tab);

		return this;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos blockPos, EntityPlayer player) {
		World world = player.world;
		final IBlockState wood = world.getBlockState(blockPos);

		if (wood != null && wood.getMaterial() == Material.WOOD) {
		  int height = ToolFunctions.detectTree(world, blockPos, wood.getBlock()); 
			if (height > 0) {
				ToolFunctions.breakTree(world, blockPos, blockPos, wood.getBlock(), player, stack);
				stack.damageItem(height, player);
			} else {
				ToolFunctions.breakBlock(world, player, blockPos);
				stack.damageItem(1, player);
			}
			return true;
		}

		return super.onBlockStartBreak(stack, blockPos, player);
	}

	
}