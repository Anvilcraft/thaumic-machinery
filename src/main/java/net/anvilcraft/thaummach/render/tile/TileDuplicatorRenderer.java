package net.anvilcraft.thaummach.render.tile;

import net.anvilcraft.thaummach.particles.FXWisp;
import net.anvilcraft.thaummach.render.model.ModelDuplicator;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileDuplicatorRenderer extends TileEntitySpecialRenderer {
    private ModelDuplicator model = new ModelDuplicator();

    public void renderEntityAt(TileDuplicator duplicator, double x, double y, double z, float fq) {
        float dist = 0.1875F - duplicator.press;
        if (duplicator.getWorldObj().rand.nextFloat()
            < duplicator.duplicatorCopyTime / duplicator.currentItemCopyCost) {
            float xx = (float) duplicator.xCoord + 0.5F
                - (duplicator.getWorldObj().rand.nextFloat()
                   - duplicator.getWorldObj().rand.nextFloat())
                    * 0.7F;
            float yy = (float) duplicator.yCoord + 0.5F
                - (duplicator.getWorldObj().rand.nextFloat()
                   - duplicator.getWorldObj().rand.nextFloat())
                    * 0.7F;
            float zz = (float) duplicator.zCoord + 0.5F
                - (duplicator.getWorldObj().rand.nextFloat()
                   - duplicator.getWorldObj().rand.nextFloat())
                    * 0.7F;
            FXWisp ef = new FXWisp(
                duplicator.getWorldObj(),
                (double) ((float) duplicator.xCoord + 0.5F),
                (double) ((float) duplicator.yCoord + 0.5F),
                (double) ((float) duplicator.zCoord + 0.5F),
                (double) xx,
                (double) yy,
                (double) zz,
                0.1F,
                duplicator.getWorldObj().rand.nextInt(5)
            );
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        }

        this.bindTexture(new ResourceLocation("thaummach", "textures/models/duplicator.png"));
        GL11.glEnable(0xba1);
        GL11.glEnable(0xbe2);
        GL11.glPushMatrix();
        GL11.glEnable(0x803a);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x + 0.5F, (float) y - 0.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -dist, 0.0F);
        this.model.render();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(0.0F, -2.0F - dist, 0.0F);
        this.model.render();
        GL11.glPopMatrix();
        GL11.glDisable(0x803a);
        GL11.glPopMatrix();
        GL11.glDisable(0xbe2);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
        this.renderEntityAt((TileDuplicator) tileentity, d, d1, d2, f);
    }
}
