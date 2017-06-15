package com.retrocraft.machine.repairer;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.network.PacketRepairer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRepairer extends GuiContainer {

	private ContainerRepairer containerRepairer;
	
	// This is the resource location for the background image
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(RetroCraft.modId, 
																			"textures/gui/repairer.png");

	public GuiRepairer(InventoryPlayer invPlayer, TileRepairer tileRepairer, ContainerRepairer containerRepairer) {
		super(containerRepairer);

		// Set the width and height of the gui
		this.xSize = 205;
		this.ySize = 130;

		this.containerRepairer = containerRepairer;
	}
	
    @Override
    public void initGui () {
        
        super.initGui();
        buttonList.add(new GuiButton(0, 
        						guiLeft + 84, 
        						guiTop + 16, 
        						40, 18, "Repair"));
    }
    
    @Override
    protected void actionPerformed (GuiButton par1GuiButton) {
        
       
        switch (par1GuiButton.id) {
            case 0:
            	if (containerRepairer.repairItem())
            		RetroCraft.network.sendToServer(new PacketRepairer(1));
                return;
        }
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(BG_TEXTURE);
		
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		final String name = RetroCraft.proxy.localize(ModBlocks.blockRepairer.getUnlocalizedName() + ".name");
		final int LABEL_XPOS = 26 + (xSize-26) / 2 - fontRendererObj.getStringWidth(name) / 2;
		final int LABEL_YPOS = 5;
		fontRendererObj.drawString(name, 
								   LABEL_XPOS, LABEL_YPOS, 
								   Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}
	}
}
