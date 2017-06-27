package com.retrocraft.machine.generator;

import java.awt.Color;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.machine.GuiEnergyDisplay;
import com.retrocraft.machine.GuiFluidDisplay;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSteamGenerator extends GuiContainer
{
  private ContainerSteamGenerator containerSteamGenerator;
  private TileSteamGenerator tileGenerator;
  private GuiEnergyDisplay energy;
  private GuiFluidDisplay fluid;
  

  private static final ResourceLocation BG_TEXTURE        = new ResourceLocation(
      RetroCraft.modId, "textures/gui/steamgenerator.png");
  private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation(
      RetroCraft.modId, "textures/gui/inventory.png");

  public GuiSteamGenerator(InventoryPlayer invPlayer, TileSteamGenerator tileGenerator,
                           ContainerSteamGenerator containerSteamGenerator)
  {
    super(containerSteamGenerator);

    // Set the width and height of the gui
    this.xSize = 176;
    this.ySize = 93 + 86;

    this.containerSteamGenerator = containerSteamGenerator;
    this.tileGenerator = tileGenerator;
  }

  @Override
  public void initGui()
  {
    super.initGui();
    this.energy = new GuiEnergyDisplay(this.guiLeft + 42, this.guiTop + 5,
        this.tileGenerator.storage);
    this.fluid = new GuiFluidDisplay(this.guiLeft + 106, this.guiTop + 5,
        this.tileGenerator.tank, false, false);
  }

  @Override
  public void drawScreen(int x, int y, float f){
      super.drawScreen(x, y, f);
      this.energy.drawOverlay(x, y);
      this.fluid.drawOverlay(x, y);
  }
  
  @Override
  public void drawGuiContainerForegroundLayer(int x, int y)
  {
    final String name = RetroCraft.proxy
        .localize(ModBlocks.blockGenerator.getUnlocalizedName() + ".name");
    final int LABEL_XPOS = (xSize) / 2
        - fontRendererObj.getStringWidth(name) / 2;
    final int LABEL_YPOS = 5;
    fontRendererObj.drawString(name, LABEL_XPOS, LABEL_YPOS,
        Color.darkGray.getRGB());
  }

  @Override
  public void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop + 93, 0, 0, 176, 86);

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

    if (this.tileGenerator.burnTimeRemaining > 0)
    {
      int i = this.tileGenerator.getBurningScaled(13);
      this.drawTexturedModalRect(this.guiLeft + 87, this.guiTop + 27 + 12 - i,
          176, 96 - i, 14, i);
    }

    this.energy.draw();
    this.fluid.draw();
  }

}
