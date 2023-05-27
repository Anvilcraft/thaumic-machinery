package net.anvilcraft.thaummach.render.apparatus.apparati.wood;

import net.anvilcraft.thaummach.TMBlocks;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.blocks.BlockApparatusWood;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class DuplicatorApparatusRenderer implements IApparatusRenderer {
    public static final DuplicatorApparatusRenderer INSTANCE = new DuplicatorApparatusRenderer();

    @Override
    public void renderApparatus(
        IBlockAccess w, RenderBlocks rb, int x, int y, int z, Block block_, int meta, boolean inv
    ) {
        BlockApparatusWood block = (BlockApparatusWood) block_;
        float w3 = 0.1775F;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconDuplicatorBottom,
                block.iconDuplicatorBottom,
                block.iconDuplicatorSide,
                block.iconDuplicatorSide,
                block.iconDuplicatorSide,
                block.iconDuplicatorFront,
                true
            );
        } else {
            rb.renderStandardBlock(block, x, y, z);
        }

        if (!inv) {
            rb.renderFaceYPos(
                block,
                (double) x,
                (double) ((float) y - 1.0F + w3),
                (double) z,
                block.iconDuplicatorInside
            );
            rb.renderFaceXPos(
                block,
                (double) ((float) (x - 1) + w3),
                (double) y,
                (double) z,
                block.iconDuplicatorInside
            );
            rb.renderFaceXNeg(
                block,
                (double) ((float) (x + 1) - w3),
                (double) y,
                (double) z,
                block.iconDuplicatorInside
            );
            rb.renderFaceZPos(
                block,
                (double) x,
                (double) y,
                (double) ((float) (z - 1) + w3),
                block.iconDuplicatorInside
            );
            rb.renderFaceZNeg(
                block,
                (double) x,
                (double) y,
                (double) ((float) (z + 1) - w3),
                block.iconDuplicatorInside
            );
        } else {
            rb.setRenderBounds(0.1F, 0.1F, 0.1F, 0.9F, 0.99F, 0.9F);
            BlockRenderer.drawFaces(
                rb,
                block,
                ((BlockApparatusMetal) TMBlocks.apparatusMetal).iconArcaneFurnaceInside,
                false
            );
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
