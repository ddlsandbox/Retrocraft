package com.retrocraft;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;

public class RetroCraftConfig
{
  public static boolean disableParticles;
  public static float soundVolume = 0.5f;
  
  public float enchantmentCostFactor = 1.5f;
  public boolean allowDisenchanting;
  
  public int     blocksPerXPLevel;
  public boolean restrictRenameToOwner;
  public boolean creativeModeOnly;

  /* machines */
  
  public int grinderXpPerItem;
  public float smelterXpFactor;
  
  public int oreManoliumMinY;
  public int oreManoliumMaxY;
  public int oreManoliumVeinSize;
  public int oreManoliumVeinVar;
  public int oreManoliumTries;
  public boolean oreManoliumInNether;
  public boolean oreManoliumInEnd;

  public int oreOctirionMinY;
  public int oreOctirionMaxY;
  public int oreOctirionVeinSize;
  public int oreOctirionVeinVar;
  public int oreOctirionTries;
  public boolean oreOctirionInNether;
  public boolean oreOctirionInEnd;
  
  public void reloadLocal(Configuration config)
  {
    /* general */
    
    disableParticles = config.getBoolean("Disable Particles", "client", false,
        "If true, entities will not emit particles.");
    soundVolume = config.getFloat("Sound Volume", "client", 0.5f, 0f, 1f,
        "The volume of the sounds played.");
    
    /* enchanting */
    
    enchantmentCostFactor = config.getFloat("Enchantment cost factor", "enchant_and_repair", 1.5f, 0f,
        10.0f,
        "Factor applied to enchanter experience costs.");
    allowDisenchanting = config.getBoolean("Allow disenchanting", "enchant_and_repair", true,
        "If true, items can be disenchanted.");
    
    /* teleporting */
    
    blocksPerXPLevel = config.getInt("Blocks per XP Level", "teleport", 500, 0,
        2000,
        "The amount of blocks per xp level requirement for teleporting. Set to 0 to disable xp requirement.");
    restrictRenameToOwner = config.getBoolean("Restrict Rename to Owner",
        "teleport", false,
        "If true, only the owner of a teleport pipe can rename it.");
    creativeModeOnly = config.getBoolean("Creative Mode Only", "teleport", false,
        "If true, waystones can only be placed in creative mode.");


    /* machines */
    
    grinderXpPerItem = config.getInt("Grind XP", "machines", 10, 0, 100,
        "XP dropped per grinded item");
    smelterXpFactor = config.getFloat("Smelter XP factor", "machines", 2.0f, 0f, 100f,
        "XP multiplier for smelting (compared to furnace)");
    
    /* world generation */

    oreManoliumMinY = config.getInt("min_y", "manolium", 10, 0, 256,
        "Lowest Y level the ore is generated.");
    oreManoliumMaxY = config.getInt("max_y", "manolium", 50, 0, 256,
        "Highest Y level the ore is generated.");
    oreManoliumVeinSize = config.getInt("vein_size", "manolium", 4, 1, 100,
        "Vein size.");
    oreManoliumVeinVar = config.getInt("vein_var", "manolium", 2, 0, 100,
        "Vein variance.");
    oreManoliumTries = config.getInt("max_frequency", "manolium", 10, 1, 100,
        "Maximum ore frequency.");
    oreManoliumInNether = config.getBoolean("in_nether", "manolium", false,
        "Generate in nether.");
    oreManoliumInEnd = config.getBoolean("in_end", "manolium", false,
        "Generate in end.");
    

    oreOctirionMinY = config.getInt("min_y", "octirion", 0, 0, 256,
        "Lowest Y level the ore is generated.");
    oreOctirionMaxY = config.getInt("max_y", "octirion", 14, 0, 256,
        "Highest Y level the ore is generated.");
    oreOctirionVeinSize = config.getInt("vein_size", "octirion", 3, 1, 100,
        "Vein size.");
    oreOctirionVeinVar = config.getInt("vein_var", "octirion", 2, 0, 100,
        "Vein variance.");
    oreOctirionTries = config.getInt("max_frequency", "octirion", 3, 1, 100,
        "Maximum ore frequency.");
    oreOctirionInNether = config.getBoolean("in_nether", "octirion", true,
        "Generate in nether.");
    oreOctirionInEnd = config.getBoolean("in_end", "octirion", true,
        "Generate in end.");
  }

  public static RetroCraftConfig read(ByteBuf buf)
  {
    RetroCraftConfig config = new RetroCraftConfig();
    
    config.enchantmentCostFactor = buf.readFloat();
    
    config.creativeModeOnly = buf.readBoolean();
    config.restrictRenameToOwner = buf.readBoolean();
    config.blocksPerXPLevel = buf.readInt();
    
    config.oreManoliumMinY = buf.readInt();
    config.oreManoliumMaxY = buf.readInt();
    config.oreManoliumVeinSize = buf.readInt();
    config.oreManoliumVeinVar = buf.readInt();
    config.oreManoliumTries = buf.readInt();
    
    config.oreOctirionMinY = buf.readInt();
    config.oreOctirionMaxY = buf.readInt();
    config.oreOctirionVeinSize = buf.readInt();
    config.oreOctirionVeinVar = buf.readInt();
    config.oreOctirionTries = buf.readInt();
    
    return config;
  }

  public void write(ByteBuf buf)
  {
    buf.writeFloat(enchantmentCostFactor);
    
    buf.writeBoolean(creativeModeOnly);
    buf.writeBoolean(restrictRenameToOwner);
    buf.writeInt(blocksPerXPLevel);
    
    buf.writeInt(oreManoliumMinY);
    buf.writeInt(oreManoliumMaxY);
    buf.writeInt(oreManoliumVeinSize);
    buf.writeInt(oreManoliumVeinVar);
    buf.writeInt(oreManoliumTries);
    
    buf.writeInt(oreOctirionMinY);
    buf.writeInt(oreOctirionMaxY);
    buf.writeInt(oreOctirionVeinSize);
    buf.writeInt(oreOctirionVeinVar);
    buf.writeInt(oreOctirionTries);
  }
}
