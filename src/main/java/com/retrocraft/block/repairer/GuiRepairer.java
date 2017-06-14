package com.retrocraft.block.repairer;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.retrocraft.RetroCraft;
import com.retrocraft.network.PacketHandler;

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
	private static final ResourceLocation texture = new ResourceLocation(RetroCraft.modId, "textures/gui/repairer.png");
	private TileRepairer tileEntity;

	public GuiRepairer(InventoryPlayer invPlayer, TileRepairer tileRepairer, ContainerRepairer containerRepairer) {
		super(containerRepairer);

		// Set the width and height of the gui
		xSize = 205;
		ySize = 130;

		this.tileEntity = tileRepairer;
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
            	containerRepairer.repairItem();
            	tileEntity.update();
            	tileEntity.markDirty();
            	RetroCraft.network.sendToServer(new PacketRepairer(1));
                return;
        }
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRendererObj.drawString(tileEntity.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}
//		// You must re bind the texture and reset the colour if you still need to use it after drawing a string
//		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
//		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}
