package com.retrocraft.block.enchanter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.block.enchantorium.EnchantHelper;

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

public class GuiEnchanter extends GuiContainer {

	private static final int BUTTON_X = 36;
	private static final int BUTTON_Z = 36;

	private static final int ENCHANTLIST_X = 62;
	private static final int ENCHANTLIST_Z_MIN = 18;
	private static final int ENCHANTLIST_Z_MAX = 86;

	private static final int SCROLLBAR_X = 206;
	private static final int SCROLLBAR_WIDTH = 11;

	private static final int guiOffset = 0;

	private InventoryPlayer playerInv;
	private ArrayList<GuiEnchanterLabel> enchantmentArray = new ArrayList<GuiEnchanterLabel>();
	private Map<Integer, Integer> enchantments;

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(RetroCraft.modId, "textures/gui/enchanter.png");
	private ContainerEnchanter container;

	private double sliderIndex = 0;
	private boolean clicked = false;
	private boolean sliding = false;
	private double enchantingPages = 0;
	private int totalCost = 0;

	public GuiEnchanter(Container container, InventoryPlayer playerInv) {
		super(container);

		this.xSize = 235;
		this.ySize = 182;

		this.playerInv = playerInv;
		this.container = (ContainerEnchanter) container;
	}

	 /**
     * Converts map to arraylist of gui items
     *
     * @param map the map of enchantments to convert
     * @param x starting x position
     * @param y starting y position
     * @return the arraylist of gui items
     */
    private ArrayList<GuiEnchanterLabel> convertMapToGuiItems (final Map<Integer, Integer> map, int x, int y) {

        final ArrayList<GuiEnchanterLabel> temp = new ArrayList<GuiEnchanterLabel>();

        if (map == null)
            return temp;

        int i = 0;
        int yPos = y;
        for (Integer obj : map.keySet()) {
//        for (i=0; i<10; i++) {
            temp.add(new GuiEnchanterLabel(container, obj, map.get(obj), x, yPos));
//        	temp.add(new GuiEnchanterLabel(container, i, i, x, yPos));

            i++;
            yPos = y + i * 18;
        }

        return temp;
}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		boolean flag = Mouse.isButtonDown(0);

		/* reset the GL color to solid white, instead of potentially something else.
		 * If we don’t reset it, and the color isn’t already white, our texture would be tinted
		 * with that color.
		 */
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.color(1, 1, 1, 1);

		/* bind the background texture that we’ve specified in our BG_TEXTURE field to Minecraft’s
		 * rendering engine, so that when we render a rectangle with a texture on it, the correct
		 * texture is used.
		 */
		mc.getTextureManager().bindTexture(BG_TEXTURE);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

//		enchantmentArray = convertMapToGuiItems(
//								container.getEnchantments(),
//								ENCHANTLIST_X + guiLeft,
//								ENCHANTLIST_Z_MIN + guiTop);

		for (final GuiEnchanterLabel item : enchantmentArray) {
            if (item.yPos < guiTop + ENCHANTLIST_Z_MIN || item.yPos >= guiTop + ENCHANTLIST_Z_MAX) {
                item.show(false);
            }
            else {
                item.show(true);
            }
            item.draw(fontRendererObj);
		}

		final int adjustedMouseX = mouseX - guiLeft;
        final int adjustedMouseY = mouseY - guiTop;
        //mc.renderEngine.bindTexture(texture);
        int tempY = adjustedMouseY - 16;
        if (tempY <= 0) {
            tempY = 0;
        }
        else if (tempY >= 57) {
            tempY = 57;
        }
        sliderIndex = sliding ? Math.round((tempY / 57D * enchantingPages) / .25) * .25 : sliderIndex;

        if (sliderIndex >= enchantingPages) {
            sliderIndex = enchantingPages;
        }

        double sliderY = sliding ? tempY : 57 * (sliderIndex / enchantingPages);

        drawTexturedModalRect(guiLeft + guiOffset + SCROLLBAR_X, //180
        					  guiTop + 16 + (int) sliderY,
        					  0, 182, 12, 15);

        if (!clicked && flag) {
            for (final GuiEnchanterLabel item : enchantmentArray) {
                if (getItemFromPos(mouseX, mouseY) == item && !item.locked) {
                    item.dragging = true;
                }
            }
            if (adjustedMouseX <= SCROLLBAR_X + SCROLLBAR_WIDTH + guiOffset &&
            	adjustedMouseX >= SCROLLBAR_X + guiOffset) {
                if (enchantingPages != 0) {
                    sliding = true;
                }
            }
        }

        for (final GuiEnchanterLabel item : enchantmentArray) {
            if (item.dragging && getItemFromPos(mouseX, mouseY) != item) {
                item.dragging = false;
            }
        }

        if (!flag) {
            for (final GuiEnchanterLabel item : enchantmentArray) {
                if (getItemFromPos(mouseX, mouseY) == item) {
                    item.dragging = false;
                }
            }
            sliding = false;
        }

        clicked = flag;

        for (final GuiEnchanterLabel item : enchantmentArray) {
            if (item.dragging) {
                item.scroll(adjustedMouseX - ENCHANTLIST_X, //TODO: or 36??
                		    ENCHANTLIST_X + guiLeft + 10);
            }
        }
	}

	private GuiEnchanterLabel getItemFromPos (int x, int y) {

        if (x < guiLeft + ENCHANTLIST_X + 35 || x > guiLeft + xSize - 32) {
            return null;
        }

        for (final GuiEnchanterLabel item : enchantmentArray) {
            if (!item.show) {
                continue;
            }
            if (y >= item.yPos && y <= item.yPos + item.height) {
                return item;
            }
        }
        return null;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = RetroCraft.proxy.localize(ModBlocks.blockEnchanter.getUnlocalizedName() + ".name");
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		fontRenderer.drawString(
				name,
				xSize / 2 - fontRenderer.getStringWidth(name) / 2,	/* x */
				2, 													/* z */
				0x404040);
//		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
	}

    @Override
    public void initGui () {

        super.initGui();
        buttonList.add(new GuiButton(0,
        						guiLeft + BUTTON_X,
        						guiTop + BUTTON_Z,
        						18, 14, "E"));
        buttonList.add(new GuiButton(1,
        						guiLeft + BUTTON_X,
        						guiTop + BUTTON_Z + 20,
        						18, 14, "R"));
//        buttonList.add(new GuiButton(2, guiLeft + xSize + 10, guiTop + 5, fontRendererObj.getStringWidth("Vanilla") + 10, 20, "Vanilla"));

    }

    @Override
    protected void actionPerformed (GuiButton par1GuiButton) {

        final HashMap<Integer, Integer> enchants = new HashMap<Integer, Integer>();
        final HashMap<Integer, Integer> levels = new HashMap<Integer, Integer>();

        for (final GuiEnchanterLabel item : enchantmentArray) {
        	int effectId = Enchantment.getEnchantmentID(item.enchantment);
            final Integer id = enchantments.get(effectId);
            if (item.enchantmentLevel != id && !item.locked) {
                enchants.put(effectId, item.enchantmentLevel);
                levels.put(effectId, item.currentLevel);
            }
        }

        switch (par1GuiButton.id) {
            case 0:
                if (enchants.size() > 0) {
                	playerInv.player.sendMessage(new TextComponentString("Enchanted!"));
                    //RetroCraft.network.sendToServer(new PacketEnchant(enchants, levels, totalCost));
                	try {
                		container.enchant(playerInv.player, enchants, levels, totalCost);
                	} catch (Exception e) {
              			System.err.print("[RETROCRAFT] Error enchanting item");
                	}
                }
                return;
            case 1:
                if (enchants.size() == 0) {
                	playerInv.player.sendMessage(new TextComponentString("Repaired!"));
                    //EnchantingPlus.network.sendToServer(new PacketRepair(totalCost));
                }
                return;
            case 2:
//                EnchantingPlus.network.sendToServer(new PacketGui(player.getDisplayName(), 1, x, y, z));
        }
}

    @Override
    public void updateScreen () {

        super.updateScreen();

        //final Map<Integer, Integer> enchantments = updateEnchantments(container.getEnchantments());

        //handleChangedScreenSize(enchantments);
        enableEnchantments();
        if (container.getEnchantments() != this.enchantments)
        {
        	this.enchantments = container.getEnchantments();
	        enchantmentArray = convertMapToGuiItems(
	        		this.enchantments,
					ENCHANTLIST_X + guiLeft,
					ENCHANTLIST_Z_MIN + guiTop);
        }
        enchantingPages = enchantmentArray.size() / 4.0 > 1 ? enchantmentArray.size() / 4.0 - 1.0 : 0;
        totalCost = 0;

        handleChangedEnchantments(enchantments);
    }

    protected boolean levelChanged () {

        for (final GuiEnchanterLabel item : enchantmentArray) {
            if (item.enchantmentLevel != item.currentLevel) {
                return true;
            }
        }
        return false;
	}

    private void handleChangedEnchantments (Map<Integer, Integer> enchantments) {

        if (!enchantmentArray.isEmpty() && levelChanged()) {
            for (final GuiEnchanterLabel item : enchantmentArray) {
                handleChangedEnchantment(enchantments, item);
            }
        }
        else if (!levelChanged()) {
            totalCost += container.repairCostMax();

            for (final GuiEnchanterLabel item : enchantmentArray) {
                item.yPos = item.startingYPos - (int) (18 * 4 * sliderIndex);
            }
        }
    }

    private void handleChangedEnchantment (Map<Integer, Integer> enchantments, GuiEnchanterLabel item) {

        item.yPos = item.startingYPos - (int) (18 * 4 * sliderIndex);

        final Integer level = enchantments.get(Enchantment.getEnchantmentID(item.enchantment));
        if (!item.locked && item.enchantmentLevel > level) {
            int temp = totalCost + container.enchantmentCost(item.enchantment, item.enchantmentLevel, level);

            if (!container.canPurchase(playerInv.player, temp)) {
                while (item.enchantmentLevel > 0) {
                    item.dragging = false;
                    item.enchantmentLevel--;
                    temp = totalCost + container.enchantmentCost(item.enchantment, item.enchantmentLevel, level);
                    if (container.canPurchase(playerInv.player, temp)) {
                        break;
                    }

                }
            }
            totalCost = temp;
        }
        else if (item.enchantmentLevel < level && !item.locked) {
            if (EnchantHelper.containsEnchantment(
            		container.getInventory().get(0).getTagCompound().getTagList("restrictions", 10),
            		Enchantment.getEnchantmentID(item.enchantment),
            		item.enchantmentLevel)) {
                totalCost += container.disenchantmentCost(item.enchantment, item.enchantmentLevel, level);
            }
            else {
                totalCost = 0;
            }
        }
    }

    private void enableEnchantments () {

        for (GuiEnchanterLabel item : enchantmentArray) {
            item.locked = false;
        }

        for (GuiEnchanterLabel item : enchantmentArray) {
            if (item.enchantmentLevel != 0) {
                for (final GuiEnchanterLabel item2 : enchantmentArray) {
                    if (item == item2)
                        continue;

                    if (!EnchantHelper.isEnchantmentsCompatible(item.enchantment, item2.enchantment)) {
                        item2.locked = true;
                    }
                }
            }
        }
	}

}
