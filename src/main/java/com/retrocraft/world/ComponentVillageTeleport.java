package com.retrocraft.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class ComponentVillageTeleport extends StructureVillagePieces.Village
{

  private static final int TELESTATION_X = 7;
  private static final int TELESTATION_Y = 6;
  private static final int TELESTATION_Z = 7;

  private static final ResourceLocation VILLAGE_TELEPORT_ID = new ResourceLocation(RetroCraft.modId,
      "teleport_station");

  public ComponentVillageTeleport(StructureVillagePieces.Start start, int type, StructureBoundingBox boundingBox,
      EnumFacing facing)
  {
    super(start, type);
    this.boundingBox = boundingBox;
    setCoordBaseMode(facing);
  }

  @Override
  public boolean addComponentParts(World world, Random rand, StructureBoundingBox boundingBox)
  {
    if (averageGroundLvl < 0)
    {
      averageGroundLvl = getAverageGroundLevel(world, boundingBox);
      if (averageGroundLvl < 0)
      {
        return true;
      }

      this.boundingBox.offset(0, averageGroundLvl - this.boundingBox.minY - 1, 0);
    }
    BlockPos pos = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
    TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
    PlacementSettings settings = (new PlacementSettings()).setReplacedBlock(Blocks.STRUCTURE_VOID)
        .setBoundingBox(boundingBox);
    Template template = templateManager.getTemplate(world.getMinecraftServer(), VILLAGE_TELEPORT_ID);
    template.addBlocksToWorldChunk(world, pos, settings);
    return true;
  }

  @Nullable
  public static StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece,
      StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z,
      EnumFacing facing, int type)
  {
    StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0,
        TELESTATION_X, TELESTATION_Y, TELESTATION_Z, facing);
    if (canVillageGoDeeper(boundingBox) && findIntersecting(pieces, boundingBox) == null)
    {
      return new ComponentVillageTeleport(startPiece, type, boundingBox, facing);
    }

    return null;
  }

}
