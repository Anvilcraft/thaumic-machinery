package net.anvilcraft.thaummach.render.tile;

import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileVoidInterfaceRenderer extends TileEntitySpecialRenderer {
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
        for (int a = 0; a < 4; ++a) {
            float xx = 0.0F;
            float zz = 0.0F;
            switch (a) {
                case 0:
                    xx = 0.375F;
                    zz = 0.2F;
                    break;
                case 1:
                    zz = -0.375F;
                    xx = 0.2F;
                    break;
                case 2:
                    xx = -0.375F;
                    zz = -0.2F;
                    break;
                case 3:
                    zz = 0.375F;
                    xx = -0.2F;
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(
                (float) x + (float) (a != 2 && a != 3 ? 1 : 0),
                (float) y + 0.44F,
                (float) z + (float) (a != 1 && a != 2 ? 1 : 0)
            );
            GL11.glTranslatef(xx, 0.0F, zz);
            GL11.glRotatef(90.0F * (float) a, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0f, 0.0f, 0.0f);
            GL11.glTranslatef(-0.75f, -0.4f, 0.0f);
            GL11.glScalef(-0.25f, 1.0f, -0.25f);
            GL11.glPushMatrix();

            //ThaumCraftRenderer.renderItemFromTexture(
            //    ModLoader.getMinecraftInstance(),
            //    "/thaumcraft/resources/particles.png",
            //    8,
            //    56 + ((TileVoidInterface) te).network,
            //    0.25F,
            //    0.0F,
            //    false,
            //    1.0F,
            //    1.0F,
            //    1.0F,
            //    220,
            //    1
            //);

            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(
                "thaummach",
                "textures/misc/i_" + ((TileVoidInterface) te).network

                    + ".png"
            ));

            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            tess.addVertexWithUV(0.0, 0.0, 1.0, 1.0, 0.0);
            tess.addVertexWithUV(1.0, 0.0, 1.0, 0.0, 0.0);
            tess.addVertexWithUV(1.0, 0.0, 0.0, 0.0, 1.0);
            tess.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 1.0);
            tess.draw();
            GL11.glDisable(3042);

            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
}
