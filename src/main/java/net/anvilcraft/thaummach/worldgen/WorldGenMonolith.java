package net.anvilcraft.thaummach.worldgen;

import java.util.Random;

import dev.tilera.auracore.api.EnumNodeType;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.alec.jalec.AlecLogger;
import net.anvilcraft.thaummach.TMBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMonolith extends WorldGenerator {
    private boolean isValidLocation(World world, int x, int y, int z) {
        for (int a = -3; a <= 3; ++a) {
            for (int c = -3; c <= 3; ++c) {
                if (world.getBlock(x + a, y, z + c) != Blocks.dirt
                    && world.getBlock(x + a, y, z + c) != Blocks.stone
                    && world.getBlock(x + a, y, z + c) != Blocks.grass
                    && world.getBlock(x + a, y, z + c) != Blocks.sand) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        AlecLogger.FMT.alec("generate %d %d %d", x, y, z);
        if (world.getBlock(x, y, z) == Blocks.tallgrass
            || world.getBlock(x, y, z) == Blocks.snow_layer) {
            --y;
        }

        if (!this.isValidLocation(world, x, y, z)) {
            AlecLogger.PLAIN.alec("invalid location");
            return false;
        }

        int bio;
        for (bio = 2; bio < 7; ++bio) {
            world.setBlock(x, y + 1 + bio, z, TMBlocks.hidden, 2, 3);
        }

        bio = world.getWorldChunkManager().getBiomeGenAt(x, z).biomeID;
        if (bio != BiomeGenBase.desert.biomeID && bio != BiomeGenBase.desertHills.biomeID
            && bio != BiomeGenBase.beach.biomeID) {
            if (bio == BiomeGenBase.swampland.biomeID) {
                this.genMossyStoneSurroundings(world, random, x, y, z);
            } else {
                this.genStoneSurroundings(world, random, x, y, z);
            }
        } else {
            this.genSandStoneSurroundings(world, random, x, y, z);
        }

        int b;
        int c;
        for (int a = -1; a <= 1; ++a) {
            for (b = y; b >= y - 1; --b) {
                for (c = -1; c <= 1; ++c) {
                    if (Math.abs(a) != 1 && Math.abs(c) != 1 && b != y - 1) {
                        if (a == 0 && c == 0) {
                            world.setBlockToAir(x + a, b, z + c);
                        }
                    } else {
                        world.setBlock(x + a, b, z + c, TMBlocks.hidden, 0, 3);
                    }
                }
            }
        }

        world.setBlock(x + 1, y, z, TMBlocks.hidden, 3, 3);
        world.setBlock(x - 1, y, z, TMBlocks.hidden, 0, 3);
        world.setBlock(x, y, z + 1, TMBlocks.hidden, 0, 3);
        world.setBlock(x, y, z - 1, TMBlocks.hidden, 0, 3);
        world.setBlock(x, y, z, TMBlocks.hidden, 5, 3);

        AuraManager.registerAuraNode(
            world,
            (short) (random.nextInt(300) + 500),
            EnumNodeType.DARK,
            world.provider.dimensionId,
            x,
            y,
            z
        );

        return true;
    }

    private void genSandStoneSurroundings(World world, Random random, int x, int y, int z) {
        int side;
        for (side = 0; side < 75; ++side) {
            world.setBlock(
                x + random.nextInt(4) - random.nextInt(4),
                y,
                z + random.nextInt(4) - random.nextInt(4),
                Blocks.sandstone
            );
        }

        for (side = 0; side < 4; ++side) {
            int mx = 0;
            int mz = 0;
            switch (side) {
                case 0:
                    mz = 2;
                    mx = 2;
                    break;
                case 1:
                    mx = 2;
                    mz = -2;
                    break;
                case 2:
                    mz = -2;
                    mx = -2;
                    break;
                case 3:
                    mx = -2;
                    mz = 2;
            }

            for (int a = 0; a < random.nextInt(5); ++a) {
                int md = 0;
                switch (a) {
                    case 1:
                    case 3:
                        md = 1;
                        break;
                    case 2:
                    case 4:
                        md = 2;
                }

                world.setBlock(x + mx, y + a + 1, z + mz, Blocks.sandstone, md, 3);
            }
        }
    }

    private void genStoneSurroundings(World world, Random random, int x, int y, int z) {
        int bio = world.getWorldChunkManager().getBiomeGenAt(x, z).biomeID;
        boolean vines = false;
        if (bio == BiomeGenBase.jungle.biomeID || bio == BiomeGenBase.jungleHills.biomeID
            || bio == BiomeGenBase.forest.biomeID) {
            vines = true;
        }

        int side;
        for (side = 0; side < 75; ++side) {
            world.setBlock(
                x + random.nextInt(4) - random.nextInt(4),
                y,
                z + random.nextInt(4) - random.nextInt(4),
                Blocks.stonebrick,
                random.nextInt(3),
                3
            );
        }

        for (side = 0; side < 4; ++side) {
            int mx = 0;
            int mz = 0;
            switch (side) {
                case 0:
                    mz = 2;
                    mx = 2;
                    break;
                case 1:
                    mx = 2;
                    mz = -2;
                    break;
                case 2:
                    mz = -2;
                    mx = -2;
                    break;
                case 3:
                    mx = -2;
                    mz = 2;
            }

            for (int a = 0; a < random.nextInt(5); ++a) {
                world.setBlock(x + mx, y + a + 1, z + mz, Blocks.stonebrick, 3, 3);
                if (a > 1) {
                    if (vines && random.nextBoolean()) {
                        world.setBlock(x + mx + 1, y + a + 1, z + mz, Blocks.vine, 2, 3);
                    }

                    if (vines && random.nextBoolean()) {
                        world.setBlock(x + mx - 1, y + a + 1, z + mz, Blocks.vine, 8, 3);
                    }

                    if (vines && random.nextBoolean()) {
                        world.setBlock(x + mx, y + a + 1, z + mz + 1, Blocks.vine, 4, 3);
                    }

                    if (vines && random.nextBoolean()) {
                        world.setBlock(x + mx, y + a + 1, z + mz - 1, Blocks.vine, 1, 3);
                    }
                }

                if (a == 3) {
                    if (random.nextBoolean()) {
                        world.setBlock(
                            x + mx + mx / 2 * -1, y + a + 1, z + mz, Blocks.stonebrick, 3, 3
                        );
                    }

                    if (random.nextBoolean()) {
                        world.setBlock(
                            x + mx, y + a + 1, z + mz + mz / 2 * -1, Blocks.stonebrick, 3, 3
                        );
                    }
                }
            }
        }
    }

    private void genMossyStoneSurroundings(World world, Random random, int x, int y, int z) {
        int side;
        for (side = 0; side < 75; ++side) {
            world.setBlock(
                x + random.nextInt(4) - random.nextInt(4),
                y,
                z + random.nextInt(4) - random.nextInt(4),
                Blocks.mossy_cobblestone
            );
        }

        for (side = 0; side < 4; ++side) {
            int mx = 0;
            int mz = 0;
            switch (side) {
                case 0:
                    mz = 2;
                    mx = 2;
                    break;
                case 1:
                    mx = 2;
                    mz = -2;
                    break;
                case 2:
                    mz = -2;
                    mx = -2;
                    break;
                case 3:
                    mx = -2;
                    mz = 2;
            }

            for (int a = 0; a < random.nextInt(5); ++a) {
                if (a < 3) {
                    world.setBlock(x + mx, y + a + 1, z + mz, Blocks.mossy_cobblestone);
                } else {
                    world.setBlock(x + mx, y + a + 1, z + mz, Blocks.stonebrick, 3, 3);
                }

                if (a > 1) {
                    if (random.nextBoolean()) {
                        world.setBlock(x + mx + 1, y + a + 1, z + mz, Blocks.vine, 2, 3);
                    }

                    if (random.nextBoolean()) {
                        world.setBlock(x + mx - 1, y + a + 1, z + mz, Blocks.vine, 8, 3);
                    }

                    if (random.nextBoolean()) {
                        world.setBlock(x + mx, y + a + 1, z + mz + 1, Blocks.vine, 4, 3);
                    }

                    if (random.nextBoolean()) {
                        world.setBlock(x + mx, y + a + 1, z + mz - 1, Blocks.vine, 1, 3);
                    }
                }

                if (a == 3) {
                    if (random.nextBoolean()) {
                        world.setBlock(
                            x + mx + mx / 2 * -1, y + a + 1, z + mz, Blocks.mossy_cobblestone
                        );
                    }

                    if (random.nextBoolean()) {
                        world.setBlock(
                            x + mx, y + a + 1, z + mz + mz / 2 * -1, Blocks.mossy_cobblestone
                        );
                    }
                }
            }
        }
    }
}
