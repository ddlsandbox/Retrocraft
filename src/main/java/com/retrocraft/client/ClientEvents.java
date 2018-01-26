package com.retrocraft.client;

import com.retrocraft.machine.GuiEnergyDisplay;
import com.retrocraft.machine.IEnergyDisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEvents
{
  private static GuiEnergyDisplay energyDisplay;

  public ClientEvents()
  {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onGameOverlay(RenderGameOverlayEvent.Post event)
  {
    if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && Minecraft.getMinecraft().currentScreen == null)
    {
      Minecraft minecraft = Minecraft.getMinecraft();
      EntityPlayer player = minecraft.player;
      RayTraceResult posHit = minecraft.objectMouseOver;

      if (posHit != null && posHit.getBlockPos() != null)
      {
        TileEntity tileHit = minecraft.world.getTileEntity(posHit.getBlockPos());

        if (tileHit instanceof IEnergyDisplay)
        {
          IEnergyDisplay display = (IEnergyDisplay) tileHit;
          if (!display.needsHoldShift() || player.isSneaking())
          {
            if (energyDisplay == null)
            {
              energyDisplay = new GuiEnergyDisplay(0, 0, null);
            }
            energyDisplay.setData(
                2, 
                event.getResolution().getScaledHeight() - 96, 
                display.getEnergyStorage(), 
                true,
                true);

            GlStateManager.pushMatrix();
            GlStateManager.color(1F, 1F, 1F, 1F);
            energyDisplay.draw();
            GlStateManager.popMatrix();
          }
        }
      }
    }
  }
}
