package com.retrocraft.entity.waystone.render;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.client.ClientWaystones;
import com.retrocraft.entity.waystone.BlockWaystone;
import com.retrocraft.entity.waystone.TileWaystone;
import com.retrocraft.entity.waystone.WaystoneManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderWaystone extends TileEntitySpecialRenderer<TileWaystone>
{

  private static final ResourceLocation texture       = new ResourceLocation(
      RetroCraft.modId, "textures/entity/waystone.png");
  private static final ResourceLocation textureActive = new ResourceLocation(
      RetroCraft.modId, "textures/entity/waystone_active.png");

  private final ModelWaystone model = new ModelWaystone();

  @Override
  public void renderTileEntityAt(TileWaystone tileEntity, double x, double y,
      double z, float partialTicks, int destroyStage)
  {
    IBlockState state = (tileEntity != null && tileEntity.hasWorld())
        ? tileEntity.getWorld().getBlockState(tileEntity.getPos()) : null;
    if (state != null && state.getBlock() != ModBlocks.blockWaystone)
    { // I don't know. But it seems for some reason the renderer gets called for
      // minecraft:air in certain cases.
      return;
    }

    bindTexture(texture);

    float angle = state != null
        ? WaystoneManager.getRotationYaw(state.getValue(BlockWaystone.FACING))
        : 0f;
    GlStateManager.pushMatrix();
    // GlStateManager.enableLighting();
    GlStateManager.color(1f, 1f, 1f, 1f);
    GlStateManager.translate(x + 0.5, y, z + 0.5);
    GlStateManager.rotate(angle, 0f, 1f, 0f);
    GlStateManager.rotate(-180f, 1f, 0f, 0f);
    GlStateManager.scale(0.5f, 0.5f, 0.5f);
    model.renderAll();
    if (tileEntity != null && tileEntity.hasWorld() && (ClientWaystones
        .getKnownWaystone(tileEntity.getWaystoneName()) != null))
    {
      bindTexture(textureActive);
      GlStateManager.scale(1.05f, 1.05f, 1.05f);
      if (!RetroCraftConfig.disableTextGlow)
      {
        // GlStateManager.disableLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
      }
      model.renderPillar();
      if (!RetroCraftConfig.disableTextGlow)
      {
        Minecraft.getMinecraft().entityRenderer.enableLightmap();
      }
    }
    GlStateManager.popMatrix();
  }
}