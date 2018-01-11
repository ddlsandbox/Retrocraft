package com.retrocraft.world;

import java.util.Random;

import com.retrocraft.RetroCraft;
import com.retrocraft.block.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ModWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random,
	                     int chunkX,
											 int chunkZ,
											 World world,
											 IChunkGenerator chunkGenerator,
											 IChunkProvider chunkProvider)
  {
		if (world.provider.getDimension() == 0)
		{
			 // the overworld
			generateOverworld(
					random,
					chunkX,
					chunkZ,
					world,
					chunkGenerator,
					chunkProvider);
		}
	}

	private void generateOverworld(Random random,
																 int chunkX,
																 int chunkZ,
																 World world,
																 IChunkGenerator chunkGenerator,
																 IChunkProvider chunkProvider)
  {
	  BlockMatcher replaceBlock = BlockMatcher.forBlock(Blocks.STONE);

		generateOre(ModBlocks.oreManolite.getDefaultState(),
				world,
				random,
				chunkX * 16,
				chunkZ * 16,
				RetroCraft.getConfig().oreManoliumMinY,
				RetroCraft.getConfig().oreManoliumMaxY,
				RetroCraft.getConfig().oreManoliumVeinSize + random.nextInt(RetroCraft.getConfig().oreManoliumVeinVar),
				RetroCraft.getConfig().oreManoliumTries,
				replaceBlock);

		generateOre(ModBlocks.oreOctirion.getDefaultState(),
				world,
				random,
				chunkX * 16,
				chunkZ * 16,
				RetroCraft.getConfig().oreOctirionMinY,
				RetroCraft.getConfig().oreOctirionMaxY,
				RetroCraft.getConfig().oreOctirionVeinSize + random.nextInt(RetroCraft.getConfig().oreOctirionVeinVar),
				RetroCraft.getConfig().oreOctirionTries,
				replaceBlock);
	}

	private void generateOre(IBlockState ore,
													 World world,
													 Random random,
													 int x,
													 int z,
													 int minY,
													 int maxY,
													 int size,
													 int chances,
													 BlockMatcher block)
  {
		int deltaY = maxY - minY;

		for (int i = 0; i < chances; i++)
		{
			BlockPos pos = new BlockPos(
			    x + random.nextInt(16), 
			    minY + random.nextInt(deltaY), 
			    z + random.nextInt(16));

			(new WorldGenMinable(ore, size)).generate(world, random, pos);
		}
	}
	
//  private void addOreSpawn(IBlockState block, World world, Random random,
//      int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize,
//      int chance, int minY, int maxY, Predicate<IBlockState> blockToSpawnIn)
//  {
//    int diffMinMaxY = maxY - minY;
//    for (int x = 0; x < chance; x++)
//    {
//      int posX = blockXPos + random.nextInt(maxX);
//      int posY = minY + random.nextInt(diffMinMaxY);
//      int posZ = blockZPos + random.nextInt(maxZ);
//      (new WorldGenMinable(block, maxVeinSize, blockToSpawnIn)).generate(world,
//          random, new BlockPos(posX, posY, posZ));
//    }
//  }

}
