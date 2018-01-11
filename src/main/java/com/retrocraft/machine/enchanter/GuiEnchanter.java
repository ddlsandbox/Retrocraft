package com.retrocraft.machine.enchanter;

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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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

  private double            sliderIndex     = 0;
  private boolean           clicked         = false;
  private boolean           sliding         = false;
  private double            enchantingPages = 0;
  private int               totalCost       = 0;
  private GuiEnchanterLabel last;

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
      item.show(item.yPos >= guiTop + ENCHANTLIST_Z_MIN
          && item.yPos < guiTop + ENCHANTLIST_Z_MAX);
      item.draw(fontRenderer);
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
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {

    super.drawScreen(mouseX, mouseY, partialTicks);
    this.updateEnchantmentLabels();

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

//        final boolean exp = this.totalCost <= 1
//            && this.totalCost >= -1;
        final boolean negExp = this.totalCost < 0;
//        final int finalCost = exp ? this.totalCost
//            : negExp
//                ? -this.totalCost
//                : this.totalCost;
        final int finalCost = negExp
                ? -this.totalCost
                : this.totalCost;

        information.add(this.fontRenderer.listFormattedStringToWidth(
            String.format("%s: %s",
//                exp ? I18n.format("tooltip.enchanter.experienceGained") :
                    I18n.format("tooltip.enchanter.levelneed"),
                finalCost),
            maxWidth));
      }
//    information.add(this.fontRenderer.listFormattedStringToWidth(
//        String.format("%s: %s", I18n.format("tooltip.eplus.maxlevel"),
//            this.container.getEnchantingPower()),
//        maxWidth));

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

    final GuiEnchanterLabel label = this.getSelectedLabel(mouseX, mouseY);

    if (isShiftKeyDown() && label != null && label.enchantment != null)
    {

      final String enchName = ChatFormatting.BOLD + label.getTranslatedName();
      String description = I18n.format("enchantment."
          + label.enchantment.getRegistryName().getResourceDomain() + "."
          + label.enchantment.getRegistryName().getResourcePath() + ".desc");

      if (description.startsWith("enchantment."))
        description = ChatFormatting.RED + I18n.format("tooltip.eplus.nodesc")
            + " " + description;

      else
        description = ChatFormatting.LIGHT_PURPLE + description;

      final List<String> display = new ArrayList<>();

      display.add(enchName);
      display.addAll(
          this.fontRenderer.listFormattedStringToWidth(description, 215));
      display.add(ChatFormatting.BLUE + "" + ChatFormatting.ITALIC
          + RetroCraft.modId);
      this.drawHoveringText(display, mouseX, mouseY, this.fontRenderer);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
  {
    String name = RetroCraft.proxy
        .localize(ModBlocks.blockEnchanter.getUnlocalizedName() + ".name");
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    fontRenderer.drawString(name,
        xSize / 2 - fontRenderer.getStringWidth(name) / 2, /* x */
        2, /* z */
        0x404040);
  }

  
  /**
   * Gets the GuiEnchantmentLabel that the mouse is currently hovering over.
   * 
   * @param mouseX The X position of the mouse.
   * @param mouseY The Y position of the mouse.
   * @return GuiEnchantmentLabel The current label being hovered over.
   */
  private GuiEnchanterLabel getSelectedLabel (int mouseX, int mouseY) {
      
      if (mouseX < this.guiLeft + guiOffset + 35 || mouseX > this.guiLeft + this.xSize - 32)
          return null;
      
      for (final GuiEnchanterLabel label : this.enchantmentArray) {
          
          if (!label.show)
              continue;
          
          if (mouseY >= label.yPos && mouseY <= label.yPos + label.HEIGHT)
              return label;
      }
      
      return null;
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

    label.yPos = label.startingYPos - (int) (18 * 4 * this.sliderIndex);

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

    int labelIndex = 0;

    for (final GuiEnchanterLabel label : this.enchantmentArray)
      label.locked = false;

    for (final GuiEnchanterLabel mainLabel : this.enchantmentArray)

      if (mainLabel.enchantmentLevel != 0)
      {

        labelIndex++;

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
    // item.yPos = item.startingYPos - (int) (18 * 4 * sliderIndex);
    // }
    // }
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
      temp.add(new GuiEnchanterLabel(container, obj, map.get(obj), x, yPos));
      i++;
      yPos = y + i * 18;
    }

    return temp;
  }
}
