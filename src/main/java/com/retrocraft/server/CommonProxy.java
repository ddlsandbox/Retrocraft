package com.retrocraft.server;

import javax.annotation.Nullable;

import com.retrocraft.entity.waystone.WaystoneEntry;

import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;

public class CommonProxy
{
  public void registerItemRenderer(Item item, int meta, String id)
  {

  }

  public String localize(String unlocalized, Object... args)
  {
    return I18n.translateToLocalFormatted(unlocalized, args);
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
      @Nullable WaystoneEntry fromWaystone)
  {

  }
}
