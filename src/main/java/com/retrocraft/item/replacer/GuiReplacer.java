package com.retrocraft.item.replacer;

import java.awt.Color;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ModItems;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiReplacer extends GuiContainer
{

  private static final ResourceLocation RES_LOC = new ResourceLocation(
      RetroCraft.modId, "textures/gui/replacer.png");
  private static final ResourceLocation INV_LOC = new ResourceLocation(
      RetroCraft.modId, "textures/gui/inventory.png");

  private final ContainerReplacer container;

  public GuiReplacer(ItemStack sack, InventoryPlayer inventory, boolean isVoid)
  {
    this(isVoid, new ContainerReplacer(sack, inventory));
  }

  private GuiReplacer(boolean isVoid, ContainerReplacer container)
  {
    super(container);
    this.xSize = 176;
    this.ySize = 93 + 86;
    this.container = container;
  }

  @Override
  public void initGui()
  {
    super.initGui();
  }

  @Override
  public void updateScreen()
  {
    super.updateScreen();
  }

  @Override
  public void drawGuiContainerForegroundLayer(int x, int y)
  {
    final String name = RetroCraft.proxy
        .localize(ModItems.backpack.getUnlocalizedName() + ".name");
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

    this.mc.getTextureManager().bindTexture(RES_LOC);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);
    
    this.mc.getTextureManager().bindTexture(INV_LOC);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop + 93, 0, 0, 176, 86);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }
}