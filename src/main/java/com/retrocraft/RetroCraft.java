/*
 ** 2012 August 13
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.retrocraft;

import com.retrocraft.block.ModBlocks;
import com.retrocraft.entity.ModEntities;
import com.retrocraft.item.ModItems;
import com.retrocraft.item.armor.ArmorMaterials;
import com.retrocraft.network.PacketHandler;
import com.retrocraft.recipe.RetrocraftRecipes;
import com.retrocraft.server.CommonProxy;
import com.retrocraft.tab.RetroCraftCreativeTab;
import com.retrocraft.world.ModWorldGen;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = RetroCraft.modId, name = RetroCraft.name, version = RetroCraft.version, acceptedMinecraftVersions = "[1.12.2]")
public class RetroCraft
{

  public static final Item.ToolMaterial manoliumToolMaterial   = EnumHelper
      .addToolMaterial("MANOLIUM", 2,                                         /* harvest level */
          500,                                                                /* durability */
          8f,                                                                 /* efficiency */
          4f,                                                                 /* damage */
          20);                                                                /* enchantability */
  
  public static final Item.ToolMaterial manolaziumToolMaterial = EnumHelper
      .addToolMaterial("MANOLAZIUM", 3,                                       /* harvest level */
          1500,                                                               /* durability */
          10f,                                                                /* efficiency */
          8f,                                                                 /* damage */
          30);                                                                /* enchantability */
  
  public static final Item.ToolMaterial octirionToolMaterial = EnumHelper
	      .addToolMaterial("OCTIRION", 4,                                         /* harvest level */
	          2000,                                                               /* durability */
	          15f,                                                                /* efficiency */
	          12f,                                                                /* damage */
	          30);                                                                /* enchantability */

  @SidedProxy(serverSide = "com.retrocraft.server.CommonProxy", clientSide = "com.retrocraft.client.ClientProxy")
  public static CommonProxy                 proxy;
  public static final RetroCraftCreativeTab creativeTab = new RetroCraftCreativeTab();

  public static final String modId   = "retrocraft";
  public static final String name    = "RetroCraft Mod";
  public static final String version = "0.2.1";

  public static SimpleNetworkWrapper network;

  public static Configuration configuration;
  private RetroCraftConfig    config;

  @Mod.Instance(modId)
  public static RetroCraft instance;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    network = NetworkRegistry.INSTANCE.newSimpleChannel(RetroCraft.modId);
    configuration = new Configuration(event.getSuggestedConfigurationFile());
    config = new RetroCraftConfig();
    config.reloadLocal(configuration);
    if (configuration.hasChanged())
    {
      configuration.save();
    }
    
    ArmorMaterials.init();
    
    ModBlocks.init();
    ModItems.init();
    ModEntities.init();

    GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);

    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    PacketHandler.init(event);
    RetrocraftRecipes.init();
    
    proxy.init(event);
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    proxy.postInit(event);
  }

  public static RetroCraftConfig getConfig()
  {
    return instance.config;
  }

  public void setConfig(RetroCraftConfig config)
  {
    this.config = config;
  }
}
