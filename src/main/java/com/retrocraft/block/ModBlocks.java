package com.retrocraft.block;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;
import com.retrocraft.item.ItemOreDict;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockOre oreCopper;
	public static BlockOre oreManolite;
	public static BlockPedestal pedestalManolium;
	
	public static BlockEnchantorium blockEnchantorium;

	//public static BlockCounter counter;
	
	public static void init() {
		oreCopper = register(new BlockOre("ore_copper", "oreCopper").setCreativeTab(RetroCraft.creativeTab));
		oreManolite = register(new BlockOre("ore_manolite", "oreManolite").setCreativeTab(RetroCraft.creativeTab));
		pedestalManolium = register(new BlockPedestal("pedestal_manolium").setCreativeTab(RetroCraft.creativeTab));
		
		blockEnchantorium = register(new BlockEnchantorium("block_enchantorium").setCreativeTab(RetroCraft.creativeTab));
		//counter = register(new BlockCounter());
	}

	private static <T extends Block> T register(T block, ItemBlock itemBlock) {
		GameRegistry.register(block);
		if (itemBlock != null) {
			GameRegistry.register(itemBlock);
		}

		if (block instanceof ItemModelProvider) {
			((ItemModelProvider) block).registerItemModel(itemBlock);
		}

		if (block instanceof ItemOreDict) {
			((ItemOreDict)block).initOreDict();
		}
		
		if (itemBlock instanceof ItemOreDict) {
			((ItemOreDict)itemBlock).initOreDict();
		}
		
		if (block instanceof BlockTileEntity) {
			GameRegistry.registerTileEntity(((BlockTileEntity<?>)block).getTileEntityClass(), 
					block.getRegistryName().toString());
		}
		
		return block;
	}

	private static <T extends Block> T register(T block) {
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		return register(block, itemBlock);
	}
}