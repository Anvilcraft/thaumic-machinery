package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileBore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BoreApparatusRenderer implements IApparatusRenderer {
    public static final BoreApparatusRenderer INSTANCE = new BoreApparatusRenderer();

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
        if (block.getRenderBlockPass() == 0 || inv) {
            boolean b1 = false;
            boolean b2 = false;
            boolean b3 = false;
            if (!inv) {
                TileBore te = (TileBore) w.getTileEntity(i, j, k);
                if (te.orientation == 0 || te.orientation == 1) {
                    b1 = true;
                }

                if (te.orientation == 4 || te.orientation == 5) {
                    b2 = true;
                }

                if (te.orientation == 2 || te.orientation == 3) {
                    b3 = true;
                }
            }

            float t2x = 0.125F;
            float t4x = 0.25F;
            IIcon t1 = block.iconGenerator3;
            IIcon t2 = block.iconGenerator2;
            IIcon tx = block.iconGenerator3;
            rb.overrideBlockTexture = tx;
            rb.setRenderBounds(t2x, t2x, t2x, 1.0F - t2x, 1.0F - t2x, 1.0F - t2x);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, t1, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            if (b1) {
                tx = t2;
            } else {
                tx = t1;
            }

            rb.overrideBlockTexture = tx;
            rb.setRenderBounds(t4x, 0.0F, t4x, 1.0F - t4x, t2x, 1.0F - t4x);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, tx, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(t4x, 1.0F - t2x, t4x, 1.0F - t4x, 1.0F, 1.0F - t4x);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, tx, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            if (b2) {
                tx = t2;
            } else {
                tx = t1;
            }

            rb.overrideBlockTexture = tx;
            rb.setRenderBounds(0.0F, t4x, t4x, t2x, 1.0F - t4x, 1.0F - t4x);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, tx, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(1.0F - t2x, t4x, t4x, 1.0F, 1.0F - t4x, 1.0F - t4x);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, tx, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            if (b3) {
                tx = t2;
            } else {
                tx = t1;
            }

            rb.overrideBlockTexture = tx;
            rb.setRenderBounds(t4x, t4x, 0.0F, 1.0F - t4x, 1.0F - t4x, t2x);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, tx, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(t4x, t4x, 1.0F - t2x, 1.0F - t4x, 1.0F - t4x, 1.0F);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(rb, block, tx, true);
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
