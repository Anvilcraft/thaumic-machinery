package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class PurifierApparatusRenderer implements IApparatusRenderer {
    public static PurifierApparatusRenderer INSTANCE = new PurifierApparatusRenderer();

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
        BlockApparatusFragile block = (BlockApparatusFragile) block_;
        if (block.getRenderBlockPass() == 0 || inv) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            if (inv) {
                BlockRenderer.drawFaces(
                    rb,
                    block,
                    block.iconPurifierTop,
                    block.iconPurifierTop,
                    block.iconPurifierFront,
                    block.iconPurifierFront,
                    block.iconPurifierSide,
                    block.iconPurifierSide,
                    false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
