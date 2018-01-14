package com.retrocraft;

import com.retrocraft.entity.teleportpipe.TileTeleportPipe;
import com.retrocraft.entity.teleportpipe.gui.ContainerEditTeleportNameDummy;
import com.retrocraft.entity.teleportpipe.gui.GuiEditTeleportPipe;
import com.retrocraft.item.backpack.ContainerBackpack;
import com.retrocraft.item.backpack.GuiBackpack;
import com.retrocraft.machine.enchanter.ContainerEnchanter;
import com.retrocraft.machine.enchanter.GuiEnchanter;
import com.retrocraft.machine.enchanter.TileEntityEnchanter;
import com.retrocraft.machine.generator.ContainerSteamGenerator;
import com.retrocraft.machine.generator.GuiSteamGenerator;
import com.retrocraft.machine.generator.TileSteamGenerator;
import com.retrocraft.machine.grinder.ContainerOreGrinder;
import com.retrocraft.machine.grinder.GuiOreGrinder;
import com.retrocraft.machine.grinder.TileOreGrinder;
import com.retrocraft.machine.multifurnace.ContainerMultifurnace;
import com.retrocraft.machine.multifurnace.GuiMultifurnace;
import com.retrocraft.machine.multifurnace.TileMultifurnace;
import com.retrocraft.machine.repairer.ContainerRepairer;
import com.retrocraft.machine.repairer.GuiRepairer;
import com.retrocraft.machine.repairer.TileRepairer;
import com.retrocraft.machine.smelter.ContainerSmelter;
import com.retrocraft.machine.smelter.GuiSmelter;
import com.retrocraft.machine.smelter.TileSmelter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/*
 * This class has methods which are called by Forge that will be responsible
 * for creating the correct instances of our GUI and container classes from
 * some pieces of information.
 */
public class ModGuiHandler implements IGuiHandler
{
  public static final int ENCHANTER      = 0;
  public static final int REPAIRER       = 1;
  public static final int MULTIFURNACE   = 2;
  public static final int STEAMGENERATOR = 3;
  public static final int OREGRINDER     = 4;
  public static final int ORESMELTER     = 5;
  public static final int ELECTRIC_FORGE = 6;
  public static final int ADVANCED_FORGE = 7;
  public static final int TELEPORT       = 8;
  public static final int BACKPACK       = 9;

  @Override
  public Container getServerGuiElement(int ID, EntityPlayer player, World world,
      int x, int y, int z)
  {
    /*
     * This method returns the appropriate instance (or null, if there is none)
     * for the given ID, player, world, and position.
     */
    switch (ID)
    {
    case ENCHANTER:
      return new ContainerEnchanter(player.inventory,
          (TileEntityEnchanter) world.getTileEntity(new BlockPos(x, y, z)));
    case MULTIFURNACE:
      return new ContainerMultifurnace(player.inventory,
          (TileMultifurnace) world.getTileEntity(new BlockPos(x, y, z)));
    case REPAIRER:
      return new ContainerRepairer(player.inventory,
          (TileRepairer) world.getTileEntity(new BlockPos(x, y, z)));
    case STEAMGENERATOR:
      return new ContainerSteamGenerator(player.inventory,
          (TileSteamGenerator) world.getTileEntity(new BlockPos(x, y, z)));
    case OREGRINDER:
      return new ContainerOreGrinder(player.inventory,
          (TileOreGrinder) world.getTileEntity(new BlockPos(x, y, z)));
    case ORESMELTER:
      return new ContainerSmelter(player.inventory,
          (TileSmelter) world.getTileEntity(new BlockPos(x, y, z)));
//    case ELECTRIC_FORGE:
//      return new ContainerElectricForge(player);
//    case ADVANCED_FORGE:
//      return new ContainerAdvancedForge(player);
    case TELEPORT:
      return new ContainerEditTeleportNameDummy();
    case BACKPACK:
      EnumHand hand = x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
      ItemStack stack = player.getHeldItem(hand); //ToDo: Helper method (not necessarily held item)
      return new ContainerBackpack(stack, player.inventory, hand);
    default:
      return null;
    }
  }
  

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world,
      int x, int y, int z)
  {
    switch (ID)
    {
    case ENCHANTER:
      return new GuiEnchanter(getServerGuiElement(ID, player, world, x, y, z),
          player.inventory);
    case MULTIFURNACE:
      return new GuiMultifurnace(player.inventory,
          (TileMultifurnace) world.getTileEntity(new BlockPos(x, y, z)));
    case REPAIRER:
      return new GuiRepairer(player.inventory,
          (TileRepairer) world.getTileEntity(new BlockPos(x, y, z)),
          new ContainerRepairer(player.inventory,
              (TileRepairer) world.getTileEntity(new BlockPos(x, y, z))));
    case STEAMGENERATOR:
      return new GuiSteamGenerator(player.inventory,
          (TileSteamGenerator) world.getTileEntity(new BlockPos(x, y, z)),
          new ContainerSteamGenerator(player.inventory,
              (TileSteamGenerator) world.getTileEntity(new BlockPos(x, y, z))));
    case OREGRINDER:
      return new GuiOreGrinder(player.inventory,
          (TileOreGrinder) world.getTileEntity(new BlockPos(x, y, z)),
          new ContainerOreGrinder(player.inventory,
              (TileOreGrinder) world.getTileEntity(new BlockPos(x, y, z))));
    case ORESMELTER:
      return new GuiSmelter(player.inventory,
          (TileSmelter) world.getTileEntity(new BlockPos(x, y, z)),
          new ContainerSmelter(player.inventory,
              (TileSmelter) world.getTileEntity(new BlockPos(x, y, z))));
//    case ELECTRIC_FORGE:
//      return new GuiElectricForge(player);
//    case ADVANCED_FORGE:
//      return new GuiAdvancedForge(player);
    case TELEPORT:
      return new GuiEditTeleportPipe(
          (TileTeleportPipe) world.getTileEntity(new BlockPos(x, y, z)));
    case BACKPACK:
      EnumHand hand = x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
      ItemStack stack = player.getHeldItem(hand); //ToDo: Helper method (not necessarily held item)
      return new GuiBackpack(new ContainerBackpack(stack, player.inventory, hand));
    default:
      return null;
    }
  }

}
