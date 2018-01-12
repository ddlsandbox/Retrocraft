package com.retrocraft.block;

import com.retrocraft.entity.teleportpipe.BlockTeleportPipe;
import com.retrocraft.machine.enchanter.BlockEnchanter;
import com.retrocraft.machine.generator.BlockSteamGenerator;
import com.retrocraft.machine.grinder.BlockOreGrinder;
import com.retrocraft.machine.repairer.BlockRepairer;
import com.retrocraft.machine.smelter.BlockSmelter;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	@GameRegistry.ObjectHolder("retrocraft:ore_manolite")
	public static BlockOre oreManolite;
	@GameRegistry.ObjectHolder("retrocraft:ore_octirion")
	public static BlockOre oreOctirion;
	
	@GameRegistry.ObjectHolder("retrocraft:block_manolium")
	public static BlockBase blockManolium;
	@GameRegistry.ObjectHolder("retrocraft:block_manolazium")
	public static BlockBase blockManolazium;
	@GameRegistry.ObjectHolder("retrocraft:block_octirion")
	public static BlockBase blockOctirion;
	
//	public static BlockPedestal pedestalManolium;
//
	@GameRegistry.ObjectHolder("retrocraft:block_machinechasis")
	public static BlockBase blockMachineChasis;
//	
//	public static BlockMultifurnace blockMultifurnace;
	
	@GameRegistry.ObjectHolder("retrocraft:block_enchanter")
	public static BlockEnchanter blockEnchanter;
	
	@GameRegistry.ObjectHolder("retrocraft:block_repairer")
	public static BlockRepairer blockRepairer;
	
	@GameRegistry.ObjectHolder("retrocraft:block_generator")
	public static BlockSteamGenerator blockGenerator;

	@GameRegistry.ObjectHolder("retrocraft:block_oregrinder")
	public static BlockOreGrinder blockOreGrinder;
	
	@GameRegistry.ObjectHolder("retrocraft:block_oresmelter")
	public static BlockSmelter blockOreSmelter;
	
//	public static BlockElectricForge blockElectricForge;
//	public static BlockAdvancedForge blockAdvancedForge;

	@GameRegistry.ObjectHolder("retrocraft:block_waystone")
	public static BlockTeleportPipe blockWaystone;
	
	@GameRegistry.ObjectHolder("retrocraft:lightpillar")
	public static BlockTorch blockLightPillar;
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
        blockWaystone.initModel();
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockOreGrinder), 0, new ModelResourceLocation(blockOreGrinder.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockOreSmelter), 0, new ModelResourceLocation(blockOreSmelter.getRegistryName(), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockGenerator), 0, new ModelResourceLocation(blockGenerator.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockEnchanter), 0, new ModelResourceLocation(blockEnchanter.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockRepairer), 0, new ModelResourceLocation(blockRepairer.getRegistryName(), "inventory"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(oreManolite), 0, new ModelResourceLocation(oreManolite.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(oreOctirion), 0, new ModelResourceLocation(oreOctirion.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockManolium), 0, new ModelResourceLocation(blockManolium.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockManolazium), 0, new ModelResourceLocation(blockManolazium.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockOctirion), 0, new ModelResourceLocation(blockOctirion.getRegistryName(), "inventory"));
        
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockMachineChasis), 0, new ModelResourceLocation(blockMachineChasis.getRegistryName(), "inventory"));
        
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLightPillar), 0, new ModelResourceLocation(blockLightPillar.getRegistryName(), "inventory"));
    }
	
	public static void init() {

//		pedestalManolium = register(new BlockPedestal("block_pedestal").setCreativeTab(RetroCraft.creativeTab));
//
//		blockMultifurnace = register(new BlockMultifurnace("multifurnace").setCreativeTab(RetroCraft.creativeTab));
//		//		
//
//		blockLightPillar = register(new BlockTorch("lightpillar").setCreativeTab(RetroCraft.creativeTab));
//		
//		blockWaystone = register(new BlockTeleportPipe("block_waystone").setCreativeTab(RetroCraft.creativeTab));
	}
}
