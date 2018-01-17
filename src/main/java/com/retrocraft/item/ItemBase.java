package com.retrocraft.item;

import com.retrocraft.RetroCraft;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements ItemModelProvider
{

  protected String name;

  public ItemBase(String name)
  {
    this.name = name;
    setUnlocalizedName(name);
    setRegistryName(name);
  }

  @Override
  public void registerItemModel(Item item)
  {
    RetroCraft.proxy.registerItemRenderer(this, 0, name);
  }

  @Override
  public ItemBase setCreativeTab(CreativeTabs tab)
  {
    super.setCreativeTab(tab);
    return this;
  }

  @SideOnly(Side.CLIENT)
  public void initModel() {
      ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }
}