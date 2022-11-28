package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.anvilcraft.thaummach.AuraUtils;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.tiles.TileBellows;

public class TileConduitTank extends TileEntity implements IConnection {
    public float pureVis = 0.0F;
    public float taintedVis = 0.0F;
    float fillAmount = 1.0F;
    int wait;
    public float displayPure;
    public float displayTaint;
    public float prevdisplayPure;
    public float prevdisplayTaint;
    public int visSuction = 10;
    public int taintSuction = 10;

    public void updateEntity() {
        if (!super.worldObj.isRemote) {
            --this.wait;
            if (this.wait <= 0) {
                if (this.prevdisplayPure != this.displayPure
                    || this.prevdisplayTaint != this.displayTaint) {
                    super.worldObj.markBlockForUpdate(
                        super.xCoord, super.yCoord, super.zCoord
                    );
                    this.prevdisplayPure = this.displayPure;
                    this.prevdisplayTaint = this.displayTaint;
                }

                this.wait = 10;
                this.calculateSuction();
                int breakchance = 999;
                if (this.getBlockMetadata() != 3) {
                    breakchance = 3333;
                }

                if (this.taintedVis > this.getMaxVis() * 0.9F) {
                    if (this.getBlockMetadata() == 3
                        && super.worldObj.rand.nextInt(breakchance) == 123) {
                        AuraUtils.taintExplosion(
                            super.worldObj, super.xCoord, super.yCoord, super.zCoord
                        );
                        super.worldObj.setBlock(
                            super.xCoord, super.yCoord, super.zCoord, Blocks.air, 0, 3
                        );
                    } else if (super.worldObj.rand.nextInt(breakchance / 8) == 42) {
                        super.worldObj.playSoundEffect(
                            (double) ((float) super.xCoord + 0.5F),
                            (double) ((float) super.yCoord + 0.5F),
                            (double) ((float) super.zCoord + 0.5F),
                            "thaumcraft.creaking",
                            0.75F,
                            1.0F
                        );
                    }
                }
            }

            this.equalizeWithNeighbours();
            this.displayTaint = Math.max(
                this.displayTaint,
                MathHelper.clamp_float(this.taintedVis, 0.0F, this.getMaxVis())
            );
            this.displayPure = Math.max(
                this.displayPure,
                MathHelper.clamp_float(this.pureVis, 0.0F, this.getMaxVis())
            );
            if (this.displayTaint + this.displayPure < 0.1F) {
                this.displayTaint = 0.0F;
                this.displayPure = 0.0F;
            }
        }
    }

    public void calculateSuction() {
        this.setSuction(10);

        int bellows = TileBellows.getBellows(
            this.worldObj,
            this.xCoord,
            this.yCoord,
            this.zCoord,
            new ForgeDirection[] { ForgeDirection.NORTH,
                                   ForgeDirection.SOUTH,
                                   ForgeDirection.WEST,
                                   ForgeDirection.EAST }
        );
        if (bellows > 0)
            this.setSuction(this.getSuction((HelperLocation) null) + (10 * bellows));
    }

    protected void equalizeWithNeighbours() {
        float stackpureVis = this.pureVis;
        float stacktaintedVis = this.taintedVis;
        float stackmaxVis = this.getMaxVis();

        TileEntity ts;
        int count;
        for (count = 1; (ts = super.worldObj.getTileEntity(
                             super.xCoord, super.yCoord + count, super.zCoord
                         )) instanceof TileConduitTank;
             ++count) {
            stackpureVis += ((TileConduitTank) ts).pureVis;
            stacktaintedVis += ((TileConduitTank) ts).taintedVis;
            stackmaxVis += ((TileConduitTank) ts).getMaxVis();
        }

        for (int dir = 0; dir < 6; ++dir) {
            HelperLocation loc = new HelperLocation(this);
            switch (dir) {
                case 0:
                    loc.facing = ForgeDirection.UP;
                    break;
                case 1:
                    loc.facing = ForgeDirection.DOWN;
                    break;
                case 2:
                    loc.facing = ForgeDirection.SOUTH;
                    break;
                case 3:
                    loc.facing = ForgeDirection.NORTH;
                    break;
                case 4:
                    loc.facing = ForgeDirection.EAST;
                    break;
                case 5:
                    loc.facing = ForgeDirection.WEST;
            }

            if (this.getConnectable(loc.facing)) {
                TileEntity te = loc.getConnectableTile(super.worldObj);
                if (te != null && te instanceof IConnection) {
                    IConnection ent = (IConnection) te;
                    if (!(te instanceof TileConduitTank)
                        && stackpureVis + stacktaintedVis < stackmaxVis
                        && (this.getVisSuction((HelperLocation) null)
                                > ent.getVisSuction(new HelperLocation(this))
                            || this.getTaintSuction((HelperLocation) null)
                                > ent.getTaintSuction(new HelperLocation(this)))) {
                        float[] results = ent.subtractVis(Math.min(
                            this.fillAmount,
                            stackmaxVis - (stackpureVis + stacktaintedVis)
                        ));
                        if (this.getVisSuction((HelperLocation) null)
                            > ent.getVisSuction(new HelperLocation(this))) {
                            stackpureVis += results[0];
                        } else {
                            ent.setPureVis(results[0] + ent.getPureVis());
                        }

                        if (this.getTaintSuction((HelperLocation) null)
                            > ent.getTaintSuction(new HelperLocation(this))) {
                            stacktaintedVis += results[1];
                        } else {
                            ent.setTaintedVis(results[1] + ent.getTaintedVis());
                        }
                    }
                }
            }
        }

        float total = stackpureVis + stacktaintedVis;
        if ((float) Math.round(total) >= stackmaxVis) {
            this.setSuction(0);
        }

        float pratio = stackpureVis / total;
        float tratio = stacktaintedVis / total;
        count = 0;

        for (boolean clearrest = false;
             (ts = super.worldObj.getTileEntity(
                  super.xCoord, super.yCoord + count, super.zCoord
              )) instanceof TileConduitTank;
             ++count) {
            if (clearrest) {
                ((TileConduitTank) ts).pureVis = 0.0F;
                ((TileConduitTank) ts).taintedVis = 0.0F;
            } else if (total <= ((TileConduitTank) ts).getMaxVis()) {
                ((TileConduitTank) ts).pureVis = stackpureVis;
                ((TileConduitTank) ts).taintedVis = stacktaintedVis;
                clearrest = true;
            } else {
                ((TileConduitTank) ts).pureVis
                    = ((TileConduitTank) ts).getMaxVis() * pratio;
                ((TileConduitTank) ts).taintedVis
                    = ((TileConduitTank) ts).getMaxVis() * tratio;
                stackpureVis -= ((TileConduitTank) ts).pureVis;
                stacktaintedVis -= ((TileConduitTank) ts).taintedVis;
            }

            total = stackpureVis + stacktaintedVis;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.pureVis = nbttagcompound.getFloat("pureVis");
        this.taintedVis = nbttagcompound.getFloat("taintedVis");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("pureVis", this.pureVis);
        nbttagcompound.setFloat("taintedVis", this.taintedVis);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("pureVis", this.pureVis);
        nbt.setFloat("taintedVis", this.taintedVis);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.pureVis = nbt.getFloat("pureVis");
        this.taintedVis = nbt.getFloat("taintedVis");

        this.worldObj.markBlockRangeForRenderUpdate(
            this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord
        );
    }

    public boolean getConnectable(ForgeDirection face) {
        return true;
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
        return this.getBlockMetadata() != 3 ? 1000.0F : 500.0F;
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
        return this.visSuction;
    }

    public void setVisSuction(int suction) {
        this.visSuction = suction;
    }

    public int getTaintSuction(HelperLocation loc) {
        return this.taintSuction;
    }

    public void setTaintSuction(int suction) {
        this.taintSuction = suction;
    }

    public void setSuction(int suction) {
        this.visSuction = suction;
        this.taintSuction = suction;
    }

    public int getSuction(HelperLocation loc) {
        return Math.max(this.visSuction, this.taintSuction);
    }
}
