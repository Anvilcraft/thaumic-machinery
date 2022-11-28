package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal.MetaVals;
import net.anvilcraft.thaummach.render.apparatus.ApparatusRenderingHelper;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileCrucible;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class CrucibleApparatusRenderer implements IApparatusRenderer {
    public static CrucibleApparatusRenderer INSTANCE = new CrucibleApparatusRenderer();

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
        BlockApparatusMetal block = (BlockApparatusMetal) block_;
        MetaVals md = MetaVals.get(meta);
        IIcon[] icons = getIcons(block, md);
        if (block.getRenderBlockPass() == 0 && !inv) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            rb.renderStandardBlock(block, i, j, k);
        } else if (inv &&
                (md == MetaVals.NORMAL_CRUCIBLE
                 || md == MetaVals.EYES_CRUCIBLE
                 || md == MetaVals.THAUMIUM_CRUCIBLE)
        ) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ApparatusRenderingHelper.drawFaces(
                rb,
                block,
                icons[3],
                icons[6],
                icons[5],
                icons[5],
                icons[5],
                icons[5],
                true
            );
        } else if (inv && md == MetaVals.SOUL_CRUCIBLE) {
            rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ApparatusRenderingHelper.drawFaces(
                rb,
                block,
                block.iconSoulCrucibleBottom,
                block.iconSoulCrucibleTopInv,
                block.iconSoulCrucibleFace3,
                block.iconSoulCrucibleFace3,
                block.iconSoulCrucibleFace3,
                block.iconSoulCrucibleFace3,
                true
            );
        }

        Tessellator tessellator = Tessellator.instance;
        if (!inv)
            tessellator.setBrightness(block.getMixedBrightnessForBlock(w, i, j, k));
        float f = 1.0F;
        int l = block.colorMultiplier(w, i, j, k);
        float f1 = (float) (l >> 16 & 255) / 255.0F;
        float f2 = (float) (l >> 8 & 255) / 255.0F;
        float f3 = (float) (l & 255) / 255.0F;
        float f5;
        if (EntityRenderer.anaglyphEnable) {
            float f6 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            f5 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f6;
            f2 = f4;
            f3 = f5;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        IIcon c = md != MetaVals.SOUL_CRUCIBLE ? icons[2] : block.iconSoulCrucibleFace3;
        IIcon c1 = md != MetaVals.SOUL_CRUCIBLE ? icons[1] : block.iconSoulCrucibleBottom;

        f5 = 0.123F;
        if (!inv) {
            if (block.getRenderBlockPass() == 0) {
                rb.renderFaceXPos(
                    block, (double) ((float) i - 1.0F + f5), (double) j, (double) k, c
                );
                rb.renderFaceXNeg(
                    block, (double) ((float) i + 1.0F - f5), (double) j, (double) k, c
                );
                rb.renderFaceZPos(
                    block, (double) i, (double) j, (double) ((float) k - 1.0F + f5), c
                );
                rb.renderFaceZNeg(
                    block, (double) i, (double) j, (double) ((float) k + 1.0F - f5), c
                );
                rb.renderFaceYPos(
                    block, (double) i, (double) ((float) j - 1.0F + 0.25F), (double) k,
                    c1
                );
                rb.renderFaceYNeg(
                    block, (double) i, (double) ((float) j + 1.0F - 0.75F), (double) k,
                    c1
                );

            } else if (block.getRenderBlockPass() == 1) {
                TileCrucible tc = (TileCrucible) w.getTileEntity(i, j, k);
                float tvis = tc.pureVis + tc.taintedVis;
                if (tvis > 0.1F) {
                    float h = Math.min(tvis, tc.maxVis);
                    float level = 0.75F * (h / tc.maxVis);
                    if (tc.maxVis == tvis) {
                        level = (float) ((double) level - 0.001);
                    }

                    float b = Math.min(1.0F, tc.pureVis / (tc.taintedVis + tc.pureVis));
                    tessellator.setBrightness(20 + (int) (b * 210.0F));
                    rb.renderFaceYPos(
                        block,
                        (double) i,
                        (double) ((float) j + 0.25F + level - 1.0F),
                        (double) k,
                        block.iconTcubeanim
                    );
                    if (tvis > tc.maxVis) {
                        // TODO: WTF
                        //rb.renderSouthFace(
                        //    block,
                        //    (double) i,
                        //    (double) j,
                        //    (double) k,
                        //    mod_ThaumCraft.visDripFX
                        //);
                        //rb.renderNorthFace(
                        //    block,
                        //    (double) i,
                        //    (double) j,
                        //    (double) k,
                        //    mod_ThaumCraft.visDripFX
                        //);
                        //rb.renderWestFace(
                        //    block,
                        //    (double) i,
                        //    (double) j,
                        //    (double) k,
                        //    mod_ThaumCraft.visDripFX
                        //);
                        //rb.renderEastFace(
                        //    block,
                        //    (double) i,
                        //    (double) j,
                        //    (double) k,
                        //    mod_ThaumCraft.visDripFX
                        //);
                    }
                }
            }
        }

        rb.overrideBlockTexture = null;
        rb.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public static IIcon[] getIcons(BlockApparatusMetal block, MetaVals meta) {
        switch (meta) {
            case NORMAL_CRUCIBLE:
                return block.iconsNormalCrucible;

            case EYES_CRUCIBLE:
                return block.iconsEyesCrucible;

            case THAUMIUM_CRUCIBLE:
                return block.iconsThaumiumCrucible;

            default:
                return null;
        }
    }
}
