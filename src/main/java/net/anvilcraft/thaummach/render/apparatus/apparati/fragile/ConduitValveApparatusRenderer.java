package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileConduitValve;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ConduitValveApparatusRenderer implements IApparatusRenderer {
    public static final ConduitValveApparatusRenderer INSTANCE
        = new ConduitValveApparatusRenderer();

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
        float w4 = 0.25F;
        float wq = 0.38125F;
        float w6 = 0.375F;
        Tessellator tessellator = Tessellator.instance;
        if (!inv) {
            TileConduitValve tc = (TileConduitValve) w.getTileEntity(i, j, k);
            float b = Math.min(1.0F, tc.pureVis / (tc.taintedVis + tc.pureVis));
            float total = 0.0F;
            float hfill = 0.0F;
            boolean visible = tc.pureVis + tc.taintedVis >= 0.1F;
            if (block.getRenderBlockPass() == 0) {
                if (tc.open) {
                    rb.overrideBlockTexture = block.iconValveOn;
                } else {
                    rb.overrideBlockTexture = block.iconValveOff;
                }

                rb.setRenderBounds(w4, w4, w4, 1.0F - w4, 1.0F - w4, 1.0F - w4);
                rb.renderStandardBlock(block, i, j, k);
                rb.overrideBlockTexture = block.iconConduitExtension;
            } else {
                if (visible) {
                    total = Math.min(tc.pureVis + tc.taintedVis, tc.maxVis);
                    hfill = (1.0F - wq * 2.0F) * (total / tc.maxVis);
                    tessellator.setBrightness(20 + (int) (b * 210.0F));
                    tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                    if (Minecraft.getMinecraft().theWorld.rand.nextInt(50) == 1
                        && w.isAirBlock(i, j - 1, k)
                        && tc.pureVis + tc.taintedVis > 3.5F) {
                        // TODO: FXDrip
                        //FXDrip obj = new FXDrip(
                        //    w,
                        //    (double) ((float) i + w4 + w.rand.nextFloat() * w6),
                        //    (double) ((float) j + w4 - 0.05F),
                        //    (double) ((float) k + w4 + w.rand.nextFloat() * w6)
                        //);
                        //obj.func_40097_b(
                        //    (0.4F + w.rand.nextFloat() * 0.2F) * (b + 0.1F),
                        //    0.0F,
                        //    (0.8F + w.rand.nextFloat() * 0.2F) * (b + 0.1F)
                        //);
                        //ModLoader.getMinecraftInstance().effectRenderer.addEffect(obj);
                    }
                }
            }

            for (int dir = 0; dir < 6; ++dir) {
                HelperLocation loc = new HelperLocation(tc);
                switch (dir) {
                    case 0:
                        loc.facing = ForgeDirection.UP;
                        break;
                    case 1:
                        loc.facing = ForgeDirection.DOWN;
                        break;
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
                if (te != null) {
                    if (block.getRenderBlockPass() == 0) {
                        switch (dir) {
                            case 0:
                                rb.setRenderBounds(
                                    w6, 1.0F - w4, w6, 1.0F - w6, 1.0F, 1.0F - w6
                                );
                                rb.renderStandardBlock(block, i, j, k);
                                break;
                            case 1:
                                rb.setRenderBounds(
                                    w6, 0.0F, w6, 1.0F - w6, w6, 1.0F - w6
                                );
                                rb.renderStandardBlock(block, i, j, k);
                                break;
                            case 2:
                                rb.setRenderBounds(
                                    w6, w6, 1.0F - w4, 1.0F - w6, 1.0F - w6, 1.0F
                                );
                                rb.renderStandardBlock(block, i, j, k);
                                break;
                            case 3:
                                rb.setRenderBounds(
                                    w6, w6, 0.0F, 1.0F - w6, 1.0F - w6, w4
                                );
                                rb.renderStandardBlock(block, i, j, k);
                                break;
                            case 4:
                                rb.setRenderBounds(
                                    1.0F - w4, w6, w6, 1.0F, 1.0F - w6, 1.0F - w6
                                );
                                rb.renderStandardBlock(block, i, j, k);
                                break;
                            case 5:
                                rb.setRenderBounds(
                                    0.0F, w6, w6, w4, 1.0F - w6, 1.0F - w6
                                );
                                rb.renderStandardBlock(block, i, j, k);
                        }
                        if (visible) {
                            rb.overrideBlockTexture = null;
                            tessellator.setBrightness(20 + (int) (b * 210.0F));
                            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                            ConduitApparatusRenderer.renderConduitVis(
                                w, rb, i, j, k, block, dir, hfill
                            );
                            rb.overrideBlockTexture = block.iconConduitExtension;
                        }
                    }
                }
            }
        } else {
            rb.setRenderBounds(w6, 0.0F, w6, 1.0F - w6, 1.0F, 1.0F - w6);
            ApparatusRenderingHelper.drawFaces(
                rb, block, block.iconConduitInventory, false
            );
            rb.setRenderBounds(w4, w4, w4, 1.0F - w4, 1.0F - w4, 1.0F - w4);
            ApparatusRenderingHelper.drawFaces(rb, block, block.iconValveOn, false);
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
