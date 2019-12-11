package com.retrocraft.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class ComponentVillageFort extends StructureVillagePieces.Village
{

  private static final int FORT_X = 32;
  private static final int FORT_Y = 17;
  private static final int FORT_Z = 32;

  private static final ResourceLocation VILLAGE_FORT_ID = new ResourceLocation(RetroCraft.modId,
      "fort");

  public ComponentVillageFort(StructureVillagePieces.Start start, int type, StructureBoundingBox boundingBox,
      EnumFacing facing)
  {
    super(start, type);
    this.boundingBox = boundingBox;
    setCoordBaseMode(facing);
  }
  
  @Override
  public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb){
      if(this.averageGroundLvl < 0){
          this.averageGroundLvl = this.getAverageGroundLevel(world, sbb);
          if(this.averageGroundLvl < 0){
              return true;
          }
          this.boundingBox.offset(0, this.averageGroundLvl-this.boundingBox.maxY+FORT_Y-2, 0);
      }

      this.fillWithBlocks(world, sbb, 0, 0, 0, FORT_X-1, FORT_Y-1, FORT_Z-1, Blocks.AIR);
      this.spawnActualHouse(world, sbb);
      //this.fillHouse(world, sbb);

      for(int i = 0; i < FORT_X; i++){
          for(int j = 0; j < FORT_Z; j++){
              this.clearCurrentPositionBlocksUpwards(world, i, FORT_Y, j, sbb);
              this.replaceAirAndLiquidDownwards(world, Blocks.DIRT.getDefaultState(), i, -1, j, sbb);
          }
      }

      this.spawnVillagers(world, sbb, 7, 4, 6, 1);

      return true;
  }
  
  private void fillWithBlocks(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block block){
    this.fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, block.getDefaultState(), block.getDefaultState(), false);
  }
  
  private void spawnActualHouse(World world, StructureBoundingBox sbb){
    TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
    MinecraftServer server = world.getMinecraftServer();

    if(manager != null && server != null){
        EnumFacing facing = this.getCoordBaseMode();

        Mirror mirror;
        Rotation rotation;
        if(facing == EnumFacing.SOUTH){
            mirror = Mirror.NONE;
            rotation = Rotation.NONE;
        }
        else if(facing == EnumFacing.WEST){
            mirror = Mirror.NONE;
            rotation = Rotation.CLOCKWISE_90;
        }
        else if(facing == EnumFacing.EAST){
            mirror = Mirror.LEFT_RIGHT;
            rotation = Rotation.CLOCKWISE_90;
        }
        else{
            mirror = Mirror.LEFT_RIGHT;
            rotation = Rotation.NONE;
        }

        PlacementSettings placement = new PlacementSettings().setRotation(rotation).setMirror(mirror).setBoundingBox(sbb);
        Template template = manager.getTemplate(server, VILLAGE_FORT_ID);

        if(template != null){
            template.addBlocksToWorld(world, new BlockPos(this.getXWithOffset(0, 0), this.getYWithOffset(0), this.getZWithOffset(0, 0)), placement);
        }
    }
}
  
  @Nullable
  public static StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece,
      StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z,
      EnumFacing facing, int type)
  {
    StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0,
        FORT_X, FORT_Y, FORT_Z, facing);
    if (canVillageGoDeeper(boundingBox) && findIntersecting(pieces, boundingBox) == null)
    {
      return new ComponentVillageFort(startPiece, type, boundingBox, facing);
    }

    return null;
  }

}
