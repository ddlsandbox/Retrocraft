package com.retrocraft.entity.teleportpipe.gui;

import java.util.Iterator;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.retrocraft.RetroCraft;
import com.retrocraft.machine.teleportpipe.TeleportEntry;
import com.retrocraft.network.PacketSortTeleportPipe;
import com.retrocraft.network.PacketTeleportToPipe;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;

public class GuiTeleportPipeList extends GuiScreen
{

  private static final int TP_BUTTONS_HEIGHT = 60;
  
  protected static final int TP_BUTTON_WIDTH = 125;
  private static final int TP_X_OFFSET = -150;
  private static final int TP_X_RIGHTCOL = 150;
  
  private final TeleportEntry[] entries;
  private final EnumHand        hand;
  private final TeleportEntry   fromTeleportPipe;
  private GuiButton             btnPrevPage;
  private GuiButton             btnNextPage;
  private int                   pageOffset;

  public GuiTeleportPipeList(TeleportEntry[] entries, EnumHand hand,
                         @Nullable TeleportEntry fromTeleportPipe)
  {
    this.entries = entries;
    this.hand = hand;
    this.fromTeleportPipe = fromTeleportPipe;
  }

  @Override
  public void initGui()
  {
    btnPrevPage = new GuiButton(0, width / 2 - 150, height / 2 + TP_BUTTONS_HEIGHT, 95, 20,
        I18n.format("gui.retrocraft:warpStone.previousPage"));
    buttonList.add(btnPrevPage);

    btnNextPage = new GuiButton(1, width / 2 + 55, height / 2 + TP_BUTTONS_HEIGHT, 95, 20,
        I18n.format("gui.retrocraft:warpStone.nextPage"));
    buttonList.add(btnNextPage);

    updateList();
  }

  public void updateList()
  {
    final int buttonsPerPage = 10;
    final int columnCount = 2;
    final int rowCount = 5;
    
    btnPrevPage.enabled = pageOffset > 0;
    btnNextPage.enabled = pageOffset < (entries.length - 1) / buttonsPerPage;

    Iterator<GuiButton> it = buttonList.iterator();
    while (it.hasNext())
    {
      GuiButton button = it.next();
      if (button instanceof GuiButtonTeleportPipeEntry
          || button instanceof GuiButtonSortTeleportPipe)
      {
        it.remove();
      }
    }

    int id = 2;
    int y = 0;
    int cur_id = 0;
    for (int j = 0; j < rowCount; ++j)
    {
      int x = 0;
      for (int i = 0; i < columnCount; i++)
      {
        int entryIndex = pageOffset * buttonsPerPage + cur_id;
        if (entryIndex >= 0 && entryIndex < entries.length)
        {
          GuiButtonTeleportPipeEntry btnTeleportPipe = new GuiButtonTeleportPipeEntry(id,
              width / 2 + TP_X_OFFSET + x, 
              height / 2 - 60 + y, 
              entries[entryIndex]);
          buttonList.add(btnTeleportPipe);
          id++;
  
          GuiButtonSortTeleportPipe sortUp = new GuiButtonSortTeleportPipe(id,
              width / 2 + TP_X_OFFSET + TP_BUTTON_WIDTH + 8 + x, 
              height / 2 - 60 + y + 2, 
              btnTeleportPipe, 
              -1);
          if (entryIndex == 0)
          {
            sortUp.visible = false;
          }
          buttonList.add(sortUp);
          id++;
  
          GuiButtonSortTeleportPipe sortDown = new GuiButtonSortTeleportPipe(id,
              width / 2 + TP_X_OFFSET + TP_BUTTON_WIDTH + 8 + x, 
              height / 2 - 60 + y + 11, 
              btnTeleportPipe, 
              1);
          if (entryIndex == entries.length - 1)
          {
            sortDown.visible = false;
          }
          buttonList.add(sortDown);
          id++;
        }
        x += TP_X_RIGHTCOL;
        cur_id++;
      }
      y += 22;
    }
  }

  @Override
  protected void actionPerformed(GuiButton button)
  {
    if (button == btnNextPage)
    {
      pageOffset++;
      updateList();
    } 
    else if (button == btnPrevPage)
    {
      pageOffset--;
      updateList();
    } 
    else if (button instanceof GuiButtonTeleportPipeEntry)
    {
      RetroCraft.network.sendToServer(new PacketTeleportToPipe(
          ((GuiButtonTeleportPipeEntry) button).getTeleportPipe(), hand, fromTeleportPipe));
      mc.displayGuiScreen(null);
    } 
    else if (button instanceof GuiButtonSortTeleportPipe)
    {
      TeleportEntry teleportPipeEntry = ((GuiButtonSortTeleportPipe) button)
          .getTeleportPipe();
      int index = ArrayUtils.indexOf(entries, teleportPipeEntry);
      int sortDir = ((GuiButtonSortTeleportPipe) button).getSortDir();
      int otherIndex = index + sortDir;
      if (index == -1 || otherIndex < 0 || otherIndex >= entries.length)
      {
        return;
      }
      TeleportEntry swap = entries[index];
      entries[index] = entries[otherIndex];
      entries[otherIndex] = swap;
      RetroCraft.network
          .sendToServer(new PacketSortTeleportPipe(index, otherIndex));
      updateList();
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    drawWorldBackground(0);
    super.drawScreen(mouseX, mouseY, partialTicks);
    GL11.glColor4f(1f, 1f, 1f, 1f);
    drawRect(
        width / 2 - 50, 
        height / 2 - 50, 
        width / 2 + 50, 
        height / 2 + 50,
        0xFFFFFF);
    drawCenteredString(fontRenderer,
        I18n.format("gui.retrocraft:warpStone.selectDestination"), 
        width / 2,
        height / 2 - 85, 0xFFFFFF);
    if (entries.length == 0)
    {
      drawCenteredString(fontRenderer,
          TextFormatting.RED + I18n.format("retrocraft:scrollNotBound"),
          width / 2, 
          height / 2 - 20, 
          0xFFFFFF);
    }
  }

}