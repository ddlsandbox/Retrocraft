package com.retrocraft;

import com.retrocraft.block.TileEntityEnchantorium;
import com.retrocraft.block.enchantorium.ContainerEnchantorium;
import com.retrocraft.block.enchantorium.GuiEnchantorium;
import com.retrocraft.block.multifurnace.ContainerMultifurnace;
import com.retrocraft.block.multifurnace.GuiMultifurnace;
import com.retrocraft.block.multifurnace.TileMultifurnace;
import com.retrocraft.block.enchanter.ContainerEnchanter;
import com.retrocraft.block.enchanter.GuiEnchanter;
import com.retrocraft.block.enchanter.TileEntityEnchanter;

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
public class ModGuiHandler implements IGuiHandler {
	public static final int ENCHANTORIUM = 0;
	public static final int ENCHANTER = 1;
	public static final int MULTIFURNACE = 2;

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		/*
		 * This method returns the appropriate instance (or null, if there is none)
		 * for the given ID, player, world, and position.
		 */
		switch (ID) {
			case ENCHANTORIUM:
				return new ContainerEnchantorium(
						player.inventory,
						(TileEntityEnchantorium)world.getTileEntity(
								new BlockPos(x, y, z)));
			case ENCHANTER:
				return new ContainerEnchanter(
						player.inventory,
						(TileEntityEnchanter)world.getTileEntity(
								new BlockPos(x, y, z)));
			case MULTIFURNACE:
				return new ContainerMultifurnace(
						player.inventory,
						(TileMultifurnace)world.getTileEntity(
								new BlockPos(x, y, z)));
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case ENCHANTORIUM:
				return new GuiEnchantorium(
						getServerGuiElement(ID, player, world, x, y, z),
						player.inventory);
			case ENCHANTER:
				return new GuiEnchanter(
						getServerGuiElement(ID, player, world, x, y, z),
						player.inventory);
			case MULTIFURNACE:
				return new GuiMultifurnace(
						player.inventory,
						(TileMultifurnace)world.getTileEntity(
								new BlockPos(x, y, z)));
			default:
				return null;
		}
	}

}
