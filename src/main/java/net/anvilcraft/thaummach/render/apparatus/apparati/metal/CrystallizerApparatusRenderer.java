package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class CrystallizerApparatusRenderer implements IApparatusRenderer {
    public static CrystallizerApparatusRenderer INSTANCE
        = new CrystallizerApparatusRenderer();

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
        float w2 = 0.125F;
        if (block.getRenderBlockPass() == 0 || inv) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F + w2, 1.0F);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb,
                    block,
                    block.iconCrystallizerBottom,
                    block.iconCrystallizerTop,
                    block.iconCrystallizerSide,
                    block.iconCrystallizerSide,
                    block.iconCrystallizerSide,
                    block.iconCrystallizerSide,
                    true
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            // TODO: no inside in inventory
            if (!inv) {
                float w3 = 0.1875F;
                rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F + w2, 1.0F);
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
                rb.renderFaceYPos(
                    block,
                    (double) i,
                    (double) ((float) j - 0.49F),
                    (double) k,
                    block.iconArcaneFurnaceInside
                );
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
