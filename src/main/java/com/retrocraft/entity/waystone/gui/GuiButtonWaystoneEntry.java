package com.retrocraft.entity.waystone.gui;

import com.google.common.collect.Lists;
import com.retrocraft.RetroCraft;
import com.retrocraft.entity.waystone.WaystoneEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiButtonWaystoneEntry extends GuiButton
{

  private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation(
      "textures/gui/container/enchanting_table.png");

  private final WaystoneEntry waystone;
  private final int           xpLevelCost;

  public GuiButtonWaystoneEntry(int id, int x, int y, WaystoneEntry waystone)
  {
    super(id, x, y, (waystone.isGlobal() ? TextFormatting.YELLOW : "")
        + waystone.getName());
    this.waystone = waystone;
    EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
    boolean enableXPCost = RetroCraft.getConfig().warpStoneXpCost;
    this.xpLevelCost = (enableXPCost
        && RetroCraft.getConfig().blocksPerXPLevel > 0)
            ? MathHelper.clamp(
                (int) Math.sqrt(player.getDistanceSqToCenter(waystone.getPos()))
                    / RetroCraft.getConfig().blocksPerXPLevel,
                0, 3)
            : 0;

    if (player.experienceLevel < xpLevelCost)
    {
      enabled = false;
    }
  }

  public WaystoneEntry getWaystone()
  {
    return waystone;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY)
  {
    super.drawButton(mc, mouseX, mouseY);
    GlStateManager.color(1f, 1f, 1f, 1f);

    if (xpLevelCost > 0)
    {
      boolean canAfford = mc.player.experienceLevel >= xpLevelCost;
      mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
      drawTexturedModalRect(xPosition + 2, yPosition + 2,
          (xpLevelCost - 1) * 16, 223 + (!canAfford ? 16 : 0), 16, 16);

      if (hovered && mouseX <= xPosition + 16)
      {
        GuiUtils.drawHoveringText(
            Lists.newArrayList(
                (canAfford ? TextFormatting.GREEN : TextFormatting.RED) + I18n
                    .format("tooltip.retrocraft:levelRequirement", xpLevelCost)),
            mouseX, mouseY + mc.fontRendererObj.FONT_HEIGHT, mc.displayWidth,
            mc.displayHeight, 200, mc.fontRendererObj);
      }
      GlStateManager.disableLighting();
    }
  }
}