package com.retrocraft.item;

import com.retrocraft.RetroCraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWoodenMilkBucket extends ItemBucketMilk
    implements ItemModelProvider
{
  private String name;

  public ItemWoodenMilkBucket(String name)
  {
    setRegistryName(name);
    setUnlocalizedName(name);
    this.name = name;
  }

  @Override
  public void registerItemModel(Item item)
  {
    RetroCraft.proxy.registerItemRenderer(this, 0, name);
  }

  @Override
  public ItemWoodenMilkBucket setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack,
      World world, EntityLivingBase entity)
  {
    super.onItemUseFinish(stack, world, entity);
    return new ItemStack(ModItems.woodenBucket);
  }
}
