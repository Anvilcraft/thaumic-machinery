package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class SoulBrazierApparatusRenderer implements IApparatusRenderer {
    public static SoulBrazierApparatusRenderer INSTANCE
        = new SoulBrazierApparatusRenderer();

    @Override
    public void renderApparatus(
        IBlockAccess w,
        RenderBlocks rb,
        int i,
        int j,
        int k,
        Block block_,
        int meta,
        boolean inv
    ) {
        BlockApparatusMetal block = (BlockApparatusMetal) block_;

        float t4 = 0.25F;
        float t2 = 0.125F;
        float t6 = 0.375F;
        rb.setRenderBounds(t2, 0.5F, t6, t4, 1.0F, 1.0F - t6);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                true
            );
        } else {
            rb.renderStandardBlock(block, i, j, k);
        }

        rb.setRenderBounds(t6, 0.5F, t2, 1.0F - t6, 1.0F, t4);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                true
            );
        } else {
            rb.renderStandardBlock(block, i, j, k);
        }

        rb.setRenderBounds(1.0F - t4, 0.5F, t6, 1.0F - t2, 1.0F, 1.0F - t6);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                true
            );
        } else {
            rb.renderStandardBlock(block, i, j, k);
        }

        rb.setRenderBounds(t6, 0.5F, 1.0F - t4, 1.0F - t6, 1.0F, 1.0F - t2);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                true
            );
        } else {
            rb.renderStandardBlock(block, i, j, k);
        }

        rb.setRenderBounds(t4, 0.0F, t4, 1.0F - t4, 0.5F + t4, 1.0F - t4);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                block.iconSoulBrazierBottom,
                true
            );
        } else {
            rb.renderStandardBlock(block, i, j, k);
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
