package com.retrocraft.block.repairer;

import javax.annotation.Nullable;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.block.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * User: brandon3055
 * Date: 06/01/2015
 *
 * BlockInventoryAdvanced is an advanced furnace with 5 input, 4 output and 4 fuel slots that smelts at twice the speed
 * of a regular furnace. The block itself doesn't do much more then any regular block except create a tile entity when
 * placed, open a gui when right clicked and drop tne inventory's contents when harvested. Everything else is handled
 * by the tile entity.
 *
 * The block model will change appearance depending on how many fuel slots are burning.
 * The amount of "block light" produced by the furnace will also depending on how many fuel slots are burning.
 *
 * //Note that in 1.10.*, extending BlockContainer can cause rendering problems if you don't extend getRenderType()
 // If you don't want to extend BlockContainer, make sure to add the tile entity manually,
 //   using hasTileEntity() and createTileEntity().  See BlockContainer for a couple of other important methods you may
 //  need to implement.
 */
public class BlockRepairer extends BlockTileEntity<TileRepairer>
{
	public BlockRepairer(String name)
	{
		super(Material.ROCK, name);
		this.setCreativeTab(RetroCraft.creativeTab);
	}
	
	@Override
	public BlockRepairer setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	@Override
	public Class<TileRepairer> getTileEntityClass() {
		return TileRepairer.class;
	}

	@Nullable
	@Override
	public TileRepairer createTileEntity(World world, IBlockState state) {
		return new TileRepairer();
	}

	// Called when the block is right clicked
	// In this block it is used to open the blocks gui when right clicked by a player
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
																	EnumFacing side, float hitX, float hitY, float hitZ) {
		// Uses the gui handler registered to your mod to open the gui for the given gui id
		// open on the server side only  (not sure why you shouldn't open client side too... vanilla doesn't, so we better not either)
		if (worldIn.isRemote) return true;

		playerIn.openGui( 
				RetroCraft.instance,
				ModGuiHandler.REPAIRER,
				worldIn,
				pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	// This is where you can do something when the block is broken. In this case drop the inventory's contents
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileEntity);
		}

//		if (inventory != null){
//			// For each slot in the inventory
//			for (int i = 0; i < inventory.getSizeInventory(); i++){
//				// If the slot is not empty
//				if (inventory.getStackInSlot(i) != null)
//				{
//					// Create a new entity item with the item stack in the slot
//					EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory.getStackInSlot(i));
//
//					// Apply some random motion to the item
//					float multiplier = 0.1f;
//					float motionX = worldIn.rand.nextFloat() - 0.5f;
//					float motionY = worldIn.rand.nextFloat() - 0.5f;
//					float motionZ = worldIn.rand.nextFloat() - 0.5f;
//
//					item.motionX = motionX * multiplier;
//					item.motionY = motionY * multiplier;
//					item.motionZ = motionZ * multiplier;
//
//					// Spawn the item in the world
//					worldIn.spawnEntityInWorld(item);
//				}
//			}
//
//			// Clear the inventory so nothing else (such as another mod) can do anything with the items
//			inventory.clear();
//		}

		// Super MUST be called last because it removes the tile entity
		super.breakBlock(worldIn, pos, state);
	}

  @Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		int lightValue = 1;
		return lightValue;
	}

	// the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState iBlockState) {
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks, also by
	// (eg) wall or fence to control whether the fence joins itself to this block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	@Deprecated
	public boolean isFullCube(IBlockState iBlockState) {
		return false;
	}

	// render using a BakedModel
  // required because the default (super method) is INVISIBLE for BlockContainers.
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}
}
