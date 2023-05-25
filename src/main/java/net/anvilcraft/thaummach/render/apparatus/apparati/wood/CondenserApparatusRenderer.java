package net.anvilcraft.thaummach.render.apparatus.apparati.wood;

import net.anvilcraft.thaummach.blocks.BlockApparatusWood;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileCondenser;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class CondenserApparatusRenderer implements IApparatusRenderer {
    public static final CondenserApparatusRenderer INSTANCE
        = new CondenserApparatusRenderer();

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
        BlockApparatusWood block = (BlockApparatusWood) block_;

        float w4 = 0.25F;
        float w3 = 0.1875F;
        float w2 = 0.125F;
        if (block.getRenderBlockPass() == 0 || inv) {
            rb.setRenderBounds(w3, 1.0F - w4, w3, 1.0F - w3, 1.0F, 1.0F - w3);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb,
                    block,
                    block.iconCondenserTop,
                    block.iconCondenserTop,
                    block.iconCondenserSide,
                    block.iconCondenserSide,
                    block.iconCondenserSide,
                    block.iconCondenserSide,
                    false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(w3, 0.0F, w3, 1.0F - w3, w4, 1.0F - w3);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb,
                    block,
                    block.iconCondenserTop,
                    block.iconCondenserTop,
                    block.iconCondenserSide,
                    block.iconCondenserSide,
                    block.iconCondenserSide,
                    block.iconCondenserSide,
                    false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.overrideBlockTexture = block.iconCondenserPart;
            rb.setRenderBounds(0.5F - w2, w2, 0.0F, 0.5F + w2, 1.0F - w2, 1.0F);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb, block, block.iconCondenserPart, false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(0.0F, w2, 0.5F - w2, 1.0F, 1.0F - w2, 0.5F + w2);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb, block, block.iconCondenserPart, false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(0.5F - w2, w2, 0.0F, 0.5F + w2, 1.0F - w2, 1.0F);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb, block, block.iconCondenserPart, false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            rb.setRenderBounds(0.0F, w2, 0.5F - w2, 1.0F, 1.0F - w2, 0.5F + w2);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb, block, block.iconCondenserPart, false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            if (!inv) {
                TileCondenser tc = (TileCondenser) w.getTileEntity(i, j, k);
                if (tc != null && tc.hasUpgrade((byte) 0)) {
                    rb.overrideBlockTexture = null;
                    rb.setRenderBounds(w4, w4, w4, 1.0F - w4, 1.0F - w4, 1.0F - w4);

                    rb.renderFaceXNeg(block, i, j, k, block.iconCondenserSpeedUpgrade);
                    rb.renderFaceXPos(block, i, j, k, block.iconCondenserSpeedUpgrade);
                    rb.renderFaceZNeg(block, i, j, k, block.iconCondenserSpeedUpgrade);
                    rb.renderFaceZPos(block, i, j, k, block.iconCondenserSpeedUpgrade);
                }
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
