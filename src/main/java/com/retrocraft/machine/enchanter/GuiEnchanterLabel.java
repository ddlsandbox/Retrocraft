package com.retrocraft.machine.enchanter;

import com.retrocraft.RetroCraft;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;

public class GuiEnchanterLabel extends Gui
{

  private final ContainerEnchanter container;
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

  public GuiEnchanterLabel(ContainerEnchanter container, Enchantment enchantment, int level, int x, int y)
  {
    this.container = container;
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

  /**
   * Updates the state of the slider. This is used to handle changing the
   * current level of the enchantment being represented.
   * 
   * @param xPos
   *          The xPos of the slider.
   * @param prevX
   *          The previous slider position.
   */
  public void updateSlider(int xPos, int prevX)
  {

    if (this.locked)
      return;
    this.sliderX = prevX + xPos;

    if (this.sliderX <= prevX)
      this.sliderX = prevX;

    if (this.sliderX >= prevX + WIDTH + 20)
      this.sliderX = prevX + WIDTH + 20;

    final float index = xPos / (float) (WIDTH + 10);
    final int tempLevel = (int) Math
        .floor(this.initialLevel > maxLevel
            ? this.initialLevel * index
            : maxLevel * index);

    if (tempLevel >= this.initialLevel
        || RetroCraft.getConfig().allowDisenchanting
            && !this.container.getItem().isItemDamaged())
      this.enchantmentLevel = tempLevel;

    if (this.enchantmentLevel <= 0)
      this.enchantmentLevel = 0;
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
        .floor(initialLevel > maxLevel 
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
