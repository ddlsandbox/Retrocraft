package com.retrocraft.block.pedestal;

import javax.annotation.Nullable;

import com.retrocraft.block.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockPedestal extends BlockTileEntity<TileEntityPedestal> {

	public BlockPedestal(String name) {
		super(Material.ROCK, name);
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockPedestal setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityPedestal tile = getTileEntity(world, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			if (!player.isSneaking()) {
				if (player.getHeldItem(hand).isEmpty()) {
					player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
				} else {
					player.setHeldItem(hand, itemHandler.insertItem(0, player.getHeldItem(hand), false));
				}
				tile.markDirty();
			} else {
				tile.switchMode();
				player.sendMessage(new TextComponentString("CHANGED MODE TO " + tile.getMode()));
				tile.markDirty();
//				ItemStack stack = itemHandler.getStackInSlot(0);
//				if (!stack.isEmpty()) {
//					String localized = RetroCraft.proxy.localize(stack.getUnlocalizedName() + ".name");
					//player.addChatMessage(new TextComponentString(stack.getCount() + "x " + localized));
//				} 
//				else {
//					player.addChatMessage(new TextComponentString("Empty"));
//				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityPedestal tile = getTileEntity(world, pos);
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
	public Class<TileEntityPedestal> getTileEntityClass() {
		return TileEntityPedestal.class;
	}

	@Nullable
	@Override
	public TileEntityPedestal createTileEntity(World world, IBlockState state) {
		return new TileEntityPedestal();
	}

}