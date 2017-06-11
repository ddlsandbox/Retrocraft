package com.retrocraft.client;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.TESRPedestal;
import com.retrocraft.block.TileEntityPedestal;
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
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        
        // register dragon entity renderer
        //RenderingRegistry.registerEntityRenderingHandler(
        //        EntityTameableDragon.class, DragonRenderer::new);
        
        // register item renderer for dragon egg block variants
        //ResourceLocation eggModelItemLoc = new ResourceLocation(DragonMounts.AID, "dragon_egg");
        //Item itemBlockDragonEgg = Item.REGISTRY.getObject(eggModelItemLoc);
        //EnumDragonBreed.META_MAPPING.forEach((breed, meta) -> {
        //    ModelResourceLocation eggModelLoc = new ModelResourceLocation(DragonMounts.AID + ":dragon_egg", "breed=" + breed.getName());
        //    ModelLoader.setCustomModelResourceLocation(itemBlockDragonEgg, meta, eggModelLoc);
        //});
    }

    @Override
    public void onInit(FMLInitializationEvent evt) {
        super.onInit(evt);
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);
        
//        if (DragonMounts.instance.getConfig().isDebug()) {
//            MinecraftForge.EVENT_BUS.register(new GuiDragonDebug());
//        }
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
}

