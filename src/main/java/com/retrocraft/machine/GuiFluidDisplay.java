package com.retrocraft.machine;

import java.text.NumberFormat;
import java.util.Collections;

import com.retrocraft.RetroCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFluidDisplay extends Gui
{

  private FluidTank fluidReference;
  private Fluid     oldFluid;

  private int     x;
  private int     y;
  private boolean outline;

  private ResourceLocation resLoc;

  private boolean drawTextNextTo;

  public GuiFluidDisplay(int x, int y, FluidTank fluidReference, boolean outline,
                      boolean drawTextNextTo)
  {
    this.setData(x, y, fluidReference, outline, drawTextNextTo);
  }

  public GuiFluidDisplay(int x, int y, FluidTank fluidReference)
  {
    this(x, y, fluidReference, false, false);
  }

  public void setData(int x, int y, FluidTank fluidReference, boolean outline,
      boolean drawTextNextTo)
  {
    this.x = x;
    this.y = y;
    this.fluidReference = fluidReference;
    this.outline = outline;
    this.drawTextNextTo = drawTextNextTo;
  }

  public void draw()
  {
    Minecraft mc = Minecraft.getMinecraft();
    mc.getTextureManager().bindTexture(
        new ResourceLocation(RetroCraft.modId, "textures/gui/inventory.png"));

    int barX = this.x;
    int barY = this.y;

    if (this.outline)
    {
      this.drawTexturedModalRect(this.x, this.y, 52, 163, 26, 93);

      barX += 4;
      barY += 4;
    }
    this.drawTexturedModalRect(barX, barY, 0, 171, 18, 85);

    FluidStack stack = this.fluidReference.getFluid();
    Fluid fluid = stack == null ? null : stack.getFluid();

    if (this.resLoc == null || this.oldFluid != fluid)
    {
      this.oldFluid = fluid;

      if (fluid != null && fluid.getStill() != null)
      {
        this.resLoc = new ResourceLocation(fluid.getStill().getResourceDomain(),
            "textures/" + fluid.getStill().getResourcePath() + ".png");
      }
    }

    if (stack != null && fluid != null && this.resLoc != null)
    {
      mc.getTextureManager().bindTexture(this.resLoc);

      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      int i = this.fluidReference.getFluidAmount() * 83
          / this.fluidReference.getCapacity();
      Gui.drawModalRectWithCustomSizedTexture(barX + 1, barY + 84 - i,
          36, 172, 16, i, 16, 512);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.popMatrix();
    }

    if (this.drawTextNextTo)
    {
      this.drawString(mc.fontRendererObj, this.getOverlayText(), barX + 25,
          barY + 78, 
          16777215); // decimal white color
    }
  }

  public void drawOverlay(int mouseX, int mouseY)
  {
    if (mouseX >= this.x && mouseY >= this.y
        && mouseX < this.x + (this.outline ? 26 : 18)
        && mouseY < this.y + (this.outline ? 93 : 85))
    {
      Minecraft mc = Minecraft.getMinecraft();
      GuiUtils.drawHoveringText(
          Collections.singletonList(this.getOverlayText()), mouseX, mouseY,
          mc.displayWidth, mc.displayHeight, -1, mc.fontRendererObj);
    }
  }

  private String getOverlayText()
  {
    NumberFormat format = NumberFormat.getInstance();
    FluidStack stack = this.fluidReference.getFluid();
    String cap = format.format(this.fluidReference.getCapacity());
    return stack == null || stack.getFluid() == null ? "0/" + cap + " mB"
        : format.format(this.fluidReference.getFluidAmount()) + "/" + cap
            + " mB " + stack.getLocalizedName();
  }
}
