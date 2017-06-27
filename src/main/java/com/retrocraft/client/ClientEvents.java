package com.retrocraft.client;

import com.retrocraft.machine.GuiEnergyDisplay;
import com.retrocraft.machine.IEnergyDisplay;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEvents
{
  private static GuiEnergyDisplay energyDisplay;

  public ClientEvents(){
      MinecraftForge.EVENT_BUS.register(this);
  }
  
//  @SubscribeEvent
//  public void onClientTick(ClientTickEvent event){
//      if(event.phase == Phase.END){
//          Minecraft mc = Minecraft.getMinecraft();
//
//          if(mc.world == null){
//              WorldData.clear();
//          }
//      }
//  }
  
  @SubscribeEvent
  public void onGameOverlay(RenderGameOverlayEvent.Post event){
      if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && Minecraft.getMinecraft().currentScreen == null){
          Minecraft minecraft = Minecraft.getMinecraft();
          EntityPlayer player = minecraft.player;
          RayTraceResult posHit = minecraft.objectMouseOver;
          FontRenderer font = minecraft.fontRendererObj;
          ItemStack stack = player.getHeldItemMainhand();

//          if(StackUtil.isValid(stack)){
//              if(stack.getItem() instanceof IHudDisplay){
//                  ((IHudDisplay)stack.getItem()).displayHud(minecraft, player, stack, posHit, event.getResolution());
//              }
//          }

          if(posHit != null && posHit.getBlockPos() != null){
              Block blockHit = minecraft.world.getBlockState(posHit.getBlockPos()).getBlock();
              TileEntity tileHit = minecraft.world.getTileEntity(posHit.getBlockPos());

//              if(blockHit instanceof IHudDisplay){
//                  ((IHudDisplay)blockHit).displayHud(minecraft, player, stack, posHit, event.getResolution());
//              }

//              if(tileHit instanceof TileEntityBase){
//                  TileEntityBase base = (TileEntityBase)tileHit;
//                  if(base.isRedstoneToggle()){
//                      String strg = String.format("%s: %s", StringUtil.localize("info."+ModUtil.MOD_ID+".redstoneMode.name"), TextFormatting.DARK_RED+StringUtil.localize("info."+ModUtil.MOD_ID+".redstoneMode."+(base.isPulseMode ? "pulse" : "deactivation"))+TextFormatting.RESET);
//                      font.drawStringWithShadow(strg, event.getResolution().getScaledWidth()/2+5, event.getResolution().getScaledHeight()/2+5, StringUtil.DECIMAL_COLOR_WHITE);
//
//                      String expl;
//                      if(StackUtil.isValid(stack) && stack.getItem() == ConfigValues.itemRedstoneTorchConfigurator){
//                          expl = TextFormatting.GREEN+StringUtil.localize("info."+ModUtil.MOD_ID+".redstoneMode.validItem");
//                      }
//                      else{
//                          expl = TextFormatting.GRAY.toString()+TextFormatting.ITALIC+StringUtil.localizeFormatted("info."+ModUtil.MOD_ID+".redstoneMode.invalidItem", StringUtil.localize(ConfigValues.itemRedstoneTorchConfigurator.getUnlocalizedName()+".name"));
//                      }
//                      font.drawStringWithShadow(expl, event.getResolution().getScaledWidth()/2+5, event.getResolution().getScaledHeight()/2+15, StringUtil.DECIMAL_COLOR_WHITE);
//                  }
//              }

              if(tileHit instanceof IEnergyDisplay){
                  IEnergyDisplay display = (IEnergyDisplay)tileHit;
                  if(!display.needsHoldShift() || player.isSneaking()){
                      if(energyDisplay == null){
                          energyDisplay = new GuiEnergyDisplay(0, 0, null);
                      }
                      energyDisplay.setData(2, event.getResolution().getScaledHeight()-96, display.getEnergyStorage(), true, true);

                      GlStateManager.pushMatrix();
                      GlStateManager.color(1F, 1F, 1F, 1F);
                      energyDisplay.draw();
                      GlStateManager.popMatrix();
                  }
              }
          }
      }
  }
}
