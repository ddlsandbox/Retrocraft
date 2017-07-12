package com.retrocraft.machine.enchanter;

import java.util.HashMap;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityEnchanter extends TileEntity implements ITickable {

	private static Random rand = new Random();

	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float flipRandom;
	public float flipTurn;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation;
	public float bookRotationPrev;
	public float offset;

	@Override
	public void update() {
		this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation;
		final EntityPlayer entityplayer = world.getClosestPlayer(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F,
				this.pos.getZ() + 0.5F, 3.0D, false);

		if (entityplayer != null) {
			final double d0 = entityplayer.posX - (this.pos.getX() + 0.5F);
			final double d1 = entityplayer.posZ - (this.pos.getZ() + 0.5F);
			this.offset = (float) MathHelper.atan2(d1, d0);
			this.bookSpread += 0.1F;

			if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				final float f1 = this.flipRandom;

				while (true) {
					this.flipRandom += rand.nextInt(4) - rand.nextInt(4);

					if (f1 != this.flipRandom)
						break;
				}
			}
		} else {
			this.offset += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while (this.bookRotation >= (float) Math.PI)
			this.bookRotation -= (float) Math.PI * 2F;

		while (this.bookRotation < -(float) Math.PI)
			this.bookRotation += (float) Math.PI * 2F;

		while (this.offset >= (float) Math.PI)
			this.offset -= (float) Math.PI * 2F;

		while (this.offset < -(float) Math.PI)
			this.offset += (float) Math.PI * 2F;

		float f2;

		for (f2 = this.offset - this.bookRotation; f2 >= (float) Math.PI; f2 -= (float) Math.PI * 2F)
			;

		while (f2 < -(float) Math.PI)
			f2 += (float) Math.PI * 2F;

		this.bookRotation += f2 * 0.4F;
		this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f = (this.flipRandom - this.pageFlip) * 0.4F;
		final float f3 = 0.2F;
		f = MathHelper.clamp(f, -f3, f3);
		this.flipTurn += (f - this.flipTurn) * 0.9F;
		this.pageFlip += this.flipTurn;
	}

	public ItemStackHandler inventory = new ItemStackHandler(1);

	/* Capability Interface */
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
  @Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory
				: super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList dataForAllSlots = new NBTTagList();
		NBTTagCompound dataForThisSlot = new NBTTagCompound();
		dataForThisSlot.setByte("Slot", (byte) 0);
		inventory.getStackInSlot(0).writeToNBT(dataForThisSlot);
		dataForAllSlots.appendTag(dataForThisSlot);
		compound.setTag("Items", dataForAllSlots);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		final byte NBT_TYPE_COMPOUND = 10; // See NBTBase.createNewByType() for a listing
		NBTTagList dataForAllSlots = compound.getTagList("Items", NBT_TYPE_COMPOUND);
		inventory.setStackInSlot(0, ItemStack.EMPTY); // set slot to empty
		NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(0);
		inventory.setStackInSlot(0, new ItemStack(dataForOneSlot));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		final int MAXIMUM_DISTANCE_IN_BLOCKS = 20;
		return MAXIMUM_DISTANCE_IN_BLOCKS * MAXIMUM_DISTANCE_IN_BLOCKS;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
	}

	public final void sendUpdate() {
		if (this.world != null && !this.world.isRemote) {
			NBTTagCompound compound = new NBTTagCompound();
			// this.writeSyncableNBT(compound, NBTType.SYNC);
			this.writeToNBT(compound);

			NBTTagCompound data = new NBTTagCompound();
			data.setTag("Data", compound);
			data.setInteger("X", this.pos.getX());
			data.setInteger("Y", this.pos.getY());
			data.setInteger("Z", this.pos.getZ());
		}
	}

	public void enchantCurrent(HashMap<Enchantment, Integer> map) {
		ItemStack itemStack = inventory.getStackInSlot(0);
		EnchantmentHelper.setEnchantments(map, itemStack);

		markDirty();
	}

	// // When the world loads from disk, the server needs to send the
	// TileEntity information to the client
	// // it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and
	// handleUpdateTag() to do this
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
		final int METADATA = 0;
		return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
		handleUpdateTag(updateTagDescribingTileEntityState);
	}

	/*
	 * Creates a tag containing the TileEntity information, used by vanilla to
	 * transmit from server to client Warning - although our getUpdatePacket()
	 * uses this method, vanilla also calls it directly, so don't remove it.
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return nbtTagCompound;
	}

	/*
	 * Populates this TileEntity with information from the tag, used by vanilla
	 * to transmit from server to client Warning - although our onDataPacket()
	 * uses this method, vanilla also calls it directly, so don't remove it.
	 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

}
