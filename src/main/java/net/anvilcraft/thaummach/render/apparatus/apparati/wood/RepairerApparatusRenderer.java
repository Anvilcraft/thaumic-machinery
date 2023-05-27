package net.anvilcraft.thaummach.render.apparatus.apparati.wood;

import net.anvilcraft.thaummach.TMBlocks;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.blocks.BlockApparatusWood;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockRenderer;

public class RepairerApparatusRenderer implements IApparatusRenderer {
    public static final RepairerApparatusRenderer INSTANCE
        = new RepairerApparatusRenderer();

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
        BlockApparatusWood block = (BlockApparatusWood) block_;
        if (block.getRenderBlockPass() == 0 || inv) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            if (inv) {
                BlockRenderer.drawFaces(
                    rb,
                    block,
                    block.iconRestorerBottom,
                    block.iconRestorerBottom,
                    block.iconRestorerBottom,
                    block.iconRestorerBottom,
                    block.iconRestorerBottom,
                    block.iconRestorerBottom,
                    false
                );
            } else {
                rb.renderStandardBlock(block, x, y, z);
            }

            if (!inv) {
                rb.renderFaceYPos(
                    block,
                    (double) x,
                    (double) ((float) y - 1.0F + 0.425F),
                    (double) z,
                    block.iconRestorerBottom
                );
                rb.renderFaceYNeg(
                    block,
                    (double) x,
                    (double) ((float) (y + 1) - 0.425F),
                    (double) z,
                    block.iconRestorerBottom
                );
                rb.renderFaceXPos(
                    block,
                    (double) ((float) (x - 1) + 0.425F),
                    (double) y,
                    (double) z,
                    block.iconRestorerBottom
                );
                rb.renderFaceXNeg(
                    block,
                    (double) ((float) (x + 1) - 0.425F),
                    (double) y,
                    (double) z,
                    block.iconRestorerBottom
                );
                rb.renderFaceZPos(
                    block,
                    (double) x,
                    (double) y,
                    (double) ((float) (z - 1) + 0.425F),
                    block.iconRestorerBottom
                );
                rb.renderFaceZNeg(
                    block,
                    (double) x,
                    (double) y,
                    (double) ((float) (z + 1) - 0.425F),
                    block.iconRestorerBottom
                );
            } else {
                rb.setRenderBounds(0.01F, 0.01F, 0.01F, 0.99F, 0.99F, 0.99F);
                BlockRenderer.drawFaces(
                    rb,
                    block,
                    ((BlockApparatusMetal) TMBlocks.apparatusMetal)
                        .iconArcaneFurnaceInside,
                    false
                );
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
