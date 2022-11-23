package net.anvilcraft.thaummach;

import net.minecraft.world.World;

public class AuraUtils {

    public static void taintExplosion(World w, int x, int y, int z) { 
        w.createExplosion(null, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 1.0F, false);
        
        int xx;
        for(xx = x - 2; xx <= x + 2; ++xx) {
           for(int yy = y - 2; yy <= y + 2; ++yy) {
              for(int zz = z - 2; zz <= z + 2; ++zz) {
                 //increaseTaintedPlants(w, xx, yy, zz);
              }
           }
        }
        
        /*for(xx = 0; xx < 100; ++xx) {
           FXWisp ef = new FXWisp(w, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), (double)((float)x + 0.5F + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0F), (double)((float)y + 0.5F + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0F), (double)((float)z + 0.5F + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0F), 1.0F, 5);
           ef.setGravity(0.02F);
           ef.shrink = true;
           ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
        }*/
        
     }
    
}
