package net.anvilcraft.thaummach.render.apparatus;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class ApparatusRenderingHelper {
    public static void
    drawFaces(RenderBlocks renderblocks, Block block, IIcon i, boolean st) {
        drawFaces(renderblocks, block, i, i, i, i, i, i, st);
    }

    public static void drawFaces(
        RenderBlocks renderblocks,
        Block block,
        IIcon i1,
        IIcon i2,
        IIcon i3,
        IIcon i4,
        IIcon i5,
        IIcon i6,
        boolean solidtop
    ) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, i1);
        tessellator.draw();
        if (solidtop) {
            GL11.glDisable(3008);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0, 0.0, 0.0, i2);
        tessellator.draw();
        if (solidtop) {
            GL11.glEnable(3008);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, i3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceZPos(block, 0.0, 0.0, 0.0, i4);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXPos(block, 0.0, 0.0, 0.0, i5);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, i6);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
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
