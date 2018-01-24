package com.retrocraft.item.replacer;

import java.util.HashSet;

import com.retrocraft.ModGuiHandler;
import com.retrocraft.RetroCraft;
import com.retrocraft.item.RetrocraftMaterials;
import com.retrocraft.util.StackUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolReplacer extends ItemTool
{
  private int maxDamage = 200;
  
  public ToolReplacer(String name)
  {
    super(RetrocraftMaterials.manoliumToolMaterial, new HashSet<Block>());

    setUnlocalizedName(name);
    setRegistryName(name);
    
    this.setMaxStackSize(1);
    this.setMaxDamage(maxDamage);
  }
  
  @SideOnly(Side.CLIENT)
  public void initModel() {
      ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }

  @Override
  public ToolReplacer setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }
  
  // Without this method, your inventory will NOT work!!!
  @Override
  public int getMaxItemUseDuration(ItemStack stack)
  {
    return 1; // return any value greater than zero
  }


  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9,
      float par10)
  {
    if (!player.isSneaking())
    {
      ItemStack heldStack = player.getHeldItem(hand);
      int currentDamage = heldStack.getItemDamage();
      
      ContainerReplacer container = new ContainerReplacer(heldStack, player.inventory);
      ItemStack contained = container.getSlot(0).getStack(); 
            
      if (currentDamage >= maxDamage || contained.isEmpty())
        return EnumActionResult.FAIL;
      
      IBlockState newState = Block.getBlockFromItem(contained.getItem()).getDefaultState();
      
      IBlockState localBlockState = world.getBlockState(pos);
      localBlockState.getBlock().harvestBlock(world, player, pos, localBlockState, world.getTileEntity(pos),
          player.getHeldItemMainhand());
      
      world.setBlockState(pos, newState);
      
      if (!player.isCreative())
      {
        contained.setCount(contained.getCount() - 1);
  
        if (contained.getCount() > 0)
          container.putStackInSlot(0, contained);
        else
          container.putStackInSlot(0, StackUtil.getNull());
        container.onContainerClosed(player);
      }
      
      heldStack.setItemDamage(currentDamage + 1);
      return EnumActionResult.SUCCESS;
    }
    return EnumActionResult.FAIL;
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
  {
    ItemStack held = player.getHeldItem(hand);
    if (player.isSneaking())
    {
      if (!world.isRemote)
      {
        player.openGui(RetroCraft.instance, ModGuiHandler.REPLACER, world, hand == EnumHand.OFF_HAND ? 1 : 0, 0, 0);
      }
    }

    return ActionResult.newResult(EnumActionResult.SUCCESS, held);
  }
}
