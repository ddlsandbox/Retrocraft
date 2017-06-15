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
    
    public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}
    
    public void registerRenderers() {
	}
    
    public void loadModels() {
    	
    }
}
