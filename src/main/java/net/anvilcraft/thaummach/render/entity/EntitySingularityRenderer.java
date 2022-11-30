package net.anvilcraft.thaummach.render.entity;

import java.util.Random;

import net.anvilcraft.thaummach.entities.EntitySingularity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EntitySingularityRenderer extends Render {
    public EntitySingularityRenderer() {
        super.shadowSize = 0.1F;
    }

    public void
    renderEntityAt(EntitySingularity tg, double x, double y, double z, float fq) {
        if (tg.fuse > 0) {
            GL11.glPushMatrix();
            GL11.glDepthMask(false);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Minecraft.getMinecraft().renderEngine.bindTexture(
                TextureMap.locationItemsTexture
            );
            int i = 99;
            int size = 16;
            //float f1 = 256.0F;
            float f3 = 15.99F;

            float f1 = ActiveRenderInfo.rotationX;
            float fa = ActiveRenderInfo.rotationXZ;
            float f4 = ActiveRenderInfo.rotationZ;
            //float f4 = ActiveRenderInfo.rotationYZ;
            float f5 = ActiveRenderInfo.rotationXY;
            float x0 = ((float) (i % 16 * size) + 0.0F) / f1;
            float x1 = ((float) (i % 16 * size) + f3) / f1;
            float x2 = ((float) (i / 16 * size) + 0.0F) / f1;
            float x3 = ((float) (i / 16 * size) + f3) / f1;
            float f10 = 0.2F;
            float f11 = (float) x;
            float f12 = (float) y;
            float f13 = (float) z;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setBrightness(240);
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.addVertexWithUV(
                (double) (f11 - f1 * f10 - f4 * f10),
                (double) (f12 - fa * f10),
                (double) (f13 - f4 * f10 - f5 * f10),
                (double) x1,
                (double) x3
            );
            tessellator.addVertexWithUV(
                (double) (f11 - f1 * f10 + f4 * f10),
                (double) (f12 + fa * f10),
                (double) (f13 - f4 * f10 + f5 * f10),
                (double) x1,
                (double) x2
            );
            tessellator.addVertexWithUV(
                (double) (f11 + f1 * f10 + f4 * f10),
                (double) (f12 + fa * f10),
                (double) (f13 + f4 * f10 + f5 * f10),
                (double) x0,
                (double) x2
            );
            tessellator.addVertexWithUV(
                (double) (f11 + f1 * f10 - f4 * f10),
                (double) (f12 - fa * f10),
                (double) (f13 + f4 * f10 - f5 * f10),
                (double) x0,
                (double) x3
            );
            tessellator.draw();
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x, (float) y, (float) z);
            int q = Minecraft.getMinecraft().gameSettings.fancyGraphics ? 60 : 30;
            Tessellator tessellator = Tessellator.instance;
            RenderHelper.disableStandardItemLighting();
            float f1 = (float) Math.abs(tg.fuse) / 170.0F;
            float f3 = 0.9F;
            float f2 = 0.0F;
            if (f1 > 0.8F) {
                f2 = (f1 - 0.8F) / 0.2F;
            }

            Random random = new Random(245L);
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(3008);
            GL11.glEnable(2884);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();

            int i;
            for (i = 0; (float) i < (f1 + f1 * f1) / 2.0F * (float) q; ++i) {
                GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(
                    random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F
                );
                tessellator.startDrawing(6);
                float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
                float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
                fa /= 3.0F;
                f4 /= 3.0F;
                tessellator.setColorRGBA_I(16777215, (int) (255.0F * (1.0F - f2)));
                tessellator.addVertex(0.0, 0.0, 0.0);
                tessellator.setColorRGBA_I(16711935, 0);
                tessellator.addVertex(
                    -0.866 * (double) f4, (double) fa, (double) (-0.5F * f4)
                );
                tessellator.addVertex(
                    0.866 * (double) f4, (double) fa, (double) (-0.5F * f4)
                );
                tessellator.addVertex(0.0, (double) fa, (double) (1.0F * f4));
                tessellator.addVertex(
                    -0.866 * (double) f4, (double) fa, (double) (-0.5F * f4)
                );
                tessellator.draw();
            }

            for (i = 0; (float) i < (f3 + f3 * f3) / 2.0F * (float) q; ++i) {
                GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(
                    random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F
                );
                tessellator.startDrawing(6);
                float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
                float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
                fa /= 7.0F;
                f4 /= 7.0F;
                tessellator.setColorRGBA_I(16777215, (int) (255.0F * (1.0F - f2)));
                tessellator.addVertex(0.0, 0.0, 0.0);
                tessellator.setColorRGBA_I(255, 0);
                tessellator.addVertex(
                    -0.866 * (double) f4, (double) fa, (double) (-0.5F * f4)
                );
                tessellator.addVertex(
                    0.866 * (double) f4, (double) fa, (double) (-0.5F * f4)
                );
                tessellator.addVertex(0.0, (double) fa, (double) (1.0F * f4));
                tessellator.addVertex(
                    -0.866 * (double) f4, (double) fa, (double) (-0.5F * f4)
                );
                tessellator.draw();
            }

            GL11.glPopMatrix();
            GL11.glDepthMask(true);
            GL11.glDisable(2884);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }
    }

    public void
    doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.renderEntityAt((EntitySingularity) entity, d, d1, d2, f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return null;
    }
}
