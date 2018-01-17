package com.retrocraft.item;

import com.retrocraft.item.armor.ItemManoliumArmor;
import com.retrocraft.item.backpack.ItemBackpack;
import com.retrocraft.item.tool.ToolEverything;
import com.retrocraft.item.tool.ToolExcavator;
import com.retrocraft.item.tool.ToolHammer;
import com.retrocraft.item.tool.ToolStreamAxe;
import com.retrocraft.item.weapon.ItemSword;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

  /* dust */
  
  @GameRegistry.ObjectHolder("retrocraft:dust_manolite")
  public static ItemOre dustManolite;
  @GameRegistry.ObjectHolder("retrocraft:dust_manolazium")
  public static ItemOre dustManolazium;
  @GameRegistry.ObjectHolder("retrocraft:gem_octirion")
  public static ItemOre gemOctirion;

  /* ingot */
  
  @GameRegistry.ObjectHolder("retrocraft:ingot_manolium")
  public static ItemOre ingotManolium;
  @GameRegistry.ObjectHolder("retrocraft:ingot_manolazium")
  public static ItemOre ingotManolazium;

  /* tools */

  @GameRegistry.ObjectHolder("retrocraft:hammer_stone")
  public static ToolHammer stoneHammer;
  @GameRegistry.ObjectHolder("retrocraft:hammer_iron")
  public static ToolHammer ironHammer;
  @GameRegistry.ObjectHolder("retrocraft:hammer_gold")
  public static ToolHammer goldHammer;
  @GameRegistry.ObjectHolder("retrocraft:hammer_diamond")
  public static ToolHammer diamondHammer;
  @GameRegistry.ObjectHolder("retrocraft:hammer_manolium")
  public static ToolHammer manoliumHammer;
  @GameRegistry.ObjectHolder("retrocraft:hammer_manolazium")
  public static ToolHammer manolaziumHammer;

  @GameRegistry.ObjectHolder("retrocraft:excavator_stone")
  public static ToolExcavator stoneExcavator;
  @GameRegistry.ObjectHolder("retrocraft:excavator_iron")
  public static ToolExcavator ironExcavator;
  @GameRegistry.ObjectHolder("retrocraft:excavator_gold")
  public static ToolExcavator goldExcavator;
  @GameRegistry.ObjectHolder("retrocraft:excavator_diamond")
  public static ToolExcavator diamondExcavator;
  @GameRegistry.ObjectHolder("retrocraft:excavator_manolium")
  public static ToolExcavator manoliumExcavator;
  @GameRegistry.ObjectHolder("retrocraft:excavator_manolazium")
  public static ToolExcavator manolaziumExcavator;

  @GameRegistry.ObjectHolder("retrocraft:streamaxe_stone")
  public static ToolStreamAxe stoneStreamAxe;
  @GameRegistry.ObjectHolder("retrocraft:streamaxe_iron")
  public static ToolStreamAxe ironStreamAxe;
  @GameRegistry.ObjectHolder("retrocraft:streamaxe_gold")
  public static ToolStreamAxe goldStreamAxe;
  @GameRegistry.ObjectHolder("retrocraft:streamaxe_diamond")
  public static ToolStreamAxe diamondStreamAxe;
  @GameRegistry.ObjectHolder("retrocraft:streamaxe_manolium")
  public static ToolStreamAxe manoliumStreamAxe;
  @GameRegistry.ObjectHolder("retrocraft:streamaxe_manolazium")
  public static ToolStreamAxe manolaziumStreamAxe;

  @GameRegistry.ObjectHolder("retrocraft:etool_manolium")
  public static ToolEverything manoliumETool;
  @GameRegistry.ObjectHolder("retrocraft:etool_manolazium")
  public static ToolEverything manolaziumETool;

  /* armor */
  
  @GameRegistry.ObjectHolder("retrocraft:head_manolium")
  public static ItemManoliumArmor manoliumHead;
  @GameRegistry.ObjectHolder("retrocraft:head_manolazium")
  public static ItemManoliumArmor manolaziumHead;
  @GameRegistry.ObjectHolder("retrocraft:chest_manolium")
  public static ItemManoliumArmor manoliumChest;
  @GameRegistry.ObjectHolder("retrocraft:chest_manolazium")
  public static ItemManoliumArmor manolaziumChest;
  @GameRegistry.ObjectHolder("retrocraft:legs_manolium")
  public static ItemManoliumArmor manoliumLegs;
  @GameRegistry.ObjectHolder("retrocraft:legs_manolazium")
  public static ItemManoliumArmor manolaziumLegs;
  @GameRegistry.ObjectHolder("retrocraft:feet_manolium")
  public static ItemManoliumArmor manoliumFeet;
  @GameRegistry.ObjectHolder("retrocraft:feet_manolazium")
  public static ItemManoliumArmor manolaziumFeet;

  /* weapons */
  
  @GameRegistry.ObjectHolder("retrocraft:sword_manolium")
  public static ItemSword manoliumSword;
  @GameRegistry.ObjectHolder("retrocraft:sword_manolazium")
  public static ItemSword manolaziumSword;
  @GameRegistry.ObjectHolder("retrocraft:sword_octirion")
  public static ItemSword octirionSword;

  /* misc */
  
  @GameRegistry.ObjectHolder("retrocraft:mechanical_core")
  public static ItemOre mechanicalCore;
  @GameRegistry.ObjectHolder("retrocraft:magical_core")
  public static ItemOre magicalCore;
  @GameRegistry.ObjectHolder("retrocraft:manure")
  public static ItemManure manure;
  @GameRegistry.ObjectHolder("retrocraft:backpack")
  public static ItemBackpack backpack;
  @GameRegistry.ObjectHolder("retrocraft:wooden_bucket")
  public static ItemWoodenBucket woodenBucket;
  @GameRegistry.ObjectHolder("retrocraft:wooden_bucket_water")
  public static ItemWoodenBucket woodenWaterBucket;
  @GameRegistry.ObjectHolder("retrocraft:wooden_bucket_milk")
  public static ItemWoodenMilkBucket woodenMilkBucket;
	
	
	public static void init() {
	}

	@SideOnly(Side.CLIENT)
    public static void initModels() {
		dustManolite.initModel();
		dustManolazium.initModel();

		ingotManolium.initModel();
		ingotManolazium.initModel();
		gemOctirion.initModel();
		
		mechanicalCore.initModel();
		magicalCore.initModel();
		
		manure.initModel();
		
		ModelLoader.setCustomModelResourceLocation(woodenBucket, 0, new ModelResourceLocation(woodenBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(woodenWaterBucket, 0, new ModelResourceLocation(woodenWaterBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(woodenMilkBucket, 0, new ModelResourceLocation(woodenMilkBucket.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(backpack, 0, new ModelResourceLocation(backpack.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(stoneExcavator, 0, new ModelResourceLocation(stoneExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironExcavator, 0, new ModelResourceLocation(ironExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldExcavator, 0, new ModelResourceLocation(goldExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondExcavator, 0, new ModelResourceLocation(diamondExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumExcavator, 0, new ModelResourceLocation(manoliumExcavator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumExcavator, 0, new ModelResourceLocation(manolaziumExcavator.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(stoneStreamAxe, 0, new ModelResourceLocation(stoneStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironStreamAxe, 0, new ModelResourceLocation(ironStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldStreamAxe, 0, new ModelResourceLocation(goldStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondStreamAxe, 0, new ModelResourceLocation(diamondStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumStreamAxe, 0, new ModelResourceLocation(manoliumStreamAxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumStreamAxe, 0, new ModelResourceLocation(manolaziumStreamAxe.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(stoneHammer, 0, new ModelResourceLocation(stoneHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ironHammer, 0, new ModelResourceLocation(ironHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(goldHammer, 0, new ModelResourceLocation(goldHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(diamondHammer, 0, new ModelResourceLocation(diamondHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumHammer, 0, new ModelResourceLocation(manoliumHammer.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumHammer, 0, new ModelResourceLocation(manolaziumHammer.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(manoliumETool, 0, new ModelResourceLocation(manoliumETool.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumETool, 0, new ModelResourceLocation(manolaziumETool.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(manoliumHead, 0, new ModelResourceLocation(manoliumHead.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumHead, 0, new ModelResourceLocation(manolaziumHead.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumChest, 0, new ModelResourceLocation(manoliumChest.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumChest, 0, new ModelResourceLocation(manolaziumChest.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumLegs, 0, new ModelResourceLocation(manoliumLegs.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumLegs, 0, new ModelResourceLocation(manolaziumLegs.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manoliumFeet, 0, new ModelResourceLocation(manoliumFeet.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumFeet, 0, new ModelResourceLocation(manolaziumFeet.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(manoliumSword, 0, new ModelResourceLocation(manoliumSword.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(manolaziumSword, 0, new ModelResourceLocation(manolaziumSword.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(octirionSword, 0, new ModelResourceLocation(octirionSword.getRegistryName(), "inventory"));
    }
}
