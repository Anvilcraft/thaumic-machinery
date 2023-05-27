package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileConduit;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.renderers.block.BlockRenderer;

public class FilterApparatusRenderer implements IApparatusRenderer {
    public static final FilterApparatusRenderer INSTANCE = new FilterApparatusRenderer();

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
        rb.setRenderBounds(w2, 0.0F, w2, 1.0F - w2, w3, 1.0F - w2);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconFilterBottom,
                block.iconFilterBottom,
                block.iconFilterSide,
                block.iconFilterSide,
                block.iconFilterSide,
                block.iconFilterSide,
                false
            );
        } else {
            rb.renderStandardBlock(block, x, y, z);
        }

        rb.setRenderBounds(w2, 1.0F - w3, w2, 1.0F - w2, 1.0F, 1.0F - w2);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconFilterBottom,
                block.iconFilterBottom,
                block.iconFilterSide,
                block.iconFilterSide,
                block.iconFilterSide,
                block.iconFilterSide,
                false
            );
        } else {
            rb.renderStandardBlock(block, x, y, z);
        }

        rb.setRenderBounds(w3, w3, w3, 1.0F - w3, 1.0F - w3, 1.0F - w3);
        if (inv) {
            BlockRenderer.drawFaces(
                rb,
                block,
                block.iconFilterBottom,
                block.iconFilterBottom,
                block.iconFilterSide,
                block.iconFilterSide,
                block.iconFilterSide,
                block.iconFilterSide,
                false
            );
        } else {
            rb.renderStandardBlock(block, x, y, z);
        }

        if (!inv) {
            float w6 = 0.375F;
            float wq = 0.38125F;
            TileConduit tc = (TileConduit) w.getTileEntity(x, y, z);
            Tessellator tessellator = Tessellator.instance;
            float b = 0.0F;
            float total = 0.0F;
            float hfill = 0.0F;
            boolean visible = false;
            visible = tc.pureVis + tc.taintedVis >= 0.1F;
            if (visible) {
                b = Math.min(1.0F, tc.pureVis / (tc.taintedVis + tc.pureVis));
                total = Math.min(tc.pureVis + tc.taintedVis, tc.maxVis);
                hfill = (1.0F - wq * 2.0F) * (total / tc.maxVis);
                tessellator.setBrightness(20 + (int) (b * 210.0F));
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            }

            for (int dir = 2; dir < 6; ++dir) {
                HelperLocation loc = new HelperLocation(tc);
                switch (dir) {
                    case 2:
                        loc.facing = ForgeDirection.SOUTH;
                        break;
                    case 3:
                        loc.facing = ForgeDirection.NORTH;
                        break;
                    case 4:
                        loc.facing = ForgeDirection.EAST;
                        break;
                    case 5:
                        loc.facing = ForgeDirection.WEST;
                }

                TileEntity te = loc.getConnectableTile(w);
                if (te != null && tc.getConnectable(loc.facing)) {
                    rb.overrideBlockTexture = block.iconConduitConnection;
                    switch (dir) {
                        case 2:
                            rb.setRenderBounds(w6, w6, 1.0F - w3, w6 + w4, w6 + w4, 1.0F);
                            rb.renderStandardBlock(block, x, y, z);
                            break;
                        case 3:
                            rb.setRenderBounds(w6, w6, 0.0F, w6 + w4, w6 + w4, w3);
                            rb.renderStandardBlock(block, x, y, z);
                            break;
                        case 4:
                            rb.setRenderBounds(1.0F - w3, w6, w6, 1.0F, w6 + w4, w6 + w4);
                            rb.renderStandardBlock(block, x, y, z);
                            break;
                        case 5:
                            rb.setRenderBounds(0.0F, w6, w6, w3, w6 + w4, w6 + w4);
                            rb.renderStandardBlock(block, x, y, z);
                    }
                    ConduitApparatusRenderer.renderConduitVis(
                        w, rb, x, y, z, block, dir, hfill
                    );
                }
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
