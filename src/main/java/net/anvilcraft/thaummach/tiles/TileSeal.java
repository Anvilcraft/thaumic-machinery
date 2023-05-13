package net.anvilcraft.thaummach.tiles;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dev.tilera.auracore.api.AuraNode;
import dev.tilera.auracore.aura.AuraManager;
import dev.tilera.auracore.client.FXSparkle;
import dev.tilera.auracore.helper.Utils;
import net.anvilcraft.thaummach.SealData;
import net.anvilcraft.thaummach.render.PortalRenderer;
import net.anvilcraft.thaummach.utils.HelperLocation;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.client.fx.WRVector3;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.client.fx.particles.FXScorch;
import thaumcraft.client.fx.particles.FXWisp;

public class TileSeal extends TileEntity {
    public static Set<SealData> SEALS = new HashSet<>();
    public SealData otherSeal;
    public PortalRenderer renderer;

    public int delay = 0;
    private int soundDelay = 0;
    public int portalWindow = 0;
    public boolean pOpen = false;
    public float pSize = 0.0F;
    //private int pDelay = 0;
    public boolean worked;
    public boolean isPowering = false;
    public short orientation = -1;
    public byte[] runes = new byte[] { -1, -1, -1 };

    protected boolean gettingPower() {
        return super.worldObj.isBlockIndirectlyGettingPowered(
                   super.xCoord, super.yCoord, super.zCoord
               )
            || super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord + 1, super.zCoord
            );
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote && this.otherSeal != null) {
            if (this.renderer == null)
                this.renderer = new PortalRenderer(this);
        }

        if (this.delay <= 0) {
            boolean oldPower = this.isPowering;
            this.isPowering = false;
            --this.soundDelay;
            if (!this.worldObj.isRemote
                && (!this.gettingPower() || this.runes[0] == 0 && this.runes[1] == 4)) {
                switch (this.runes[0]) {
                    case 0:
                        this.magicSeal();
                        break;
                    case 1:
                        this.airSeal();
                        break;
                    case 2:
                        this.waterSeal();
                        break;
                    case 3:
                        this.earthSeal();
                        break;
                    case 4:
                        this.fireSeal();
                        break;
                    case 5:
                        this.darkSeal();
                }
            }

            if (oldPower != this.isPowering) {
                HelperLocation loc = new HelperLocation(this, this.orientation);

                int xx;
                int yy;
                int zz;
                for (xx = -1; xx <= 1; ++xx) {
                    for (yy = -1; yy <= 1; ++yy) {
                        for (zz = -1; zz <= 1; ++zz) {
                            super.worldObj.markBlockForUpdate(
                                (int) loc.x + xx, (int) loc.y + yy, (int) loc.z + zz
                            );
                        }
                    }
                }

                super.worldObj.notifyBlocksOfNeighborChange(
                    (int) loc.x, (int) loc.y, (int) loc.z, Blocks.air
                );
                loc.moveBackwards(1.0);

                for (xx = -1; xx <= 1; ++xx) {
                    for (yy = -1; yy <= 1; ++yy) {
                        for (zz = -1; zz <= 1; ++zz) {
                            super.worldObj.markBlockForUpdate(
                                (int) loc.x + xx, (int) loc.y + yy, (int) loc.z + zz
                            );
                        }
                    }
                }

                super.worldObj.notifyBlocksOfNeighborChange(
                    (int) loc.x, (int) loc.y, (int) loc.z, Blocks.air
                );
            }
        }

        if (this.delay > 0) {
            --this.delay;
        }

        if (this.pOpen && (double) this.pSize < 1.4) {
            this.pSize += 0.15F;
        }

        if (!this.pOpen && this.pSize > 0.0F || this.delay > 0 && this.pSize > 0.0F) {
            this.pSize -= 0.25F;
        }

        if ((double) this.pSize > 1.4) {
            this.pSize = 1.4F;
        }

        if (this.pSize < 0.0F) {
            this.pSize = 0.0F;
        }
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    private void magicSeal() {
        switch (this.runes[1]) {
            case -1:
                this.magicBoost();
                this.delay = 20;
                break;
            case 0:
                switch (this.runes[2]) {
                    case -1:
                        this.magicBoost();
                        this.delay = 15;
                        return;
                    case 0:
                        this.magicBoost();
                        this.delay = 10;
                        return;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 1:
                SealData sd = new SealData(this);
                if (!SEALS.contains(sd)) {
                    SEALS.add(sd);
                    this.worldObj.markBlockForUpdate(
                        this.xCoord, this.yCoord, this.zCoord
                    );
                }

                this.handlePortals();
                break;
            case 2:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 3:
                switch (this.runes[2]) {
                    case -1:
                        if (this.worldObj.isRemote) {
                            FXSparkle ef = new FXSparkle(
                                super.worldObj,
                                (double) this.sealX(false),
                                (double) this.sealY(false),
                                (double) this.sealZ(false),
                                (double
                                ) ((float) super.xCoord + super.worldObj.rand.nextFloat()
                                ),
                                (double
                                ) ((float) super.yCoord + super.worldObj.rand.nextFloat()
                                ),
                                (double
                                ) ((float) super.zCoord + super.worldObj.rand.nextFloat()
                                ),
                                1.0F,
                                super.worldObj.rand.nextBoolean() ? 0 : 3,
                                4
                            );
                            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                            this.delay = 5;
                        }
                        return;
                    case 3:
                        FXSparkle ef2 = new FXSparkle(
                            super.worldObj,
                            (double) this.sealX(false),
                            (double) this.sealY(false),
                            (double) this.sealZ(false),
                            (double
                            ) ((float) super.xCoord + super.worldObj.rand.nextFloat()),
                            (double
                            ) ((float) super.yCoord + super.worldObj.rand.nextFloat()),
                            (double
                            ) ((float) super.zCoord + super.worldObj.rand.nextFloat()),
                            1.0F,
                            super.worldObj.rand.nextBoolean() ? 0 : 3,
                            4
                        );
                        Minecraft.getMinecraft().effectRenderer.addEffect(ef2);
                        this.delay = 5;
                        return;
                    default:
                        return;
                }
            case 4:
                switch (this.runes[2]) {
                    case -1:
                        this.scan(3, true, true, true, true);
                        break;
                    case 0:
                        this.scan(6, false, true, false, false);
                        break;
                    case 1:
                        this.scan(9, true, true, true, true);
                    case 2:
                    default:
                        break;
                    case 3:
                        this.scan(6, false, false, true, false);
                        break;
                    case 4:
                        this.scan(6, true, false, false, false);
                        break;
                    case 5:
                        this.scan(6, false, false, false, true);
                }

                this.delay = 5;
            case 5:
        }
    }

    private void airSeal() {
        switch (this.runes[1]) {
            case -1:
                this.pushEntity(false, true, true, 3, 0.03F);
                this.delay = 2;
                break;
            case 0:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 1:
                switch (this.runes[2]) {
                    case -1:
                        this.pushEntity(false, true, true, 5, 0.06F);
                        break;
                    case 0:
                        this.pushEntity(false, true, false, 7, 0.08F);
                        break;
                    case 1:
                        this.pushEntity(false, true, true, 7, 0.08F);
                    case 2:
                    case 4:
                    case 5:
                    default:
                        break;
                    case 3:
                        this.pushEntity(false, false, true, 7, 0.08F);
                }

                this.delay = 2;
                break;
            case 2:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 3:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 4:
                this.shock();
                this.delay = 8 + super.worldObj.rand.nextInt(3) - this.runeAmount(1) * 2;
                break;
            case 5:
            label86:
                switch (this.runes[2]) {
                    case -1:
                        this.pushEntity(true, true, true, 5, 0.06F);
                        break;
                    case 0:
                        this.pushEntity(true, true, false, 7, 0.08F);
                        break;
                    case 1:
                        this.pushEntity(true, true, true, 7, 0.08F);
                    case 2:
                    default:
                        break;
                    case 3:
                        this.pushEntity(true, false, true, 7, 0.08F);
                        break;
                    case 4:
                        this.pushEntity(true, true, true, 6, 0.07F);
                        List<Entity> list = this.getEntities(
                            super.worldObj, 0.0F, this.orientation, false
                        );
                        int a = 0;

                        while (true) {
                            if (a >= list.size()) {
                                break label86;
                            }

                            Entity entity = (Entity) list.get(a);
                            if (!(entity instanceof EntityPlayer)
                                /*&& !(entity instanceof EntityTravelingTrunk)*/) {
                                entity.attackEntityFrom(DamageSource.generic, 1);
                                net.anvilcraft.thaummach.utils.UtilsFX.poof(
                                    super.worldObj,
                                    (float) entity.posX - 0.5F,
                                    (float) entity.posY - 0.5F + entity.getEyeHeight(),
                                    (float) entity.posZ - 0.5F
                                );
                                super.worldObj.playSoundEffect(
                                    (double) super.xCoord,
                                    (double) super.yCoord,
                                    (double) super.zCoord,
                                    // TODO: 99% wrong sound ID
                                    "random.fizz",
                                    0.5F,
                                    2.0F + super.worldObj.rand.nextFloat() * 0.4F
                                );
                            }

                            ++a;
                        }
                    case 5:
                        this.pushEntity(true, false, true, 6, 0.04F);
                        List<Entity> list2 = this.getEntities(
                            super.worldObj, 0.2F, this.orientation, false
                        );

                        for (int b = 0; b < list2.size(); ++b) {
                            Entity entity = (Entity) list2.get(b);
                            if (entity instanceof EntityItem) {
                                this.attemptItemPickup((EntityItem) entity);
                            }
                        }
                }

                this.delay = 2;
        }
    }

    private void waterSeal() {
        switch (this.runes[1]) {
            case -1:
                this.hydrate(3);
                this.delay = 20;
                break;
            case 0:
                switch (this.runes[2]) {
                    case -1:
                        this.heal(3, true, true, true, false);
                        this.delay = 20;
                        return;
                    case 0:
                        this.heal(5, false, false, true, false);
                        this.delay = 20;
                        return;
                    case 1:
                        this.heal(5, true, true, true, false);
                        this.delay = 10;
                        return;
                    case 2:
                        this.heal(5, true, true, true, true);
                        this.delay = 20;
                        return;
                    case 3:
                        this.heal(5, false, true, false, false);
                        this.delay = 20;
                        return;
                    case 4:
                        this.heal(5, true, true, true, false);
                        this.delay = 20;
                        return;
                    case 5:
                        this.heal(5, true, false, false, false);
                        this.delay = 20;
                        return;
                    default:
                        return;
                }
            case 1:
                switch (this.runes[2]) {
                    case -1:
                        this.freeze(6, true, true, false);
                        return;
                    case 0:
                        this.freeze(10, true, false, false);
                        return;
                    case 1:
                        this.freeze(10, true, true, false);
                        return;
                    case 2:
                        this.freeze(15, true, false, false);
                        return;
                    case 3:
                        this.freeze(10, false, true, false);
                        return;
                    case 4:
                        this.freeze(10, true, true, false);
                        return;
                    case 5:
                        this.freeze(10, false, false, true);
                        return;
                    default:
                        return;
                }
            case 2:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 3:
                switch (this.runes[2]) {
                    case -1:
                        this.fertilize(3);
                        this.delay = 30;
                        return;
                    case 0:
                    case 2:
                    case 4:
                    case 5:
                    default:
                        return;
                    case 1:
                        this.fertilize(3);
                        this.delay = 15;
                        return;
                    case 3:
                        this.fertilize(6);
                        this.delay = 30;
                        return;
                }
            case 4:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 5:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                }
        }
    }

    private void earthSeal() {
        switch (this.runes[1]) {
            case -1:
                this.till(3);
                this.delay = 20;
                break;
            case 0:
                switch (this.runes[2]) {
                    case -1:
                        //this.replant(6);
                        this.delay = 40;
                        return;
                    case 0:
                    case 2:
                    case 4:
                    case 5:
                    default:
                        return;
                    case 1:
                        //this.replant(6);
                        this.delay = 20;
                        return;
                    case 3:
                        //this.replant(12);
                        this.delay = 40;
                        return;
                }
            case 1:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 2:
                switch (this.runes[2]) {
                    case -1:
                        this.harvest(3);
                        this.delay = 20;
                        return;
                    case 0:
                    case 2:
                    case 4:
                    case 5:
                    default:
                        return;
                    case 1:
                        this.harvest(3);
                        this.delay = 10;
                        return;
                    case 3:
                        this.harvest(6);
                        this.delay = 20;
                        return;
                }
            case 3:
                switch (this.runes[2]) {
                    case -1:
                        this.till(3);
                        this.delay = 20;
                        return;
                    case 0:
                    case 2:
                    case 4:
                    case 5:
                    default:
                        return;
                    case 1:
                        this.till(3);
                        this.delay = 10;
                        return;
                    case 3:
                        this.till(6);
                        this.delay = 20;
                        return;
                }
            case 4:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 5:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                }
        }
    }

    private void fireSeal() {
        switch (this.runes[1]) {
            case -1:
                this.scorch(4, 1, true, true, false);
                break;
            case 0:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 1:
                switch (this.runes[2]) {
                    case -1:
                        this.beam(6, 1, true, false, true, false, 6.0F, 2.0F);
                        this.delay = 5;
                        return;
                    case 0:
                    case 2:
                    case 3:
                    case 5:
                    default:
                        return;
                    case 1:
                        this.beam(9, 2, true, true, true, false, 9.0F, 2.0F);
                        this.delay = 5;
                        return;
                    case 4:
                        this.beam(9, 3, true, false, true, true, 9.0F, 3.0F);
                        this.delay = 5;
                        return;
                }
            case 2:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 3:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 4:
                switch (this.runes[2]) {
                    case -1:
                        this.scorch(5, 2, true, true, false);
                        return;
                    case 0:
                        this.scorch(7, 3, true, false, false);
                        return;
                    case 1:
                        this.scorch(8, 3, true, true, false);
                        return;
                    case 2:
                        this.scorch(6, 6, true, true, false);
                        return;
                    case 3:
                        this.scorch(7, 3, false, true, false);
                        return;
                    case 4:
                        this.scorch(7, 3, true, true, false);
                        return;
                    case 5:
                        this.scorch(7, 3, false, false, true);
                        return;
                    default:
                        return;
                }
            case 5:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                }
        }
    }

    private void darkSeal() {
        switch (this.runes[1]) {
            case -1:
            default:
                break;
            case 0:
                switch (this.runes[2]) {
                    case -1:
                        this.nullifyAura();
                        this.delay = 100;
                        return;
                    case 0:
                        this.nullifyAura();
                        this.delay = 80;
                        return;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 1:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 2:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 3:
                switch (this.runes[2]) {
                    case -1:
                        this.preventSpawn(6);
                        return;
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    default:
                        return;
                    case 3:
                        this.preventSpawn(12);
                        return;
                }
            case 4:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    default:
                        return;
                }
            case 5:
                switch (this.runes[2]) {
                    case -1:
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                }
        }
    }

    private void nullifyAura() {
        if (super.worldObj.isRemote)
            return;

        int nodeId = AuraManager.getClosestAuraWithinRange(
            super.worldObj, super.xCoord, super.yCoord, super.zCoord, 512
        );

        if (nodeId < 0)
            return;

        AuraNode node = AuraManager.getNode(nodeId);

        if (node.level > 0 /* && node.taint > 0*/) {
            AuraManager.addRandomFlux(super.worldObj, node, 1);
            AuraManager.queueNodeChanges(nodeId, -1, 0, false, null, 0, 0, 0);

            FXSparkle ef = new FXSparkle(
                super.worldObj,
                (double) ((float) super.xCoord + super.worldObj.rand.nextFloat()),
                (double) ((float) super.yCoord + super.worldObj.rand.nextFloat()),
                (double) ((float) super.zCoord + super.worldObj.rand.nextFloat()),
                2.0F,
                5,
                5
            );
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        }
    }

    private void preventSpawn(int range) {
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        for (int a = 0; a < list.size(); ++a) {
            Entity entity = (Entity) list.get(a);
            if (entity instanceof EntityMob && ((EntityMob) entity).ticksExisted < 5) {
                entity.setDead();
            }
        }
    }

    private boolean
    scan(int range, boolean items, boolean mobs, boolean animals, boolean pvp) {
        if (super.worldObj.isRemote)
            return false;

        boolean foundsomething = false;
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        int q;
        for (q = 0; q < list.size(); ++q) {
            Entity entity = (Entity) list.get(q);
            if (items && entity instanceof EntityItem
                || animals && entity instanceof EntityAnimal
                || mobs && (entity instanceof EntityMob || entity instanceof IMob)
                || pvp && entity instanceof EntityPlayer) {
                this.worked = true;
                foundsomething = true;
                break;
            }
        }

        this.isPowering = foundsomething;
        if (foundsomething) {
            q = Minecraft.getMinecraft().gameSettings.fancyGraphics ? 6 : 3;

            for (int a = 0; a < q; ++a) {
                FXWisp ef = new FXWisp(
                    super.worldObj,
                    (double) this.sealX(false),
                    (double) this.sealY(false),
                    (double) this.sealZ(false),
                    (double) ((float) super.xCoord + super.worldObj.rand.nextFloat()),
                    (double) ((float) super.yCoord + super.worldObj.rand.nextFloat()),
                    (double) ((float) super.zCoord + super.worldObj.rand.nextFloat()),
                    0.1F,
                    4
                );
                Minecraft.getMinecraft().effectRenderer.addEffect(ef);
            }
        }

        return false;
    }

    private boolean freeze(int range, boolean mobs, boolean animals, boolean pvp) {
        if (super.worldObj.isRemote)
            return false;

        boolean didsomething = false;
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        for (int a = 0; a < list.size(); ++a) {
            Entity entity = (Entity) list.get(a);
            if (entity instanceof EntityLivingBase
                && (animals && entity instanceof EntityAnimal

                        && !(entity instanceof EntityTameable)
                    || mobs && (entity instanceof EntityMob || entity instanceof IMob)
                    || pvp && entity instanceof EntityPlayer)) {
                entity.motionX *= 0.05;
                entity.motionY *= 0.05;
                entity.motionZ *= 0.05;
                if (this.runes[2] == 1) {
                    entity.motionY += 0.05999999865889549;
                    entity.onGround = false;
                }

                // TODO: implement FXFreeze
                //for (int repeat = 0; repeat < 3; ++repeat) {
                //    FXFreeze ef = new FXFreeze(
                //        super.worldObj,
                //        this.sealX(false),
                //        this.sealY(false),
                //        this.sealZ(false),
                //        entity
                //    );
                //    ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
                //}

                this.worked = true;
                didsomething = true;
                if (this.runes[2] != 4) {
                    break;
                }
            }
        }

        if (this.soundDelay <= 0 && didsomething) {
            super.worldObj.playSoundEffect(
                (double) ((float) super.xCoord + 0.5F),
                (double) ((float) super.yCoord + 0.5F),
                (double) ((float) super.zCoord + 0.5F),
                "thaumcraft.wind",
                0.2F,
                0.9F + super.worldObj.rand.nextFloat() * 0.1F
            );
            this.soundDelay = 25;
        }

        return false;
    }

    private boolean fertilize(int range) {
        if (super.worldObj.isRemote)
            return false;

        int xm = 0;
        int xp = 0;
        int ym = 0;
        int yp = 0;
        int zm = 0;
        int zp = 0;

        if (this.orientation == 0) {
            xm = zm = -range;
            zp = range;
            xp = range;
            ym = -(range * 2);
        } else if (this.orientation == 1) {
            xm = zm = -range;
            zp = range;
            xp = range;
            yp = range * 2;
        } else if (this.orientation == 2) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zm = -(range * 2);
        } else if (this.orientation == 3) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zp = range * 2;
        } else if (this.orientation == 4) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xm = -(range * 2);
        } else if (this.orientation == 5) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xp = range * 2;
        }

        for (int x = xm; x <= xp; ++x) {
            for (int y = ym; y <= yp; ++y) {
                for (int z = zm; z <= zp; ++z) {
                    if (super.worldObj.getBlockLightValue(
                            super.xCoord + x, super.yCoord + y + 1, super.zCoord + z
                        ) >= 8
                        && super.yCoord + y + 1 <= super.worldObj.getHeight()
                        && super.yCoord + y - 1 >= 0) {
                        if (Utils.useBonemealAtLoc(
                                super.worldObj,
                                super.xCoord + x,
                                super.yCoord + y,
                                super.zCoord + z
                            )) {
                            net.anvilcraft.thaummach.utils.UtilsFX.poofUpwards(
                                super.worldObj,
                                super.xCoord + x,
                                super.yCoord + y,
                                super.zCoord + z,
                                3
                            );
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean harvest(int range) {
        int xm = 0;
        int xp = 0;
        int ym = 0;
        int yp = 0;
        int zm = 0;
        int zp = 0;
        if (this.orientation == 0) {
            xm = zm = -range;
            zp = range;
            xp = range;
            ym = -(range * 2);
        } else if (this.orientation == 1) {
            xm = zm = -range;
            zp = range;
            xp = range;
            yp = range * 2;
        } else if (this.orientation == 2) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zm = -(range * 2);
        } else if (this.orientation == 3) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zp = range * 2;
        } else if (this.orientation == 4) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xm = -(range * 2);
        } else if (this.orientation == 5) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xp = range * 2;
        }

        for (int x = xm; x <= xp; ++x) {
            for (int y = ym; y <= yp; ++y) {
                for (int z = zm; z <= zp; ++z) {
                    if (super.yCoord + y + 1 <= super.worldObj.getHeight()
                        && super.yCoord + y - 1 >= 0) {
                        // TODO: alter ne
                        //int cbID = super.worldObj.getBlockId(
                        //    super.xCoord + x, super.yCoord + y, super.zCoord + z
                        //);
                        //super.worldObj.getBlockId(
                        //    super.xCoord + x, super.yCoord + y + 1, super.zCoord + z
                        //);
                        //int bbID = super.worldObj.getBlockId(
                        //    super.xCoord + x, super.yCoord + y - 1, super.zCoord + z
                        //);
                        //TileEntity te = super.worldObj.getBlockTileEntity(
                        //    super.xCoord + x, super.yCoord + y - 1, super.zCoord + z
                        //);
                        //if (super.worldObj.rand.nextInt(10) == 0) {
                        //    if (cbID == Block.crops.blockID
                        //            && super.worldObj.getBlockMetadata(
                        //                   super.xCoord + x,
                        //                   super.yCoord + y,
                        //                   super.zCoord + z
                        //               ) == 7
                        //        || cbID == Block.melon.blockID
                        //        || cbID == Block.tallGrass.blockID
                        //        || cbID == Block.pumpkin.blockID
                        //        || cbID == Block.plantYellow.blockID
                        //        || cbID == Block.plantRed.blockID
                        //        || cbID == flaxId
                        //            && super.worldObj.getBlockMetadata(
                        //                   super.xCoord + x,
                        //                   super.yCoord + y,
                        //                   super.zCoord + z
                        //               ) == 4
                        //        || cbID == Block.plantRed.blockID
                        //        || cbID == Block.reed.blockID
                        //            && bbID == Block.reed.blockID
                        //        || cbID == Block.cactus.blockID
                        //            && bbID == Block.cactus.blockID) {
                        //        Block.blocksList[cbID].dropBlockAsItem(
                        //            super.worldObj,
                        //            super.xCoord + x,
                        //            super.yCoord + y,
                        //            super.zCoord + z,
                        //            super.worldObj.getBlockMetadata(
                        //                super.xCoord + x,
                        //                super.yCoord + y,
                        //                super.zCoord + z
                        //            ),
                        //            0
                        //        );
                        //        super.worldObj.setBlockWithNotify(
                        //            super.xCoord + x,
                        //            super.yCoord + y,
                        //            super.zCoord + z,
                        //            0
                        //        );
                        //        ThaumCraftCore.poof(
                        //            super.worldObj,
                        //            (float) (super.xCoord + x),
                        //            (float) (super.yCoord + y),
                        //            (float) (super.zCoord + z)
                        //        );
                        //        this.worked = true;
                        //        return true;
                        //    }

                        //    if (te != null && te instanceof TECrop) {
                        //        TECrop tec = (TECrop) te;
                        //        if (tec.id != -1 && tec.harvest(false)) {
                        //            ThaumCraftCore.poof(
                        //                super.worldObj,
                        //                (float) (super.xCoord + x),
                        //                (float) (super.yCoord + y),
                        //                (float) (super.zCoord + z)
                        //            );
                        //            this.worked = true;
                        //            return true;
                        //        }
                        //    }
                        //}
                    }
                }
            }
        }

        return false;
    }

    private boolean hydrate(int range) {
        int xm = 0;
        int xp = 0;
        int ym = 0;
        int yp = 0;
        int zm = 0;
        int zp = 0;
        if (this.orientation == 0) {
            xm = zm = -range;
            zp = range;
            xp = range;
            ym = -(range * 2);
        } else if (this.orientation == 1) {
            xm = zm = -range;
            zp = range;
            xp = range;
            yp = range * 2;
        } else if (this.orientation == 2) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zm = -(range * 2);
        } else if (this.orientation == 3) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zp = range * 2;
        } else if (this.orientation == 4) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xm = -(range * 2);
        } else if (this.orientation == 5) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xp = range * 2;
        }

        for (int x = xm; x <= xp; ++x) {
            for (int y = ym; y <= yp; ++y) {
                for (int z = zm; z <= zp; ++z) {
                    if (super.yCoord + y <= super.worldObj.getHeight()
                        && super.yCoord + y >= 0) {
                        Block cbID = super.worldObj.getBlock(
                            super.xCoord + x, super.yCoord + y, super.zCoord + z
                        );
                        int md = super.worldObj.getBlockMetadata(
                            super.xCoord + x, super.yCoord + y, super.zCoord + z
                        );
                        if (cbID == Blocks.farmland && md != 7
                            && super.worldObj.rand.nextInt(10) == 0) {
                            super.worldObj.setBlockMetadataWithNotify(
                                super.xCoord + x, super.yCoord + y, super.zCoord + z, 7, 3
                            );
                            net.anvilcraft.thaummach.utils.UtilsFX.sparkleDown(
                                super.worldObj,
                                super.xCoord + x,
                                super.yCoord + y + 1,
                                super.zCoord + z,
                                2
                            );
                            this.worked = true;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    // TODO: implement plant stuff
    //private boolean replant(int range) {
    //    int xm = 0;
    //    int xp = 0;
    //    int ym = 0;
    //    int yp = 0;
    //    int zm = 0;
    //    int zp = 0;
    //    if (this.orientation == 0) {
    //        xm = zm = -range;
    //        zp = range;
    //        xp = range;
    //        ym = -(range * 2);
    //    } else if (this.orientation == 1) {
    //        xm = zm = -range;
    //        zp = range;
    //        xp = range;
    //        yp = range * 2;
    //    } else if (this.orientation == 2) {
    //        xm = ym = -range;
    //        yp = range;
    //        xp = range;
    //        zm = -(range * 2);
    //    } else if (this.orientation == 3) {
    //        xm = ym = -range;
    //        yp = range;
    //        xp = range;
    //        zp = range * 2;
    //    } else if (this.orientation == 4) {
    //        zm = ym = -range;
    //        yp = range;
    //        zp = range;
    //        xm = -(range * 2);
    //    } else if (this.orientation == 5) {
    //        zm = ym = -range;
    //        yp = range;
    //        zp = range;
    //        xp = range * 2;
    //    }

    //    for (int x = xm; x <= xp; ++x) {
    //        for (int y = ym; y <= yp; ++y) {
    //            for (int z = zm; z <= zp; ++z) {
    //                if (super.yCoord + y + 1 <= super.worldObj.getHeight()
    //                    && super.yCoord + y >= 0) {
    //                    Block cbID = super.worldObj.getBlock(
    //                        super.xCoord + x, super.yCoord + y, super.zCoord + z
    //                    );
    //                    super.worldObj.getBlockMetadata(
    //                        super.xCoord + x, super.yCoord + y, super.zCoord + z
    //                    );
    //                    if (cbID == Blocks.farmland
    //                        && super.worldObj.isAirBlock(
    //                            super.xCoord + x, super.yCoord + y + 1, super.zCoord + z
    //                        )
    //                        && super.worldObj.rand.nextInt(10) == 0 && this.fetchSeed())
    //                        { super.worldObj.setBlockAndMetadataWithNotify(
    //                            super.xCoord + x,
    //                            super.yCoord + y + 1,
    //                            super.zCoord + z,
    //                            Blocks.crops.blockID,
    //                            0
    //                        );
    //                        ThaumCraftCore.poofUpwards(
    //                            super.worldObj,
    //                            super.xCoord + x,
    //                            super.yCoord + y + 1,
    //                            super.zCoord + z,
    //                            0
    //                        );
    //                        this.worked = true;
    //                        return true;
    //                    }
    //                }
    //            }
    //        }
    //    }

    //    return false;
    //}

    //private boolean fetchSeed() {
    //    for (int x = -2; x <= 2; ++x) {
    //        for (int y = -2; y <= 2; ++y) {
    //            for (int z = -2; z <= 2; ++z) {
    //                if ((x != 0 || y != 0 || z != 0) && super.yCoord + y >= 0) {
    //                    TileEntity block = super.worldObj.getBlockTileEntity(
    //                        super.xCoord + x, super.yCoord + y, super.zCoord + z
    //                    );
    //                    if (block instanceof IInventory) {
    //                        IInventory chest = (IInventory) block;

    //                        for (int a = 0; a < chest.getSizeInventory(); ++a) {
    //                            if (chest.getStackInSlot(a) != null
    //                                && chest.getStackInSlot(a).getItem().shiftedIndex
    //                                    == Item.seeds.shiftedIndex) {
    //                                chest.decrStackSize(a, 1);
    //                                ThaumCraftCore.poofGood(
    //                                    super.worldObj,
    //                                    (float) block.xCoord,
    //                                    (float) block.yCoord,
    //                                    (float) block.zCoord
    //                                );
    //                                return true;
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //    }

    //    return false;
    //}

    private boolean till(int range) {
        int xm = 0;
        int xp = 0;
        int ym = 0;
        int yp = 0;
        int zm = 0;
        int zp = 0;
        if (this.orientation == 0) {
            xm = zm = -range;
            zp = range;
            xp = range;
            ym = -(range * 2);
        } else if (this.orientation == 1) {
            xm = zm = -range;
            zp = range;
            xp = range;
            yp = range * 2;
        } else if (this.orientation == 2) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zm = -(range * 2);
        } else if (this.orientation == 3) {
            xm = ym = -range;
            yp = range;
            xp = range;
            zp = range * 2;
        } else if (this.orientation == 4) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xm = -(range * 2);
        } else if (this.orientation == 5) {
            zm = ym = -range;
            yp = range;
            zp = range;
            xp = range * 2;
        }

        for (int x = xm; x <= xp; ++x) {
            for (int y = ym; y <= yp; ++y) {
                for (int z = zm; z <= zp; ++z) {
                    if (super.yCoord + y <= super.worldObj.getHeight()
                        && super.yCoord + y >= 0) {
                        Block cbID = super.worldObj.getBlock(
                            super.xCoord + x, super.yCoord + y, super.zCoord + z
                        );
                        Block tbID = super.worldObj.getBlock(
                            super.xCoord + x, super.yCoord + y + 1, super.zCoord + z
                        );
                        if ((cbID == Blocks.dirt || cbID == Blocks.grass)
                            && tbID == Blocks.air
                            && super.worldObj.rand.nextInt(10) == 0) {
                            super.worldObj.setBlock(
                                super.xCoord + x,
                                super.yCoord + y,
                                super.zCoord + z,
                                Blocks.farmland,
                                0,
                                3
                            );
                            net.anvilcraft.thaummach.utils.UtilsFX.sparkleUp(
                                super.worldObj,
                                super.xCoord + x,
                                super.yCoord + y + 1,
                                super.zCoord + z,
                                3
                            );
                            this.worked = true;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void magicBoost() {
        int auraId = AuraManager.getClosestAuraWithinRange(
            super.worldObj, super.xCoord, super.yCoord, super.zCoord, 512
        );

        if (auraId < 0)
            return;

        // TODO: WTF
        //int auraX = super.xCoord >> 4;
        //int auraZ = super.zCoord >> 4;
        //SIAuraChunk ac = (SIAuraChunk) mod_ThaumCraft.AuraHM.get(
        //    Arrays.asList(auraX, auraZ, ThaumCraftCore.getDimension(super.worldObj))
        //);
        //if (ac != null && ac.boost < 100) {
        //    this.worked = true;
        //    ++ac.boost;
        //    FXWisp ef = new FXWisp(
        //        super.worldObj,
        //        (double) this.sealX(false),
        //        (double) this.sealY(false),
        //        (double) this.sealZ(false),
        //        (double) ((float) super.xCoord + super.worldObj.rand.nextFloat()),
        //        (double) ((float) super.yCoord + super.worldObj.rand.nextFloat()),
        //        (double) ((float) super.zCoord + super.worldObj.rand.nextFloat()),
        //        0.1F,
        //        super.worldObj.rand.nextInt(5)
        //    );
        //    ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
        //}
    }

    private void handlePortals() {
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, 1, this.orientation, false);
        boolean p = false;

        int q;
        for (q = 0; q < list.size(); ++q) {
            if (list.get(q) instanceof EntityPlayer) {
                p = true;
                break;
            }
        }

        if (list.size() > 0) {
            Iterator<SealData> i$ = SEALS.iterator();

            boolean fs = false;
        label74: {
            SealData pd;
            do {
                do {
                    do {
                        if (!i$.hasNext()) {
                            break label74;
                        }

                        pd = i$.next();
                    } while (pd.dim != list.get(0).worldObj.provider.dimensionId);
                } while (pd.rune != this.runes[2]);
            } while (pd.x == super.xCoord && pd.y == super.yCoord && pd.z == super.zCoord
            );

            this.otherSeal = pd;
            super.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

            if (!this.pOpen && p) {
                super.worldObj.playSoundEffect(
                    (double) super.xCoord + 0.5,
                    (double) super.yCoord + 0.5,
                    (double) super.zCoord + 0.5,
                    "thaummach:popen",
                    0.4F,
                    1.0F + super.worldObj.rand.nextFloat() * 0.2F
                );
            }

            if (this.delay <= 0 && p) {
                //this.renderTeleportDest();
                this.delay = 10;
            }

            --this.delay;
            this.pOpen = true;
            fs = true;
        }
            if (!fs && this.pOpen) {
                this.pOpen = false;
            }
        } else {
            this.otherSeal = null;
            super.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

            if (this.pOpen) {
                list = this.getEntitiesSorted(super.worldObj, 2, this.orientation, false);

                for (q = 0; q < list.size(); ++q) {
                    super.worldObj.playSoundEffect(
                        (double) super.xCoord + 0.5,
                        (double) super.yCoord + 0.5,
                        (double) super.zCoord + 0.5,
                        "thaummach:pclose",
                        0.4F,
                        1.0F + super.worldObj.rand.nextFloat() * 0.2F
                    );
                }
            }

            this.pOpen = false;
        }

        this.teleport();
    }

    // TODO: implement portal rendering with looking glass
    //private void renderTeleportDest() {
    //    if (this.txRender == null) {
    //        this.txRender = new PortalRenderer();
    //    }

    //    new ArrayList();
    //    Collection p = mod_ThaumCraft.SpecialTileHM.values();
    //    int count = -1;
    //    boolean skipped = true;
    //    boolean playsound = false;
    //    Iterator i$ = p.iterator();

    //    while (i$.hasNext()) {
    //        SISpecialTile pd = (SISpecialTile) i$.next();
    //        if (pd.type == 0
    //            && pd.dimension == ModLoader.getMinecraftInstance().thePlayer.dimension
    //            && pd.rune == this.runes[2]
    //            && (pd.x != super.xCoord || pd.y != super.yCoord || pd.z != super.zCoord
    //            )) {
    //            if (!(super.worldObj.getBlockTileEntity(pd.x, pd.y, pd.z)
    //                      instanceof TileSeal)) {
    //                mod_ThaumCraft.DeleteSpecialTileFromList(pd);
    //                break;
    //            }

    //            ++count;
    //            if (count >= this.portalWindow) {
    //                skipped = false;
    //                TileEntity ts = super.worldObj.getBlockTileEntity(pd.x, pd.y,
    //                pd.z);Api if (ts != null && ts instanceof TileSeal) {
    //                    PortalRenderer.createPortalView(
    //                        this.txRender, this, (TileSeal) ts
    //                    );
    //                    break;
    //                }
    //            }
    //        }
    //    }

    //    if (skipped) {
    //        this.portalWindow = 0;
    //    }
    //}

    public boolean teleport() {
        List<Entity> list = super.worldObj.getEntitiesWithinAABB(
            Entity.class,
            AxisAlignedBB.getBoundingBox(
                (double) super.xCoord,
                (double) super.yCoord,
                (double) super.zCoord,
                (double) (super.xCoord + 1),
                (double) (super.yCoord + 1),
                (double) (super.zCoord + 1)
            )
        );

        if (list.isEmpty()) {
            return false;
        } else {
            Entity entity = (Entity) list.get(0);
            if (entity instanceof EntityFX) {
                return false;
            } else {
                if (this.otherSeal == null) {
                    return false;
                } else {
                    SealData targetDest = this.otherSeal;
                    TileEntity ts = super.worldObj.getTileEntity(
                        targetDest.x, targetDest.y, targetDest.z
                    );
                    if (ts != null && ts instanceof TileSeal) {
                        TileSeal target = (TileSeal) ts;
                        if (target.runes[0] == 0 && target.runes[1] == 1) {
                            target.delay = 40;
                            float tYaw = entity.rotationYaw;
                            switch (target.orientation) {
                                case 2:
                                    tYaw = 180.0F;
                                    break;
                                case 3:
                                    tYaw = 0.0F;
                                    break;
                                case 4:
                                    tYaw = 90.0F;
                                    break;
                                case 5:
                                    tYaw = 270.0F;
                            }

                            int diff = this.orientation - target.orientation;
                            double t;
                            if (diff == -3 || diff == 2
                                || diff == -1
                                    && this.orientation + target.orientation != 5
                                    && this.orientation + target.orientation != 9) {
                                t = entity.motionX;
                                entity.motionX = entity.motionZ;
                                entity.motionZ = -t;
                                if (entity.ridingEntity != null) {
                                    entity.ridingEntity.motionX
                                        = entity.ridingEntity.motionZ;
                                    entity.ridingEntity.motionZ = -t;
                                }
                            } else if (diff == -2 || diff == 3 || diff == 1 &&
                                    this.orientation + target.orientation != 5 &&
                                    this.orientation + target.orientation != 9) {
                                t = entity.motionX;
                                entity.motionX = -entity.motionZ;
                                entity.motionZ = t;
                                if (entity.ridingEntity != null) {
                                    entity.ridingEntity.motionX
                                        = -entity.ridingEntity.motionZ;
                                    entity.ridingEntity.motionZ = t;
                                }
                            } else if (diff == 0) {
                                entity.motionX = -entity.motionX;
                                entity.motionZ = -entity.motionZ;
                                if (entity.ridingEntity != null) {
                                    entity.ridingEntity.motionX
                                        = -entity.ridingEntity.motionX;
                                    entity.ridingEntity.motionZ
                                        = -entity.ridingEntity.motionZ;
                                }
                            }

                            if (diff == 0
                                && (this.orientation == 1 || this.orientation == 0)) {
                                entity.motionY = -entity.motionY;
                                if (entity.ridingEntity != null) {
                                    entity.ridingEntity.motionY
                                        = -entity.ridingEntity.motionY;
                                }
                            }

                            UtilsFX.poof(
                                super.worldObj,
                                (float) entity.posX - 0.5F,
                                (float) entity.posY - 0.5F,
                                (float) entity.posZ - 0.5F
                            );
                            super.worldObj.playSoundEffect(
                                entity.posX,
                                entity.posY,
                                entity.posZ,
                                "mob.endermen.portal",
                                1.0F,
                                1.0F
                            );
                            int xm = 0;
                            int zm = 0;
                            int ym = 0;
                            switch (target.orientation) {
                                case 0:
                                    ym = -1;
                                    break;
                                case 1:
                                    ym = 1;
                                    break;
                                case 2:
                                    zm = -1;
                                    break;
                                case 3:
                                    zm = 1;
                                    break;
                                case 4:
                                    xm = -1;
                                    break;
                                case 5:
                                    xm = 1;
                            }

                            if (target.orientation > 1
                                && super.worldObj.isAirBlock(
                                    target.xCoord + xm,
                                    target.yCoord + ym - 1,
                                    target.zCoord + zm
                                )) {
                                --ym;
                            }

                            entity.setLocationAndAngles(
                                (double) target.xCoord + 0.5 + (double) xm,
                                (double) target.yCoord + 0.5 + (double) ym,
                                (double) target.zCoord + 0.5 + (double) zm,
                                tYaw,
                                entity.rotationPitch
                            );
                            if (entity.ridingEntity != null) {
                                entity.ridingEntity.setLocationAndAngles(
                                    (double) target.xCoord + 0.5 + (double) xm,
                                    (double) target.yCoord + 0.5 + (double) ym,
                                    (double) target.zCoord + 0.5 + (double) zm,
                                    tYaw,
                                    entity.ridingEntity.rotationPitch
                                );
                            }

                            UtilsFX.poof(
                                super.worldObj,
                                (float) entity.posX - 0.5F,
                                (float) entity.posY - 0.5F,
                                (float) entity.posZ - 0.5F
                            );
                            super.worldObj.playSoundEffect(
                                entity.posX,
                                entity.posY,
                                entity.posZ,
                                "mob.endermen.portal",
                                1.0F,
                                1.0F
                            );

                            // TODO: use specific aspect for flux
                            int thisAura = AuraManager.getClosestAuraWithinRange(
                                this.worldObj, this.xCoord, this.yCoord, this.zCoord, 512
                            );

                            if (thisAura >= 0) {
                                AuraManager.addRandomFlux(
                                    this.worldObj,
                                    AuraManager.getNode(thisAura),
                                    (entity instanceof EntityItem) ? 1 : 4
                                );
                            }

                            int otherAura = AuraManager.getClosestAuraWithinRange(
                                target.worldObj,
                                target.xCoord,
                                target.yCoord,
                                target.zCoord,
                                512
                            );

                            if (otherAura >= 0) {
                                AuraManager.addRandomFlux(
                                    this.worldObj,
                                    AuraManager.getNode(otherAura),
                                    (entity instanceof EntityItem) ? 1 : 4
                                );
                            }

                            if (entity instanceof EntityPlayer) {
                                ((EntityPlayerMP) entity)
                                    .playerNetServerHandler.sendPacket(
                                        new S08PacketPlayerPosLook(
                                            entity.posX,
                                            entity.posY + 1.6,
                                            entity.posZ,
                                            entity.rotationYaw,
                                            entity.rotationPitch,
                                            false
                                        )
                                    );
                            } else {
                                System.out.println(entity.posX + " " + entity.posY + " " + entity.posZ);
                                    Packet pkt1
                                    = new S18PacketEntityTeleport(
                                        entity.getEntityId(),
                                        MathHelper.floor_double(entity.posX * 32.0D),
                                        MathHelper.floor_double(entity.posY * 32.0D),
                                        MathHelper.floor_double(entity.posZ * 32.0D),
                                        (byte
                                        ) ((int) (entity.rotationYaw * 256.0F / 360.0F)),
                                        (byte
                                        ) ((int) (entity.rotationPitch * 256.0F / 360.0F))
                                    );
                                Packet pkt2 = new S12PacketEntityVelocity(entity);

                                for (EntityPlayerMP pl : (List<EntityPlayerMP>) this
                                                             .worldObj.playerEntities) {
                                    pl.playerNetServerHandler.sendPacket(pkt1);
                                    pl.playerNetServerHandler.sendPacket(pkt2);
                                }
                            }

                            this.worked = true;
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private void beam(
        int range,
        int damage,
        boolean push,
        boolean homing,
        boolean motes,
        boolean core,
        float speed1,
        float speed2
    ) {
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        for (int a = 0; a < list.size(); ++a) {
            Entity entity = (Entity) list.get(a);
            if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)
                && !(entity instanceof EntityTameable)) {
                //FXBeam ef = new FXBeam(
                //    super.worldObj,
                //    (double) this.sealX(false),
                //    (double) this.sealY(false),
                //    (double) this.sealZ(false),
                //    entity,
                //    damage
                //);
                //ef.drawCore = core;
                //ef.drawMotes = motes;
                //ef.push = push;
                //ef.speed = speed2;
                //ef.moteSpeed = speed1;
                //ef.homing = homing;
                //ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
                //if (this.soundDelay <= 0) {
                //    super.worldObj.playSoundEffect(
                //        (double) super.xCoord + 0.5,
                //        (double) super.yCoord + 0.5,
                //        (double) super.zCoord + 0.5,
                //        "thaummach:beamloop",
                //        0.6F,
                //        1.0F
                //    );
                //    this.soundDelay = 5;
                //}

                //this.worked = true;
                //break;
            }
        }
    }

    private void
    heal(int range, boolean mobs, boolean animals, boolean player, boolean buff) {
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        for (int a = list.size() - 1; a >= 0; --a) {
            Entity entity = (Entity) list.get(a);
            if (entity instanceof EntityLivingBase
                && (animals
                        && (entity instanceof EntityAnimal
                            || entity instanceof EntityVillager)
                    || mobs && (entity instanceof EntityMob || entity instanceof IMob)
                    || player
                        && (entity instanceof EntityPlayer
                            || entity instanceof EntityTameable))) {
                if (!(entity instanceof EntityPlayer)
                    && ((EntityLivingBase) entity).getActivePotionEffect(Potion.hunger)
                        != null) {
                    break;
                }

                if (((EntityLivingBase) entity).getHealth()
                    != ((EntityLivingBase) entity).getMaxHealth()) {
                    ((EntityLivingBase) entity).heal(1);
                    if (buff) {
                        ((EntityLivingBase) entity)
                            .addPotionEffect(
                                new PotionEffect(Potion.regeneration.id, 60, 1)
                            );
                    }

                    super.worldObj.playSoundAtEntity(
                        entity, "thaumcraft.heal", 1.0F, 1.0F
                    );

                    for (int qq = 0; qq < 5; ++qq) {
                        FXWisp ef = new FXWisp(
                            super.worldObj,
                            (double) this.sealX(false),
                            (double) this.sealY(false),
                            (double) this.sealZ(false),
                            entity.posX
                                + (double
                                ) ((super.worldObj.rand.nextFloat()
                                    - super.worldObj.rand.nextFloat())
                                   * 0.5F),
                            entity.boundingBox.minY + (double) (entity.height / 2.0F)
                                + (double
                                ) ((super.worldObj.rand.nextFloat()
                                    - super.worldObj.rand.nextFloat())
                                   * 0.5F),
                            entity.posZ
                                + (double
                                ) ((super.worldObj.rand.nextFloat()
                                    - super.worldObj.rand.nextFloat())
                                   * 0.5F),
                            0.2F,
                            super.worldObj.rand.nextBoolean() ? 0 : 2
                        );
                        Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                    }

                    this.worked = true;
                    if (this.runes[2] != 4) {
                        break;
                    }
                }
            }
        }
    }

    private void
    scorch(int range, int damage, boolean mobs, boolean animals, boolean pvp) {
        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        for (int a = list.size() - 1; a >= 0; --a) {
            Entity entity = (Entity) list.get(a);
            if (entity instanceof EntityLivingBase
                && (animals && entity instanceof EntityAnimal
                        && !(entity instanceof EntityTameable)
                    || mobs && (entity instanceof EntityMob || entity instanceof IMob)
                    || pvp && entity instanceof EntityPlayer)) {
                for (int q = 0; q < damage; ++q) {
                    entity.attackEntityFrom(DamageSource.onFire, damage);
                    FXScorch ef = new FXScorch(
                        super.worldObj,
                        (double) this.sealX(false),
                        (double) this.sealY(false),
                        (double) this.sealZ(false),
                        Vec3.createVectorHelper(
                            entity.posX - this.sealX(false),
                            entity.posY - this.sealY(false),
                            entity.posZ - this.sealZ(false)
                        ),
                        (float) damage * 0.5F,
                        this.runeAmount(1) > 0
                    );
                    ef.mobs = mobs;
                    ef.pvp = pvp;
                    ef.animals = animals;
                    Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                }

                if (this.soundDelay <= 0) {
                    super.worldObj.playSoundEffect(
                        (double) super.xCoord + 0.5,
                        (double) super.yCoord + 0.5,
                        (double) super.zCoord + 0.5,
                        "thaumcraft:fireloop",
                        0.33F,
                        1.0F
                    );
                    this.soundDelay = 20;
                }

                this.worked = true;
                break;
            }
        }
    }

    private boolean shock() {
        int range = 6 + this.runeAmount(1) * 2;
        boolean shocked = false;
        boolean mobs = this.runeAmount(3) == 0;
        boolean animals = this.runeAmount(0) == 0;
        boolean pvp = this.runeAmount(5) > 0;
        if (pvp) {
            mobs = false;
            animals = false;
        }

        List<Entity> list
            = this.getEntitiesSorted(super.worldObj, range, this.orientation, true);

        for (int a = 0; a < list.size(); ++a) {
            if (list.get(a) instanceof EntityLivingBase
                && this.canEntityBeSeen((Entity) list.get(a))) {
                EntityLivingBase entity = (EntityLivingBase) list.get(a);
                if (animals && entity instanceof EntityAnimal
                        && !(entity instanceof EntityTameable)
                    || mobs && (entity instanceof EntityMob || entity instanceof IMob)
                    || pvp && entity instanceof EntityPlayer) {
                    shocked = true;
                    FXLightningBolt bolt = new FXLightningBolt(
                        super.worldObj,
                        this.sealPos(false),
                        new WRVector3(entity),
                        super.worldObj.rand.nextLong()
                    );
                    bolt.defaultFractal();
                    bolt.setType(2);
                    bolt.finalizeBolt();
                    entity.attackEntityFrom(DamageSource.magic, 3);
                    entity.addPotionEffect(new PotionEffect(
                        Potion.moveSlowdown.id, 100, this.runeAmount(2) * 4
                    ));
                    this.worked = true;
                    if (this.runeAmount(4) < 2) {
                        break;
                    }
                }
            }
        }

        if (shocked) {
            super.worldObj.playSoundEffect(
                (double) super.xCoord + 0.5,
                (double) super.yCoord + 0.5,
                (double) super.zCoord + 0.5,
                "thaumcraft:shock",
                0.33F,
                1.0F
            );
        }

        return false;
    }

    private void pushEntity(
        boolean pull, boolean creatures, boolean items, int range, float strength
    ) {
        boolean pushed = false;
        List<Entity> list
            = this.getEntities(super.worldObj, (float) range, this.orientation, false);

        for (int a = 0; a < list.size(); ++a) {
            Entity entity = (Entity) list.get(a);
            if (!(entity instanceof IProjectile) && !(entity instanceof EntityXPOrb)
                && !(entity instanceof EntityPlayer)
                && !(entity instanceof EntityTameable)
                && (creatures || !(entity instanceof EntityLivingBase))
                && (items || !(entity instanceof EntityItem))) {
                double d6 = entity.posX - (double) this.sealX(false);
                double d8 = entity.posY - (double) this.sealY(false);
                double d10 = entity.posZ - (double) this.sealZ(false);
                double d11
                    = (double) MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                if (pull) {
                    entity.motionX -= d6 * (double) strength * 2.0;
                    if (this.orientation <= 1 || this.runes[2] == 4
                        || this.runes[2] == 5) {
                        entity.motionY -= d8 * (double) strength * 3.0;
                    }

                    entity.motionZ -= d10 * (double) strength * 2.0;
                } else {
                    float power = entity.onGround ? strength * 2.0F : strength;
                    entity.motionX += d6 * (double) power;
                    entity.motionZ += d10 * (double) power;
                    if (this.orientation <= 1) {
                        entity.motionY += d8 * (double) strength * 3.0;
                    }
                }

                pushed = true;
            }
        }

        if (pushed) {
            this.worked = true;
            if (this.soundDelay <= 0) {
                if (pull) {
                    super.worldObj.playSoundEffect(
                        (double) ((float) super.xCoord + 0.5F),
                        (double) ((float) super.yCoord + 0.5F),
                        (double) ((float) super.zCoord + 0.5F),
                        "thaumcraft.suck",
                        0.1F,
                        strength * 3.0F + 0.9F + super.worldObj.rand.nextFloat() * 0.1F
                    );
                    this.soundDelay = 30;
                } else {
                    super.worldObj.playSoundEffect(
                        (double) ((float) super.xCoord + 0.5F),
                        (double) ((float) super.yCoord + 0.5F),
                        (double) ((float) super.zCoord + 0.5F),
                        "thaumcraft.wind",
                        0.1F,
                        strength * 3.0F + 0.9F + super.worldObj.rand.nextFloat() * 0.1F
                    );
                    this.soundDelay = 25;
                }
            }

            float q = 0.0F;
            float w = 0.0F;
            float e = 0.0F;
            switch (this.orientation) {
                case 0:
                    w = -1.0F;
                    q = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    e = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    break;
                case 1:
                    w = 1.0F;
                    q = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    e = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    break;
                case 2:
                    e = -1.0F;
                    q = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    w = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    break;
                case 3:
                    e = 1.0F;
                    q = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    w = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    break;
                case 4:
                    q = -1.0F;
                    w = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    e = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    break;
                case 5:
                    q = 1.0F;
                    w = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
                    e = super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat();
            }

            // TODO: more effects!
            //FXWind ef = new FXWind(
            //    super.worldObj,
            //    (double) this.sealX(false),
            //    (double) this.sealY(false),
            //    (double) this.sealZ(false),
            //    (double) (this.sealX(false) + q),
            //    (double) (this.sealY(false) + w),
            //    (double) (this.sealZ(false) + e),
            //    pull
            //);
            //ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
        }
    }

    protected void attemptItemPickup(EntityItem entity) {
        ItemStack items = entity.getEntityItem();

        for (int x = -2; x <= 2; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -2; z <= 2; ++z) {
                    if ((x != 0 || y != 0 || z != 0) && super.yCoord + y >= 0) {
                        TileEntity block = super.worldObj.getTileEntity(
                            super.xCoord + x, super.yCoord + y, super.zCoord + z
                        );
                        if (block instanceof IInventory) {
                            IInventory chest = (IInventory) block;

                            for (int a = 0; a < chest.getSizeInventory(); ++a) {
                                int j;
                                FXSparkle ef1;
                                FXSparkle ef2;
                                if (chest.getStackInSlot(a) == null) {
                                    chest.setInventorySlotContents(a, items);
                                    entity.setDead();
                                    super.worldObj.playSoundAtEntity(
                                        entity,
                                        "random.pop",
                                        0.15F,
                                        2.0F + super.worldObj.rand.nextFloat() * 0.45F
                                    );

                                    for (j = 0; j < 5; ++j) {
                                        ef1 = new FXSparkle(
                                            super.worldObj,
                                            entity.posX,
                                            entity.posY,
                                            entity.posZ,
                                            entity.posX
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.5F),
                                            entity.posY
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.5F),
                                            entity.posZ
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.5F),
                                            2.0F,
                                            1,
                                            3
                                        );
                                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                            ef1
                                        );
                                        ef2 = new FXSparkle(
                                            super.worldObj,
                                            entity.posX
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.2F),
                                            entity.posY
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.2F),
                                            entity.posZ
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.2F),
                                            (double) ((float) block.xCoord + 0.5F),
                                            (double) ((float) block.yCoord + 0.5F),
                                            (double) ((float) block.zCoord + 0.5F),
                                            1.0F,
                                            1,
                                            3
                                        );
                                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                            ef2
                                        );
                                    }

                                    return;
                                }

                                if (chest.getStackInSlot(a).isItemEqual(items)
                                    && chest.getStackInSlot(a).stackSize + items.stackSize
                                        <= items.getMaxStackSize()) {
                                    items.stackSize += chest.getStackInSlot(a).stackSize;
                                    chest.setInventorySlotContents(a, items);
                                    entity.setDead();
                                    super.worldObj.playSoundAtEntity(
                                        entity,
                                        "random.pop",
                                        0.15F,
                                        2.0F + super.worldObj.rand.nextFloat() * 0.45F
                                    );

                                    for (j = 0; j < 5; ++j) {
                                        ef1 = new FXSparkle(
                                            super.worldObj,
                                            entity.posX,
                                            entity.posY,
                                            entity.posZ,
                                            entity.posX
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.5F),
                                            entity.posY
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.5F),
                                            entity.posZ
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.5F),
                                            2.0F,
                                            1,
                                            3
                                        );
                                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                            ef1
                                        );
                                        ef2 = new FXSparkle(
                                            super.worldObj,
                                            entity.posX
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.2F),
                                            entity.posY
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.2F),
                                            entity.posZ
                                                + (double
                                                ) ((super.worldObj.rand.nextFloat()
                                                    - super.worldObj.rand.nextFloat())
                                                   * 0.2F),
                                            (double) ((float) block.xCoord + 0.5F),
                                            (double) ((float) block.yCoord + 0.5F),
                                            (double) ((float) block.zCoord + 0.5F),
                                            1.0F,
                                            1,
                                            3
                                        );
                                        Minecraft.getMinecraft().effectRenderer.addEffect(
                                            ef2
                                        );
                                    }

                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private float sealX(boolean reverse) {
        float calc = (float) super.xCoord;
        switch (this.orientation) {
            case 0:
                calc += 0.5F;
                break;
            case 1:
                calc += 0.5F;
                break;
            case 2:
                calc += 0.5F;
                break;
            case 3:
                calc += 0.5F;
                break;
            case 4:
                calc += reverse ? 0.0F : 1.0F;
                break;
            case 5:
                calc += reverse ? 1.0F : 0.0F;
        }

        return calc;
    }

    private float sealY(boolean reverse) {
        float calc = (float) super.yCoord;
        switch (this.orientation) {
            case 0:
                calc += reverse ? 0.0F : 1.0F;
                break;
            case 1:
                calc += reverse ? 1.0F : 0.0F;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                calc += 0.5F;
        }

        return calc;
    }

    private float sealZ(boolean reverse) {
        float calc = (float) super.zCoord;
        switch (this.orientation) {
            case 0:
                calc += 0.5F;
                break;
            case 1:
                calc += 0.5F;
                break;
            case 2:
                calc += reverse ? 0.0F : 1.0F;
                break;
            case 3:
                calc += reverse ? 1.0F : 0.0F;
                break;
            case 4:
                calc += 0.5F;
                break;
            case 5:
                calc += 0.5F;
        }

        return calc;
    }

    private WRVector3 sealPos(boolean reverse) {
        return new WRVector3(
            this.sealX(reverse), this.sealY(reverse), this.sealZ(reverse)
        );
    }

    public int runeAmount(int type) {
        int count = 0;
        if (this.runes[1] == type) {
            ++count;
        }

        if (this.runes[2] == type) {
            ++count;
        }

        return count;
    }

    public int runeCount() {
        int count = 0;
        if (this.runes[0] != -1) {
            ++count;
        }

        if (this.runes[1] != -1) {
            ++count;
        }

        if (this.runes[2] != -1) {
            ++count;
        }

        return count;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.orientation = nbt.getShort("orientation");
        this.runes = nbt.getByteArray("runes");
        this.portalWindow = nbt.getInteger("window");
        if (nbt.hasKey("other"))
            this.otherSeal = SealData.readFromNbt(nbt.getCompoundTag("other"));
        else
            this.otherSeal = null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("orientation", this.orientation);
        nbt.setByteArray("runes", this.runes);
        nbt.setInteger("window", this.portalWindow);
        if (this.otherSeal != null)
            nbt.setTag("other", this.otherSeal.writeToNbt(new NBTTagCompound()));
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setShort("orientation", this.orientation);
        nbt.setByteArray("runes", this.runes);
        nbt.setInteger("window", this.portalWindow);
        if (this.otherSeal != null)
            nbt.setTag("other", this.otherSeal.writeToNbt(new NBTTagCompound()));
        //nbt.setFloat("pSize", this.pSize);
        nbt.setBoolean("pOpen", this.pOpen);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.orientation = nbt.getShort("orientation");
        this.runes = nbt.getByteArray("runes");
        this.portalWindow = nbt.getInteger("window");
        if (nbt.hasKey("other"))
            this.otherSeal = SealData.readFromNbt(nbt.getCompoundTag("other"));
        else
            this.otherSeal = null;
        //this.pSize = nbt.getFloat("pSize");
        this.pOpen = nbt.getBoolean("pOpen");
    }

    public List<Entity>
    getEntities(World world, float range, int orientation, boolean visibleonly) {
        double xm = 0.0;
        double xp = 0.0;
        double ym = 0.0;
        double yp = 0.0;
        double zm = 0.0;
        double zp = 0.0;
        if (orientation == 0) {
            xm = zm = (double) (-range);
            xp = zp = (double) range;
            ym = (double) (-(range * 2.0F));
        } else if (orientation == 1) {
            xm = zm = (double) (-range);
            xp = zp = (double) range;
            yp = (double) (range * 2.0F);
        } else if (orientation == 2) {
            xm = ym = (double) (-range);
            xp = yp = (double) range;
            zm = (double) (-(range * 2.0F));
        } else if (orientation == 3) {
            xm = ym = (double) (-range);
            xp = yp = (double) range;
            zp = (double) (range * 2.0F);
        } else if (orientation == 4) {
            zm = ym = (double) (-range);
            zp = yp = (double) range;
            xm = (double) (-(range * 2.0F));
        } else if (orientation == 5) {
            zm = ym = (double) (-range);
            zp = yp = (double) range;
            xp = (double) (range * 2.0F);
        }

        List<Entity> list = world.getEntitiesWithinAABB(
            Entity.class,
            AxisAlignedBB.getBoundingBox(
                (double) this.xCoord + xm,
                (double) this.yCoord + ym,
                (double) this.zCoord + zm,
                (double) this.xCoord + 1.0 + xp,
                (double) this.yCoord + 1.0 + yp,
                (double) this.zCoord + 1.0 + zp
            )
        );
        if (visibleonly) {
            for (int q = 0; q < list.size(); ++q) {
                if (!this.canEntityBeSeen((Entity) list.get(q))) {
                    list.remove(q);
                    --q;
                }
            }
        }

        return list;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!this.worldObj.isRemote)
            SEALS.remove(new SealData(this));
        else if (this.renderer != null)
            this.renderer.deinit();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.invalidate();
    }

    public boolean canEntityBeSeen(Entity entity) {
        return this.worldObj.rayTraceBlocks(
                   Vec3.createVectorHelper(
                       (double) this.xCoord + 0.5,
                       (double) this.yCoord + 0.5,
                       (double) this.zCoord + 0.5
                   ),
                   Vec3.createVectorHelper(
                       entity.posX,
                       entity.posY + (double) entity.getEyeHeight(),
                       entity.posZ
                   ),
                   true
               )
            == null;
    }

    public List<Entity>
    getEntitiesSorted(World world, int range, int orientation, boolean visibleonly) {
        List<Entity> ws
            = this.getEntities(world, (float) range, orientation, visibleonly);
        boolean didSort = false;

        do {
            didSort = false;

            for (int a = 0; a < ws.size() - 1; ++a) {
                Entity entity = (Entity) ws.get(a);
                double dist = this.getDistanceFrom(entity.posX, entity.posY, entity.posZ);
                Entity entity2 = (Entity) ws.get(a + 1);
                double dist2
                    = this.getDistanceFrom(entity2.posX, entity2.posY, entity2.posZ);
                if (dist > dist2) {
                    ws.remove(a);
                    ws.add(entity);
                    didSort = true;
                    break;
                }
            }
        } while (didSort);

        return ws;
    }
}
