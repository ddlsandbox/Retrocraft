package com.retrocraft.item.weapon;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;
import com.retrocraft.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSword extends net.minecraft.item.ItemSword implements ItemModelProvider
{
  private static final int LEVITATION_MODIFIER = 3; 

  private String name;

  public ItemSword(ToolMaterial material, String name)
  {
    super(material);
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
	 public ItemSword setCreativeTab(CreativeTabs tab) {
  	 super.setCreativeTab(tab);
  	 return this;
	 }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
  {
    ItemStack held = player.getHeldItem(hand);
    if (this == ModItems.octirionSword)
    {
      player.addPotionEffect(
          new PotionEffect(MobEffects.LEVITATION, 5, LEVITATION_MODIFIER, false, false));
      
      if (world.getWorldTime() % 20 == 0)
        RetroCraft.proxy.playSound(SoundEvents.BLOCK_CHORUS_FLOWER_GROW,
            player.getPosition(), 1.0F);
    }
    return ActionResult.newResult(EnumActionResult.FAIL, held);
  }
}