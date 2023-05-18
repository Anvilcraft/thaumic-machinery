package net.anvilcraft.thaummach.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class EntitySingularity extends Entity {
    public float rotation;
    double bounceFactor;
    public int fuse;
    int fusemax;
    boolean exploded;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public Set<ChunkPosition> destroyedBlockPositions;
    private float currentVis;

    public EntitySingularity(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
        super.yOffset = super.height / 2.0F;
        this.bounceFactor = 0.25;
        this.exploded = false;
        this.destroyedBlockPositions = new HashSet<>();
        super.worldObj = world;
    }

    public EntitySingularity(World world, Entity entity) {
        this(world);
        this.setRotation(entity.rotationYaw, 0.0F);
        double xHeading
            = (double) (-MathHelper.sin(entity.rotationYaw * 3.141593F / 180.0F));
        double zHeading
            = (double) MathHelper.cos(entity.rotationYaw * 3.141593F / 180.0F);
        super.motionX = 0.75 * xHeading
            * (double) MathHelper.cos(entity.rotationPitch / 180.0F * 3.141593F);
        super.motionY
            = -0.5 * (double) MathHelper.sin(entity.rotationPitch / 180.0F * 3.141593F);
        super.motionZ = 0.75 * zHeading
            * (double) MathHelper.cos(entity.rotationPitch / 180.0F * 3.141593F);
        this.setPosition(
            entity.posX + xHeading * 0.8, entity.posY, entity.posZ + zHeading * 0.8
        );
        super.prevPosX = super.posX;
        super.prevPosY = super.posY;
        super.prevPosZ = super.posZ;
        this.explosionSize = 4.0F;
        this.fuse = 60;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void onUpdate() {
        if (this.fuse-- == 0) {
            this.explode();
        }

        if (this.fuse > 0) {
            double prevVelX = super.motionX;
            double prevVelY = super.motionY;
            double prevVelZ = super.motionZ;
            super.prevPosX = super.posX;
            super.prevPosY = super.posY;
            super.prevPosZ = super.posZ;
            this.moveEntity(super.motionX, super.motionY, super.motionZ);
            if (super.motionX != prevVelX) {
                super.motionX = -this.bounceFactor * prevVelX;
            }

            if (super.motionY != prevVelY) {
                super.motionY = -this.bounceFactor * prevVelY;
            } else {
                super.motionY -= 0.04;
            }

            if (super.motionZ != prevVelZ) {
                super.motionZ = -this.bounceFactor * prevVelZ;
            }

            super.motionX *= 0.98;
            super.motionY *= 0.98;
            super.motionZ *= 0.98;
            // TODO: FX
            //FXWisp ef = new FXWisp(
            //    super.worldObj,
            //    (double) ((float) super.posX),
            //    (double) ((float) super.posY + 0.1F),
            //    (double) ((float) super.posZ),
            //    0.4F,
            //    0
            //);
            //ef.shrink = true;
            //Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        } else {
            ++this.rotation;
            if (this.rotation > 360.0F) {
                this.rotation -= 360.0F;
            }

            this.doSuckage();
        }

        if (this.fuse < -170) {
            this.doSpawnResult();
            this.setDead();
        }
    }

    @SuppressWarnings("unchecked")
    public void doExplosion() {
        super.worldObj.playSoundEffect(
            this.explosionX,
            this.explosionY,
            this.explosionZ,
            "random.explode",
            4.0F,
            (1.0F
             + (super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat()) * 0.2F)
                * 0.7F
        );
        super.worldObj.spawnParticle(
            "largeexplode",
            this.explosionX,
            this.explosionY,
            this.explosionZ,
            0.0,
            0.0,
            0.0
        );
        float f = this.explosionSize;
        int i = 16;

        int j;
        int l;
        int j1;
        double d5;
        double d7;
        double d9;
        for (j = 0; j < i; ++j) {
            for (l = 0; l < i; ++l) {
                for (j1 = 0; j1 < i; ++j1) {
                    if (j == 0 || j == i - 1 || l == 0 || l == i - 1 || j1 == 0
                        || j1 == i - 1) {
                        double d
                            = (double) ((float) j / ((float) i - 1.0F) * 2.0F - 1.0F);
                        double d1
                            = (double) ((float) l / ((float) i - 1.0F) * 2.0F - 1.0F);
                        double d2
                            = (double) ((float) j1 / ((float) i - 1.0F) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
                        d /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f1 = this.explosionSize
                            * (0.7F + super.worldObj.rand.nextFloat() * 0.6F);
                        d5 = this.explosionX;
                        d7 = this.explosionY;
                        d9 = this.explosionZ;

                        for (float f2 = 0.3F; !(f1 <= 0.0F); f1 -= f2 * 0.75F) {
                            int j4 = MathHelper.floor_double(d5);
                            int k4 = MathHelper.floor_double(d7);
                            int l4 = MathHelper.floor_double(d9);
                            Block i5 = super.worldObj.getBlock(j4, k4, l4);
                            if (i5 != Blocks.air) {
                                f1 -= (i5.getExplosionResistance(this) + 0.3F) * f2;
                            }

                            if (f1 > 0.0F) {
                                this.destroyedBlockPositions.add(
                                    new ChunkPosition(j4, k4, l4)
                                );
                            }

                            d5 += d * (double) f2;
                            d7 += d1 * (double) f2;
                            d9 += d2 * (double) f2;
                        }
                    }
                }
            }
        }

        this.explosionSize *= 2.0F;
        j = MathHelper.floor_double(this.explosionX - (double) this.explosionSize - 1.0);
        l = MathHelper.floor_double(this.explosionX + (double) this.explosionSize + 1.0);
        j1 = MathHelper.floor_double(this.explosionY - (double) this.explosionSize - 1.0);
        int l1 = MathHelper.floor_double(
            this.explosionY + (double) this.explosionSize + 1.0
        );
        int i2 = MathHelper.floor_double(
            this.explosionZ - (double) this.explosionSize - 1.0
        );
        int j2 = MathHelper.floor_double(
            this.explosionZ + (double) this.explosionSize + 1.0
        );
        List<Entity> list = super.worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            AxisAlignedBB.getBoundingBox(
                (double) j, (double) j1, (double) i2, (double) l, (double) l1, (double) j2
            )
        );
        Vec3 vec3d
            = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = (Entity) list.get(k2);
            double d4
                = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ)
                / (double) this.explosionSize;
            if (d4 <= 1.0) {
                d5 = entity.posX - this.explosionX;
                d7 = entity.posY - this.explosionY;
                d9 = entity.posZ - this.explosionZ;
                double d11 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);
                d5 /= d11;
                d7 /= d11;
                d9 /= d11;
                double d12
                    = (double) super.worldObj.getBlockDensity(vec3d, entity.boundingBox);
                double d13 = (1.0 - d4) * d12;
                entity.attackEntityFrom(
                    // TODO: WTF
                    //DamageSource.explosion,
                    DamageSource.magic,
                    (int
                    ) ((d13 * d13 + d13) / 2.0 * 8.0 * (double) this.explosionSize + 1.0)
                );
                entity.motionX += d5 * d13;
                entity.motionY += d7 * d13;
                entity.motionZ += d9 * d13;
            }
        }

        this.explosionSize = f;
        ArrayList<ChunkPosition> arraylist = new ArrayList<>();
        arraylist.addAll(this.destroyedBlockPositions);

        for (int ii = arraylist.size() - 1; ii >= 0; --ii) {
            ChunkPosition chunkposition = (ChunkPosition) arraylist.get(ii);
            int jj = chunkposition.chunkPosX;
            int kk = chunkposition.chunkPosY;
            int ll = chunkposition.chunkPosZ;
            Block id = super.worldObj.getBlock(jj, kk, ll);
            double d = (double) ((float) jj + super.worldObj.rand.nextFloat());
            double d1 = (double) ((float) kk + super.worldObj.rand.nextFloat());
            double d2 = (double) ((float) ll + super.worldObj.rand.nextFloat());
            double d3 = d - this.explosionX;
            double d4 = d1 - this.explosionY;
            double d10 = d2 - this.explosionZ;
            double d6 = (double) MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d10 * d10);
            d3 /= d6;
            d4 /= d6;
            d10 /= d6;
            double d11 = 0.5 / (d6 / (double) this.explosionSize + 0.1);
            d11 *= (double
            ) (super.worldObj.rand.nextFloat() * super.worldObj.rand.nextFloat() + 0.3F);
            d3 *= -d11;
            d4 *= -d11;
            d10 *= -d11;
            super.worldObj.spawnParticle("smoke", d, d1, d2, d3, d4, d10);
            if (id != Blocks.air) {
                id.dropBlockAsItemWithChance(
                    super.worldObj,
                    jj,
                    kk,
                    ll,
                    super.worldObj.getBlockMetadata(jj, kk, ll),
                    0.9F,
                    0
                );
                super.worldObj.setBlockToAir(jj, kk, ll);
                // TODO: need `Explosion` for this
                //id.onBlockDestroyedByExplosion(
                //    super.worldObj, jj, kk, ll
                //);
            }
        }
    }

    private void explode() {
        if (!this.exploded) {
            this.exploded = true;
            this.explosionX = super.posX;
            this.explosionY = super.posY;
            this.explosionZ = super.posZ;
            this.doExplosion();
            super.worldObj.playSoundEffect(
                super.posX, super.posY, super.posZ, "thaumcraft.singularity", 2.0F, 1.0F
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void doSuckage() {
        int auraSize = 10;
        int lastdistance = 999;
        int lastx = 0;
        int lasty = 0;
        int lastz = 0;
        int mx = super.worldObj.rand.nextBoolean() ? 1 : -1;
        int my = super.worldObj.rand.nextBoolean() ? 1 : -1;
        int mz = super.worldObj.rand.nextBoolean() ? 1 : -1;

        int a;
        int b;
        int c;
        int distance;
        for (a = -auraSize; a < auraSize + 1; ++a) {
            for (b = -auraSize; b < auraSize + 1; ++b) {
                for (c = -auraSize; c < auraSize + 1; ++c) {
                    if (super.worldObj.getBlock(
                            (int) super.posX + a * mx,
                            (int) super.posY + b * my,
                            (int) super.posZ + c * mz
                        )
                        != Blocks.air) {
                        distance = (int) this.getDistance(
                            super.posX + (double) (a * mx) + 0.5,
                            super.posY + (double) (b * my) + 0.5,
                            super.posZ + (double) (c * mz) + 0.5
                        );
                        if (distance < lastdistance && distance <= auraSize) {
                            lastdistance = distance;
                            lastx = (int) super.posX + a * mx;
                            lasty = (int) super.posY + b * my;
                            lastz = (int) super.posZ + c * mz;
                        }
                    }
                }
            }
        }

        int zm;
        if (lastdistance < 999) {
            Block block = super.worldObj.getBlock(lastx, lasty, lastz);
            zm = super.worldObj.getBlockMetadata(lastx, lasty, lastz);
            if (block != Blocks.bedrock && block.getExplosionResistance(this) < 100.0F
                && block.getBlockHardness(this.worldObj, lastx, lasty, lastz) != -1.0F) {
                block.dropBlockAsItemWithChance(
                    super.worldObj,
                    lastx,
                    lasty,
                    lastz,
                    super.worldObj.getBlockMetadata(lastx, lasty, lastz),
                    0.9F,
                    0
                );
                super.worldObj.setBlockToAir(lastx, lasty, lastz);
            }
        }

        a = MathHelper.floor_double(super.posX - (double) auraSize - 1.0);
        b = MathHelper.floor_double(super.posX + (double) auraSize + 1.0);
        c = MathHelper.floor_double(super.posY - (double) auraSize - 1.0);
        distance = MathHelper.floor_double(super.posY + (double) auraSize + 1.0);
        zm = MathHelper.floor_double(super.posZ - (double) auraSize - 1.0);
        int zp = MathHelper.floor_double(super.posZ + (double) auraSize + 1.0);
        List<Entity> list = super.worldObj.getEntitiesWithinAABB(
            Entity.class,
            AxisAlignedBB.getBoundingBox(
                (double) a,
                (double) c,
                (double) zm,
                (double) b,
                (double) distance,
                (double) zp
            )
        );

        Entity entity;
        for (a = 0; a < list.size(); ++a) {
            entity = (Entity) list.get(a);
            double d4 = entity.getDistance(super.posX, super.posY, super.posZ)
                / (double) auraSize;
            double dx = entity.posX - super.posX;
            double dy = entity.posY - super.posY;
            double dz = entity.posZ - super.posZ;
            double d11 = (double) MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
            dx /= d11;
            dy /= d11;
            dz /= d11;
            double d13 = (1.0 - d4) * 0.1;
            if (d13 < 0.0) {
                d13 = 0.0;
            }

            entity.motionX -= dx * d13;
            entity.motionY -= dy * d13;
            entity.motionZ -= dz * d13;
            if (!(entity instanceof EntityLiving)) {
                // TODO: FX
                //FXWisp ef = new FXWisp(
                //    super.worldObj,
                //    (double) ((float) entity.prevPosX),
                //    (double) ((float) entity.prevPosY + 0.1F),
                //    (double) ((float) entity.prevPosZ),
                //    0.4F,
                //    5
                //);
                //ef.shrink = true;
                //Minecraft.getMinecraft().effectRenderer.addEffect(ef);
            }
        }

        a = MathHelper.floor_double(super.posX - 0.75);
        b = MathHelper.floor_double(super.posX + 0.75);
        c = MathHelper.floor_double(super.posY - 0.75);
        distance = MathHelper.floor_double(super.posY + 0.75);
        zm = MathHelper.floor_double(super.posZ - 0.75);
        zp = MathHelper.floor_double(super.posZ + 0.75);
        list = super.worldObj.getEntitiesWithinAABB(
            Entity.class,
            AxisAlignedBB.getBoundingBox(
                (double) a,
                (double) c,
                (double) zm,
                (double) b,
                (double) distance,
                (double) zp
            )
        );

        for (a = 0; a < list.size(); ++a) {
            entity = (Entity) list.get(a);
            if (!(entity instanceof EntitySingularity)) {
                if (entity instanceof EntityItem) {
                    // TODO: crucible recipes
                    //int val = (int) RecipesCrucible.smelting().getSmeltingResult(
                    //    ((EntityItem) entity).item, true, true
                    //);
                    //this.currentVis += (float) val;
                    entity.setDead();
                    // TODO: FX
                    //FXWisp ef = new FXWisp(
                    //    super.worldObj,
                    //    (double) ((float) super.posX),
                    //    (double) ((float) super.posY + 0.1F),
                    //    (double) ((float) super.posZ),
                    //    1.0F,
                    //    5
                    //);
                    //ef.shrink = true;
                    //Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                } else {
                    entity.attackEntityFrom(DamageSource.magic, 3);
                    if (entity instanceof EntityLiving) {
                        ++this.currentVis;
                    }
                }
            }
        }
    }

    private void doSpawnResult() {
        AuraManager.addFluxToClosest(
            this.worldObj,
            (float) this.posX,
            (float) this.posY,
            (float) this.posZ,
            new AspectList().add(Aspect.DARKNESS, (int) (this.currentVis / 4.0))
        );
    }

    @Override
    public boolean attackEntityFrom(DamageSource entity, float i) {
        super.attackEntityFrom(entity, i);
        this.explode();
        return false;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    @Override
    public boolean canBeCollidedWith() {
        return !super.isDead;
    }

    @Override
    public float getShadowSize() {
        return 0.1F;
    }

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
        var1.setByte("Fuse", (byte) this.fuse);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
        this.fuse = var1.getByte("Fuse");
    }
}
