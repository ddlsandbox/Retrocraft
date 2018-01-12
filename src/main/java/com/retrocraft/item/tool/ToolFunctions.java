package com.retrocraft.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class ToolFunctions
{

  public static boolean isEffective(Material material, Material[] materials)
  {
    for (Material m : materials)
      if (m == material)
        return true;
    return false;
  }

  public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3, double range)
  {
    float angel = 0.017453292F;
    float f = 1.0F;
    float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
    float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
    double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
    double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
    if (!world.isRemote && player instanceof EntityPlayer)
      d1 += 1.62D;
    double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
    Vec3d vec3 = new Vec3d(d0, d1, d2);
    float f3 = MathHelper.cos(-f2 * angel - (float) Math.PI);
    float f4 = MathHelper.sin(-f2 * angel - (float) Math.PI);
    float f5 = -MathHelper.cos(-f1 * angel);
    float f6 = MathHelper.sin(-f1 * angel);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    double d3 = range;
    if (player instanceof EntityPlayerMP)
      d3 = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
    Vec3d vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
    return world.rayTraceBlocks(vec3, vec31, par3, !par3, par3);
  }

  protected static void breakExtraBlock(World world, BlockPos blockPos, EnumFacing sidehit, EntityPlayer playerEntity,
      BlockPos refPos)
  {

    if (world.isAirBlock(blockPos))
      return;

    if (!(playerEntity instanceof EntityPlayerMP))
      return;

    EntityPlayerMP player = (EntityPlayerMP) playerEntity;
    IBlockState blockState = world.getBlockState(blockPos);
    Block block = blockState.getBlock();

    IBlockState refBlockState = world.getBlockState(refPos);
    // Block refBlock = world.getBlock(refX, refY, refZ);
    float refStrength = ForgeHooks.blockStrength(refBlockState, player, world, refPos);
    float strength = ForgeHooks.blockStrength(blockState, player, world, blockPos);

    if (!ForgeHooks.canHarvestBlock(block, player, world, blockPos) || refStrength / strength > 10f)
    {
      System.out.println("[RETROCRAFT] Return by strength dif... " + blockPos);
      return;
    }

    int event = ForgeHooks.onBlockBreakEvent(world,
        player.capabilities.isCreativeMode ? GameType.CREATIVE : GameType.SURVIVAL, player, blockPos);
    if (event == -1)
    {
      System.out.println("[RETROCRAFT] Return by event... " + blockPos);
      return;
    }

    if (player.capabilities.isCreativeMode)
    {
      block.onBlockHarvested(world, blockPos, blockState, player);
      if (block.removedByPlayer(blockState, world, blockPos, player, false))
        block.onBlockDestroyedByPlayer(world, blockPos, blockState);
      if (!world.isRemote)
      {
        player.connection.sendPacket(new SPacketBlockChange(world, blockPos));
      }
      return;
    }

    // callback to the tool the player uses. Called on both sides. This
    // damages the tool n stuff.
    player.getHeldItemMainhand().onBlockDestroyed(world, blockState, blockPos, player);
    if (!world.isRemote)
    {
      block.onBlockHarvested(world, blockPos, blockState, player);

      if (block.removedByPlayer(blockState, world, blockPos, player, true))
      {
        System.out.println("[RETROCRAFT] C by Player Destroy " + blockPos);
        block.onBlockDestroyedByPlayer(world, blockPos, blockState);
        block.harvestBlock(world, player, blockPos, blockState, world.getTileEntity(blockPos),
            player.getHeldItemMainhand());
        block.dropXpOnBlockBreak(world, blockPos, event);
      }
      player.connection.sendPacket(new SPacketBlockChange(world, blockPos));
    } else
    {
      world.playBroadcastSound(2001, blockPos, Block.getStateId(blockState));
      if (block.removedByPlayer(blockState, world, blockPos, player, true))
      {
        System.out.println("[RETROCRAFT] S by Player Destroy " + blockPos);
        block.onBlockDestroyedByPlayer(world, blockPos, blockState);
      }
      ItemStack itemstack = player.getHeldItemMainhand();
      if (itemstack != null)
      {
        System.out.println("[RETROCRAFT] Destroy " + blockPos);
        itemstack.onBlockDestroyed(world, blockState, blockPos, player);
        if (itemstack.getCount() == 0)
        {
          ForgeEventFactory.onPlayerDestroyItem(player, itemstack, EnumHand.MAIN_HAND);
          player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }
      }
      NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
      assert netHandlerPlayClient != null;

      netHandlerPlayClient.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos,
          Minecraft.getMinecraft().objectMouseOver.sideHit));
    }
  }
}
