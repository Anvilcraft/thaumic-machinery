package net.anvilcraft.thaummach.render.tile;

import net.anvilcraft.thaummach.tiles.TileBore;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.ModelCrystal;

public class TileBoreRenderer extends TileEntitySpecialRenderer {
    private ModelCrystal model = new ModelCrystal();

    public void renderEntityAt(TileBore cr, double x, double y, double z, float fq) {
        Minecraft mc = Minecraft.getMinecraft();
        int count = mc.thePlayer.ticksExisted;
        float bob = 0.0F;
        float angleS = (float) cr.rotation;
        float jitter = 0.0F;
        if (cr.duration > 0 && cr.gettingPower()) {
            jitter
                = (cr.getWorldObj().rand.nextFloat() - cr.getWorldObj().rand.nextFloat())
                * 0.1F;
        }

        this.translateFromOrientation(x, y, z, cr.orientation);
        GL11.glTranslatef(0.5F, 0.5F, 0.0F);
        GL11.glRotatef(angleS, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(0.25f, 0.25f, 0.0F);
        GL11.glScalef(-0.5f, -0.5f, 1f);

        if (cr.boreItemStacks[0] != null) {
            mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
            IIcon icon = cr.boreItemStacks[0].getIconIndex();
            UtilsFX.renderItemIn2D(
                Tessellator.instance,
                icon.getMaxU(),
                icon.getMinV(),
                icon.getMinU(),
                icon.getMaxV(),
                icon.getIconWidth(),
                icon.getIconHeight(),
                0.6f + jitter,
                128
            );
        }

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void translateFromOrientation(double x, double y, double z, int orientation) {
        GL11.glPushMatrix();
        if (orientation == 0) {
            GL11.glTranslatef((float) x, (float) y, (float) z + 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        } else if (orientation == 1) {
            GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        } else if (orientation == 2) {
            GL11.glTranslatef((float) x, (float) y, (float) z);
        } else if (orientation == 3) {
            GL11.glTranslatef((float) x + 1.0F, (float) y, (float) z + 1.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        } else if (orientation == 4) {
            GL11.glTranslatef((float) x, (float) y, (float) z + 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        } else if (orientation == 5) {
            GL11.glTranslatef((float) x + 1.0F, (float) y, (float) z);
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glPushMatrix();
    }

    public void
    renderTileEntityAt(TileEntity te, double d, double d1, double d2, float f) {
        // TODO: WTF
        //double var10002 = (double) te.xCoord + 0.5;
        //double var10003 = (double) te.yCoord + 0.5;
        //double var10004 = (double) te.zCoord;
        //if (ThaumCraftCore.isVisibleTo(
        //        1.5F,
        //        ModLoader.getMinecraftInstance().thePlayer,
        //        var10002,
        //        var10003,
        //        var10004 + 0.5
        //    )) {
        this.renderEntityAt((TileBore) te, d, d1, d2, f);
        //}
    }
}
