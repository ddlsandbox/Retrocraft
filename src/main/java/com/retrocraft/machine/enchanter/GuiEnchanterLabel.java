package com.retrocraft.machine.enchanter;

import com.retrocraft.RetroCraft;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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
  public static final int WIDTH  = 140;

  private int    sliderX;
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
  }

  public void draw(FontRenderer fontRenderer)
  {

    if (!show)
    {
      return;
    }

    final int indexX = dragging ? sliderX
        : enchantmentLevel <= enchantment.getMaxLevel()
            ? (int) (xPos + 1
                + (WIDTH - 6)
                    * (enchantmentLevel / (double) enchantment.getMaxLevel()))
            : xPos + 1 + WIDTH - 6;

    drawRect(indexX, yPos + 1, indexX + 5, yPos - 1 + HEIGHT, 0xff000000);
    fontRenderer.drawString(getTranslatedName(), xPos + 5, yPos + HEIGHT / 4,
        0x55aaff00);
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
        .floor(this.initialLevel > this.enchantment.getMaxLevel()
            ? this.initialLevel * index
            : this.enchantment.getMaxLevel() * index);

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
        .floor(initialLevel > enchantment.getMaxLevel() ? initialLevel * index
            : enchantment.getMaxLevel() * index);

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
