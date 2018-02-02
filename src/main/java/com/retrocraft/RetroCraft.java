package com.retrocraft;

import com.retrocraft.block.ModBlocks;
import com.retrocraft.item.ModItems;
import com.retrocraft.item.RetrocraftMaterials;
import com.retrocraft.network.PacketHandler;
import com.retrocraft.recipe.RetrocraftRecipes;
import com.retrocraft.server.CommonProxy;
import com.retrocraft.tab.RetroCraftCreativeTab;
import com.retrocraft.world.ComponentVillageTelehouse;
import com.retrocraft.world.ComponentVillageTeleport;
import com.retrocraft.world.ModWorldGen;
import com.retrocraft.world.TeleportCreationHandler;
import com.retrocraft.world.VillageTelehouseCreationHandler;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod.EventBusSubscriber
@Mod(modid = RetroCraft.modId, name = RetroCraft.name, version = RetroCraft.version, acceptedMinecraftVersions = "[1.12.2]")
public class RetroCraft
{
  @SidedProxy(serverSide = "com.retrocraft.server.CommonProxy", clientSide = "com.retrocraft.client.ClientProxy")
  public static CommonProxy                 proxy;
  public static final RetroCraftCreativeTab creativeTab = new RetroCraftCreativeTab();

  public static final String modId   = "retrocraft";
  public static final String name    = "RetroCraft Mod";
  public static final String version = "0.4.0";

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
    
    RetrocraftMaterials.init();
    
    ModBlocks.init();
    ModItems.init();

    GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);

    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    
    VillagerRegistry.instance().registerVillageCreationHandler(new TeleportCreationHandler());
//    VillagerRegistry.instance().registerVillageCreationHandler(new VillageTelehouseCreationHandler());
    MapGenStructureIO.registerStructureComponent(ComponentVillageTeleport.class, "retrocraft:teleport_station");
//    MapGenStructureIO.registerStructureComponent(ComponentVillageTelehouse.class, "retrocraft:teleport_house");
    
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
  
  /* events */
  
  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    proxy.loadModels();
  }
}
