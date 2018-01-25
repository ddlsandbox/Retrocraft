package com.retrocraft;

import net.minecraftforge.fluids.Fluid;

/* Not configurable global definitions */

public abstract class RetroCraftGlobals
{
  public static final int TICKS_PER_SECOND = 20;
  
  public static final double manoliumSwordDamage     = 4.0;
  public static final int    manoliumSwordDurability = 1750;

  public static final int defaultCapacity      = 30000;

  /* generators */
  
  public static final int steamGeneratorNrgCapacity   = 10000;
  public static final int steamGeneratorNrgThroughput = 120;
  public static final int steamGeneratorEfficiency    = 30;
  public static final int steamGeneratorFluidCapacity = 2 * Fluid.BUCKET_VOLUME;
  
  public static final int manureGeneratorNrgCapacity   = 10000;
  public static final int manureGeneratorNrgThroughput = 240;
  public static final int manureGeneratorEfficiency    = 50;
  
  /* machines */
  
  public static final int oreGrinderCapacity   = 5000;
  public static final int oreGrinderCrushTime  = 100;
  public static final int oreGrinderEnergyUsed = 40;
  public static final int oreGrinderThroughput = 200;
  
  public static final int oreSmelterCapacity   = 5000;
  public static final int oreSmelterBurnTime   = 100;
  public static final int oreSmelterEnergyUsed = 30;
  public static final int oreSmelterThroughput = 200;
  
  public static final int repairerCapacity   = 10000;
  public static final int repairerEnergyUsed = 30;
  public static final int repairerThroughput = 200;
  
  /* blocks */
  
  public static final double manureProb = 0.001;
}
