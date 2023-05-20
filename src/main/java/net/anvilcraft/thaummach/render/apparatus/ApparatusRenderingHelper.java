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
}
