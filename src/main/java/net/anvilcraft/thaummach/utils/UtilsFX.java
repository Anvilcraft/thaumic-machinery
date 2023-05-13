package net.anvilcraft.thaummach.utils;

import dev.tilera.auracore.client.FXSparkle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import thaumcraft.client.fx.particles.FXWisp;

public class UtilsFX {
    public static void poof(World worldObj, float x, float y, float z) {
        int puffs = 6;

        for (int a = 0; a < puffs; ++a) {
            worldObj.spawnParticle(
                "explode",
                (double) (x + worldObj.rand.nextFloat()),
                (double) (y + worldObj.rand.nextFloat()),
                (double) (z + worldObj.rand.nextFloat()),
                -0.0,
                -0.0,
                -0.0
            );
        }
    }

    public static void poofUpwards(World w, int x, int y, int z, int color) {
        int puffs = 10;

        for (int a = 0; a < puffs; ++a) {
            float xx = (float) x + w.rand.nextFloat();
            float zz = (float) z + w.rand.nextFloat();
            FXWisp ef = new FXWisp(
                w,
                (double) xx,
                (double) y,
                (double) zz,
                (double) xx,
                (double) ((float) y + 0.5F + w.rand.nextFloat()),
                (double) zz,
                0.3F,
                color
            );
            ef.tinkle = false;
            ef.shrink = true;
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        }
    }

    public static void sparkleDown(World w, int x, int y, int z, int color) {
        int puffs = 16;

        for (int a = 0; a < puffs; ++a) {
            float xx = (float) x + w.rand.nextFloat();
            float zz = (float) z + w.rand.nextFloat();
            FXSparkle ef = new FXSparkle(
                w,
                (double) xx,
                (double) ((float) y + w.rand.nextFloat() * 0.5F),
                (double) zz,
                (double) xx,
                (double) y,
                (double) zz,
                2.0F,
                color,
                5
            );
            ef.tinkle = true;
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        }
    }

    public static void sparkleUp(World w, int x, int y, int z, int color) {
        int puffs = 16;

        for (int a = 0; a < puffs; ++a) {
            float xx = (float) x + w.rand.nextFloat();
            float zz = (float) z + w.rand.nextFloat();
            FXSparkle ef = new FXSparkle(
                w,
                (double) xx,
                (double) y,
                (double) zz,
                (double) xx,
                (double) ((float) y + w.rand.nextFloat() * 0.5F),
                (double) zz,
                2.0F,
                color,
                5
            );
            ef.tinkle = true;
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        }
    }

    public static void renderItemIn2D(
        Tessellator tes,
        float p_78439_1_,
        float p_78439_2_,
        float p_78439_3_,
        float p_78439_4_,
        int p_78439_5_,
        int p_78439_6_,
        float p_78439_7_,
        int brightness
    ) {
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, 0.0F, 1.0F);
        tes.addVertexWithUV(0.0D, 0.0D, 0.0D, (double) p_78439_1_, (double) p_78439_4_);
        tes.addVertexWithUV(1.0D, 0.0D, 0.0D, (double) p_78439_3_, (double) p_78439_4_);
        tes.addVertexWithUV(1.0D, 1.0D, 0.0D, (double) p_78439_3_, (double) p_78439_2_);
        tes.addVertexWithUV(0.0D, 1.0D, 0.0D, (double) p_78439_1_, (double) p_78439_2_);
        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, 0.0F, -1.0F);
        tes.addVertexWithUV(
            0.0D,
            1.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_1_,
            (double) p_78439_2_
        );
        tes.addVertexWithUV(
            1.0D,
            1.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_3_,
            (double) p_78439_2_
        );
        tes.addVertexWithUV(
            1.0D,
            0.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_3_,
            (double) p_78439_4_
        );
        tes.addVertexWithUV(
            0.0D,
            0.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_1_,
            (double) p_78439_4_
        );
        tes.draw();
        float f5 = 0.5F * (p_78439_1_ - p_78439_3_) / (float) p_78439_5_;
        float f6 = 0.5F * (p_78439_4_ - p_78439_2_) / (float) p_78439_6_;
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        int k;
        float f7;
        float f8;

        for (k = 0; k < p_78439_5_; ++k) {
            f7 = (float) k / (float) p_78439_5_;
            f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
            tes.addVertexWithUV(
                (double) f7,
                0.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_4_
            );
            tes.addVertexWithUV(
                (double) f7, 0.0D, 0.0D, (double) f8, (double) p_78439_4_
            );
            tes.addVertexWithUV(
                (double) f7, 1.0D, 0.0D, (double) f8, (double) p_78439_2_
            );
            tes.addVertexWithUV(
                (double) f7,
                1.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_2_
            );
        }

        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(1.0F, 0.0F, 0.0F);
        float f9;

        for (k = 0; k < p_78439_5_; ++k) {
            f7 = (float) k / (float) p_78439_5_;
            f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
            f9 = f7 + 1.0F / (float) p_78439_5_;
            tes.addVertexWithUV(
                (double) f9,
                1.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_2_
            );
            tes.addVertexWithUV(
                (double) f9, 1.0D, 0.0D, (double) f8, (double) p_78439_2_
            );
            tes.addVertexWithUV(
                (double) f9, 0.0D, 0.0D, (double) f8, (double) p_78439_4_
            );
            tes.addVertexWithUV(
                (double) f9,
                0.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_4_
            );
        }

        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, 1.0F, 0.0F);

        for (k = 0; k < p_78439_6_; ++k) {
            f7 = (float) k / (float) p_78439_6_;
            f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
            f9 = f7 + 1.0F / (float) p_78439_6_;
            tes.addVertexWithUV(
                0.0D, (double) f9, 0.0D, (double) p_78439_1_, (double) f8
            );
            tes.addVertexWithUV(
                1.0D, (double) f9, 0.0D, (double) p_78439_3_, (double) f8
            );
            tes.addVertexWithUV(
                1.0D,
                (double) f9,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_3_,
                (double) f8
            );
            tes.addVertexWithUV(
                0.0D,
                (double) f9,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_1_,
                (double) f8
            );
        }

        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, -1.0F, 0.0F);

        for (k = 0; k < p_78439_6_; ++k) {
            f7 = (float) k / (float) p_78439_6_;
            f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
            tes.addVertexWithUV(
                1.0D, (double) f7, 0.0D, (double) p_78439_3_, (double) f8
            );
            tes.addVertexWithUV(
                0.0D, (double) f7, 0.0D, (double) p_78439_1_, (double) f8
            );
            tes.addVertexWithUV(
                0.0D,
                (double) f7,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_1_,
                (double) f8
            );
            tes.addVertexWithUV(
                1.0D,
                (double) f7,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_3_,
                (double) f8
            );
        }

        tes.draw();
    }
}
