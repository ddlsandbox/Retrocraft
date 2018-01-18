package com.retrocraft.client;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.block.pedestal.TESRPedestal;
import com.retrocraft.block.pedestal.TileEntityPedestal;
import com.retrocraft.entity.teleportpipe.gui.GuiTeleportPipeList;
import com.retrocraft.item.ModItems;
import com.retrocraft.machine.enchanter.TESREnchanter;
import com.retrocraft.machine.enchanter.TileEntityEnchanter;
import com.retrocraft.machine.teleportpipe.PlayerTeleportData;
import com.retrocraft.machine.teleportpipe.TeleportEntry;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{

  @Override
  public void preInit(FMLPreInitializationEvent event)
  {
    super.preInit(event);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void init(FMLInitializationEvent event)
  {
      new ClientEvents();
      
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class,
          new TESRPedestal());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnchanter.class,
          new TESREnchanter());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event)
  {
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
    ModBlocks.initModels();
    ModItems.initModels();
  }

  @Override
  public void playSound(SoundEvent sound, BlockPos pos, float pitch)
  {
    Minecraft.getMinecraft().getSoundHandler()
        .playSound(new PositionedSoundRecord(sound, SoundCategory.AMBIENT,
            RetroCraftConfig.soundVolume, pitch, pos));
  }

  @Override
  public void openTeleportPipeSelection(EnumHand hand,
      @Nullable TeleportEntry fromTeleportPipe)
  {
    TeleportEntry[] teleportPipes = PlayerTeleportData
        .fromPlayer(FMLClientHandler.instance().getClientPlayerEntity())
        .getTeleportPipes();
    Minecraft.getMinecraft()
        .displayGuiScreen(new GuiTeleportPipeList(teleportPipes, hand, fromTeleportPipe));
  }
}
