package net.anvilcraft.thaummach.render.apparatus;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class SimpleBlockApparatusRenderer implements IApparatusRenderer {
    public static final SimpleBlockApparatusRenderer INSTANCE = new SimpleBlockApparatusRenderer();

    @Override
    public void renderApparatus(
        IBlockAccess w, RenderBlocks rb, int x, int y, int z, Block block, int meta, boolean inv
    ) {
        if (inv) {
            rb.setRenderBoundsFromBlock(block);
            BlockRenderer.drawFaces(
                rb,
                block,
                block.getIcon(0, meta),
                block.getIcon(1, meta),
                block.getIcon(2, meta),
                block.getIcon(3, meta),
                block.getIcon(4, meta),
                block.getIcon(5, meta),
                true
            );
        } else {
            rb.renderStandardBlock(block, x, y, z);
        }
    }
}
