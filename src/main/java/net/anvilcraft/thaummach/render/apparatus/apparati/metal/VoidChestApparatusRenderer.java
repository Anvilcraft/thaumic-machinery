package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class VoidChestApparatusRenderer implements IApparatusRenderer {
    public static final VoidChestApparatusRenderer INSTANCE
        = new VoidChestApparatusRenderer();

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
        if (inv) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ApparatusRenderingHelper.drawFaces(
                rb,
                block,
                block.iconVoidChestBottom,
                block.iconVoidChestTop,
                block.iconVoidChestSide,
                block.iconVoidChestSide,
                block.iconVoidChestSide,
                block.iconVoidChestSide,
                true
            );
        } else if (block.getRenderBlockPass() == 0) {
            rb.renderStandardBlock(block, x, y, z);
        }
    }
}
