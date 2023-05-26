package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.machine.IUpgradable;
import dev.tilera.auracore.api.machine.TileVisUser;
import dev.tilera.auracore.aura.AuraManager;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TileGenerator
    extends TileVisUser implements IEnergySource, IUpgradable, ITileGui {
    public float rotation = 0.0F;
    public int storedEnergy = 0;

    public int energyMax = 5000;
    private int genloop;
    private byte[] upgrades = new byte[] { -1, -1 };
    private boolean emitPower = false;

    private boolean isInit = false;

    @Override
    public GuiID getGuiID() {
        return GuiID.GENERATOR;
    }

    public void updateEntity() {
        super.updateEntity();
        if (!super.worldObj.isRemote) {
            if (!this.isInit) {
                this.isInit = true;

                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            }

            if (this.hasUpgrade((byte) 5)) {
                this.energyMax = 10000;
            }

            int emit;
            int ret;
            if (!this.gettingPower()) {
                if (this.storedEnergy < this.energyMax) {
                    float moon = (float) (2 + Math.abs(super.worldObj.getMoonPhase() - 4))
                        * 0.2F;
                    if (this.hasUpgrade((byte) 0)) {
                        moon += 0.2F;
                    }

                    float mod = this.hasUpgrade((byte) 1) ? 0.8F : 1.0F;
                    float visperunit = 6.6666666E-4F * mod;
                    float suck = visperunit
                        * Math.min(
                            75.0F * moon, (float) (this.energyMax - this.storedEnergy)
                        );
                    if (suck > 0.006666667F && this.getExactPureVis(suck)) {
                        float add = suck * 150.0F;
                        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                        this.storedEnergy += Math.round(add);
                    }
                }

                if (this.storedEnergy > this.energyMax) {
                    this.storedEnergy = this.energyMax;
                }

                emit = 2;

                // === BUILDCRAFT ===
                //int arcs;
                //FXLightningBolt bolt;
                //Position ourbp;
                //for (ret = 0; ret < 6; ++ret) {
                //    Orientations o = Orientations.values()[ret];
                //    ourbp = new Position(this);
                //    Position bp = new Position(ourbp);
                //    bp.orientation = o;
                //    bp.moveForwards(1.0);
                //    TileEntity te = super.worldObj.getBlockTileEntity(
                //        (int) bp.x, (int) bp.y, (int) bp.z
                //    );
                //    if (te != null && te instanceof IPowerReceptor) {
                //        PowerProvider pp = ((IPowerReceptor) te).getPowerProvider();
                //        if (pp != null && pp.preConditions((IPowerReceptor) te)
                //            && pp.minEnergyReceived <= this.storedEnergy / 3) {
                //            int energyUsed = Math.min(
                //                (int) Math.min(
                //                    (float) Math.min(
                //                        pp.maxEnergyReceived, this.storedEnergy / 3
                //                    ),
                //                    (float) pp.maxEnergyStored - pp.energyStored
                //                ),
                //                emit
                //            );
                //            pp.receiveEnergy((float) energyUsed, o);
                //            this.storedEnergy -= energyUsed * 3;
                //            emitpower = true;
                //            arcs = energyUsed / 2;
                //            if (!ModLoader.getMinecraftInstance()
                //                     .gameSettings.fancyGraphics
                //                || Config.lowGfx) {
                //                arcs = energyUsed / 3;
                //            }

                //            if (super.worldObj.rand.nextInt(6) < arcs && energyUsed > 0)
                //            {
                //                bolt = new LightningBolt(
                //                    super.worldObj,
                //                    (double) super.xCoord + 0.5,
                //                    (double) super.yCoord + 0.5,
                //                    (double) super.zCoord + 0.5,
                //                    (double) te.xCoord + 0.5,
                //                    (double) te.yCoord + 0.5,
                //                    (double) te.zCoord + 0.5,
                //                    super.worldObj.rand.nextLong(),
                //                    6,
                //                    9.0F
                //                );
                //                bolt.defaultFractal();
                //                bolt.setType(0);
                //                bolt.setNonLethal();
                //                bolt.finalizeBolt();
                //            }
                //            break;
                //        }
                //    }
                //}

                // TODO: IC2
                //try {
                //    if (!this.isAddedToEnergyNet) {
                //        EnergyNet.getForWorld(super.worldObj).addTileEntity(this);
                //        this.isAddedToEnergyNet = true;
                //    }

                //    emit = 15;
                //    if (this.storedEnergy >= emit) {
                //        this.storedEnergy -= emit;
                //    } else {
                //        emit = this.storedEnergy;
                //        this.storedEnergy = 0;
                //    }

                //    ret = EnergyNet.getForWorld(super.worldObj)
                //              .emitEnergyFrom(this, emit);
                //    this.storedEnergy += ret;
                //    emit -= ret;
                //    Position ourbp = new Position(this);

                //    for (int i = 0; i < 6; ++i) {
                //        Orientations o = Orientations.values()[i];
                //        Position bp = new Position(ourbp);
                //        bp.orientation = o;
                //        bp.moveForwards(1.0);
                //        TileEntity te = super.worldObj.getBlockTileEntity(
                //            (int) bp.x, (int) bp.y, (int) bp.z
                //        );
                //        if (te != null && te instanceof IEnergyAcceptor) {
                //            IEnergyAcceptor es = (IEnergyAcceptor) te;
                //            if (es.isAddedToEnergyNet()) {
                //                if (emit > 0) {
                //                    emitpower = true;
                //                }

                //                arcs = emit;
                //                if (!ModLoader.getMinecraftInstance()
                //                         .gameSettings.fancyGraphics
                //                    || Config.lowGfx) {
                //                    arcs = emit / 2;
                //                }

                //                if (super.worldObj.rand.nextInt(45) < arcs) {
                //                    bolt = new LightningBolt(
                //                        super.worldObj,
                //                        (double) super.xCoord + 0.5,
                //                        (double) super.yCoord + 0.5,
                //                        (double) super.zCoord + 0.5,
                //                        (double) te.xCoord + 0.5,
                //                        (double) te.yCoord + 0.5,
                //                        (double) te.zCoord + 0.5,
                //                        super.worldObj.rand.nextLong(),
                //                        6,
                //                        9.0F
                //                    );
                //                    bolt.defaultFractal();
                //                    bolt.setType(0);
                //                    bolt.setNonLethal();
                //                    bolt.finalizeBolt();
                //                }
                //            }
                //        }
                //    }
                //} catch (Exception var17) {}

                //  _____ ___  ____   ___     __        _______ _____
                // |_   _/ _ \|  _ \ / _ \ _  \ \      / /_   _|  ___|
                //   | || | | | | | | | | (_)  \ \ /\ / /  | | | |_
                //   | || |_| | |_| | |_| |_    \ V  V /   | | |  _|
                //   |_| \___/|____/ \___/(_)    \_/\_/    |_| |_|
                //try {
                //    Class cls = Class.forName("eloraam.core.IBluePowerConnectable");
                //    Class bpc = Class.forName("eloraam.core.BluePowerConductor");
                //    ourbp = new Position(this);

                //    for (int i = 0; i < 6; ++i) {
                //        Orientations o = Orientations.values()[i];
                //        Position bp = new Position(ourbp);
                //        bp.orientation = o;
                //        bp.moveForwards(1.0);
                //        TileEntity te = super.worldObj.getBlockTileEntity(
                //            (int) bp.x, (int) bp.y, (int) bp.z
                //        );
                //        if (te != null) {
                //            arcs = Math.min(this.storedEnergy, 8);
                //            if (arcs < 1) {
                //                break;
                //            }

                //            try {
                //                double voltage = (Double
                //                ) ((Double) bpc.getMethod("getVoltage", (Class[]) null)
                //                       .invoke(
                //                           bpc.cast(
                //                               cls.getMethod(
                //                                      "getBlueConductor", (Class[]) null
                //                               )
                //                                   .invoke(cls.cast(te), (Object[])
                //                                   null)
                //                           ),
                //                           (Object[]) null
                //                       ));
                //                if (!(voltage > 100.0)) {
                //                    bpc.getMethod("applyDirect", Double.TYPE)
                //                        .invoke(
                //                            bpc.cast(
                //                                cls.getMethod(
                //                                       "getBlueConductor", (Class[])
                //                                       null
                //                                )
                //                                    .invoke(cls.cast(te), (Object[])
                //                                    null)
                //                            ),
                //                            (double) arcs
                //                        );
                //                    this.storedEnergy -= arcs;
                //                    emitpower = true;
                //                    int arcs = 2;
                //                    if (!ModLoader.getMinecraftInstance()
                //                             .gameSettings.fancyGraphics
                //                        || Config.lowGfx) {
                //                        arcs = 1;
                //                    }

                //                    if (super.worldObj.rand.nextInt(45) < arcs) {
                //                        LightningBolt bolt = new LightningBolt(
                //                            super.worldObj,
                //                            (double) super.xCoord + 0.5,
                //                            (double) super.yCoord + 0.5,
                //                            (double) super.zCoord + 0.5,
                //                            (double) te.xCoord + 0.5,
                //                            (double) te.yCoord + 0.5,
                //                            (double) te.zCoord + 0.5,
                //                            super.worldObj.rand.nextLong(),
                //                            6,
                //                            9.0F
                //                        );
                //                        bolt.defaultFractal();
                //                        bolt.setType(0);
                //                        bolt.setNonLethal();
                //                        bolt.finalizeBolt();
                //                    }
                //                    break;
                //                }
                //            } catch (Exception var15) {}
                //        }
                //    }
                //} catch (Exception var16) {}
            }

            if (this.genloop == 0 && this.emitPower) {
                this.emitPower = false;
                super.worldObj.playSoundEffect(
                    (double) ((float) super.xCoord + 0.5F),
                    (double) ((float) super.yCoord + 0.5F),
                    (double) ((float) super.zCoord + 0.5F),
                    "thaummach:alecloop",
                    0.05F,
                    1.0F
                );

                AuraManager.addFluxToClosest(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    new AspectList().add(Aspect.ENERGY, 2)
                );
            }

            ++this.genloop;
            if (this.genloop >= 70) {
                this.genloop = 0;
            }
        } else {
            if (this.rotation == -1.0F) {
                this.rotation = (float) super.worldObj.rand.nextInt(360);
            }

            ++this.rotation;
            if (this.rotation > 360.0F) {
                this.rotation -= 360.0F;
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.storedEnergy = nbttagcompound.getShort("Energy");
        this.upgrades = nbttagcompound.getByteArray("upgrades");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("Energy", (short) this.storedEnergy);
        nbttagcompound.setByteArray("upgrades", this.upgrades);
    }

    public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
        return true;
    }

    public int getMaxEnergyOutput() {
        return 15;
    }

    public boolean getConnectable(ForgeDirection face) {
        return true;
    }

    public boolean canAcceptUpgrade(byte upgrade) {
        if (upgrade != 0 && upgrade != 1 && upgrade != 5) {
            return false;
        } else {
            return !this.hasUpgrade(upgrade);
        }
    }

    public int getUpgradeLimit() {
        return 2;
    }

    public byte[] getUpgrades() {
        return this.upgrades;
    }

    public boolean hasUpgrade(byte upgrade) {
        if (this.upgrades.length < 1) {
            return false;
        } else {
            for (int a = 0; a < this.getUpgradeLimit(); ++a) {
                if (this.upgrades[a] == upgrade) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean setUpgrade(byte upgrade) {
        for (int a = 0; a < this.getUpgradeLimit(); ++a) {
            if (this.upgrades[a] < 0 && this.canAcceptUpgrade(upgrade)) {
                this.upgrades[a] = upgrade;
                return true;
            }
        }

        return false;
    }

    public boolean clearUpgrade(int index) {
        if (this.upgrades[index] >= 0) {
            this.upgrades[index] = -1;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return true;
    }

    @Override
    public double getOfferedEnergy() {
        return Math.min(this.storedEnergy, 32);
    }

    @Override
    public void drawEnergy(double amount) {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        this.emitPower = true;
        this.storedEnergy -= amount;
    }

    @Override
    public int getSourceTier() {
        return 1;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.storedEnergy = nbt.getInteger("storedEnergy");
        this.energyMax = nbt.getInteger("energyMax");
        this.upgrades = nbt.getByteArray("upgrades");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("storedEnergy", this.storedEnergy);
        nbt.setInteger("energyMax", this.energyMax);
        nbt.setByteArray("upgrades", this.upgrades);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }
}
