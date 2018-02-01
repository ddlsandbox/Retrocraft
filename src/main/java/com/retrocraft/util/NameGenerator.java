package com.retrocraft.util;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public abstract class NameGenerator
{
  private static Map<Biome, String> BIOME_NAMES;
  private static final Set<String> usedNames = Sets.newHashSet();

  private static final String[] random1 = new String[]
  { "Purple", "Blue", "Holy", "Big", "Great", "Rapid", "Little" };

  private static final String[] random2 = new String[]
  { "Nasty", "Fox", "Penguin", "Stone", "Flume", "Windy", "Rocky" };

  public static String getName(Biome biome, Random rand)
  {
    if (BIOME_NAMES == null)
    {
      init();
    }
    String name;
    name = randomName(rand, biome);
    String tryName = name;
    int i = 1;
    while (usedNames.contains(tryName))
    {
      tryName = name + " " + i;
      i++;
    }
    name = tryName;

    usedNames.add(name);
    return name;
  }

  private static String randomName(Random rand, Biome biome)
  {
    boolean useR1 = rand.nextBoolean();
    String biomeSuffix = BIOME_NAMES.get(biome);
    return useR1 ? (random1[rand.nextInt(random1.length)] + " ")
        : "" + random2[rand.nextInt(random2.length)] + (biomeSuffix != null ? " " + biomeSuffix : "");
  }

  public static void init()
  {
    BIOME_NAMES = Maps.newHashMap();

    addBiomeName(Biomes.COLD_TAIGA, "Taiga");
    addBiomeName(Biomes.REDWOOD_TAIGA, "Taiga");
    addBiomeName(Biomes.TAIGA, "Taiga");

    addBiomeName(Biomes.PLAINS, "Plains");

    addBiomeName(Biomes.MUSHROOM_ISLAND, "Island");
    addBiomeName(Biomes.MUSHROOM_ISLAND_SHORE, "Island");

    addBiomeName(Biomes.RIVER, "River");
    addBiomeName(Biomes.FROZEN_RIVER, "River");

    addBiomeName(Biomes.BEACH, "Beach");
    addBiomeName(Biomes.COLD_BEACH, "Beach");
    addBiomeName(Biomes.STONE_BEACH, "Beach");

    addBiomeName(Biomes.BIRCH_FOREST, "Forest");
    addBiomeName(Biomes.BIRCH_FOREST_HILLS, "Forest");
    addBiomeName(Biomes.FOREST_HILLS, "Forest");
    addBiomeName(Biomes.FOREST, "Forest");
    addBiomeName(Biomes.ROOFED_FOREST, "Forest");
    addBiomeName(Biomes.MUTATED_FOREST, "Forest");
    addBiomeName(Biomes.MUTATED_BIRCH_FOREST, "Forest");
    addBiomeName(Biomes.MUTATED_BIRCH_FOREST_HILLS, "Forest");
    addBiomeName(Biomes.MUTATED_ROOFED_FOREST, "Forest");

    addBiomeName(Biomes.DEEP_OCEAN, "Ocean");
    addBiomeName(Biomes.OCEAN, "Ocean");
    addBiomeName(Biomes.FROZEN_OCEAN, "Ocean");

    addBiomeName(Biomes.DESERT, "Desert");
    addBiomeName(Biomes.DESERT_HILLS, "Desert");
    addBiomeName(Biomes.MUTATED_DESERT, "Desert");

    addBiomeName(Biomes.COLD_TAIGA_HILLS, "Hills");
    addBiomeName(Biomes.EXTREME_HILLS, "Hills");
    addBiomeName(Biomes.EXTREME_HILLS_EDGE, "Hills");
    addBiomeName(Biomes.EXTREME_HILLS_WITH_TREES, "Hills");
    addBiomeName(Biomes.MUTATED_EXTREME_HILLS, "Hills");
    addBiomeName(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, "Hills");
    addBiomeName(Biomes.REDWOOD_TAIGA_HILLS, "Hills");
    addBiomeName(Biomes.TAIGA_HILLS, "Hills");
    addBiomeName(Biomes.MUTATED_REDWOOD_TAIGA_HILLS, "Hills");

    addBiomeName(Biomes.SWAMPLAND, "Swamps");
    addBiomeName(Biomes.MUTATED_SWAMPLAND, "Swamps");

    addBiomeName(Biomes.SAVANNA, "Savanna");
    addBiomeName(Biomes.SAVANNA_PLATEAU, "Plateau");
    addBiomeName(Biomes.MUTATED_SAVANNA, "Savanna");
    addBiomeName(Biomes.MUTATED_SAVANNA_ROCK, "Savanna");

    addBiomeName(Biomes.ICE_PLAINS, "Icelands");
    addBiomeName(Biomes.ICE_MOUNTAINS, "Icelands");
    addBiomeName(Biomes.MUTATED_ICE_FLATS, "Icelands");

    addBiomeName(Biomes.JUNGLE, "Jungle");
    addBiomeName(Biomes.JUNGLE_EDGE, "Jungle");
    addBiomeName(Biomes.JUNGLE_HILLS, "Jungle");
    addBiomeName(Biomes.MUTATED_JUNGLE, "Jungle");
    addBiomeName(Biomes.MUTATED_JUNGLE_EDGE, "Jungle");

    addBiomeName(Biomes.MESA_ROCK, "Mesa");
    addBiomeName(Biomes.MESA, "Mesa");
    addBiomeName(Biomes.MESA_CLEAR_ROCK, "Mesa");
    addBiomeName(Biomes.MUTATED_MESA, "Mesa");
    addBiomeName(Biomes.MUTATED_MESA_CLEAR_ROCK, "Mesa");
    addBiomeName(Biomes.MUTATED_MESA_ROCK, "Mesa");

    addBiomeName(Biomes.VOID, "Void");
    addBiomeName(Biomes.SKY, "Skies");
  }

  private static void addBiomeName(Biome biome, String name)
  {
    BIOME_NAMES.put(biome, name);
  }
}
