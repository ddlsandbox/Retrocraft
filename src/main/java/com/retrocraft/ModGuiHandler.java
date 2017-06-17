package com.retrocraft;

import com.retrocraft.entity.waystone.TileWaystone;
import com.retrocraft.entity.waystone.gui.ContainerEditWaystoneNameDummy;
import com.retrocraft.entity.waystone.gui.GuiEditWaystone;
import com.retrocraft.machine.enchanter.ContainerEnchanter;
import com.retrocraft.machine.enchanter.GuiEnchanter;
import com.retrocraft.machine.enchanter.TileEntityEnchanter;
import com.retrocraft.machine.multifurnace.ContainerMultifurnace;
import com.retrocraft.machine.multifurnace.GuiMultifurnace;
import com.retrocraft.machine.multifurnace.TileMultifurnace;
import com.retrocraft.machine.repairer.ContainerRepairer;
import com.retrocraft.machine.repairer.GuiRepairer;
import com.retrocraft.machine.repairer.TileRepairer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
  public static final int ENCHANTER    = 0;
  public static final int REPAIRER     = 1;
  public static final int MULTIFURNACE = 2;
  public static final int WAYSTONE     = 3;

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
    case WAYSTONE:
      return new ContainerEditWaystoneNameDummy();
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
    case WAYSTONE:
      return new GuiEditWaystone(
          (TileWaystone) world.getTileEntity(new BlockPos(x, y, z)));
    default:
      return null;
    }
  }

}
