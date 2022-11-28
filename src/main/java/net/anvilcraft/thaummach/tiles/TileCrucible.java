package net.anvilcraft.thaummach.tiles;

import java.util.List;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;

public class TileCrucible extends TileEntity implements IConnection {
    public int smeltDelay;
    public float pureVis = 0.0F;
    public float taintedVis = 0.0F;
    public float maxVis;
    public int face = 3;
    private short type;
    private float conversion;
    private float speed;
    public int bellows = 0;
    private int soundDelay = 25;
    float pPure;
    float pTaint;
    int wait;
    boolean updateNextPeriod;
    public boolean isPowering = false;

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.pureVis = nbttagcompound.getFloat("pureVis");
        this.taintedVis = nbttagcompound.getFloat("taintedVis");
        this.type = nbttagcompound.getShort("type");
        this.setTier(this.type);
        this.bellows = nbttagcompound.getShort("bellows");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("pureVis", this.pureVis);
        nbttagcompound.setFloat("taintedVis", this.taintedVis);
        nbttagcompound.setShort("type", this.type);
        nbttagcompound.setShort("bellows", (short) this.bellows);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("pureVis", this.pureVis);
        nbt.setFloat("taintedVis", this.taintedVis);
        nbt.setShort("type", this.type);
        nbt.setInteger("bellows", this.bellows);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.pureVis = nbt.getFloat("pureVis");
        this.taintedVis = nbt.getFloat("taintedVis");
        this.type = nbt.getShort("type");
        this.bellows = nbt.getInteger("bellows");
    }

    public void setTier(short t) {
        switch (t) {
            case 1:
                this.maxVis = 500.0F;
                this.conversion = 0.5F;
                this.speed = 0.25F;
                this.type = 1;
                break;
            case 2:
                this.maxVis = 600.0F;
                this.conversion = 0.6F;
                this.speed = 0.5F;
                this.type = 2;
                break;
            case 3:
                this.maxVis = 750.0F;
                this.conversion = 0.7F;
                this.speed = 0.75F;
                this.type = 3;
                break;
            case 4:
                this.maxVis = 750.0F;
                this.conversion = 0.4F;
                this.speed = 0.75F;
                this.type = 4;
                break;
        }
    }

    public void updateEntity() {
        if (this.worldObj.isRemote)
            return;

        float totalVis = this.pureVis + this.taintedVis;
        --this.smeltDelay;
        --this.wait;
        if (this.pPure != this.pureVis || this.pTaint != this.taintedVis) {
            this.pTaint = this.taintedVis;
            this.pPure = this.pureVis;
            this.updateNextPeriod = true;
        }

        if (this.wait <= 0 && this.updateNextPeriod) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.updateNextPeriod = false;
            this.wait = 10;
        }

        --this.soundDelay;
        if (this.soundDelay <= 0) {
            this.soundDelay = 15 + super.worldObj.rand.nextInt(15);
        }

        if (totalVis > this.maxVis) {
            float overflowSplit
                = Math.min((this.pureVis + this.taintedVis - this.maxVis) / 2.0F, 1.0F);
            if (this.pureVis >= overflowSplit) {
                this.pureVis -= overflowSplit;
            }

            if (overflowSplit >= 1.0F) {
                if (this.taintedVis >= 1.0F) {
                    AuraManager.addTaintToClosest(
                        this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1
                    );
                    --this.taintedVis;
                    FXWisp ef = new FXWisp(
                        super.worldObj,
                        (double) ((float) super.xCoord + super.worldObj.rand.nextFloat()),
                        (double) ((float) super.yCoord + 0.8F),
                        (double) ((float) super.zCoord + super.worldObj.rand.nextFloat()),
                        (double
                        ) ((float) super.xCoord + 0.5F
                           + (super.worldObj.rand.nextFloat()
                              - super.worldObj.rand.nextFloat())),
                        (double
                        ) ((float) super.yCoord + 3.0F + super.worldObj.rand.nextFloat()),
                        (double
                        ) ((float) super.zCoord + 0.5F
                           + (super.worldObj.rand.nextFloat()
                              - super.worldObj.rand.nextFloat())),
                        0.5F,
                        5
                    );
                    Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                }
            }

            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        if (this.getBlockMetadata() == 1 || this.getBlockMetadata() == 2) {
            boolean oldPower = this.isPowering;
            if ((double) totalVis >= (double) this.maxVis * 0.9) {
                this.isPowering = true;
            } else {
                this.isPowering = false;
            }

            if (oldPower != this.isPowering) {
                for (int a = -1; a < 2; ++a) {
                    for (int b = -1; b < 2; ++b) {
                        for (int c = -1; c < 2; ++c) {
                            this.worldObj.markBlockForUpdate(
                                this.xCoord + a, this.yCoord + b, this.zCoord + c
                            );
                            this.worldObj.notifyBlocksOfNeighborChange(
                                this.xCoord + a,
                                this.yCoord + b,
                                this.zCoord + c,
                                Blocks.air
                            );
                        }
                    }
                }
            }
        }

        float tconv = 0.0f;
        float sa;
        float pureCook;
        float taintCook;
        boolean aboveFurnace;
        boolean aboveBoostedFurnace;
        if (this.smeltDelay <= 0 && this.getBlockMetadata() != 3) {
            this.smeltDelay = 5;
            List<EntityItem> list = this.getContents();
            if (list.size() > 0) {
                EntityItem entity
                    = (EntityItem) list.get(super.worldObj.rand.nextInt(list.size()));
                ItemStack item = entity.getEntityItem();
                if (this.canCook(item)) {
                    aboveFurnace = false;
                    aboveBoostedFurnace = false;
                    if (super.worldObj.getTileEntity(
                            super.xCoord, super.yCoord - 1, super.zCoord
                        ) instanceof TileArcaneFurnace
                        && ((TileArcaneFurnace) super.worldObj.getTileEntity(
                                super.xCoord, super.yCoord - 1, super.zCoord
                            ))
                               .isBurning()) {
                        aboveFurnace = true;
                        if (((TileArcaneFurnace) super.worldObj.getTileEntity(
                                 super.xCoord, super.yCoord - 1, super.zCoord
                             ))
                                .boost) {
                            aboveBoostedFurnace = true;
                        }
                    }

                    // TODO: recipes
                    //tconv
                    //    = RecipesCrucible.smelting().getSmeltingResult(item, true,
                    //    false);
                    sa = this.conversion;
                    if (aboveFurnace) {
                        sa += 0.1F
                            + (float) ((TileArcaneFurnace) super.worldObj.getTileEntity(
                                           super.xCoord, super.yCoord - 1, super.zCoord
                                       ))
                                    .bellows
                                * 0.025F;
                        if (aboveBoostedFurnace) {
                            sa += 0.1F;
                        }

                        sa = Math.min(sa, 1.0F);
                    }

                    pureCook = tconv * sa;
                    taintCook = tconv - pureCook;
                    if (this.getBlockMetadata() != 2
                        || !(totalVis + tconv > this.maxVis)) {
                        this.pureVis += pureCook;
                        this.taintedVis += taintCook;
                        float tspeed = this.speed + (float) this.bellows * 0.1F;
                        this.smeltDelay = 10 + Math.round(tconv / 5.0F / tspeed);
                        if (aboveFurnace) {
                            this.smeltDelay = (int
                            ) ((float) this.smeltDelay
                               * (0.8F
                                  - (float
                                    ) ((TileArcaneFurnace) super.worldObj.getTileEntity(
                                           super.xCoord, super.yCoord - 1, super.zCoord
                                       ))
                                          .bellows
                                      * 0.05F));
                        }

                        AuraManager.addFluxToClosest(
                            this.worldObj,
                            this.xCoord,
                            this.yCoord,
                            this.zCoord,
                            new AspectList().add(Aspect.TAINT, (int) (tconv / 10.0))
                        );

                        --item.stackSize;
                        if (item.stackSize <= 0) {
                            entity.setDead();
                        }

                        this.worldObj.markBlockForUpdate(
                            this.xCoord, this.yCoord, this.zCoord
                        );
                        super.worldObj.spawnParticle(
                            "largesmoke",
                            entity.posX,
                            entity.posY,
                            entity.posZ,
                            0.0,
                            0.0,
                            0.0
                        );
                        super.worldObj.playSoundEffect(
                            (double) ((float) super.xCoord + 0.5F),
                            (double) ((float) super.yCoord + 0.5F),
                            (double) ((float) super.zCoord + 0.5F),
                            "thaumcraft.bubbling",
                            0.25F,
                            0.9F + super.worldObj.rand.nextFloat() * 0.2F
                        );
                    }
                } else {
                    entity.motionX = (double
                    ) ((super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat())
                       * 0.2F);
                    entity.motionY
                        = (double) (0.2F + super.worldObj.rand.nextFloat() * 0.3F);
                    entity.motionZ = (double
                    ) ((super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat())
                       * 0.2F);
                    super.worldObj.playSoundAtEntity(
                        entity,
                        "random.pop",
                        0.5F,
                        2.0F + super.worldObj.rand.nextFloat() * 0.45F
                    );
                    entity.delayBeforeCanPickup = 10;
                    entity.age = 0;
                }
            }
        } else if (this.smeltDelay <= 0 && this.getBlockMetadata() == 3 && (float) Math.round(totalVis + 1.0F) <= this.maxVis) {
            this.smeltDelay = 20 - this.bellows * 2;
            List<EntityLivingBase> list = super.worldObj.getEntitiesWithinAABB(
                EntityLivingBase.class,
                AxisAlignedBB.getBoundingBox(
                    (double) (super.xCoord - 4),
                    (double) (super.yCoord - 4),
                    (double) (super.zCoord - 4),
                    (double) (super.xCoord + 5),
                    (double) (super.yCoord + 5),
                    (double) (super.zCoord + 5)
                )
            );
            boolean sucked = false;

            for (int a = 0; a < list.size(); ++a) {
                if (!(list.get(a) instanceof EntityPlayer)
                    && !(list.get(a) instanceof EntityTameable)
                    && !(list.get(a) instanceof EntityTravelingTrunk)
                    && ((EntityLiving) list.get(a)).hurtTime <= 0
                    && ((EntityLiving) list.get(a)).deathTime <= 0) {
                    if (list.get(a) instanceof EntitySnowman) {
                        ((EntityLiving) list.get(a)).spawnExplosionParticle();
                        ((EntityLiving) list.get(a)).setDead();
                    }

                    aboveFurnace = false;
                    aboveBoostedFurnace = false;
                    if (super.worldObj.getTileEntity(
                            super.xCoord, super.yCoord - 1, super.zCoord
                        ) instanceof TileArcaneFurnace
                        && ((TileArcaneFurnace) super.worldObj.getTileEntity(
                                super.xCoord, super.yCoord - 1, super.zCoord
                            ))
                               .isBurning()) {
                        aboveFurnace = true;
                        if (((TileArcaneFurnace) super.worldObj.getTileEntity(
                                 super.xCoord, super.yCoord - 1, super.zCoord
                             ))
                                .boost) {
                            aboveBoostedFurnace = true;
                        }
                    }

                    tconv = this.conversion;
                    if (aboveFurnace) {
                        tconv += 0.1F
                            + (float) ((TileArcaneFurnace) super.worldObj.getTileEntity(
                                           super.xCoord, super.yCoord - 1, super.zCoord
                                       ))
                                    .bellows
                                * 0.025F;
                        if (aboveBoostedFurnace) {
                            tconv += 0.1F;
                        }

                        tconv = Math.min(tconv, 1.0F);
                    }

                    sa = 1.0F;
                    if (((EntityLiving) list.get(a)).isEntityUndead()) {
                        sa = 0.5F;
                    }

                    pureCook = sa * tconv;
                    taintCook = sa - pureCook;
                    this.pureVis += pureCook;
                    this.taintedVis += taintCook;
                    ((EntityLiving) list.get(a))
                        .attackEntityFrom(DamageSource.generic, 1);
                    ((EntityLiving) list.get(a))
                        .addPotionEffect(new PotionEffect(Potion.hunger.id, 3000, 0));
                    sucked = true;

                    for (int b = 0; b < 3; ++b) {
                        //FXWisp ef = new FXWisp(
                        //    super.worldObj,
                        //    ((EntityLiving) list.get(a)).posX
                        //        + (double) super.worldObj.rand.nextFloat()
                        //        - (double) super.worldObj.rand.nextFloat(),
                        //    ((EntityLiving) list.get(a)).posY
                        //        + (double) (((EntityLiving) list.get(a)).height / 2.0F)
                        //        + (double) super.worldObj.rand.nextFloat()
                        //        - (double) super.worldObj.rand.nextFloat(),
                        //    ((EntityLiving) list.get(a)).posZ
                        //        + (double) super.worldObj.rand.nextFloat()
                        //        - (double) super.worldObj.rand.nextFloat(),
                        //    (double) ((float) super.xCoord + 0.5F),
                        //    (double) ((float) super.yCoord + 0.25F),
                        //    (double) ((float) super.zCoord + 0.5F),
                        //    0.3F,
                        //    5
                        //);
                        //Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                    }
                }
            }

            if (sucked) {
                AuraManager.addFluxToClosest(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    new AspectList().add(Aspect.SOUL, 1)
                );

                this.face = 0;
                super.worldObj.playSoundEffect(
                    (double) super.xCoord,
                    (double) super.yCoord,
                    (double) super.zCoord,
                    "thaumcraft:suck",
                    0.1F,
                    0.8F + super.worldObj.rand.nextFloat() * 0.3F
                );
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            } else if (this.face < 3) {
                ++this.face;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<EntityItem> getContents() {
        float t2x = 0.0F;
        List<EntityItem> list = super.worldObj.getEntitiesWithinAABB(
            EntityItem.class,
            AxisAlignedBB.getBoundingBox(
                (double) super.xCoord,
                (double) ((float) super.yCoord + t2x),
                (double) super.zCoord,
                (double) super.xCoord + 1.0,
                (double) super.yCoord + 1.0 - (double) t2x,
                (double) super.zCoord + 1.0
            )
        );
        return list;
    }

    public boolean ejectContents(EntityPlayer player) {
        boolean ret = false;
        List<EntityItem> list = this.getContents();

        for (int a = 0; a < list.size(); ++a) {
            ((EntityItem) list.get(a)).noClip = true;
            ((EntityItem) list.get(a)).delayBeforeCanPickup = 0;
            ((EntityItem) list.get(a)).motionX
                = (player.posX - ((EntityItem) list.get(a)).posX) * 0.20000000298023224;
            ((EntityItem) list.get(a)).motionY
                = (player.posY - ((EntityItem) list.get(a)).posY) * 0.20000000298023224;
            ((EntityItem) list.get(a)).motionZ
                = (player.posZ - ((EntityItem) list.get(a)).posZ) * 0.20000000298023224;
            ((EntityItem) list.get(a))
                .moveEntity(
                    ((EntityItem) list.get(a)).motionX,
                    ((EntityItem) list.get(a)).motionY,
                    ((EntityItem) list.get(a)).motionZ
                );
            ((EntityItem) list.get(a)).noClip = false;
            ret = true;
        }

        return ret;
    }

    private boolean canCook(ItemStack items) {
        // TODO: recipes
        //if (items == null) {
        //    return false;
        //} else {
        //    float cookvalue
        //        = RecipesCrucible.smelting().getSmeltingResult(items, true, false);
        //    return cookvalue != 0.0F;
        //}
        return false;
    }

    public boolean getConnectable(ForgeDirection face) {
        switch (face) {
            case EAST:
            case SOUTH:
            case WEST:
            case NORTH:
            case DOWN:
                return true;

            default:
                return false;
        }
    }

    public boolean isVisSource() {
        return true;
    }

    public boolean isVisConduit() {
        return false;
    }

    public float getPureVis() {
        return this.pureVis;
    }

    public void setPureVis(float amount) {
        this.pureVis = amount;
    }

    public float getTaintedVis() {
        return this.taintedVis;
    }

    public void setTaintedVis(float amount) {
        this.taintedVis = amount;
    }

    public float getMaxVis() {
        return this.maxVis;
    }

    public float[] subtractVis(float amount) {
        float pureAmount = amount / 2.0F;
        float taintAmount = amount / 2.0F;
        float[] result = new float[] { 0.0F, 0.0F };
        if (amount < 0.001F) {
            return result;
        } else {
            if (this.pureVis < pureAmount) {
                pureAmount = this.pureVis;
            }

            if (this.taintedVis < taintAmount) {
                taintAmount = this.taintedVis;
            }

            if (pureAmount < amount / 2.0F && taintAmount == amount / 2.0F) {
                taintAmount = Math.min(amount - pureAmount, this.taintedVis);
            } else if (taintAmount < amount / 2.0F && pureAmount == amount / 2.0F) {
                pureAmount = Math.min(amount - taintAmount, this.pureVis);
            }

            this.pureVis -= pureAmount;
            this.taintedVis -= taintAmount;
            result[0] = pureAmount;
            result[1] = taintAmount;
            return result;
        }
    }

    public int getVisSuction(HelperLocation loc) {
        return 0;
    }

    public void setVisSuction(int suction) {}

    public int getTaintSuction(HelperLocation loc) {
        return 0;
    }

    public void setTaintSuction(int suction) {}

    public void setSuction(int suction) {}

    public int getSuction(HelperLocation loc) {
        return 0;
    }
}
