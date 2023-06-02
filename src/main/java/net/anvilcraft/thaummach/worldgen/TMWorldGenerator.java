package net.anvilcraft.thaummach.worldgen;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class TMWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(
        Random rand,
        int chunkX,
        int chunkZ,
        World world,
        IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider
    ) {
        // TODO: generate in other overworld-y dimensions
        if (world.provider.dimensionId != 0)
            return;

        if (rand.nextInt(66) != 42)
            return;

        WorldGenMonolith wgm = new WorldGenMonolith();

        int xPos = (chunkX << 4) + rand.nextInt(16);
        int zPos = (chunkZ << 4) + rand.nextInt(16);
        int yPos = world.getHeightValue(xPos, zPos);

        wgm.generate(world, rand, xPos, yPos, zPos);
    }
}
