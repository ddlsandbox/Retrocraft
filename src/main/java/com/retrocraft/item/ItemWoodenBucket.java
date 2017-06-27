package com.retrocraft.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.retrocraft.FluidHandler;
import com.retrocraft.RetroCraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemWoodenBucket extends ItemFluidContainer
    implements ItemModelProvider
{

  private static final int AMOUNT = 1000;
  private String           name;

  public ItemWoodenBucket(String name)
  {
    super(AMOUNT);

    this.setCreativeTab(CreativeTabs.MISC);
    this.setHasSubtypes(true);
    this.setMaxStackSize(1);
    this.setRegistryName(name);
    this.setUnlocalizedName(name);
    this.name = name;

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void registerItemModel(Item item)
  {
    RetroCraft.proxy.registerItemRenderer(this, 0, name);

    /* register subitems */
    for (int i = 0; i < FluidHandler.NAMES.length; i++)
    {
      RetroCraft.proxy.registerItemRenderer(this, i + 1,
          name + "_" + FluidHandler.NAMES[i]);
    }
  }

  @Override
  public ItemWoodenBucket setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack)
  {
    if (stack.getItemDamage() > 0
        && stack.getItemDamage() <= FluidHandler.NAMES.length)
    {
      return getUnlocalizedName() + "_"
          + FluidHandler.NAMES[stack.getItemDamage() - 1];
    } else
    {
      return getUnlocalizedName();
    }
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab,
      NonNullList<ItemStack> subItems)
  {
    for (int i = 0; i <= FluidHandler.NAMES.length; i++)
    {
      subItems.add(new ItemStack(ModItems.woodenBucket, 1, i));
    }
  }

  @SubscribeEvent
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    if (event.getWorld().isRemote)
      return;

    EnumHand hand = null;
    if (event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND) != null
        && event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND)
            .getItem() == ModItems.woodenBucket)
    {
      hand = EnumHand.MAIN_HAND;
    } else if (event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND) != null
        && event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND)
            .getItem() == ModItems.woodenBucket)
    {
      hand = EnumHand.OFF_HAND;
    }
    if (hand == null)
      return;

    TileEntity te = event.getWorld().getTileEntity(event.getPos());
    if (te != null && te instanceof IFluidHandler)
    {
      if (event.getEntityPlayer().getHeldItem(hand)
          .getItemDamage() == 0) /* Empty */
      {
        IFluidHandler tank = (IFluidHandler) te;
        FluidStack stack = tank.drain(AMOUNT, false);
        if (stack != null && stack.amount == AMOUNT)
        {
          for (int i = 0; i < FluidHandler.FLUIDS.length; i++)
          {
            if (stack.getFluid() == FluidHandler.FLUIDS[i])
            {
              ItemStack item = event.getEntityPlayer().getHeldItem(hand);
              item.setItemDamage(i + 1);
              tank.drain(AMOUNT, true);
              event.getEntityPlayer().setHeldItem(hand, item);
              if (event.isCancelable())
              {
                event.setCanceled(true);
              }
              return;
            }
          }
        }
      } else /* Filled */
      {
        IFluidHandler tank = (IFluidHandler) te;
        FluidStack fluid = new FluidStack(FluidHandler.FLUIDS[event
            .getEntityPlayer().getHeldItem(hand).getItemDamage() - 1], AMOUNT);
        if (tank.fill(fluid, false) == AMOUNT)
        {
          ItemStack item = event.getEntityPlayer().getHeldItem(hand);
          if (FluidHandler.DESTROY_BUCKET[item.getItemDamage() - 1])
          {
            item.setCount(0); /* set stackSize to 0 */
          } else
          {
            item.setItemDamage(0);
          }
          tank.fill(fluid, true);
          event.getEntityPlayer().setHeldItem(hand, item);
          if (event.isCancelable())
          {
            event.setCanceled(true);
          }
        }
      }
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn,
      EntityPlayer playerIn, EnumHand hand)
  {
    ItemStack stack = playerIn.getHeldItem(hand);
    if (!worldIn.isRemote && stack != null)
    {
      RayTraceResult rtr = this.rayTrace(worldIn, playerIn,
          stack.getItemDamage() == 0);

      if (rtr != null && rtr.typeOfHit == RayTraceResult.Type.BLOCK)
      {
        BlockPos pos = rtr.getBlockPos();
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof IFluidHandler)
        {
          /* gets handled by the SubscribeEvent */
          return ActionResult.newResult(EnumActionResult.FAIL, stack);
        }

        if (stack.getItemDamage() == 0) /* Empty */
        {
          Block block = worldIn.getBlockState(pos).getBlock();
          for (int i = 0; i < FluidHandler.BLOCKS.length; i++)
          {
            if (block.equals(FluidHandler.BLOCKS[i]))
            {
              stack.setItemDamage(i + 1);
              worldIn.setBlockToAir(pos);
              break;
            }
          }
        } else /* Filled */
        {
          Block block = FluidHandler.BLOCKS[stack.getItemDamage() - 1];
          pos = pos.offset(rtr.sideHit);
          if (block.isReplaceable(worldIn, pos))
          {
            if (block == Blocks.WATER)
            {
              block = Blocks.FLOWING_WATER;
            } else if (block == Blocks.LAVA)
            {
              block = Blocks.FLOWING_LAVA;
            }
            worldIn.setBlockState(pos, block.getDefaultState(), 3);

            if (FluidHandler.DESTROY_BUCKET[stack.getItemDamage() - 1])
            {
              stack.setCount(0);// func_190918_g(stack.func_190916_E()); /* set
                                // stackSize to 0 */
            } else
            {
              stack.setItemDamage(0);
            }
            // }
          }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
      }
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
  }

  @Override
  public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack,
      @Nullable NBTTagCompound nbt)
  {
    return new FluidHandler(stack, AMOUNT);
  }
}
