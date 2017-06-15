package com.retrocraft.item.tool;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class ToolStreamAxe extends ItemAxe implements ItemModelProvider {

	private String name;
	public int height;

	public ToolStreamAxe(ToolMaterial material, String name) {
		super(material);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}

	@Override
	public void registerItemModel(Item item) {
		RetroCraft.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public ToolStreamAxe setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);

		return this;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos blockPos, EntityPlayer player) {
		World world = player.world;
		final IBlockState wood = world.getBlockState(blockPos);

		if (wood != null && wood.getMaterial() == Material.WOOD) {
			if (detectTree(world, blockPos, wood.getBlock())) {
				breakTree(world, blockPos, blockPos, wood.getBlock(), player, stack);
				stack.damageItem(height, player);
			} else {
				breakBlock(world, player, blockPos);
				stack.damageItem(1, player);
			}
			return true;
		}

		return super.onBlockStartBreak(stack, blockPos, player);
	}

	private boolean detectTree(World world, BlockPos blockPos, Block wood) {
		height = blockPos.getY();
		boolean foundTop = false;
		do {
			height++;
			IBlockState block = world.getBlockState(new BlockPos(blockPos.getX(), height, blockPos.getZ()));
			if (block.getBlock() != wood) {
				height--;
				foundTop = true;
			}
		} while (!foundTop);

		int numLeaves = 0;
		if (height - blockPos.getY() < 50) {
			for (int xPos = blockPos.getX() - 1; xPos <= blockPos.getX() + 1; xPos++) {
				for (int yPos = height - 1; yPos <= height + 1; yPos++) {
					for (int zPos = blockPos.getZ() - 1; zPos <= blockPos.getZ() + 1; zPos++) {
						IBlockState leaves = world.getBlockState(new BlockPos(xPos, yPos, zPos));
						if (leaves != null && leaves.getMaterial() == Material.LEAVES)
							numLeaves++;
					}
				}
			}
		}
		return numLeaves > 3;
	}

	private void breakBlock(World world, EntityPlayer player, BlockPos blockPos) {
		IBlockState localBlockState = world.getBlockState(blockPos);
		localBlockState.getBlock().harvestBlock(world, player, blockPos, localBlockState, world.getTileEntity(blockPos),
				player.getHeldItemMainhand());
		world.setBlockToAir(blockPos);
	}

	private void breakTree(World world, BlockPos blockPos, BlockPos blockPosStart, Block blockID, EntityPlayer player,
			ItemStack stack) {

		for (int posX = blockPos.getX() - 1; posX <= blockPos.getX() + 1; posX++) {
			for (int posY = blockPos.getY() - 1; posY <= blockPos.getY() + 1; posY++) {
				for (int posZ = blockPos.getZ() - 1; posZ <= blockPos.getZ() + 1; posZ++) {
					BlockPos localPos = new BlockPos(posX, posY, posZ);
					IBlockState localBlockState = world.getBlockState(localPos);
					if (blockID == localBlockState.getBlock()) {

						boolean cancelHarvest = false;

						BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, localBlockState,
								player);
						event.setCanceled(cancelHarvest);
						MinecraftForge.EVENT_BUS.post(event);
						cancelHarvest = event.isCanceled();

						if (cancelHarvest) {
							breakTree(world, blockPos, blockPosStart, blockID, player, stack);
						} else {
							if (!player.capabilities.isCreativeMode) {
								localBlockState.getBlock().harvestBlock(world, player, blockPos, localBlockState,
										world.getTileEntity(blockPos), player.getHeldItemMainhand());
								player.getHeldItemMainhand().onBlockDestroyed(world, localBlockState, localPos, player);
								// onBlockDestroyed(world, localBlockState,
								// blockPos, player);
							}
							world.setBlockToAir(localPos);
							if (!world.isRemote) {
								breakTree(world, localPos, blockPosStart, blockID, player, stack);
							}
						}
					}
				}
			}
		}
	}
}