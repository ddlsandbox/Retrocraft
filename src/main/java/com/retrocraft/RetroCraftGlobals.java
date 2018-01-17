package com.retrocraft;

import net.minecraftforge.fluids.Fluid;

/* Not configurable global definitions */

public class RetroCraftGlobals
{
  public static double manoliumSwordDamage     = 4.0;
  public static int    manoliumSwordDurability = 1750;

  public static int defaultCapacity      = 30000;

  /* generators */
  
  public static int steamGeneratorNrgCapacity   = 10000;
  public static int steamGeneratorNrgThroughput = 120;
  public static int steamGeneratorEfficiency    = 30;
  public static int steamGeneratorFluidCapacity = 2 * Fluid.BUCKET_VOLUME;
  
  public static int manureGeneratorNrgCapacity   = 10000;
  public static int manureGeneratorNrgThroughput = 240;
  public static int manureGeneratorEfficiency    = 50;
  
  /* machines */
  
  public static int oreGrinderCapacity   = 5000;
  public static int oreGrinderCrushTime  = 100;
  public static int oreGrinderEnergyUsed = 40;
  public static int oreGrinderThroughput = 200;
  
  public static int oreSmelterCapacity   = 5000;
  public static int oreSmelterBurnTime   = 100;
  public static int oreSmelterEnergyUsed = 30;
  public static int oreSmelterThroughput = 200;
  
  public static int repairerCapacity   = 10000;
  public static int repairerEnergyUsed = 30;
  public static int repairerThroughput = 200;
}
