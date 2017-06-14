/*
 ** 2012 August 13
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.retrocraft;

import com.retrocraft.block.ModBlocks;
import com.retrocraft.block.repairer.PacketRepairer;
import com.retrocraft.entity.ModEntities;
import com.retrocraft.item.ModItems;
import com.retrocraft.network.PacketRequestUpdateEnchantorium;
import com.retrocraft.network.PacketRequestUpdatePedestal;
import com.retrocraft.network.PacketUpdateEnchantorium;
import com.retrocraft.network.PacketUpdatePedestal;
import com.retrocraft.recipe.RetrocraftRecipes;
import com.retrocraft.server.CommonProxy;
import com.retrocraft.tab.RetroCraftCreativeTab;
import com.retrocraft.world.ModWorldGen;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = RetroCraft.modId, name = RetroCraft.name, version = RetroCraft.version, acceptedMinecraftVersions = "[1.11.2]")
public class RetroCraft {

	public static final Item.ToolMaterial manoliumToolMaterial = 
			EnumHelper.addToolMaterial("MANOLIUM", 
					3,	  /* harvest level diamond=3*/ 
					500,  /* durability */
					8f,   /* efficiency */
					4f,   /* damage */
					20);  /* enchantability */
	public static final Item.ToolMaterial manolaziumToolMaterial = 
			EnumHelper.addToolMaterial("MANOLIUM", 
					5,	  /* harvest level diamond=3*/ 
					1500, /* durability */
					10f,  /* efficiency */
					8f,   /* damage */
					30);  /* enchantability */
	
	@SidedProxy(serverSide = "com.retrocraft.server.CommonProxy", clientSide = "com.retrocraft.client.ClientProxy")
	public static CommonProxy proxy;
	public static final RetroCraftCreativeTab creativeTab = new RetroCraftCreativeTab();
	
	public static final String modId = "retrocraft";
	public static final String name = "RetroCraft Mod";
	public static final String version = "0.1.0";

	public static SimpleNetworkWrapper network;
	
	@Mod.Instance(modId)
	public static RetroCraft instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println(name + " is loading!");
		ModBlocks.init();
		ModItems.init();
		ModEntities.init();
		
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(modId);
		network.registerMessage(new PacketUpdatePedestal.Handler(), PacketUpdatePedestal.class, 0, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdatePedestal.Handler(), PacketRequestUpdatePedestal.class, 1, Side.SERVER);
		network.registerMessage(new PacketUpdateEnchantorium.Handler(), PacketUpdateEnchantorium.class, 3, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateEnchantorium.Handler(), PacketRequestUpdateEnchantorium.class, 4, Side.SERVER);
		network.registerMessage(new PacketRepairer.Handler(), PacketRepairer.class, 5, Side.SERVER);
		
		proxy.registerRenderers();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		RetrocraftRecipes.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}