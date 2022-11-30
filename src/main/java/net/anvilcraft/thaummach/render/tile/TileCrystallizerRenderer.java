package net.anvilcraft.thaummach.render.tile;

import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.ModelCrystal;
import thaumcraft.codechicken.lib.math.MathHelper;

public class TileCrystallizerRenderer extends TileEntitySpecialRenderer {
    private ModelCrystal model = new ModelCrystal();

    private void drawCrystal(float x, float y, float z, float a1, float a2, float b) {
        GL11.glEnable(2977);
        GL11.glEnable(3042);
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(220);
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(a1, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(a2, 1.0F, 0.0F, 0.0F);
        GL11.glPushMatrix();
        GL11.glColor4f(b, b, b, 1.0F);
        GL11.glScalef(0.15F, 0.45F, 0.15F);
        this.model.render();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void
    renderEntityAt(TileCrystallizer cr, double x, double y, double z, float fq) {
        Minecraft mc = Minecraft.getMinecraft();
        int count = mc.thePlayer.ticksExisted;
        float bob = 0.0F;
        float angleS = 45.0F;
        float angleI = 90.0F;
        if (cr.hasUpgrade((byte) 3)) {
            angleS = 36.0F;
            angleI = 72.0F;
        }

        if (cr.isCooking()) {
            angleS += (float) (count % 360);
            bob = (float) MathHelper.sin((float) count / 5.0F) * 0.15F + 0.15F;
        }

        this.bindTexture(new ResourceLocation("thaummach", "textures/models/crystal.png")
        );
        this.drawCrystal(
            (float) x + 0.5F,
            (float) y + 0.25F,
            (float) z + 0.5F,
            angleS,
            0.0F,
            1.0F - bob
        );
        this.bindTexture(new ResourceLocation("thaummach", "textures/models/crystaly.png")
        );
        this.drawCrystal(
            (float) x + 0.5F,
            (float) y + 0.25F,
            (float) z + 0.5F,
            angleS,
            25.0F,
            1.0F - bob
        );
        angleS += angleI;
        this.bindTexture(new ResourceLocation("thaummach", "textures/models/crystalb.png")
        );
        this.drawCrystal(
            (float) x + 0.5F,
            (float) y + 0.25F,
            (float) z + 0.5F,
            angleS,
            25.0F,
            1.0F - bob
        );
        angleS += angleI;
        this.bindTexture(new ResourceLocation("thaummach", "textures/models/crystalg.png")
        );
        this.drawCrystal(
            (float) x + 0.5F,
            (float) y + 0.25F,
            (float) z + 0.5F,
            angleS,
            25.0F,
            1.0F - bob
        );
        angleS += angleI;
        this.bindTexture(new ResourceLocation("thaummach", "textures/models/crystalr.png")
        );
        this.drawCrystal(
            (float) x + 0.5F,
            (float) y + 0.25F,
            (float) z + 0.5F,
            angleS,
            25.0F,
            1.0F - bob
        );
        if (cr.hasUpgrade((byte) 3)) {
            angleS += angleI;
            this.bindTexture(
                new ResourceLocation("thaummach", "textures/models/crystal.png")
            );
            this.drawCrystal(
                (float) x + 0.5F,
                (float) y + 0.25F,
                (float) z + 0.5F,
                angleS,
                25.0F,
                0.4F - bob
            );
        }
    }

    public void
    renderTileEntityAt(TileEntity te, double d, double d1, double d2, float f) {
        // TODO: WTF
        //double var10002 = (double) te.xCoord + 0.5;
        //double var10003 = (double) te.yCoord + 0.5;
        //double var10004 = (double) te.zCoord;
        //if (ThaumCraftCore.isVisibleTo(
        //        1.15F,
        //        ModLoader.getMinecraftInstance().thePlayer,
        //        var10002,
        //        var10003,
        //        var10004 + 0.5
        //    )) {
        this.renderEntityAt((TileCrystallizer) te, d, d1, d2, f);
        //}
    }
}
