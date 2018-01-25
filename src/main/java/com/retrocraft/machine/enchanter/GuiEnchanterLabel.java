package com.retrocraft.machine.enchanter;

import com.retrocraft.RetroCraft;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnchanterLabel extends Gui
{

  public final Enchantment enchantment;
  public int               enchantmentLevel;
  public final int         initialLevel;

  public int              startingXPos;
  public int              startingYPos;
  public int              xPos;
  public int              yPos;
  public static final int HEIGHT = 13;
  public static final int WIDTH  = 140/2-2;

  private int sliderX;
  private int maxLevel;
  public boolean dragging = false;

  public boolean show   = true;
  public boolean locked = false;

  public GuiEnchanterLabel(Enchantment enchantment, int level, int x, int y)
  {
    this.enchantment = enchantment;
    this.enchantmentLevel = level;
    this.initialLevel = level;
    this.xPos = startingXPos = x;
    this.yPos = startingYPos = y;

    this.sliderX = xPos + 1;
    this.maxLevel = RetroCraft.getConfig().allowOverpower>0
        ? (enchantment.getMaxLevel()==1?1:RetroCraft.getConfig().allowOverpower)
        : enchantment.getMaxLevel();
  }

  public void draw(FontRenderer fontRenderer)
  {
    
    final float fontScale = 0.5f;
    
    if (!show)
    {
      return;
    }

    final int indexX = dragging ? sliderX
        : enchantmentLevel <= maxLevel
            ? (int) (xPos + 1
                + (WIDTH - 6)
                    * (enchantmentLevel / (double) maxLevel))
            : xPos + 1 + WIDTH - 6;

    drawRect(indexX, yPos + 1, indexX + 5, yPos - 1 + HEIGHT, 0xff000000);
    
    GlStateManager.scale(fontScale, fontScale, fontScale);
    fontRenderer.drawString(
        getTranslatedName(), 
        (int) ((xPos + 5)/fontScale), 
        (int) ((yPos + HEIGHT / 4)/fontScale),
        0xff0000ff);
    GlStateManager.scale(1.0/fontScale, 1.0/fontScale, 1.0/fontScale);
    
    if (locked)
      drawRect(xPos, yPos + 1, xPos + WIDTH, yPos - 1 + HEIGHT, 0x44ff0000);
    else
      drawRect(xPos, yPos + 1, xPos + WIDTH, yPos - 1 + HEIGHT, 0x4400ff00);
  }

  public String getTranslatedName()
  {

    String name = enchantment.getTranslatedName(enchantmentLevel);

    if (enchantmentLevel == 0)
    {
      if (name.lastIndexOf(" ") == -1)
      {
        name = enchantment.getName();
      } else
      {
        name = name.substring(0, name.lastIndexOf(" "));
      }
    }

    return name;
  }

  public void show(boolean b)
  {
    show = b;
  }

  public void scroll(int xPos, int start)
  {

    if (locked)
    {
      return;
    }

    sliderX = start + xPos;

    if (sliderX <= start)
    {
      sliderX = start;
    }

    if (sliderX >= start + WIDTH + 20)
    {
      sliderX = start + WIDTH + 20;
    }

    float index = xPos / (float) WIDTH;
    final int tempLevel = (int) Math
        .round(initialLevel > maxLevel 
            ? initialLevel * index
            : maxLevel * index);

    if (tempLevel >= initialLevel)
    {
      enchantmentLevel = tempLevel;
    }

    if (enchantmentLevel <= 0)
    {
      enchantmentLevel = 0;
    }
  }
}
