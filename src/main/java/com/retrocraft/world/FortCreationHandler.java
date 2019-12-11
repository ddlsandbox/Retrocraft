package com.retrocraft.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class FortCreationHandler implements IVillageCreationHandler
{

  @Override
  public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size)
  {
    return new StructureVillagePieces.PieceWeight(ComponentVillageFort.class, 3, 1);
  }

  @Override
  public Class<? extends StructureVillagePieces.Village> getComponentClass()
  {
    return ComponentVillageFort.class;
  }

  @Override
  @Nullable
  public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece,
      StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z,
      EnumFacing facing, int type)
  {
    return ComponentVillageFort.buildComponent(villagePiece, startPiece, pieces, random, x, y, z, facing, type);
  }

}
