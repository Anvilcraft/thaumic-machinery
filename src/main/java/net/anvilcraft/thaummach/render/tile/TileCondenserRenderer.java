package net.anvilcraft.thaummach.render.tile;

import java.awt.Color;

import dev.tilera.auracore.api.CrystalColors;
import net.anvilcraft.thaummach.tiles.TileCondenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.ModelCrystal;

public class TileCondenserRenderer extends TileEntitySpecialRenderer {
    private ModelCrystal model = new ModelCrystal();
    private float bob = 0.0F;

    private void drawDisk(double x, double y, double z, float angle) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.3F, 0.0F, -0.3F);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/misc/portal2.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(200);
        tessellator.setColorRGBA_F(1.0F, 0.5F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(0.0, 0.0, 0.6, 0.0, 1.0);
        tessellator.addVertexWithUV(0.6, 0.0, 0.6, 1.0, 1.0);
        tessellator.addVertexWithUV(0.6, 0.0, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(0.6, 0.0, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(0.6, 0.0, 0.6, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.6, 0.0, 0.0);
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void
    renderEntityAt(TileCondenser condenser, double x, double y, double z, float fq) {
        if (condenser.degredation > 0.0F) {
            float tbob = this.bob;
            if (condenser.hasUpgrade((byte) 1)) {
                tbob = 0.0F;
            }

            this.bindTexture(
                new ResourceLocation("thaumcraft", "textures/models/crystal.png")
            );

            GL11.glEnable(0xba1);
            GL11.glEnable(0xbe2);
            GL11.glPushMatrix();
            GL11.glEnable(0x803a);
            GL11.glBlendFunc(770, 771);
            Color c = new Color(CrystalColors.getColorForShard(condenser.currentType));
            GL11.glColor4f(
                c.getRed() / 220.0f, c.getGreen() / 220.0f, c.getBlue() / 220.0f, 1.0F
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(220);
            GL11.glTranslatef(
                (float) x + 0.5F, (float) y + tbob + 0.95F, (float) z + 0.5F
            );
            GL11.glRotatef(condenser.angle, 0.0F, 1.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glScalef(0.15F, 0.45F, 0.15F);
            this.model.render();
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(0x803a);
            GL11.glPopMatrix();
            GL11.glDisable(0xbe2);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (condenser.hasUpgrade((byte) 1)) {
                this.drawDisk(
                    x,
                    y + 1.1749999523162842 + (double) (this.bob * 6.0F),
                    z,
                    360.0F - condenser.angle
                );
            }
        }
    }

    public void
    renderTileEntityAt(TileEntity te, double d, double d1, double d2, float f) {
        int count = Minecraft.getMinecraft().thePlayer.ticksExisted;
        this.bob = MathHelper.sin((float) count / 10.0F) * 0.05F + 0.05F;
        this.renderEntityAt((TileCondenser) te, d, d1, d2, f);
    }
}
