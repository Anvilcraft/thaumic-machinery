package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class ConduitPumpApparatusRenderer implements IApparatusRenderer {
    public static ConduitPumpApparatusRenderer INSTANCE
        = new ConduitPumpApparatusRenderer();

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
        BlockApparatusFragile block = (BlockApparatusFragile) block_;
        float w4 = 0.25F;
        float w3 = 0.1875F;
        float w2 = 0.125F;
        if (inv) {
            rb.setRenderBounds(w2, 0.0F, w2, 1.0F - w2, w2, 1.0F - w2);
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconConduitPumpTop,
                block.iconConduitPumpTop,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                false
            );
            rb.setRenderBounds(w3, w2, w3, 1.0F - w3, w4, 1.0F - w3);
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconConduitPumpTop,
                block.iconConduitPumpTop,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                false
            );
            rb.setRenderBounds(0.0F, w4, 0.0F, 1.0F, 0.5F + w2, 1.0F);
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconConduitPumpTop,
                block.iconConduitPumpTop,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                false
            );
            rb.setRenderBounds(w2, 0.5F + w2, w2, 1.0F - w2, 1.0F, 1.0F - w2);
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconConduitPumpTop,
                block.iconConduitPumpTop,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                block.iconConduitPumpSide,
                false
            );
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
