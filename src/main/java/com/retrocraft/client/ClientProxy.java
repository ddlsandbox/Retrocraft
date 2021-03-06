package com.retrocraft.client;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.block.pedestal.TESRPedestal;
import com.retrocraft.block.pedestal.TileEntityPedestal;
import com.retrocraft.entity.teleportpipe.PlayerTeleportData;
import com.retrocraft.entity.teleportpipe.TileTeleportPipe;
import com.retrocraft.entity.teleportpipe.TeleportEntry;
import com.retrocraft.entity.waystone.gui.GuiWaystoneList;
import com.retrocraft.entity.waystone.render.RenderWaystone;
import com.retrocraft.machine.enchanter.TESREnchanter;
import com.retrocraft.machine.enchanter.TileEntityEnchanter;
import com.retrocraft.server.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{

  @SuppressWarnings("deprecation")
  @Override
  public void preInit(FMLPreInitializationEvent event)
  {
    super.preInit(event);

//    ClientRegistry.bindTileEntitySpecialRenderer(TileWaystone.class,
//        new RenderWaystone());
//    ForgeHooksClient.registerTESRItemStack(
//        Item.getItemFromBlock(ModBlocks.blockWaystone), 0, TileWaystone.class);
//    ModelLoader.setCustomModelResourceLocation(
//        Item.getItemFromBlock(ModBlocks.blockWaystone), 0,
//        new ModelResourceLocation("retrocraft:waystone", "inventory"));
  }

  @Override
  public void init(FMLInitializationEvent event){
      new ClientEvents();
      
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class,
          new TESRPedestal());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnchanter.class,
          new TESREnchanter());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event){
  }
  
  public void registerItemRenderer(Item item, int meta, String id)
  {
    ModelLoader.setCustomModelResourceLocation(item, meta,
        new ModelResourceLocation(RetroCraft.modId + ":" + id, "inventory"));
  }

  @Override
  public String localize(String unlocalized, Object... args)
  {
    // return I18n.format(unlocalized, args);
    return I18n.translateToLocalFormatted(unlocalized, args);
  }

  @Override
  public void loadModels()
  {
    // Stopwatch watch = Stopwatch.createStarted();
    // Logger.info("Binding tools to model renderer started");
    // for (int i = 0; i < MaterialLoader.Materials.length; i++) {
    // MinecraftForgeClient.registerItemRenderer(ToolLoader.hammers[i], new
    // HammerRenderer(Reference.Materials[i]));
    // log(ToolLoader.hammers[i]);
    // MinecraftForgeClient.registerItemRenderer(ToolLoader.excavators[i], new
    // ExcavatorRenderer(Reference.Materials[i]));
    // log(ToolLoader.excavators[i]);
    // MinecraftForgeClient.registerItemRenderer(ToolLoader.lumberAxes[i], new
    // LumberAxeRenderer(Reference.Materials[i]));
    // log(ToolLoader.lumberAxes[i]);
    // MinecraftForgeClient.registerItemRenderer(ToolLoader.sickles[i], new
    // SickleRenderer(Reference.Materials[i]));
    // log(ToolLoader.sickles[i]);
    // }
    // Logger.info("Binding all tools to model renderer finished after " +
    // watch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    // }
  }

  @Override
  public void playSound(SoundEvent sound, BlockPos pos, float pitch)
  {
    Minecraft.getMinecraft().getSoundHandler()
        .playSound(new PositionedSoundRecord(sound, SoundCategory.AMBIENT,
            RetroCraftConfig.soundVolume, pitch, pos));
  }

  @Override
  public void openWaystoneSelection(EnumHand hand,
      @Nullable TeleportEntry fromWaystone)
  {
    TeleportEntry[] waystones = PlayerTeleportData
        .fromPlayer(FMLClientHandler.instance().getClientPlayerEntity())
        .getWaystones();
    Minecraft.getMinecraft()
        .displayGuiScreen(new GuiWaystoneList(waystones, hand, fromWaystone));
  }
}
