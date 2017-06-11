package com.retrocraft.server;

import com.retrocraft.block.ModBlocks;

import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

public class CommonProxy {
    
    private final int ENTITY_TRACKING_RANGE = 80;
    private final int ENTITY_UPDATE_FREQ = 3;
    private final int ENTITY_ID = 0;
    private final boolean ENTITY_SEND_VELO_UPDATES = true;
    
    public void registerItemRenderer(Item item, int meta, String id) {

  	}
    
    public void onPreInit(FMLPreInitializationEvent event) {
//        GameRegistry.register(BlockDragonBreedEgg.INSTANCE.setRegistryName("dragon_egg"));
//        GameRegistry.register(ItemDragonBreedEgg.INSTANCE.setRegistryName("dragon_egg"));
    	System.out.println("MOD is loading!");
//   	 ModItems.init();
   	 ModBlocks.init();
   	 
//       config = new DragonMountsConfig(new Configuration(evt.getSuggestedConfigurationFile()));
    }
    
    public void onInit(FMLInitializationEvent evt) {
        registerEntities();

//        MinecraftForge.EVENT_BUS.register(new DragonEggBlockHandler());
    }

    public void onPostInit(FMLPostInitializationEvent event) {
    }
    
    public void onServerStarting(FMLServerStartingEvent evt) {
//        MinecraftServer server = evt.getServer();
//        ServerCommandManager cmdman = (ServerCommandManager) server.getCommandManager(); 
//        cmdman.registerCommand(new CommandDragon());
    }
    
    public void onServerStopped(FMLServerStoppedEvent evt) {
    }
    
    private void registerEntities() {
//        ResourceLocation res = new ResourceLocation(RetroCraft.AID, "dragon");
//        EntityRegistry.registerModEntity(res, EntityTameableDragon.class, "DragonMount",
//                ENTITY_ID, RetroCraft.instance, ENTITY_TRACKING_RANGE, ENTITY_UPDATE_FREQ,
//                ENTITY_SEND_VELO_UPDATES);
    }
    
    public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}
    
    public void registerRenderers() {
	}
}