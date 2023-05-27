package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileConduit;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.renderers.block.BlockRenderer;

public class ConduitApparatusRenderer implements IApparatusRenderer {
    public static ConduitApparatusRenderer INSTANCE = new ConduitApparatusRenderer();

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
        float w6 = 0.375F;
        float wq = 0.38125F;
        Tessellator tessellator = Tessellator.instance;
        if (!inv) {
            TileConduit tc = (TileConduit) w.getTileEntity(x, y, z);
            float b = 0.0F;
            float total = 0.0F;
            float hfill = 0.0F;
            boolean visible = false;
            total = Math.min(tc.pureVis + tc.taintedVis, tc.maxVis);
            hfill = (1.0F - wq * 2.0F) * (total / tc.maxVis);
            if (block.getRenderBlockPass() == 0) {
                rb.overrideBlockTexture = block.iconConduit;
                rb.setRenderBounds(w6, w6, w6, w6 + w4, w6 + w4, w6 + w4);
                rb.renderStandardBlock(block, x, y, z);
                visible = tc.pureVis + tc.taintedVis >= 0.1F;
                if (visible) {
                    b = Math.min(1.0F, tc.pureVis / (tc.taintedVis + tc.pureVis));
                    tessellator.setBrightness(20 + (int) (b * 210.0F));
                    tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                    rb.setRenderBounds(wq, wq, wq, 1.0F - wq, wq + hfill, 1.0F - wq);
                    rb.overrideBlockTexture = null;
                    rb.renderFaceXNeg(
                        block, (double) x, (double) y, (double) z, block.iconTcubeanim
                    );
                    rb.renderFaceXPos(
                        block, (double) x, (double) y, (double) z, block.iconTcubeanim
                    );
                    rb.renderFaceYNeg(
                        block, (double) x, (double) y, (double) z, block.iconTcubeanim
                    );
                    rb.renderFaceYPos(
                        block, (double) x, (double) y, (double) z, block.iconTcubeanim
                    );
                    rb.renderFaceZNeg(
                        block, (double) x, (double) y, (double) z, block.iconTcubeanim
                    );
                    rb.renderFaceZPos(
                        block, (double) x, (double) y, (double) z, block.iconTcubeanim
                    );
                    if (/*!Config.lowGfx && Config.pipedrips && */ Minecraft
                                .getMinecraft()
                                .theWorld.rand.nextInt(50)
                            == 1
                        && w.isAirBlock(x, y - 1, z)
                        && tc.pureVis + tc.taintedVis > 3.5F) {
                        // TODO: FXDrip
                        //FXDrip obj = new FXDrip(
                        //    w,
                        //    (double) ((float) x + w6 + w.rand.nextFloat() * w4),
                        //    (double) ((float) y + w6 - 0.05F),
                        //    (double) ((float) z + w6 + w.rand.nextFloat() * w4)
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

            rb.overrideBlockTexture = block.iconConduitExtension;
            for (int dir = 0; dir < 6; ++dir) {
                HelperLocation loc = new HelperLocation(tc);
                loc.facing = ForgeDirection.getOrientation(dir);

                TileEntity te = loc.getConnectableTile(w);
                if (te != null) {
                    if (block.getRenderBlockPass() == 0) {
                        switch (loc.facing.getOpposite().ordinal()) {
                            case 0:
                                rb.setRenderBounds(
                                    w6, w6 + w4, w6, w6 + w4, 1.0F, w6 + w4
                                );
                                rb.renderStandardBlock(block, x, y, z);
                                break;
                            case 1:
                                rb.setRenderBounds(w6, 0.0F, w6, w6 + w4, w6, w6 + w4);
                                rb.renderStandardBlock(block, x, y, z);
                                break;
                            case 2:
                                rb.setRenderBounds(
                                    w6, w6, w6 + w4, w6 + w4, w6 + w4, 1.0F
                                );
                                rb.renderStandardBlock(block, x, y, z);
                                break;
                            case 3:
                                rb.setRenderBounds(w6, w6, 0.0F, w6 + w4, w6 + w4, w6);
                                rb.renderStandardBlock(block, x, y, z);
                                break;
                            case 4:
                                rb.setRenderBounds(
                                    w6 + w4, w6, w6, 1.0F, w6 + w4, w6 + w4
                                );
                                rb.renderStandardBlock(block, x, y, z);
                                break;
                            case 5:
                                rb.setRenderBounds(0.0F, w6, w6, w6, w6 + w4, w6 + w4);
                                rb.renderStandardBlock(block, x, y, z);
                        }

                        if (visible) {
                            rb.overrideBlockTexture = null;
                            tessellator.setBrightness(20 + (int) (b * 210.0F));
                            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                            renderConduitVis(
                                w,
                                rb,
                                x,
                                y,
                                z,
                                block,
                                loc.facing.getOpposite().ordinal(),
                                hfill
                            );
                            rb.overrideBlockTexture = block.iconConduitExtension;
                        }
                    }
                }
            }
        } else {
            rb.setRenderBounds(w6, 0.0F, w6, w6 + w4, 1.0F, w6 + w4);
            BlockRenderer.drawFaces(
                rb, block, block.iconConduitInventory, false
            );
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderConduitVis(
        IBlockAccess w,
        RenderBlocks rb,
        int i,
        int j,
        int k,
        BlockApparatusFragile block,
        int dir,
        float hfill
    ) {
        float wq = 0.38125F;
        switch (dir) {
            case 0:
                rb.setRenderBounds(
                    0.5F - hfill / 2.0F,
                    wq + hfill,
                    0.5F - hfill / 2.0F,
                    0.5F + hfill / 2.0F,
                    1.0F,
                    0.5F + hfill / 2.0F
                );
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZPos(
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
                break;
            case 1:
                rb.setRenderBounds(
                    0.5F - hfill / 2.0F,
                    0.0F,
                    0.5F - hfill / 2.0F,
                    0.5F + hfill / 2.0F,
                    wq,
                    0.5F + hfill / 2.0F
                );
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZPos(
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
                break;
            case 2:
                rb.setRenderBounds(wq, wq, 1.0F - wq, 1.0F - wq, wq + hfill, 1.0F);
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZPos(
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
                break;
            case 3:
                rb.setRenderBounds(wq, wq, 0.0F, 1.0F - wq, wq + hfill, wq);
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZPos(
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
                break;
            case 4:
                rb.setRenderBounds(1.0F - wq, wq, wq, 1.0F, wq + hfill, 1.0F - wq);
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZPos(
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
                break;
            case 5:
                rb.setRenderBounds(0.0F, wq, wq, wq, wq + hfill, 1.0F - wq);
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) k, block.iconTcubeanim
                );
                rb.renderFaceZPos(
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
                break;
        }
    }
}
