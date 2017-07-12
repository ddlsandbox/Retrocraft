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

  public int oreManoliumMinY;
  public int oreManoliumMaxY;
  public int oreManoliumVeinSize;
  public int oreManoliumVeinVar;
  public int oreManoliumTries;

  public int oreOctirionMinY;
  public int oreOctirionMaxY;
  public int oreOctirionVeinSize;
  public int oreOctirionVeinVar;
  public int oreOctirionTries;
  
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

    
    
    /* world generation */

    oreManoliumMinY = config.getInt("min_y", "manolium", 16, 0, 256,
        "Lowest Y level the ore is generated.");
    oreManoliumMaxY = config.getInt("max_y", "manolium", 50, 0, 256,
        "Highest Y level the ore is generated.");
    oreManoliumVeinSize = config.getInt("vein_size", "manolium", 2, 1, 100,
        "Vein size.");
    oreManoliumVeinVar = config.getInt("vein_size", "manolium", 2, 1, 100,
        "Vein variance.");
    oreManoliumTries = config.getInt("max_frequency", "manolium", 15, 1, 100,
        "Maximum ore frequency.");
    

    oreOctirionMinY = config.getInt("min_y", "octirion", 1, 0, 256,
        "Lowest Y level the ore is generated.");
    oreOctirionMaxY = config.getInt("max_y", "octirion", 14, 0, 256,
        "Highest Y level the ore is generated.");
    oreOctirionVeinSize = config.getInt("vein_size", "octirion", 2, 1, 100,
        "Vein size.");
    oreOctirionVeinVar = config.getInt("vein_size", "octirion", 2, 1, 100,
        "Vein variance.");
    oreOctirionTries = config.getInt("max_frequency", "octirion", 10, 1, 100,
        "Maximum ore frequency.");
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
