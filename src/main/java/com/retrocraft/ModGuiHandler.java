package com.retrocraft;

import com.retrocraft.block.TileEntityEnchantorium;
import com.retrocraft.block.enchantorium.ContainerEnchantorium;
import com.retrocraft.block.enchantorium.GuiEnchantorium;

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
			default:
				return null;
		}
	}

}