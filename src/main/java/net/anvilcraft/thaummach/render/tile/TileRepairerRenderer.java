package net.anvilcraft.thaummach.render.tile;

import net.anvilcraft.thaummach.render.model.ModelGear;
import net.anvilcraft.thaummach.tiles.TileRepairer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileRepairerRenderer extends TileEntitySpecialRenderer {
    private ModelGear model = new ModelGear();

    public void renderEntityAt(TileRepairer rp, double x, double y, double z, float fq) {
        int count = Minecraft.getMinecraft().thePlayer.ticksExisted;
        int angle = 0;
        if (rp.worked) {
            angle = count % 360;
        }

        this.bindTexture(new ResourceLocation("thaummach", "textures/models/gear.png"));
        GL11.glEnable(2977);
        GL11.glEnable(3042);
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glScalef(0.43F, 0.94F, 0.94F);
        GL11.glRotatef((float) angle, 1.0F, 0.0F, 0.0F);
        this.model.render();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.94F, 0.94F, 0.43F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) angle, 1.0F, 0.0F, 0.0F);
        this.model.render();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(0.94F, 0.43F, 0.94F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef((float) angle, 1.0F, 0.0F, 0.0F);
        this.model.render();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void
    renderTileEntityAt(TileEntity te, double d, double d1, double d2, float f) {
        this.renderEntityAt((TileRepairer) te, d, d1, d2, f);
    }
}
