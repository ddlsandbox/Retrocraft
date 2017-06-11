package com.retrocraft.block;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockEnchantorium extends BlockTileEntity<TileEntityEnchantorium> {

	public BlockEnchantorium(String name) {
		super(Material.ROCK, name);
	}

	@Override
	public BlockEnchantorium setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityEnchantorium tile = getTileEntity(world, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			if (!player.isSneaking()) {
				player.openGui(RetroCraft.instance, 
							ModGuiHandler.ENCHANTORIUM, 
							world, 
							pos.getX(), pos.getY(), pos.getZ());
//				if (player.getHeldItem(hand).isEmpty()) {
//					player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
//				} else {
//					player.setHeldItem(hand, itemHandler.insertItem(0, player.getHeldItem(hand), false));
//				}
//				tile.markDirty();
			} 
//			else {
//				ItemStack stack = itemHandler.getStackInSlot(0);
//				if (!stack.isEmpty()) {
//					String localized = RetroCraft.proxy.localize(stack.getUnlocalizedName() + ".name");
//				} 
//			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityEnchantorium tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		//TODO> Drop held object!
		ItemStack stack = itemHandler.getStackInSlot(0);
		if (!stack.isEmpty()) {
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			world.spawnEntity(item);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public Class<TileEntityEnchantorium> getTileEntityClass() {
		return TileEntityEnchantorium.class;
	}

	@Nullable
	@Override
	public TileEntityEnchantorium createTileEntity(World world, IBlockState state) {
		return new TileEntityEnchantorium();
	}
}