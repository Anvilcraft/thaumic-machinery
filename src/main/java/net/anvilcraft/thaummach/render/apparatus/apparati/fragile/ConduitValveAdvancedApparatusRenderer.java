package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileConduitValveAdvanced;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.renderers.block.BlockRenderer;

public class ConduitValveAdvancedApparatusRenderer implements IApparatusRenderer {
    public static final ConduitValveAdvancedApparatusRenderer INSTANCE
        = new ConduitValveAdvancedApparatusRenderer();

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
            TileConduitValveAdvanced tc
                = (TileConduitValveAdvanced) w.getTileEntity(i, j, k);
            float b = 0.0F;
            float total = 0.0F;
            float hfill = 0.0F;
            boolean visible = false;
            if (block.getRenderBlockPass() == 0) {
                switch (tc.setting) {
                    case 0:
                        rb.overrideBlockTexture = block.iconValveAdvancedOff;
                        break;
                    case 1:
                        rb.overrideBlockTexture = block.iconValveAdvancedOnVis;
                        break;
                    case 2:
                        rb.overrideBlockTexture = block.iconValveAdvancedOnTaint;
                }

                rb.setRenderBounds(w4, w4, w4, 1.0F - w4, 1.0F - w4, 1.0F - w4);
                rb.renderStandardBlock(block, i, j, k);
                rb.overrideBlockTexture = block.iconConduitExtension;
            } else {
                visible = tc.pureVis + tc.taintedVis >= 0.1F;
                if (visible) {
                    b = Math.min(
                        1.0F, tc.pureVis / (tc.taintedVis + tc.pureVis)
                    );
                    total = Math.min(tc.pureVis + tc.taintedVis, tc.maxVis);
                    hfill = (1.0F - wq * 2.0F) * (total / tc.maxVis);
                    tessellator.setBrightness(20 + (int) (b * 210.0F));
                    tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                    if (Minecraft.getMinecraft().theWorld.rand.nextInt(50) == 1
                        && w.isAirBlock(i, j - 1, k)
                        && tc.pureVis + tc.taintedVis > 3.5F) {
                        // TODO: WTF
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
                loc.facing = ForgeDirection.getOrientation(dir).getOpposite();

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
                    } else if (visible && (((IConnection)te).getPureVis() + ((IConnection)te).getTaintedVis() > 0.1F || !((IConnection)te).isVisConduit())) {
                        ConduitApparatusRenderer.renderConduitVis(
                            w, rb, i, j, k, block, dir, hfill
                        );
                    }
                }
            }
        } else {
            rb.setRenderBounds(w6, 0.0F, w6, 1.0F - w6, 1.0F, 1.0F - w6);
            BlockRenderer.drawFaces(
                rb, block, block.iconConduitInventory, false
            );
            rb.setRenderBounds(w4, w4, w4, 1.0F - w4, 1.0F - w4, 1.0F - w4);
            BlockRenderer.drawFaces(
                rb, block, block.iconValveAdvancedOff, false
            );
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
