package com.retrocraft.entity.teleportpipe.render;

import com.retrocraft.RetroCraft;
import com.retrocraft.RetroCraftConfig;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.client.ClientWaystones;
import com.retrocraft.entity.teleportpipe.BlockTeleportPipe;
import com.retrocraft.entity.teleportpipe.TileTeleportPipe;
import com.retrocraft.entity.teleportpipe.TeleportManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderWaystone extends TileEntitySpecialRenderer<TileTeleportPipe>
{

  private static final ResourceLocation texture       = new ResourceLocation(
      RetroCraft.modId, "textures/entity/waystone.png");
  private static final ResourceLocation textureActive = new ResourceLocation(
      RetroCraft.modId, "textures/entity/waystone_active.png");

  private final ModelWaystone model = new ModelWaystone();

  @Override
  public void render(TileTeleportPipe tileEntity, double x, double y,
      double z, float partialTicks, int destroyStage, float alpha)
  {
    IBlockState state = (tileEntity != null && tileEntity.hasWorld())
        ? tileEntity.getWorld().getBlockState(tileEntity.getPos()) : null;
    if (state != null && state.getBlock() != ModBlocks.blockWaystone)
    {
      return;
    }

    bindTexture(texture);

    float angle = state != null
        ? TeleportManager.getRotationYaw(state.getValue(BlockTeleportPipe.FACING))
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
      Minecraft.getMinecraft().entityRenderer.disableLightmap();
      model.renderPillar();
      Minecraft.getMinecraft().entityRenderer.enableLightmap();
    }
    GlStateManager.popMatrix();
  }
}