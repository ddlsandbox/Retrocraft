package com.retrocraft.machine.enchanter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.network.PacketEnchant;
import com.retrocraft.util.StackUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnchanter extends GuiContainer
{

  private static final int BUTTON_X = 36;
  private static final int BUTTON_Z = 36;

  private static final int ENCHANTLIST_X     = 62;
  private static final int ENCHANTLIST_Y_MIN = 18;
  private static final int ENCHANTLIST_Y_MAX = 132;

  private InventoryPlayer              playerInv;
  private ArrayList<GuiEnchanterLabel> enchantmentArray = new ArrayList<GuiEnchanterLabel>();
  private Map<Enchantment, Integer>    enchantments;

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(
      RetroCraft.modId, "textures/gui/enchanter.png");
  private ContainerEnchanter            container;

  private boolean           clicked         = false;
  private int               totalCost       = 0;
  private GuiEnchanterLabel last;

  public GuiEnchanter(Container containerEnchanter, InventoryPlayer playerInv)
  {
    super(containerEnchanter);

    this.xSize = 221;
    this.ySize = 226;

    this.playerInv = playerInv;
    this.container = (ContainerEnchanter) containerEnchanter;

    // this.fontRenderer.FONT_HEIGHT = 5;
  }

  @Override
  public void initGui()
  {
    super.initGui();
    buttonList.add(
        new GuiButton(0, 
            guiLeft + BUTTON_X, 
            guiTop + BUTTON_Z, 
            18, 
            18, 
            "E"));
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
      }
    }

    if (enchants.size() > 0)
    {
      try
      {
        // if (container.enchant(playerInv.player, enchants, totalCost))
        RetroCraft.network.sendToServer(new PacketEnchant(enchants, totalCost));
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

    mc.getTextureManager().bindTexture(BG_TEXTURE);

    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      item.show(item.yPos >= guiTop + ENCHANTLIST_Y_MIN
          && item.yPos < guiTop + ENCHANTLIST_Y_MAX);
      item.draw(fontRenderer);
    }

    final int adjustedMouseX = mouseX - guiLeft;
    final int adjustedMouseY = mouseY - guiTop;

    int tempY = adjustedMouseY - 16;
    if (tempY <= 0)
    {
      tempY = 0;
    } else if (tempY >= 57)
    {
      tempY = 57;
    }

    if (!clicked && flag)
    {
      for (final GuiEnchanterLabel item : enchantmentArray)
      {
        if (getItemFromPos(mouseX, mouseY) == item && !item.locked)
        {
          item.dragging = true;
        }
      }
    }

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.dragging && getItemFromPos(mouseX, mouseY) != item)
      {
        item.dragging = false;
        this.last = item;
      }
    }

    if (!flag)
    {
      for (final GuiEnchanterLabel item : enchantmentArray)
      {
        if (getItemFromPos(mouseX, mouseY) == item)
        {
          item.dragging = false;
          this.last = item;
        }
      }
    }

    clicked = flag;

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.dragging)
      {
        item.scroll(adjustedMouseX - item.xPos + guiLeft, // TODO: or 36??
            item.xPos);
      }
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {

    super.drawScreen(mouseX, mouseY, partialTicks);
    this.updateEnchantmentLabels();
    this.renderHoveredToolTip(mouseX, mouseY);

    final int maxWidth = this.guiLeft - 20;
    final List<List<String>> information = new ArrayList<>();
    final ItemStack stack = this.container.getItem();

    information.add(this.fontRenderer.listFormattedStringToWidth(
        String.format("%s: %s", I18n.format("tooltip.enchanter.playerlevel"),
            this.playerInv.player.experienceLevel),
        maxWidth));

    if (StackUtil.isValid(stack))
      if (this.hasLevelChanged())
      {

        // final boolean exp = this.totalCost <= 1
        // && this.totalCost >= -1;
        final boolean negExp = this.totalCost < 0;
        // final int finalCost = exp ? this.totalCost
        // : negExp
        // ? -this.totalCost
        // : this.totalCost;
        final int finalCost = negExp ? -this.totalCost : this.totalCost;

        information.add(this.fontRenderer.listFormattedStringToWidth(
            String.format("%s: %s",
                // exp ? I18n.format("tooltip.enchanter.experienceGained") :
                I18n.format("tooltip.enchanter.levelneed"), finalCost),
            maxWidth));
      }
    // information.add(this.fontRenderer.listFormattedStringToWidth(
    // String.format("%s: %s", I18n.format("tooltip.eplus.maxlevel"),
    // this.container.getEnchantingPower()),
    // maxWidth));

    if (!StackUtil.isValid(this.container.getItem()))
      information.add(this.fontRenderer.listFormattedStringToWidth(
          I18n.format("tooltip.enchanter.additem"), maxWidth));

    for (final List<String> display : information)
    {

      int height = information.indexOf(display) == 0
          ? this.guiTop + this.fontRenderer.FONT_HEIGHT + 8
          : this.guiTop + (this.fontRenderer.FONT_HEIGHT + 8)
              * (information.indexOf(display) + 1);

      if (information.indexOf(display) > 0)
        for (int i = information.indexOf(display) - 1; i >= 0; i--)
          height += (this.fontRenderer.FONT_HEIGHT + 3)
              * (information.get(i).size() - 1);

      this.drawHoveringText(display, this.guiLeft - 20 - maxWidth, height,
          this.fontRenderer);
    }

    final GuiEnchanterLabel label = this.getItemFromPos(mouseX, mouseY);

    if (isShiftKeyDown() && label != null && label.enchantment != null)
    {

      final String enchName = ChatFormatting.BOLD + label.getTranslatedName();
      String description = I18n.format("enchantment."
          + label.enchantment.getRegistryName().getResourceDomain() + "."
          + label.enchantment.getRegistryName().getResourcePath() + ".desc");

//      if (description.startsWith("enchantment."))
//        description = ChatFormatting.RED + I18n.format("tooltip.retrocraft.enchant")
//            + " " + description;
//      else
        description = ChatFormatting.LIGHT_PURPLE + description;

      final List<String> display = new ArrayList<>();

      display.add(enchName);
      display.addAll(
          this.fontRenderer.listFormattedStringToWidth(description, 215));
      display.add(
          ChatFormatting.BLUE + "" + ChatFormatting.ITALIC + RetroCraft.modId);
      this.drawHoveringText(display, mouseX, mouseY, this.fontRenderer);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
  {
    final String name = RetroCraft.proxy
        .localize(ModBlocks.blockEnchanter.getUnlocalizedName() + ".name");
    final int LABEL_XPOS = (xSize) / 2 - fontRenderer.getStringWidth(name) / 2;
    final int LABEL_YPOS = -10;
    fontRenderer.drawString(name, LABEL_XPOS, LABEL_YPOS, Color.cyan.getRGB());
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
          ENCHANTLIST_X + guiLeft, ENCHANTLIST_Y_MIN + guiTop);
    }
    totalCost = 0;

    handleChangedEnchantments(enchantments);
    this.updateEnchantmentLabels(enchantments);
  }

  /**
   * Checks if the level of any of the enchantments has changed.
   * 
   * @return boolean Whether or not a level of an enchantment has changed.
   */
  protected boolean hasLevelChanged()
  {

    for (final GuiEnchanterLabel label : this.enchantmentArray)
      if (label.enchantmentLevel != label.initialLevel)
        return true;

    return false;
  }

  /**
   * Updates a specific GuiEnchantmentLabel to show correct position and level.
   * This will also update the cost.
   * 
   * @param level
   *          The correct level for the label to display.
   * @param label
   *          The label to update.
   */
  private void updateEnchantmentLabel(int level, GuiEnchanterLabel label)
  {

    label.yPos = label.startingYPos;

    if (!label.locked && label.enchantmentLevel > level)
    {

      int cost = this.totalCost + this.container
          .enchantmentCost(label.enchantment, label.enchantmentLevel, level);

      if (!this.container.canPurchase(this.playerInv.player, cost))

        while (label.enchantmentLevel > 0)
        {

          label.dragging = false;
          label.enchantmentLevel--;
          cost = this.totalCost + this.container.enchantmentCost(
              label.enchantment, label.enchantmentLevel, level);

          if (this.container.canPurchase(this.playerInv.player, cost))
            break;
        }

      this.totalCost = cost;
    }

    else if (label.enchantmentLevel < level && !label.locked)
      this.totalCost += this.container.getRebate(label.enchantment,
          label.enchantmentLevel, level);
  }

  /**
   * Updates through all enchantment labels to update their status. For example,
   * locking or unlocking the slider label.
   */
  private void updateEnchantmentLabels()
  {
    for (final GuiEnchanterLabel label : this.enchantmentArray)
      label.locked = false;

    for (final GuiEnchanterLabel mainLabel : this.enchantmentArray)

      if (mainLabel.enchantmentLevel != 0)
      {
        for (final GuiEnchanterLabel otherLabel : this.enchantmentArray)
          if (mainLabel != otherLabel
              && !EnchantHelper.isEnchantmentsCompatible(mainLabel.enchantment,
                  otherLabel.enchantment))
            otherLabel.locked = true;
      }

    // else if (!this.playerInv.player.capabilities.isCreativeMode)
    // {
    // && ConfigurationHandler.maxEnchantmentAmount > 0
    // && labelIndex >= ConfigurationHandler.maxEnchantmentAmount)
    // mainLabel.locked = true;
    // }
  }

  private void updateEnchantmentLabels(Map<Enchantment, Integer> enchantments)
  {

    if (!this.enchantmentArray.isEmpty() && this.hasLevelChanged())
    {

      for (final GuiEnchanterLabel label : this.enchantmentArray)
        if (label != this.last)
          this.updateEnchantmentLabel(enchantments.get(label.enchantment),
              label);

      if (this.last != null)
        this.updateEnchantmentLabel(enchantments.get(this.last.enchantment),
            this.last);
    }
  }

  protected boolean levelChanged()
  {

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (item.enchantmentLevel != item.enchantmentLevel)
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
    }
    // else if (!levelChanged())
    // {
    // totalCost += container.repairCostMax();
    //
    // for (final GuiEnchanterLabel item : enchantmentArray)
    // {
    // item.yPos = item.startingYPos;
    // }
    // }
  }

  private void handleChangedEnchantment(Map<Enchantment, Integer> enchantments,
      GuiEnchanterLabel item)
  {

    item.yPos = item.startingYPos;

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
     || x > guiLeft + ENCHANTLIST_X + 2*GuiEnchanterLabel.WIDTH + 5
     || y < guiTop + ENCHANTLIST_Y_MIN
     || y > guiTop + ENCHANTLIST_Y_MAX)
    {
      return null;
    }

    for (final GuiEnchanterLabel item : enchantmentArray)
    {
      if (!item.show)
      {
        continue;
      }
      if (y >= item.yPos && y <= item.yPos + GuiEnchanterLabel.HEIGHT
       && x >= item.xPos && x <= item.xPos + GuiEnchanterLabel.WIDTH)
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
    boolean j = false;
    int xPos, yPos;
    for (Enchantment obj : map.keySet())
    {
      xPos = j?(x+GuiEnchanterLabel.WIDTH + 4):x;
      yPos = y + i * GuiEnchanterLabel.HEIGHT;
      temp.add(new GuiEnchanterLabel(container, obj, map.get(obj), xPos, yPos));
      if (j) ++i;
      j = !j;
    }

    return temp;
  }
}
