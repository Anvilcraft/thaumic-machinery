package net.anvilcraft.thaummach.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXWisp extends EntityFX {
    public boolean shrink;
    float moteParticleScale;
    int moteHalfLife;
    public boolean tinkle;
    public int blendmode;

    public FXWisp(
        World world, double d, double d1, double d2, float f, float f1, float f2
    ) {
        this(world, d, d1, d2, 1.0F, f, f1, f2);
    }

    public FXWisp(
        World world, double d, double d1, double d2, float f, float f1, float f2, float f3
    ) {
        super(world, d, d1, d2, 0.0, 0.0, 0.0);
        this.shrink = false;
        this.tinkle = false;
        this.blendmode = 1;
        if (f1 == 0.0F) {
            f1 = 1.0F;
        }

        super.particleRed = f1;
        super.particleGreen = f2;
        super.particleBlue = f3;
        super.particleGravity = 0.0F;
        super.motionX = super.motionY = super.motionZ = 0.0;
        super.particleScale *= f;
        this.moteParticleScale = super.particleScale;
        super.particleMaxAge = (int) (36.0 / (Math.random() * 0.3 + 0.7));
        this.moteHalfLife = super.particleMaxAge / 2;
        super.noClip = false;
    }

    public FXWisp(World world, double d, double d1, double d2, float f, int type) {
        this(world, d, d1, d2, f, 0.0F, 0.0F, 0.0F);
        switch (type) {
            case 0:
                super.particleRed = 0.75F + world.rand.nextFloat() * 0.25F;
                super.particleGreen = 0.25F + world.rand.nextFloat() * 0.25F;
                super.particleBlue = 0.75F + world.rand.nextFloat() * 0.25F;
                break;
            case 1:
                super.particleRed = 0.5F + world.rand.nextFloat() * 0.3F;
                super.particleGreen = 0.5F + world.rand.nextFloat() * 0.3F;
                super.particleBlue = 0.2F;
                break;
            case 2:
                super.particleRed = 0.2F;
                super.particleGreen = 0.2F;
                super.particleBlue = 0.7F + world.rand.nextFloat() * 0.3F;
                break;
            case 3:
                super.particleRed = 0.2F;
                super.particleGreen = 0.7F + world.rand.nextFloat() * 0.3F;
                super.particleBlue = 0.2F;
                break;
            case 4:
                super.particleRed = 0.7F + world.rand.nextFloat() * 0.3F;
                super.particleGreen = 0.2F;
                super.particleBlue = 0.2F;
                break;
            case 5:
                this.blendmode = 771;
                super.particleRed = world.rand.nextFloat() * 0.1F;
                super.particleGreen = world.rand.nextFloat() * 0.1F;
                super.particleBlue = world.rand.nextFloat() * 0.1F;
                break;
            case 6:
                super.particleRed = 0.8F + world.rand.nextFloat() * 0.2F;
                super.particleGreen = 0.8F + world.rand.nextFloat() * 0.2F;
                super.particleBlue = 0.8F + world.rand.nextFloat() * 0.2F;
                break;
            case 7:
                float rr = world.rand.nextFloat();
                super.particleRed = 0.2F + rr * 0.3F;
                super.particleGreen = 0.2F + rr * 0.3F;
                super.particleBlue = 0.7F + world.rand.nextFloat() * 0.3F;
        }
    }

    public FXWisp(
        World world,
        double d,
        double d1,
        double d2,
        double x,
        double y,
        double z,
        float f,
        int type
    ) {
        this(world, d, d1, d2, f, type);
        double dx = x - super.posX;
        double dy = y - super.posY;
        double dz = z - super.posZ;
        super.motionX = dx / (double) super.particleMaxAge;
        super.motionY = dy / (double) super.particleMaxAge;
        super.motionZ = dz / (double) super.particleMaxAge;
    }

    public void renderParticle(
        Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5
    ) {
        float agescale = 0.0F;
        if (this.shrink) {
            agescale = ((float) super.particleMaxAge - (float) super.particleAge)
                / (float) super.particleMaxAge;
        } else {
            agescale = (float) super.particleAge / (float) this.moteHalfLife;
            if (agescale > 1.0F) {
                agescale = 2.0F - agescale;
            }
        }

        super.particleScale = this.moteParticleScale * agescale;
        tessellator.draw();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, this.blendmode);
        Minecraft.getMinecraft().renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/misc/p_large.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
        float f10 = 0.5F * super.particleScale;
        float f11 = (float
        ) (super.prevPosX + (super.posX - super.prevPosX) * (double) f
           - EntityFX.interpPosX);
        float f12 = (float
        ) (super.prevPosY + (super.posY - super.prevPosY) * (double) f
           - EntityFX.interpPosY);
        float f13 = (float
        ) (super.prevPosZ + (super.posZ - super.prevPosZ) * (double) f
           - EntityFX.interpPosZ);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);
        tessellator.setColorRGBA_F(
            super.particleRed, super.particleGreen, super.particleBlue, 0.5F
        );
        tessellator.addVertexWithUV(
            (double) (f11 - f1 * f10 - f4 * f10),
            (double) (f12 - f2 * f10),
            (double) (f13 - f3 * f10 - f5 * f10),
            0.0,
            1.0
        );
        tessellator.addVertexWithUV(
            (double) (f11 - f1 * f10 + f4 * f10),
            (double) (f12 + f2 * f10),
            (double) (f13 - f3 * f10 + f5 * f10),
            1.0,
            1.0
        );
        tessellator.addVertexWithUV(
            (double) (f11 + f1 * f10 + f4 * f10),
            (double) (f12 + f2 * f10),
            (double) (f13 + f3 * f10 + f5 * f10),
            1.0,
            0.0
        );
        tessellator.addVertexWithUV(
            (double) (f11 + f1 * f10 - f4 * f10),
            (double) (f12 - f2 * f10),
            (double) (f13 + f3 * f10 - f5 * f10),
            0.0,
            0.0
        );
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(
            new ResourceLocation("textures/particles/particles.png")
        );
        tessellator.startDrawingQuads();
    }

    public void onUpdate() {
        EntityPlayer renderentity = Minecraft.getMinecraft().thePlayer;
        int visibleDistance = 50;

        if (renderentity.getDistance(super.posX, super.posY, super.posZ)
            > (double) visibleDistance) {
            this.setDead();
        }

        super.prevPosX = super.posX;
        super.prevPosY = super.posY;
        super.prevPosZ = super.posZ;
        if (super.particleAge == 0 && this.tinkle
            && super.worldObj.rand.nextInt(3) == 0) {
            super.worldObj.playSoundAtEntity(
                this,
                "random.orb",
                0.02F,
                0.5F
                    * ((super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat())
                           * 0.6F
                       + 2.0F)
            );
        }

        if (super.particleAge++ >= super.particleMaxAge) {
            this.setDead();
        }

        super.motionY -= 0.04 * (double) super.particleGravity;
        this.moveEntity(super.motionX, super.motionY, super.motionZ);
        super.motionX *= 0.9800000190734863;
        super.motionY *= 0.9800000190734863;
        super.motionZ *= 0.9800000190734863;
        if (super.onGround) {
            super.motionX *= 0.699999988079071;
            super.motionZ *= 0.699999988079071;
        }
    }

    public void setGravity(float value) {
        super.particleGravity = value;
    }
}
