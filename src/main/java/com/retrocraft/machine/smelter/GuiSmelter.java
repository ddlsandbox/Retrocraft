package com.retrocraft.machine.smelter;

import java.awt.Color;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.machine.GuiEnergyDisplay;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSmelter extends GuiContainer
{
  private ContainerSmelter containerSmelter;
  private TileSmelter      tileSmelter;
  private GuiEnergyDisplay energy;

  private static final ResourceLocation BG_TEXTURE        = new ResourceLocation(
      RetroCraft.modId, "textures/gui/oresmelter.png");
  private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation(
      RetroCraft.modId, "textures/gui/inventory.png");

  public GuiSmelter(InventoryPlayer invPlayer, TileSmelter tileSmelter,
                    ContainerSmelter containerSmelter)
  {
    super(containerSmelter);

    // Set the width and height of the gui
    this.xSize = 176;
    this.ySize = 93 + 86;

    this.containerSmelter = containerSmelter;
    this.tileSmelter = tileSmelter;
  }

  @Override
  public void initGui()
  {
    super.initGui();
    this.energy = new GuiEnergyDisplay(this.guiLeft + 42, this.guiTop + 6,
        this.tileSmelter.storage);
  }

  @Override
  public void drawScreen(int x, int y, float f)
  {
    super.drawScreen(x, y, f);
    this.energy.drawOverlay(x, y);
  }

  @Override
  public void drawGuiContainerForegroundLayer(int x, int y)
  {
    final String name = RetroCraft.proxy
        .localize(ModBlocks.blockOreSmelter.getUnlocalizedName() + ".name");
    final int LABEL_XPOS = (xSize) / 2
        - fontRenderer.getStringWidth(name) / 2;
    final int LABEL_YPOS = -10;
    fontRenderer.drawString(name, LABEL_XPOS, LABEL_YPOS,
        Color.cyan.getRGB());
  }

  @Override
  public void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop + 93, 0, 0, 176, 86);

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

    if (this.tileSmelter.burnTimeRemaining > 0)
    {
      int i = this.tileSmelter.getBurnTimeScaled(22);
      this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 33, 176, 83,
          24, 22 - i);
    }

    this.energy.draw();
  }

}
