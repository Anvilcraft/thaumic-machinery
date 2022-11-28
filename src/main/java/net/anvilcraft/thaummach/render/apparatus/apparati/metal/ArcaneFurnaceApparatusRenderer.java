package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class ArcaneFurnaceApparatusRenderer implements IApparatusRenderer {
    public static ArcaneFurnaceApparatusRenderer INSTANCE
        = new ArcaneFurnaceApparatusRenderer();

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
        if (block.getRenderBlockPass() == 0 && !inv) {
            float w3 = 0.1875F;
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            rb.renderStandardBlock(block, i, j, k);
            rb.renderFaceYPos(
                block,
                (double) i,
                (double) ((float) j - 1.0F + w3),
                (double) k,
                block.iconArcaneFurnaceInside
            );
            rb.renderFaceXPos(
                block,
                (double) ((float) (i - 1) + w3),
                (double) j,
                (double) k,
                block.iconArcaneFurnaceInside
            );
            rb.renderFaceXNeg(
                block,
                (double) ((float) (i + 1) - w3),
                (double) j,
                (double) k,
                block.iconArcaneFurnaceInside
            );
            rb.renderFaceZPos(
                block,
                (double) i,
                (double) j,
                (double) ((float) (k - 1) + w3),
                block.iconArcaneFurnaceInside
            );
            rb.renderFaceZNeg(
                block,
                (double) i,
                (double) j,
                (double) ((float) (k + 1) - w3),
                block.iconArcaneFurnaceInside
            );
        } else if (inv) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ApparatusRenderingHelper.drawFaces(
                rb,
                block,
                block.iconArcaneFurnaceBottom,
                block.iconArcaneFurnaceTop,
                block.iconArcaneFurnaceSide,
                block.iconArcaneFurnaceSide,
                block.iconArcaneFurnaceSide,
                block.iconArcaneFurnaceSide,
                true
            );
            rb.setRenderBounds(0.1F, 0.1F, 0.1F, 0.9F, 0.99F, 0.9F);
            ApparatusRenderingHelper.drawFaces(
                rb, block, block.iconArcaneFurnaceInside, false
            );
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
