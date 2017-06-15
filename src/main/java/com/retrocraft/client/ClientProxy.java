package com.retrocraft.client;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.pedestal.TESRPedestal;
import com.retrocraft.block.pedestal.TileEntityPedestal;
import com.retrocraft.server.CommonProxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    
	public void registerItemRenderer(Item item, int meta, String id) {
	  ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RetroCraft.modId + ":" + id, "inventory"));
	}
    
    @Override
    public String localize(String unlocalized, Object... args) {
//		return I18n.format(unlocalized, args);
		return I18n.translateToLocalFormatted(unlocalized, args);
	}
    
    @Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new TESRPedestal());
	}
    
    @Override
    public void loadModels() {
//    	Stopwatch watch = Stopwatch.createStarted();
//        Logger.info("Binding tools to model renderer started");
//            for (int i = 0; i < MaterialLoader.Materials.length; i++) {
//                MinecraftForgeClient.registerItemRenderer(ToolLoader.hammers[i], new HammerRenderer(Reference.Materials[i]));
//                    log(ToolLoader.hammers[i]);
//                MinecraftForgeClient.registerItemRenderer(ToolLoader.excavators[i], new ExcavatorRenderer(Reference.Materials[i]));
//                    log(ToolLoader.excavators[i]);
//                MinecraftForgeClient.registerItemRenderer(ToolLoader.lumberAxes[i], new LumberAxeRenderer(Reference.Materials[i]));
//                    log(ToolLoader.lumberAxes[i]);
//                MinecraftForgeClient.registerItemRenderer(ToolLoader.sickles[i], new SickleRenderer(Reference.Materials[i]));
//                    log(ToolLoader.sickles[i]);
//            }
//    Logger.info("Binding all tools to model renderer finished after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms");
//    }
    }
}

