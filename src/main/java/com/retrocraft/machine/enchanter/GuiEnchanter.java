package com.retrocraft.machine.enchanter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.network.PacketEnchant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class GuiEnchanter extends GuiContainer
{

  private static final int BUTTON_X = 36;
  private static final int BUTTON_Z = 36;

  private static final int ENCHANTLIST_X     = 62;
  private static final int ENCHANTLIST_Z_MIN = 18;
  private static final int ENCHANTLIST_Z_MAX = 86;

  private static final int SCROLLBAR_X     = 206;
  private static final int SCROLLBAR_WIDTH = 11;

  private static final int SCROLL_IMG_OFFSET_X = 0;
  private static final int SCROLL_IMG_OFFSET_Y = 182;
  private static final int SCROLL_IMG_WIDTH    = 12;
  private static final int SCROLL_IMG_HEIGHT   = 15;

  private static final int guiOffset = 0;

  private InventoryPlayer              playerInv;
  private ArrayList<GuiEnchanterLabel> enchantmentArray = new ArrayList<GuiEnchanterLabel>();
  private Map<Enchantment, Integer>    enchantments;

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(
      RetroCraft.modId, "textures/gui/enchanter.png");
  private ContainerEnchanter            container;

  private double  sliderIndex     = 0;
  private boolean clicked         = false;
  private boolean sliding         = false;
  private double  enchantingPages = 0;
  private int     totalCost       = 0;

  public GuiEnchanter(Container containerEnchanter, InventoryPlayer playerInv)
  {
    super(containerEnchanter);

    this.xSize = 235;
    this.ySize = 182;

    this.playerInv = playerInv;
    this.container = (ContainerEnchanter) containerEnchanter;
  }

  @Override
  public void initGui()
  {
    super.initGui();
    buttonList.add(
        new GuiButton(0, guiLeft + BUTTON_X, guiTop + BUTTON_Z, 18, 14, "E"));
  }

  @Override
  protected void actionPerformed(GuiButton par1GuiButton)
  {

    final HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      final Integer newLevel = enchantments.get(item.enchantment);
      if (item.enchantmentLevel != newLevel && !item.locked)
      {
        enchants.put(item.enchantment, item.enchantmentLevel);
        playerInv.player.sendMessage(new TextComponentString(
            "Apply enchantment " + item.enchantment.getName()));
      }
    }
    playerInv.player.sendMessage(
        new TextComponentString("Apply " + enchants.size() + " enchantments"));
    System.out
        .println("[RETROCRAFT] Apply " + enchants.size() + " enchantments");

    if (enchants.size() > 0)
    {
      playerInv.player.sendMessage(new TextComponentString("Enchanting..."));
      try
      {
        // if (container.enchant(playerInv.player, enchants, totalCost))
        RetroCraft.network.sendToServer(new PacketEnchant(enchants, totalCost));
        playerInv.player.sendMessage(new TextComponentString("Enchanted!"));
      } catch (Exception e)
      {
        playerInv.player.sendMessage(new TextComponentString(e.getMessage()));
      }
    }
    return;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX,
      int mouseY)
  {

    boolean flag = Mouse.isButtonDown(0);

    /*
     * reset the GL color to solid white, instead of potentially something else.
     * If we don’t reset it, and the color isn’t already white, our texture
     * would be tinted with that color.
     */
    GlStateManager.disableLighting();
    GlStateManager.disableDepth();
    GlStateManager.color(1, 1, 1, 1);

    /*
     * bind the background texture that we’ve specified in our BG_TEXTURE field
     * to Minecraft’s rendering engine, so that when we render a rectangle with
     * a texture on it, the correct texture is used.
     */
    mc.getTextureManager().bindTexture(BG_TEXTURE);

    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    // enchantmentArray = convertMapToGuiItems(
    // container.getEnchantments(),
    // ENCHANTLIST_X + guiLeft,
    // ENCHANTLIST_Z_MIN + guiTop);

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      item.show(
             item.yPos >= guiTop + ENCHANTLIST_Z_MIN
          && item.yPos  < guiTop + ENCHANTLIST_Z_MAX);
      item.draw(fontRendererObj);
    }

    final int adjustedMouseX = mouseX - guiLeft;
    final int adjustedMouseY = mouseY - guiTop;

    // mc.renderEngine.bindTexture(texture);
    int tempY = adjustedMouseY - 16;
    if (tempY <= 0)
    {
      tempY = 0;
    } else if (tempY >= 57)
    {
      tempY = 57;
    }
    sliderIndex = sliding
        ? Math.round((tempY / 57D * enchantingPages) / .25) * .25 : sliderIndex;

    if (sliderIndex >= enchantingPages)
    {
      sliderIndex = enchantingPages;
    }

    double sliderY = sliding ? tempY : 57 * (sliderIndex / enchantingPages);

    drawTexturedModalRect(guiLeft + guiOffset + SCROLLBAR_X, // 180
        guiTop + 16 + (int) sliderY, SCROLL_IMG_OFFSET_X, SCROLL_IMG_OFFSET_Y,
        SCROLL_IMG_WIDTH, SCROLL_IMG_HEIGHT);

    if (!clicked && flag)
    {
      for (final GuiEnchanterLabel item : enchantmentArray)
      {
        if (getItemFromPos(mouseX, mouseY) == item && !item.locked)
        {
          item.dragging = true;
        }
      }
      if (adjustedMouseX <= SCROLLBAR_X + SCROLLBAR_WIDTH + guiOffset
          && adjustedMouseX >= SCROLLBAR_X + guiOffset)
      {
        if (enchantingPages != 0)
        {
          sliding = true;
        }
      }
    }

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.dragging && getItemFromPos(mouseX, mouseY) != item)
      {
        item.dragging = false;
      }
    }

    if (!flag)
    {
      for (final GuiEnchanterLabel item : enchantmentArray)
      {
        if (getItemFromPos(mouseX, mouseY) == item)
        {
          item.dragging = false;
        }
      }
      sliding = false;
    }

    clicked = flag;

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.dragging)
      {
        item.scroll(adjustedMouseX - ENCHANTLIST_X, // TODO: or 36??
            ENCHANTLIST_X + guiLeft);
      }
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
  {
    String name = RetroCraft.proxy
        .localize(ModBlocks.blockEnchanter.getUnlocalizedName() + ".name");
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    fontRenderer.drawString(name,
        xSize / 2 - fontRenderer.getStringWidth(name) / 2, /* x */
        2, /* z */
        0x404040);
  }

  @Override
  public void updateScreen()
  {

    super.updateScreen();

    // final Map<Integer, Integer> enchantments =
    // updateEnchantments(container.getEnchantments());

    // handleChangedScreenSize(enchantments);
    enableEnchantments();
    if (container.getEnchantments() != this.enchantments)
    {
      this.enchantments = container.getEnchantments();
      enchantmentArray = convertMapToGuiItems(this.enchantments,
          ENCHANTLIST_X + guiLeft, ENCHANTLIST_Z_MIN + guiTop);
    }
    enchantingPages = enchantmentArray.size() / 4.0 > 1
        ? enchantmentArray.size() / 4.0 - 1.0 : 0;
    totalCost = 0;

    handleChangedEnchantments(enchantments);
  }

  protected boolean levelChanged()
  {

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.enchantmentLevel != item.currentLevel)
      {
        return true;
      }
    }
    return false;
  }

  private void handleChangedEnchantments(Map<Enchantment, Integer> enchantments)
  {

    if (!enchantmentArray.isEmpty() && levelChanged())
    {
      for (final GuiEnchanterLabel item : enchantmentArray)
      {
        handleChangedEnchantment(enchantments, item);
      }
    } else if (!levelChanged())
    {
      totalCost += container.repairCostMax();

      for (final GuiEnchanterLabel item : enchantmentArray)
      {
        item.yPos = item.startingYPos - (int) (18 * 4 * sliderIndex);
      }
    }
  }

  private void handleChangedEnchantment(Map<Enchantment, Integer> enchantments,
      GuiEnchanterLabel item)
  {

    item.yPos = item.startingYPos - (int) (18 * 4 * sliderIndex);

    final Integer level = enchantments.get(item.enchantment);
    if (!item.locked && item.enchantmentLevel > level)
    {
      int temp = totalCost + container.enchantmentCost(item.enchantment,
          item.enchantmentLevel, level);

      if (!container.canPurchase(playerInv.player, temp))
      {
        while (item.enchantmentLevel > 0)
        {
          item.dragging = false;
          item.enchantmentLevel--;
          temp = totalCost + container.enchantmentCost(item.enchantment,
              item.enchantmentLevel, level);
          if (container.canPurchase(playerInv.player, temp))
          {
            break;
          }

        }
      }

      totalCost = temp;
    } else if (item.enchantmentLevel < level && !item.locked)
    {
      if (EnchantHelper.containsEnchantment(
          container.getInventory().get(0).getTagCompound()
              .getTagList("restrictions", 10),
          Enchantment.getEnchantmentID(item.enchantment),
          item.enchantmentLevel))
      {
        totalCost += container.disenchantmentCost(item.enchantment,
            item.enchantmentLevel, level);
      } else
      {
        totalCost = 0;
      }
    }
  }

  private GuiEnchanterLabel getItemFromPos(int x, int y)
  {

    if (x < guiLeft + ENCHANTLIST_X
        || x > guiLeft + ENCHANTLIST_X + GuiEnchanterLabel.WIDTH + 5)
    {
      return null;
    }

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (!item.show)
      {
        continue;
      }
      if (y >= item.yPos && y <= item.yPos + GuiEnchanterLabel.HEIGHT)
      {
        return item;
      }
    }
    return null;
  }

  private void enableEnchantments()
  {

    for (GuiEnchanterLabel item : enchantmentArray)
    {
      item.locked = false;
    }

    for (GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.enchantmentLevel != 0)
      {
        for (final GuiEnchanterLabel item2 : enchantmentArray)
        {
          if (item == item2)
            continue;

          if (!EnchantHelper.isEnchantmentsCompatible(item.enchantment,
              item2.enchantment))
          {
            item2.locked = true;
          }
        }
      }
    }
  }

  /**
   * Converts map to arraylist of gui items
   *
   * @param map
   *          the map of enchantments to convert
   * @param x
   *          starting x position
   * @param y
   *          starting y position
   * @return the arraylist of gui items
   */
  private ArrayList<GuiEnchanterLabel> convertMapToGuiItems(
      final Map<Enchantment, Integer> map, int x, int y)
  {

    final ArrayList<GuiEnchanterLabel> temp = new ArrayList<GuiEnchanterLabel>();

    if (map == null)
      return temp;

    int i = 0;
    int yPos = y;
    for (Enchantment obj : map.keySet())
    {
      temp.add(new GuiEnchanterLabel(obj, map.get(obj), x, yPos));
      i++;
      yPos = y + i * 18;
    }

    return temp;
  }
}
