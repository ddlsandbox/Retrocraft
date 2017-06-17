package com.retrocraft.block;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.pedestal.BlockPedestal;
import com.retrocraft.entity.waystone.BlockWaystone;
import com.retrocraft.entity.waystone.TileWaystone;
import com.retrocraft.item.ItemModelProvider;
import com.retrocraft.item.ItemOreDict;
import com.retrocraft.machine.enchanter.BlockEnchanter;
import com.retrocraft.machine.multifurnace.BlockMultifurnace;
import com.retrocraft.machine.repairer.BlockRepairer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockOre oreCopper;
	public static BlockOre oreManolite;
	public static BlockPedestal pedestalManolium;

	public static BlockMultifurnace blockMultifurnace;
	public static BlockEnchanter blockEnchanter;
	public static BlockRepairer blockRepairer;

	public static BlockWaystone blockWaystone;
	
	public static void init() {
		oreCopper = register(new BlockOre("ore_copper", "oreCopper").setCreativeTab(RetroCraft.creativeTab));
		oreManolite = register(new BlockOre("ore_manolite", "oreManolite").setCreativeTab(RetroCraft.creativeTab));
		pedestalManolium = register(new BlockPedestal("pedestal_manolium").setCreativeTab(RetroCraft.creativeTab));

		blockEnchanter = register(new BlockEnchanter("block_enchanter").setCreativeTab(RetroCraft.creativeTab));
		blockMultifurnace = register(new BlockMultifurnace("multifurnace").setCreativeTab(RetroCraft.creativeTab));
		blockRepairer = register(new BlockRepairer("block_repairer").setCreativeTab(RetroCraft.creativeTab));
		
		blockWaystone = new BlockWaystone();
    GameRegistry.register(blockWaystone);
    GameRegistry.register(new ItemBlock(blockWaystone).setRegistryName(blockWaystone.getRegistryName()));
    GameRegistry.registerTileEntity(TileWaystone.class, "block_waystone");
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
