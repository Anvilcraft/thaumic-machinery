package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class VoidInterfaceApparatusRenderer implements IApparatusRenderer {
    public static VoidInterfaceApparatusRenderer INSTANCE
        = new VoidInterfaceApparatusRenderer();

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
        float t1x = 0.0625F;
        float t3x = 0.1875F;
        float t4x = 0.25F;
        BlockApparatusMetal block = (BlockApparatusMetal) block_;
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, t3x, 1.0F);
            if (inv) {
                BlockRenderer.drawFaces(
                    rb,
                    block,
                    block.iconVoidInterfaceBottom,
                    block.iconVoidInterfaceBottom,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    true
                );
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(0.0F, 0.5F - t4x, 0.0F, 1.0F, 0.5F - t1x, 1.0F);
            if (inv) {
                BlockRenderer.drawFaces(
                    rb,
                    block,
                    block.iconVoidInterfaceBottom,
                    block.iconVoidInterfaceBottom,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    true
                );
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            rb.setRenderBounds(t4x, 0.5F - t1x, t4x, 1.0F - t4x, 0.5F + t4x, 1.0F - t4x);
            if (inv) {
                BlockRenderer.drawFaces(
                    rb,
                    block,
                    block.iconVoidInterfaceBottom,
                    block.iconVoidInterfaceBottom,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    block.iconVoidInterfaceSide,
                    true
                );
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

        rb.setRenderBounds(t1x, t3x, t1x, 1.0F - t1x, 0.5F - t4x, 1.0F - t1x);
        if (!inv) {
            rb.renderFaceXNeg(
                block, (double) x, (double) y, (double) z, block.iconTcubeanim
            );
            rb.renderFaceXPos(
                block, (double) x, (double) y, (double) z, block.iconTcubeanim
            );
            rb.renderFaceZNeg(
                block, (double) x, (double) y, (double) z, block.iconTcubeanim
            );
            rb.renderFaceZPos(
                block, (double) x, (double) y, (double) z, block.iconTcubeanim
            );
        } else {
            rb.overrideBlockTexture = block.iconTcubeanim;
            BlockRenderer.drawFaces(rb, block, block.iconTcubeanim, true);
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
