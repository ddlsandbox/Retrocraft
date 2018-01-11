package com.retrocraft.server;

import javax.annotation.Nullable;

import com.retrocraft.entity.teleportpipe.TeleportEntry;

import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy
{
  public void preInit(FMLPreInitializationEvent event){
  }

  public void init(FMLInitializationEvent event){
  }

  public void postInit(FMLPostInitializationEvent event){
  }

  public String localize(String unlocalized, Object... args)
  {
    return I18n.translateToLocalFormatted(unlocalized, args);
  }

  public void registerItemRenderer(Item item, int meta, String id)
  {
  }
  
  public void registerRenderers()
  {
  }

  public void loadModels()
  {

  }

  public void playSound(SoundEvent sound, BlockPos pos, float pitch)
  {

  }

  public void openWaystoneSelection(EnumHand hand,
      @Nullable TeleportEntry fromWaystone)
  {

  }
}
