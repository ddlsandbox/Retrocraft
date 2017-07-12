package com.retrocraft.block;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.pedestal.BlockPedestal;
import com.retrocraft.entity.teleportpipe.BlockTeleportPipe;
import com.retrocraft.item.ItemModelProvider;
import com.retrocraft.item.ItemOreDict;
import com.retrocraft.machine.crafter.BlockAdvancedForge;
import com.retrocraft.machine.crafter.BlockElectricForge;
import com.retrocraft.machine.enchanter.BlockEnchanter;
import com.retrocraft.machine.generator.BlockSteamGenerator;
import com.retrocraft.machine.grinder.BlockOreGrinder;
import com.retrocraft.machine.multifurnace.BlockMultifurnace;
import com.retrocraft.machine.repairer.BlockRepairer;
import com.retrocraft.machine.smelter.BlockSmelter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockOre oreManolite;
	public static BlockOre oreOctirion;
	
	public static BlockBase blockManolium;
	public static BlockBase blockManolazium;
	public static BlockBase blockOctirion;
	
	public static BlockPedestal pedestalManolium;

	public static BlockBase blockMachineChasis;
	
	public static BlockMultifurnace blockMultifurnace;
	public static BlockEnchanter blockEnchanter;
	public static BlockRepairer blockRepairer;
	public static BlockSteamGenerator blockGenerator;
	public static BlockOreGrinder blockOreGrinder;
	public static BlockSmelter blockOreSmelter;
	public static BlockElectricForge blockElectricForge;
	public static BlockAdvancedForge blockAdvancedForge;

	public static BlockTeleportPipe blockWaystone;
	
	public static BlockTorch blockLightPillar;
	
	public static void init() {

	  oreOctirion = new BlockOre("ore_octirion", "oreOctirion").setCreativeTab(RetroCraft.creativeTab);
	  oreOctirion.setLightLevel(0.9f);
	  register(oreOctirion);
	  
		oreManolite = register(new BlockOre("ore_manolite", "oreManolite").setCreativeTab(RetroCraft.creativeTab));
		
		blockManolium = register(new BlockBase(Material.IRON, "block_manolium").setCreativeTab(RetroCraft.creativeTab));
		blockManolazium = register(new BlockBase(Material.IRON, "block_manolazium").setCreativeTab(RetroCraft.creativeTab));
		blockOctirion = register(new BlockBase(Material.IRON, "block_octirion").setCreativeTab(RetroCraft.creativeTab));
		
		pedestalManolium = register(new BlockPedestal("block_pedestal").setCreativeTab(RetroCraft.creativeTab));

		blockMachineChasis = register(new BlockBase(Material.IRON, "block_machinechasis").setCreativeTab(RetroCraft.creativeTab));
		
		blockEnchanter = register(new BlockEnchanter("block_enchanter").setCreativeTab(RetroCraft.creativeTab));
		blockMultifurnace = register(new BlockMultifurnace("multifurnace").setCreativeTab(RetroCraft.creativeTab));
		blockRepairer = register(new BlockRepairer("block_repairer").setCreativeTab(RetroCraft.creativeTab));
		blockGenerator = register(new BlockSteamGenerator("block_steamgenerator").setCreativeTab(RetroCraft.creativeTab));
		blockOreGrinder = register(new BlockOreGrinder("block_oregrinder").setCreativeTab(RetroCraft.creativeTab));
		blockOreSmelter = register(new BlockSmelter("block_oresmelter").setCreativeTab(RetroCraft.creativeTab));

		blockLightPillar = register(new BlockTorch("lightpillar").setCreativeTab(RetroCraft.creativeTab));
		
//		blockElectricForge = register(new BlockElectricForge("block_electricforge").setCreativeTab(RetroCraft.creativeTab));
//		blockAdvancedForge = register(new BlockAdvancedForge("block_advancedforge").setCreativeTab(RetroCraft.creativeTab));
		
		blockWaystone = register(new BlockTeleportPipe("block_waystone").setCreativeTab(RetroCraft.creativeTab));
//		new BlockWaystone();
//    GameRegistry.register(blockWaystone);
//    GameRegistry.register(new ItemBlock(blockWaystone).setRegistryName(blockWaystone.getRegistryName()));
//    GameRegistry.registerTileEntity(TileWaystone.class, "block_waystone");
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
