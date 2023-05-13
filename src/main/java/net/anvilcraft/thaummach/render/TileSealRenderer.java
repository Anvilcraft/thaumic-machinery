package net.anvilcraft.thaummach.render;

import net.anvilcraft.thaummach.TMBlocks;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileSealRenderer extends TileEntitySpecialRenderer {
    private float bob = 0.0F;
    private int count = 0;
    private static int[] colors
        = new int[] { 13532671, 16777088, 8421631, 8454016, 16744576, 4194368 };

    private void translateFromOrientation(double x, double y, double z, int orientation) {
        GL11.glPushMatrix();
        if (orientation == 0) {
            GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        } else if (orientation == 1) {
            GL11.glTranslatef((float) x, (float) y, (float) z);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        } else if (orientation == 2) {
            GL11.glTranslatef((float) x, (float) y, (float) z + 1.0F);
        } else if (orientation == 3) {
            GL11.glTranslatef((float) x + 1.0F, (float) y, (float) z);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        } else if (orientation == 4) {
            GL11.glTranslatef((float) x + 1.0F, (float) y, (float) z + 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        } else if (orientation == 5) {
            GL11.glTranslatef((float) x, (float) y, (float) z);
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        }
    }

    private void drawSeal(float angle, int level, int rune) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        if (level != 2) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(
                "thaummach", "textures/misc/s_" + level + "_" + rune + ".png"
            ));
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(
                new ResourceLocation("thaummach", "textures/misc/seal5.png")
            );
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(220);
        if (level == 2) {
            tessellator.setColorRGBA_I(colors[rune], 255);
        }

        tessellator.addVertexWithUV(0.0, 0.0, 1.0, 0.0, 1.0);
        tessellator.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0);
        tessellator.addVertexWithUV(1.0, 0.0, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
    }

    private void drawPortal(TileSeal seal, float angle, double x, double y, double z) {
        Tessellator tessellator = Tessellator.instance;
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glDisable(2896);
        if (seal.otherSeal != null && seal.renderer != null) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glColor4f(
                1.0f,
                1.0f,
                1.0f,
                1.0f
            );
            tessellator.setBrightness(220);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScaled(
                (double) (seal.pSize / 2.0F),
                (double) (seal.pSize / 2.0F),
                (double) (seal.pSize / 2.0F)
            );
            GL11.glBegin(6);
            GL11.glVertex2f(0.0F, 0.0F);

            for (int oh = 0; oh <= 10; ++oh) {
                double aa = 6.283185307179586 * (double) oh / 10.0;
                GL11.glVertex2f((float) Math.cos(aa), (float) Math.sin(aa));
            }

            GL11.glEnd();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            GL11.glEnable(3553);
            GL11.glPushMatrix();
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-seal.pSize / 2.0F, -0.01F, -seal.pSize / 2.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, seal.renderer.portalTexture);
            tessellator.startDrawingQuads();
            tessellator.setBrightness(220);
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
            tessellator.addVertexWithUV((double) seal.pSize, 0.0, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV(
                (double) seal.pSize, 0.0, (double) seal.pSize, 1.0, 1.0
            );
            tessellator.addVertexWithUV(0.0, 0.0, (double) seal.pSize, 0.0, 1.0);
            tessellator.draw();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-seal.pSize, 0.02F, -seal.pSize);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (seal.otherSeal != null && seal.renderer != null) {
            mc.renderEngine.bindTexture(
                new ResourceLocation("thaummach", "textures/misc/portal2.png")
            );
        } else {
            mc.renderEngine.bindTexture(
                new ResourceLocation("thaummach", "textures/misc/portal.png")
            );
        }

        tessellator.startDrawingQuads();
        tessellator.setBrightness(220);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(0.0, 0.0, (double) (seal.pSize * 2.0F), 0.0, 1.0);
        tessellator.addVertexWithUV(
            (double) (seal.pSize * 2.0F), 0.0, (double) (seal.pSize * 2.0F), 1.0, 1.0
        );
        tessellator.addVertexWithUV((double) (seal.pSize * 2.0F), 0.0, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2896);
    }

    public void renderEntityAt(TileSeal seal, double x, double y, double z, float fq) {
        int a = this.count % 360;
        Minecraft mc = Minecraft.getMinecraft();
        this.translateFromOrientation(
            (double) ((float) x),
            (double) ((float) y),
            (double) ((float) z),
            seal.orientation
        );
        GL11.glTranslatef(0.33F, 0.33F, -0.01F);
        GL11.glScalef(0.33f, 0.33f, 0.33f);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        IIcon icon = TMBlocks.seal.getIcon(0, 0);
        UtilsFX.renderItemIn2D(
            Tessellator.instance,
            icon.getMaxU(),
            icon.getMinV(),
            icon.getMinU(),
            icon.getMaxV(),
            icon.getIconWidth(),
            icon.getIconHeight(),
            0.0625f,
            128
        );
        GL11.glPopMatrix();
        this.translateFromOrientation(
            (double) ((float) x),
            (double) ((float) y),
            (double) ((float) z),
            seal.orientation
        );
        if (seal.runes[0] != -1) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, -0.02f);
            this.drawSeal(180.0F, 0, seal.runes[0]);
            GL11.glPopMatrix();
        }

        if (seal.runes[1] != -1) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, -0.03f);
            this.drawSeal((float) (-a), 1, seal.runes[1]);
            GL11.glPopMatrix();
        }

        if (seal.runes[2] != -1) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, -0.03f - this.bob);
            this.drawSeal((float) a, 2, seal.runes[2]);
            GL11.glPopMatrix();
        }

        if (seal.runes[0] == 0 && seal.runes[1] == 1 && seal.pSize > 0.0F) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, -seal.pSize / 5.0F);
            this.drawPortal(seal, (float) (-a * 4), x, y, z);
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    public void
    renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
        this.count = Minecraft.getMinecraft().thePlayer.ticksExisted;
        this.bob = MathHelper.sin((float) this.count / 10.0F) * 0.025F + 0.03F;
        this.renderEntityAt((TileSeal) tileentity, d, d1, d2, f);
    }
}
