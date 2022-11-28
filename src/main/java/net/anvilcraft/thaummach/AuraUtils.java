package net.anvilcraft.thaummach;

import dev.tilera.auracore.api.machine.IConnection;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.client.fx.particles.FXWisp;

public class AuraUtils {
    public static void taintExplosion(World w, int x, int y, int z) {
        w.createExplosion(
            null,
            (double) ((float) x + 0.5F),
            (double) ((float) y + 0.5F),
            (double) ((float) z + 0.5F),
            1.0F,
            false
        );

        int xx;
        for (xx = x - 2; xx <= x + 2; ++xx) {
            for (int yy = y - 2; yy <= y + 2; ++yy) {
                for (int zz = z - 2; zz <= z + 2; ++zz) {
                    // increaseTaintedPlants(w, xx, yy, zz);
                }
            }
        }

        /*for(xx = 0; xx < 100; ++xx) {
           FXWisp ef = new FXWisp(w, (double)((float)x + 0.5F), (double)((float)y +
        0.5F), (double)((float)z + 0.5F), (double)((float)x + 0.5F +
        (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0F), (double)((float)y + 0.5F
        + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0F), (double)((float)z +
        0.5F + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0F), 1.0F, 5);
           ef.setGravity(0.02F);
           ef.shrink = true;
           ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
        }*/
    }

    public static void spillTaint(World world, int x, int y, int z) {
        TileEntity tc = world.getTileEntity(x, y, z);
        if (tc != null && tc instanceof IConnection) {
            IConnection ic = (IConnection) tc;
            if (ic.getTaintedVis() > 0.0F) {
                int at = (int) ic.getTaintedVis();
                AuraManager.addTaintToClosest(world, x, y, z, at);
                world.playSoundEffect(
                    (double) x,
                    (double) y,
                    (double) z,
                    "random.fizz",
                    0.2F,
                    2.0F + world.rand.nextFloat() * 0.4F
                );

                for (int a = 0; a < Math.min(at, 50); ++a) {
                    float x1 = (float) x + world.rand.nextFloat();
                    float y1 = (float) y + world.rand.nextFloat();
                    float z1 = (float) z + world.rand.nextFloat();
                    FXWisp ef = new FXWisp(
                        world,
                        (double) x1,
                        (double) y1,
                        (double) z1,
                        (double) x1,
                        (double) (y1 + 1.0F),
                        (double) z1,
                        0.5F,
                        5
                    );
                    ef.tinkle = false;
                    Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                }
            }
        }
    }
}
