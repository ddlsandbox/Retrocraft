package com.retrocraft;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;

public class RetroCraftConfig {

  public static boolean disableParticles;
  public static boolean disableTextGlow;

  public int blocksPerXPLevel;
  public boolean warpStoneXpCost;

  public boolean restrictRenameToOwner;
  public boolean creativeModeOnly;
  public boolean setSpawnPoint;

  public boolean globalInterDimension;

  public static float soundVolume = 0.5f;

  public static int worldGenChance = 0;

  public void reloadLocal(Configuration config) {
    disableTextGlow = config.getBoolean("Disable Text Glow", "client", false, "If true, the text overlay on waystones will no longer always render at full brightness.");
    disableParticles = config.getBoolean("Disable Particles", "client", false, "If true, activated waystones will not emit particles.");

    blocksPerXPLevel = config.getInt("Blocks per XP Level", "general", 500, 0, 2000, "The amount of blocks per xp level requirement (for inventory button & waystone-to-waystone teleport). Set to 0 to disable xp requirement.");
    warpStoneXpCost = config.getBoolean("Warp Stone Costs XP", "general", false, "Set to true if you want the warp stone to cost experience when used as well.");

    setSpawnPoint = config.getBoolean("Set Spawnpoint on Activation", "general", false, "If true, the player's spawnpoint will be set to the last activated waystone.");

    restrictRenameToOwner = config.getBoolean("Restrict Rename to Owner", "general", false, "If true, only the owner of a waystone can rename it.");
    creativeModeOnly = config.getBoolean("Creative Mode Only", "general", false, "If true, waystones can only be placed in creative mode.");

    globalInterDimension = config.getBoolean("Interdimensional Teleport on Global Waystones", "general", true, "If true, waystones marked as global work inter-dimensionally.");
 
    soundVolume = config.getFloat("Sound Volume", "client", 0.5f, 0f, 1f, "The volume of the sound played when teleporting.");

    worldGenChance = config.getInt("World Gen Chance", "general", 0, 0, 10000, "The chance for a waystone to spawn in world gen, per 10000 blocks. Set to 0 to disable");
  }

  public static RetroCraftConfig read(ByteBuf buf) {
    RetroCraftConfig config = new RetroCraftConfig();
    config.creativeModeOnly = buf.readBoolean();
    config.setSpawnPoint = buf.readBoolean();
    config.restrictRenameToOwner = buf.readBoolean();
    config.blocksPerXPLevel = buf.readInt();
    return config;
  }

  public void write(ByteBuf buf) {
    buf.writeBoolean(creativeModeOnly);
    buf.writeBoolean(setSpawnPoint);
    buf.writeBoolean(restrictRenameToOwner);
    buf.writeInt(blocksPerXPLevel);
  }
}
