package com.retrocraft.item.backpack;

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
public class GuiBackpack extends GuiContainer
{

  private static final ResourceLocation RES_LOC = new ResourceLocation(
      RetroCraft.modId, "textures/gui/backpack_gui.png");
  private static final ResourceLocation INV_LOC = new ResourceLocation(
      RetroCraft.modId, "textures/gui/inventory.png");

  public GuiBackpack(ItemStack sack, InventoryPlayer inventory)
  {
    this(new ContainerBackpack(sack, inventory));
  }

  private GuiBackpack(ContainerBackpack container)
  {
    super(container);
    this.xSize = 176;
    this.ySize = 126 + 86;
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
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 126);
    
    this.mc.getTextureManager().bindTexture(INV_LOC);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop + 126, 0, 0, 176, 86);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }
}