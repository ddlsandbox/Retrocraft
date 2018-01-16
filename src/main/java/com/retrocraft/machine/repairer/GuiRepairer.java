package com.retrocraft.machine.repairer;


import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.machine.GuiEnergyDisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRepairer extends GuiContainer {

	private TileRepairer tileRepairer;
	private GuiEnergyDisplay energy;
	
	// This is the resource location for the background image
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(RetroCraft.modId, 
																			"textures/gui/repairer.png");
  private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation(
      RetroCraft.modId, "textures/gui/inventory.png");

	public GuiRepairer(InventoryPlayer invPlayer, TileRepairer tileRepairer, ContainerRepairer containerRepairer) {
		super(containerRepairer);

		this.xSize = 176;
    this.ySize = 93 + 86;

		this.tileRepairer = tileRepairer;
	}
	
    @Override
    public void initGui () {
        
        super.initGui();
        this.energy = new GuiEnergyDisplay(this.guiLeft + 42, this.guiTop + 6,
            this.tileRepairer.storage);
    }
    
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.energy.drawOverlay(mouseX, mouseY);
    this.drawOverlay(mouseX, mouseY);
    this.renderHoveredToolTip(mouseX, mouseY);
  }
    
  public void drawOverlay(int mouseX, int mouseY)
  {
    if (
        (mouseX >=  this.guiLeft + 80 && mouseY >= guiTop + 33
        && mouseX < this.guiLeft + 104
        && mouseY < this.guiTop + 55)
        ||
        (mouseX >=  this.guiLeft + 79 && mouseY >= guiTop + 13
        && mouseX < this.guiLeft + 96
        && mouseY < this.guiTop + 30))
    {
      Minecraft mc = Minecraft.getMinecraft();

      List<String> text = new ArrayList<String>();
      text.add(this.getOverlayText());
      GuiUtils.drawHoveringText(text, mouseX, mouseY, mc.displayWidth,
          mc.displayHeight, -1, mc.fontRenderer);
    }
  }
  
  private String getOverlayText()
  {
    NumberFormat format = NumberFormat.getInstance();
    return String.format("%s/%s damage points",
        format.format(this.tileRepairer.repairTimeRemaining),
        format.format(this.tileRepairer.REPAIR_TIME));
  }
  
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
    this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop + 93, 0, 0, 176, 86);

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);
    
    if (this.tileRepairer.repairTimeRemaining > 0)
    {
      int i = this.tileRepairer.getRepairTimeScaled(22);
      this.drawTexturedModalRect(
          this.guiLeft + 80, 
          this.guiTop + 33,
          176, 
          83, 
          24, 
          22-i);
    }

    this.energy.draw();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	  final String name = RetroCraft.proxy
        .localize(ModBlocks.blockOreGrinder.getUnlocalizedName() + ".name");
    final int LABEL_XPOS = (xSize) / 2
        - fontRenderer.getStringWidth(name) / 2;
    final int LABEL_YPOS = -10;
    fontRenderer.drawString(name, LABEL_XPOS, LABEL_YPOS,
        Color.cyan.getRGB());
	}
}
