package net.anvilcraft.thaummach.render.tile;

import java.nio.FloatBuffer;
import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileVoidChestRenderer extends TileEntitySpecialRenderer {
    FloatBuffer fBuffer = GLAllocation.createDirectFloatBuffer(16);

    private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
        this.fBuffer.clear();
        this.fBuffer.put(f).put(f1).put(f2).put(f3);
        this.fBuffer.flip();
        return this.fBuffer;
    }

    //  _____ ___  ____   ___     __        _______ _____
    // |_   _/ _ \|  _ \ / _ \ _  \ \      / /_   _|  ___|
    //   | || | | | | | | | | (_)  \ \ /\ / /  | | | |_
    //   | || |_| | |_| | |_| |_    \ V  V /   | | |  _|
    //   |_| \___/|____/ \___/(_)    \_/\_/    |_| |_|

    public void drawPlaneYNeg(double x, double y, double z, float f) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
        float px = (float) player.posX;
        float py = (float) player.posY;
        float pz = (float) player.posZ;
        GL11.glDisable(2896);
        Random random = new Random(31100L);
        float offset = 0.01F;
        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/tunnel.png")
                );
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/particlefield.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float) (y + (double) offset);
            float f9 = f8 - ActiveRenderInfo.objectY;
            float f10 = f8 + f5 - ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float) (y + (double) offset);
            GL11.glTranslatef(px, f11, pz);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(
                0.0F, (float) (System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F
            );
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-px, -pz, -py);
            GL11.glTranslatef(
                ActiveRenderInfo.objectX * f5 / f9,
                ActiveRenderInfo.objectZ * f5 / f9,
                -py
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y + (double) offset, z + 1.0);
            tessellator.addVertex(x, y + (double) offset, z);
            tessellator.addVertex(x + 1.0, y + (double) offset, z);
            tessellator.addVertex(x + 1.0, y + (double) offset, z + 1.0);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
    }

    public void drawPlaneYPos(double x, double y, double z, float f) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
        float f1 = (float) player.posX;
        float f2 = (float) player.posY;
        float f3 = (float) player.posZ;
        GL11.glDisable(2896);
        Random random = new Random(31100L);
        float offset = 0.99F;
        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/tunnel.png")
                );
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/particlefield.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float) (-(y + (double) offset));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float) (y + (double) offset);
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(
                0.0F, (float) (System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F
            );
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            GL11.glTranslatef(
                ActiveRenderInfo.objectX * f5 / f9,
                ActiveRenderInfo.objectZ * f5 / f9,
                -f2
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y + (double) offset, z);
            tessellator.addVertex(x, y + (double) offset, z + 1.0);
            tessellator.addVertex(x + 1.0, y + (double) offset, z + 1.0);
            tessellator.addVertex(x + 1.0, y + (double) offset, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
    }

    public void drawPlaneZPos(double x, double y, double z, float f) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
        float px = (float) player.posX;
        float py = (float) player.posY;
        float pz = (float) player.posZ;
        GL11.glDisable(2896);
        Random random = new Random(31100L);
        float offset = 0.99F;
        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/tunnel.png")
                );
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/particlefield.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float) (-(z + (double) offset));
            float f9 = f8 + ActiveRenderInfo.objectZ;
            float f10 = f8 + f5 + ActiveRenderInfo.objectZ;
            float f11 = f9 / f10;
            f11 += (float) (z + (double) offset);
            GL11.glTranslatef(px, py, f11);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(
                0.0F, (float) (System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F
            );
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-px, -py, -pz);
            GL11.glTranslatef(
                ActiveRenderInfo.objectX * f5 / f9,
                ActiveRenderInfo.objectY * f5 / f9,
                -pz
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y + 1.0, z + (double) offset);
            tessellator.addVertex(x, y, z + (double) offset);
            tessellator.addVertex(x + 1.0, y, z + (double) offset);
            tessellator.addVertex(x + 1.0, y + 1.0, z + (double) offset);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
    }

    public void drawPlaneZNeg(double x, double y, double z, float f) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
        float px = (float) player.posX;
        float py = (float) player.posY;
        float pz = (float) player.posZ;
        GL11.glDisable(2896);
        Random random = new Random(31100L);
        float offset = 0.01F;
        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/tunnel.png")
                );
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/particlefield.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float) (z + (double) offset);
            float f9 = f8 - ActiveRenderInfo.objectZ;
            float f10 = f8 + f5 - ActiveRenderInfo.objectZ;
            float f11 = f9 / f10;
            f11 += (float) (z + (double) offset);
            GL11.glTranslatef(px, py, f11);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(
                0.0F, (float) (System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F
            );
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-px, -py, -pz);
            GL11.glTranslatef(
                ActiveRenderInfo.objectX * f5 / f9,
                ActiveRenderInfo.objectY * f5 / f9,
                -pz
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y, z + (double) offset);
            tessellator.addVertex(x, y + 1.0, z + (double) offset);
            tessellator.addVertex(x + 1.0, y + 1.0, z + (double) offset);
            tessellator.addVertex(x + 1.0, y, z + (double) offset);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
    }

    public void drawPlaneXPos(double x, double y, double z, float f) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
        float px = (float) player.posX;
        float py = (float) player.posY;
        float pz = (float) player.posZ;
        GL11.glDisable(2896);
        Random random = new Random(31100L);
        float offset = 0.99F;
        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/tunnel.png")
                );
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/particlefield.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float) (-(x + (double) offset));
            float f9 = f8 + ActiveRenderInfo.objectX;
            float f10 = f8 + f5 + ActiveRenderInfo.objectX;
            float f11 = f9 / f10;
            f11 += (float) (x + (double) offset);
            GL11.glTranslatef(f11, py, pz);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(
                0.0F, (float) (System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F
            );
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-pz, -py, -px);
            GL11.glTranslatef(
                ActiveRenderInfo.objectZ * f5 / f9,
                ActiveRenderInfo.objectY * f5 / f9,
                -px
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + (double) offset, y + 1.0, z);
            tessellator.addVertex(x + (double) offset, y + 1.0, z + 1.0);
            tessellator.addVertex(x + (double) offset, y, z + 1.0);
            tessellator.addVertex(x + (double) offset, y, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
    }

    public void drawPlaneXNeg(double x, double y, double z, float f) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClientPlayerEntity();
        float px = (float) player.posX;
        float py = (float) player.posY;
        float pz = (float) player.posZ;
        GL11.glDisable(2896);
        Random random = new Random(31100L);
        float offset = 0.01F;
        for (int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/tunnel.png")
                );
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(
                    new ResourceLocation("thaummach", "textures/misc/particlefield.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float) (x + (double) offset);
            float f9 = f8 - ActiveRenderInfo.objectX;
            float f10 = f8 + f5 - ActiveRenderInfo.objectX;
            float f11 = f9 / f10;
            f11 += (float) (x + (double) offset);
            GL11.glTranslatef(f11, py, pz);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(
                0.0F, (float) (System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F
            );
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-pz, -py, -px);
            GL11.glTranslatef(
                ActiveRenderInfo.objectZ * f5 / f9,
                ActiveRenderInfo.objectY * f5 / f9,
                -px
            );
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + (double) offset, y, z);
            tessellator.addVertex(x + (double) offset, y, z + 1.0);
            tessellator.addVertex(x + (double) offset, y + 1.0, z + 1.0);
            tessellator.addVertex(x + (double) offset, y + 1.0, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
        this.drawPlaneZNeg(x, y, z, f);
        this.drawPlaneZPos(x, y, z, f);
        this.drawPlaneXNeg(x, y, z, f);
        this.drawPlaneXPos(x, y, z, f);
    }
}
