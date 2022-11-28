package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import dev.tilera.auracore.api.HelperLocation;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileConduitTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ConduitTankApparatusRenderer implements IApparatusRenderer {
    public static final ConduitTankApparatusRenderer INSTANCE
        = new ConduitTankApparatusRenderer();

    @Override
    public void renderApparatus(
        IBlockAccess w,
        RenderBlocks rb,
        int i,
        int j,
        int k,
        Block block_,
        int md,
        boolean inv
    ) {
        BlockApparatusFragile block = (BlockApparatusFragile) block_;
        float w1;
        float w2;
        if (block.getRenderBlockPass() == 0 || inv) {
            w1 = 0.0625F;
            w2 = 0.125F;
            IIcon t1 = block.iconTankBottom;
            IIcon t2 = block.iconTankSide;
            // TODO: WTF
            //if (md != 3) {
            //    t1 = 78;
            //    t2 = 79;
            //}

            rb.setRenderBounds(w1, 0.0F, w1, 1.0F - w1, 1.0F, 1.0F - w1);
            if (inv) {
                ApparatusRenderingHelper.drawFaces(
                    rb, block, t1, t1, t2, t2, t2, t2, false
                );
            } else {
                rb.renderStandardBlock(block, i, j, k);
            }

            if (!inv) {
                rb.overrideBlockTexture = block.iconTankBottom;
                TileConduitTank tc = (TileConduitTank) w.getTileEntity(i, j, k);
                HelperLocation loc = new HelperLocation(tc);
                loc.facing = ForgeDirection.WEST;
                TileEntity te = loc.getConnectableTile(w);
                if (te != null && tc.getConnectable(loc.facing)
                    && !(te instanceof TileConduitTank)) {
                    rb.setRenderBounds(
                        0.0F, 0.5F - w2, 0.5F - w2, w1, 0.5F + w2, 0.5F + w2
                    );
                    rb.renderStandardBlock(block, i, j, k);
                }

                loc = new HelperLocation(tc);
                loc.facing = ForgeDirection.EAST;
                te = loc.getConnectableTile(w);
                if (te != null && tc.getConnectable(loc.facing)
                    && !(te instanceof TileConduitTank)) {
                    rb.setRenderBounds(
                        1.0F - w1, 0.5F - w2, 0.5F - w2, 1.0F, 0.5F + w2, 0.5F + w2
                    );
                    rb.renderStandardBlock(block, i, j, k);
                }

                loc = new HelperLocation(tc);
                loc.facing = ForgeDirection.NORTH;
                te = loc.getConnectableTile(w);
                if (te != null && tc.getConnectable(loc.facing)
                    && !(te instanceof TileConduitTank)) {
                    rb.setRenderBounds(
                        0.5F - w2, 0.5F - w2, 0.0F, 0.5F + w2, 0.5F + w2, w1
                    );
                    rb.renderStandardBlock(block, i, j, k);
                }

                loc = new HelperLocation(tc);
                loc.facing = ForgeDirection.SOUTH;
                te = loc.getConnectableTile(w);
                if (te != null && tc.getConnectable(loc.facing)
                    && !(te instanceof TileConduitTank)) {
                    rb.setRenderBounds(
                        0.5F - w2, 0.5F - w2, 1.0F - w1, 0.5F + w2, 0.5F + w2, 1.0F
                    );
                    rb.renderStandardBlock(block, i, j, k);
                }
            }
        }

        rb.overrideBlockTexture = null;

        if (block.getRenderBlockPass() == 0 && !inv) {
            w1 = 0.003F;
            w2 = 0.0625F;
            TileConduitTank tc = (TileConduitTank) w.getTileEntity(i, j, k);
            if (tc != null && tc.pureVis + tc.taintedVis > 0.1F) {
                Tessellator tessellator = Tessellator.instance;
                float hfill = (1.0F - w1 * 2.0F)
                    * ((tc.pureVis + tc.taintedVis) / tc.getMaxVis());
                float b = Math.min(1.0F, tc.pureVis / (tc.taintedVis + tc.pureVis));
                rb.setRenderBounds(
                    w1 + w2, w1, w1 + w2, 1.0F - w1 - w2, w1 + hfill, 1.0F - w1 - w2
                );
                tessellator.setBrightness(20 + (int) (b * 210.0F));
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                rb.renderFaceZPos(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceXNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceXPos(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceYPos(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceYNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
            }
        }

        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
