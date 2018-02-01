package com.retrocraft.world;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.retrocraft.RetroCraft;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
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
import net.minecraft.world.storage.loot.LootTableList;

public class ComponentVillageTelehouse extends StructureVillagePieces.House1
{

  private static final int TELEHOUSE_X = 14;
  private static final int TELEHOUSE_Y = 12;
  private static final int TELEHOUSE_Z = 15;

  private static final ResourceLocation VILLAGE_TELEHOUSE_ID = new ResourceLocation(RetroCraft.modId, "teleport_house");

  private int averageGroundLevel = -1;

  public ComponentVillageTelehouse()
  {
  }

  public ComponentVillageTelehouse(StructureBoundingBox boundingBox, EnumFacing par5)
  {
    this.setCoordBaseMode(par5);
    this.boundingBox = boundingBox;
  }

  @Override
  public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb)
  {
    if (this.averageGroundLevel < 0)
    {
      this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);
      if (this.averageGroundLevel < 0)
      {
        return true;
      }
      this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + TELEHOUSE_Y - 2, 0);
    }

    this.fillWithBlocks(world, sbb, 0, 0, 0, TELEHOUSE_X - 1, TELEHOUSE_Y - 1, TELEHOUSE_Z - 1, Blocks.AIR);
    this.spawnActualHouse(world, sbb, rand);

    for (int i = 0; i < TELEHOUSE_X; i++)
    {
      for (int j = 0; j < TELEHOUSE_Z; j++)
      {
        this.clearCurrentPositionBlocksUpwards(world, i, TELEHOUSE_Y, j, sbb);
        this.replaceAirAndLiquidDownwards(world, Blocks.DIRT.getDefaultState(), i, -1, j, sbb);
      }
    }

    this.spawnVillagers(world, sbb, 7, 4, 6, 1);

    return true;
  }

  private void fillWithBlocks(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY,
      int maxZ, Block block)
  {
    this.fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, block.getDefaultState(),
        block.getDefaultState(), false);
  }

  @Nullable
  public static ComponentVillageTelehouse buildComponent(List<StructureComponent> pieces, int p1, int p2, int p3,
      EnumFacing p4)
  {
    StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0,
        TELEHOUSE_X, TELEHOUSE_Y, TELEHOUSE_Z, p4);
    return canVillageGoDeeper(boundingBox) && StructureComponent.findIntersecting(pieces, boundingBox) == null
        ? new ComponentVillageTelehouse(boundingBox, p4)
        : null;
  }

  private void spawnActualHouse(World world, StructureBoundingBox sbb, Random rand)
  {
    TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
    MinecraftServer server = world.getMinecraftServer();

    if (manager != null && server != null)
    {
      EnumFacing facing = this.getCoordBaseMode();

      Mirror mirror;
      Rotation rotation;
      if (facing == EnumFacing.SOUTH)
      {
        mirror = Mirror.FRONT_BACK;
        rotation = Rotation.NONE;
      } 
      else if (facing == EnumFacing.WEST)
      {
        mirror = Mirror.NONE;
        rotation = Rotation.CLOCKWISE_90;
      } 
      else if (facing == EnumFacing.EAST)
      {
        mirror = Mirror.LEFT_RIGHT;
        rotation = Rotation.CLOCKWISE_90;
      } 
      else
      {
        mirror = Mirror.NONE;
        rotation = Rotation.NONE;
      }

      BlockPos pos = new BlockPos(this.getXWithOffset(0, 0), this.getYWithOffset(0), this.getZWithOffset(0, 0));
      PlacementSettings placement = new PlacementSettings().setRotation(rotation).setMirror(mirror).setBoundingBox(sbb);
      Template template = manager.getTemplate(server, VILLAGE_TELEHOUSE_ID);

      if (template != null)
      {
        template.addBlocksToWorld(world, pos, placement);

        Map<BlockPos, String> dataBlocks = template.getDataBlocks(pos, placement);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet())
        {
          if ("chest".equals(entry.getValue()))
          {
            BlockPos blockpos2 = entry.getKey();
            world.setBlockState(blockpos2.up(), Blocks.AIR.getDefaultState(), 3);
            TileEntity tileentity = world.getTileEntity(blockpos2.down());

            if (tileentity instanceof TileEntityChest)
            {
              ((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_WOODLAND_MANSION, rand.nextLong());
            }
          }
        }
      }
    }
  }

}
