package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class GeneratorApparatusRenderer implements IApparatusRenderer {
    public static final GeneratorApparatusRenderer INSTANCE
        = new GeneratorApparatusRenderer();

    @Override
    public void renderApparatus(
        IBlockAccess w,
        RenderBlocks rb,
        int x,
        int y,
        int z,
        Block block_,
        int meta,
        boolean inv
    ) {
        BlockApparatusMetal block = (BlockApparatusMetal) block_;
        float w2 = 0.125F;
        float w4 = 0.25F;
        if (block.getRenderBlockPass() == 0 || inv) {
            rb.setRenderBounds(w4, 0.0F, w4, 1.0F - w4, w2, 1.0F - w4);
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator1, true);
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(w4, 1.0F - w2, w4, 1.0F - w4, 1.0F, 1.0F - w4);
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator1, true);
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(
                1.0F - w2, 0.5F - w4, 0.5F - w4, 1.0F, 0.5F + w4, 0.5F + w4
            );
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator1, true);
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(0.0F, 0.5F - w4, 0.5F - w4, w2, 0.5F + w4, 0.5F + w4);
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator1, true);
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(
                0.5F - w4, 0.5F - w4, 1.0F - w2, 0.5F + w4, 0.5F + w4, 1.0F
            );
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator1, true);
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(0.5F - w4, 0.5F - w4, 0.0F, 0.5F + w4, 0.5F + w4, w2);
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator1, true);
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }
        }

        if (block.getRenderBlockPass() == 1 || inv) {
            rb.overrideBlockTexture = null;
            rb.setRenderBounds(w2, w2, w2, 1.0F - w2, 1.0F - w2, 1.0F - w2);
            if (inv) {
                BlockRenderer.drawFaces(rb, block, block.iconGenerator4, true);
            } else {
                rb.renderFaceYNeg(block, x, y, z, block.iconGenerator4);
                rb.renderFaceYPos(block, x, y, z, block.iconGenerator4);
                rb.renderFaceXNeg(block, x, y, z, block.iconGenerator4);
                rb.renderFaceXPos(block, x, y, z, block.iconGenerator4);
                rb.renderFaceZNeg(block, x, y, z, block.iconGenerator4);
                rb.renderFaceZPos(block, x, y, z, block.iconGenerator4);
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
