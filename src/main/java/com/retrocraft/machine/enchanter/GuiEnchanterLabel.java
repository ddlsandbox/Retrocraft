package com.retrocraft.machine.enchanter;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.enchantment.Enchantment;

public class GuiEnchanterLabel extends Gui
{

  public final Enchantment enchantment;
  public int enchantmentLevel;
  public final int currentLevel;

  public int startingXPos;
  public int startingYPos;
  public int xPos;
  public int yPos;
  public static final int HEIGHT = 18;
  public static final int WIDTH = 143;

  private int sliderX;
  public boolean dragging = false;

  public boolean show = true;
  public boolean locked = false;

  public GuiEnchanterLabel(Enchantment enchantment, int level, int x, int y)
  {
    this.enchantment = enchantment;
    this.enchantmentLevel = level;
    this.currentLevel = level;
    this.xPos = startingXPos = x;
    this.yPos = startingYPos = y;

    this.sliderX = xPos + 1;
  }

  public void draw(FontRenderer fontRendererObj)
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
    fontRendererObj.drawString(getTranslatedName(), xPos + 5, yPos + HEIGHT / 4,
        0x55aaff00);
    if (locked)
      drawRect(xPos, yPos + 1, xPos + WIDTH, yPos - 1 + HEIGHT, 0x44aaffff);
    else
      drawRect(xPos, yPos + 1, xPos + WIDTH, yPos - 1 + HEIGHT, 0x44aa55ff);
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
        .floor(currentLevel > enchantment.getMaxLevel() ? currentLevel * index
            : enchantment.getMaxLevel() * index);

    if (tempLevel >= currentLevel)
    {
      enchantmentLevel = tempLevel;
    }

    if (enchantmentLevel <= 0)
    {
      enchantmentLevel = 0;
    }
  }
}
