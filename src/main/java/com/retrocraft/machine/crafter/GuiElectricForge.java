package com.retrocraft.machine.crafter;

import java.awt.Color;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElectricForge extends GuiContainer
{

  private static final ResourceLocation RES_LOC = new ResourceLocation(
      "textures/gui/container/crafting_table.png");

  public GuiElectricForge(EntityPlayer player)
  {
    super(new ContainerElectricForge(player));

    this.xSize = 176;
    this.ySize = 166;
  }

  @Override
  public void drawGuiContainerForegroundLayer(int x, int y)
  {
    final String name = RetroCraft.proxy
        .localize(ModBlocks.blockElectricForge.getUnlocalizedName() + ".name");
    final int LABEL_XPOS = (xSize) / 2
        - fontRendererObj.getStringWidth(name) / 2;
    final int LABEL_YPOS = -10;
    fontRendererObj.drawString(name, LABEL_XPOS, LABEL_YPOS,
        Color.cyan.getRGB());
  }

  @Override
  public void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(RES_LOC);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize,
        this.ySize);
  }
}