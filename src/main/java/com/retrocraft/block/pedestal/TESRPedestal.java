package com.retrocraft.block.pedestal;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRPedestal extends TileEntitySpecialRenderer<TileEntityPedestal> {

	private static final double DISTANCE_FOR_LEVITATE = 4.0;
	private static final double LEVITATE_HEIGHT_CENTER = 1.0;
	private static final double LEVITATE_HEIGHT_OFFSET = 0.5;
	
	@Override
	public void render(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		ItemStack stack = te.inventory.getStackInSlot(0);
		if (!stack.isEmpty()) {
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.pushMatrix();
			
			if (te.getMode() == TileEntityPedestal.PedestalMode.DANCING)
			{
				double offset = LEVITATE_HEIGHT_OFFSET * Math.sin((te.getWorld().getTotalWorldTime() - te.lastChangeTime + partialTicks) / 8) / 4.0;
				GlStateManager.translate(x + 0.5, y + LEVITATE_HEIGHT_CENTER + offset, z + 0.5);
			}
			else
			{
				GlStateManager.translate(x + 0.5, y + 1.0, z + 0.5);
			}
			
			if (te.getMode() !=  TileEntityPedestal.PedestalMode.STATIC)
			{
				GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);
			}
			else
			{
				GlStateManager.rotate(0, 0, 1, 0);
			}

			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(),
					null);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}

}