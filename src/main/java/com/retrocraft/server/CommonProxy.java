package com.retrocraft.server;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.BlockBase;
import com.retrocraft.block.BlockOre;
import com.retrocraft.block.BlockTorch;
import com.retrocraft.block.ModBlocks;
import com.retrocraft.entity.teleportpipe.BlockTeleportPipe;
import com.retrocraft.entity.teleportpipe.TeleportEntry;
import com.retrocraft.entity.teleportpipe.TileTeleportPipe;
import com.retrocraft.item.ItemOre;
import com.retrocraft.item.ItemWoodenBucket;
import com.retrocraft.item.ItemWoodenMilkBucket;
import com.retrocraft.item.armor.ArmorMaterials;
import com.retrocraft.item.armor.ItemManoliumArmor;
import com.retrocraft.item.tool.ToolEverything;
import com.retrocraft.item.tool.ToolExcavator;
import com.retrocraft.item.tool.ToolHammer;
import com.retrocraft.item.tool.ToolStreamAxe;
import com.retrocraft.item.weapon.ItemSword;
import com.retrocraft.machine.enchanter.BlockEnchanter;
import com.retrocraft.machine.enchanter.TileEntityEnchanter;
import com.retrocraft.machine.generator.BlockSteamGenerator;
import com.retrocraft.machine.generator.TileSteamGenerator;
import com.retrocraft.machine.grinder.BlockOreGrinder;
import com.retrocraft.machine.grinder.TileOreGrinder;
import com.retrocraft.machine.repairer.BlockRepairer;
import com.retrocraft.machine.repairer.TileRepairer;
import com.retrocraft.machine.smelter.BlockSmelter;
import com.retrocraft.machine.smelter.TileSmelter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy
{
  public void preInit(FMLPreInitializationEvent event){
  }

  public void init(FMLInitializationEvent event){
  }

  public void postInit(FMLPostInitializationEvent event){
  }

  public String localize(String unlocalized, Object... args)
  {
    return I18n.translateToLocalFormatted(unlocalized, args);
  }

	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockOre("ore_octirion", "oreOctirion").setCreativeTab(RetroCraft.creativeTab).setLightLevel(0.9f));
		event.getRegistry().register(new BlockOre("ore_manolite", "oreManolite").setCreativeTab(RetroCraft.creativeTab));
      	
		event.getRegistry().register(new BlockBase(Material.IRON, "block_manolium").setCreativeTab(RetroCraft.creativeTab));
		event.getRegistry().register(new BlockBase(Material.IRON, "block_manolazium").setCreativeTab(RetroCraft.creativeTab));
		event.getRegistry().register(new BlockBase(Material.IRON, "block_octirion").setCreativeTab(RetroCraft.creativeTab));
		event.getRegistry().register(new BlockBase(Material.IRON, "block_machinechasis").setCreativeTab(RetroCraft.creativeTab));
		event.getRegistry().register(new BlockBase(Material.IRON, "block_octirionchasis").setCreativeTab(RetroCraft.creativeTab));
		
		event.getRegistry().register(new BlockTorch("lightpillar").setCreativeTab(RetroCraft.creativeTab));
		
		event.getRegistry().register(new BlockTeleportPipe("block_waystone").setCreativeTab(RetroCraft.creativeTab));
		GameRegistry.registerTileEntity(TileTeleportPipe.class, RetroCraft.modId + "_block_waystone");
		
		event.getRegistry().register(new BlockSteamGenerator("block_steamgenerator").setCreativeTab(RetroCraft.creativeTab));
		GameRegistry.registerTileEntity(TileSteamGenerator.class, RetroCraft.modId + "_block_steamgenerator");
		event.getRegistry().register(new BlockOreGrinder("block_oregrinder").setCreativeTab(RetroCraft.creativeTab));
		GameRegistry.registerTileEntity(TileOreGrinder.class, RetroCraft.modId + "_block_oregrinder");
		event.getRegistry().register(new BlockSmelter("block_oresmelter").setCreativeTab(RetroCraft.creativeTab));
		GameRegistry.registerTileEntity(TileSmelter.class, RetroCraft.modId + "_block_oresmelter");
		event.getRegistry().register(new BlockEnchanter("block_enchanter").setCreativeTab(RetroCraft.creativeTab));
		GameRegistry.registerTileEntity(TileEntityEnchanter.class, RetroCraft.modId + "_block_enchanter");
		event.getRegistry().register(new BlockRepairer("block_repairer").setCreativeTab(RetroCraft.creativeTab));
		GameRegistry.registerTileEntity(TileRepairer.class, RetroCraft.modId + "_block_repairer");
    }
    
  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
	  event.getRegistry().register(new ItemOre("dust_manolite", "dustManolite").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemOre("dust_manolazium", "dustManolazium").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemOre("gem_octirion", "gemOctirion").setCreativeTab(RetroCraft.creativeTab));

	  event.getRegistry().register(new ItemOre("ingot_manolium", "ingotManolium").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemOre("ingot_manolazium", "ingotManolazium").setCreativeTab(RetroCraft.creativeTab));

	  event.getRegistry().register(new ItemOre("mechanical_core", "mechanicalCore").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemOre("magical_core", "magicalCore").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemWoodenBucket("wooden_bucket", Blocks.AIR).setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemWoodenMilkBucket("wooden_bucket_milk").setCreativeTab(RetroCraft.creativeTab));
	  
	  if (RetroCraft.getConfig().supportBasicMaterials)
	  {
		  event.getRegistry().register(new ToolExcavator(ToolMaterial.STONE, "excavator_stone").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolExcavator(ToolMaterial.IRON, "excavator_iron").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolExcavator(ToolMaterial.GOLD, "excavator_gold").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolExcavator(ToolMaterial.DIAMOND, "excavator_diamond").setCreativeTab(RetroCraft.creativeTab));
	  }
	  event.getRegistry().register(new ToolExcavator(RetroCraft.manoliumToolMaterial, "excavator_manolium").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ToolExcavator(RetroCraft.manolaziumToolMaterial, "excavator_manolazium").setCreativeTab(RetroCraft.creativeTab));
	  
	  if (RetroCraft.getConfig().supportBasicMaterials)
	  {
		  event.getRegistry().register(new ToolStreamAxe(ToolMaterial.STONE, "streamaxe_stone").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolStreamAxe(ToolMaterial.IRON, "streamaxe_iron").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolStreamAxe(ToolMaterial.GOLD, "streamaxe_gold").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_diamond").setCreativeTab(RetroCraft.creativeTab));
	  }
	  event.getRegistry().register(new ToolStreamAxe(ToolMaterial.DIAMOND, "streamaxe_manolium").setCreativeTab(RetroCraft.creativeTab));

	  if (RetroCraft.getConfig().supportBasicMaterials)
	  {
		  event.getRegistry().register(new ToolHammer(ToolMaterial.STONE, "hammer_stone").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolHammer(ToolMaterial.IRON, "hammer_iron").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolHammer(ToolMaterial.GOLD, "hammer_gold").setCreativeTab(RetroCraft.creativeTab));
		  event.getRegistry().register(new ToolHammer(ToolMaterial.DIAMOND, "hammer_diamond").setCreativeTab(RetroCraft.creativeTab));
	  }
	  event.getRegistry().register(new ToolHammer(RetroCraft.manoliumToolMaterial, "hammer_manolium").setCreativeTab(RetroCraft.creativeTab));

	  event.getRegistry().register(new ToolEverything(RetroCraft.manoliumToolMaterial, "etool_manolium").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ToolEverything(RetroCraft.manolaziumToolMaterial, "etool_manolazium").setCreativeTab(RetroCraft.creativeTab));
	  
	  /* armor */
	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "head_manolium", 0,
				EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "head_manolazium", 0,
				EntityEquipmentSlot.HEAD).setCreativeTab(RetroCraft.creativeTab));

	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "chest_manolium", 0,
				EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "chest_manolazium", 0,
				EntityEquipmentSlot.CHEST).setCreativeTab(RetroCraft.creativeTab));

	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "legs_manolium", 0,
				EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "legs_manolazium", 0,
				EntityEquipmentSlot.LEGS).setCreativeTab(RetroCraft.creativeTab));

	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manoliumArmorMaterial, "feet_manolium", 0,
				EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemManoliumArmor(ArmorMaterials.manolaziumArmorMaterial, "feet_manolazium", 0,
				EntityEquipmentSlot.FEET).setCreativeTab(RetroCraft.creativeTab));
	  
	  /* weapons */
	  event.getRegistry().register(new ItemSword(RetroCraft.manoliumToolMaterial, "sword_manolium").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemSword(RetroCraft.manolaziumToolMaterial, "sword_manolazium").setCreativeTab(RetroCraft.creativeTab));
	  event.getRegistry().register(new ItemSword(RetroCraft.octirionToolMaterial, "sword_octirion").setCreativeTab(RetroCraft.creativeTab));

	  /* blocks */
	  event.getRegistry().register(new ItemBlock(ModBlocks.oreOctirion).setRegistryName(ModBlocks.oreOctirion.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.oreManolite).setRegistryName(ModBlocks.oreManolite.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockManolium).setRegistryName(ModBlocks.blockManolium.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockManolazium).setRegistryName(ModBlocks.blockManolazium.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockOctirion).setRegistryName(ModBlocks.blockOctirion.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockMachineChasis).setRegistryName(ModBlocks.blockMachineChasis.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockOctirionChasis).setRegistryName(ModBlocks.blockOctirionChasis.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockLightPillar).setRegistryName(ModBlocks.blockLightPillar.getRegistryName()));

	  event.getRegistry().register(new ItemBlock(ModBlocks.blockWaystone).setRegistryName(ModBlocks.blockWaystone.getRegistryName()));
//	  event.getRegistry().register(new ItemBlock(ModBlocks.blockGenerator).setRegistryName(ModBlocks.blockGenerator.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockOreGrinder).setRegistryName(ModBlocks.blockOreGrinder.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockOreSmelter).setRegistryName(ModBlocks.blockOreSmelter.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockEnchanter).setRegistryName(ModBlocks.blockEnchanter.getRegistryName()));
	  event.getRegistry().register(new ItemBlock(ModBlocks.blockRepairer).setRegistryName(ModBlocks.blockRepairer.getRegistryName()));
	  
  }
  
  public void registerItemRenderer(Item item, int meta, String id)
  {
  }
  
  public void registerRenderers()
  {
  }

  public void loadModels()
  {

  }

  public void playSound(SoundEvent sound, BlockPos pos, float pitch)
  {

  }

  public void openWaystoneSelection(EnumHand hand,
      @Nullable TeleportEntry fromWaystone)
  {

  }
}
