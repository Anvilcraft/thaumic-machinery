package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.tiles.TileBellows;

public class TileConduitPump extends TileEntity implements IConnection {
    public float pureVis = 0.0F;
    public float taintedVis = 0.0F;
    public float maxVis = 4.0F;
    float fillAmount = 1.0F;
    public int orientation = 0;

    @Override
    public void updateEntity() {
        if (!super.worldObj.isRemote) {
            if (!this.gettingPower()) {
                if (this.pureVis + this.taintedVis < this.maxVis) {
                    HelperLocation me = new HelperLocation(this, this.orientation);
                    TileEntity te = me.getConnectableTile(super.worldObj);
                    if (te != null
                        && (((IConnection) te).isVisConduit()
                            || ((IConnection) te).isVisSource())) {
                        float suckamount = Math.min(
                            this.fillAmount,
                            this.maxVis - (this.pureVis + this.taintedVis)
                        );
                        float[] yum = ((IConnection) te).subtractVis(suckamount);
                        this.pureVis += yum[0];
                        this.taintedVis += yum[1];
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.pureVis = nbttagcompound.getFloat("pureVis");
        this.taintedVis = nbttagcompound.getFloat("taintedVis");
        this.orientation = nbttagcompound.getShort("orientation");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("pureVis", this.pureVis);
        nbttagcompound.setFloat("taintedVis", this.taintedVis);
        nbttagcompound.setShort("orientation", (short) this.orientation);
    }

    @Override
    public boolean getConnectable(ForgeDirection face) {
        if (this.orientation != 4 && this.orientation != 5
            || face != ForgeDirection.EAST && face != ForgeDirection.WEST) {
            if (this.orientation != 2 && this.orientation != 3
                || face != ForgeDirection.SOUTH && face != ForgeDirection.NORTH) {
                return (this.orientation == 0 || this.orientation == 1)
                    && (face == ForgeDirection.UP || face == ForgeDirection.DOWN);
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean isVisSource() {
        return true;
    }

    @Override
    public boolean isVisConduit() {
        return false;
    }

    @Override
    public float getPureVis() {
        return this.pureVis;
    }

    @Override
    public void setPureVis(float amount) {
        this.pureVis = amount;
    }

    @Override
    public float getTaintedVis() {
        return this.taintedVis;
    }

    @Override
    public void setTaintedVis(float amount) {
        this.taintedVis = amount;
    }

    @Override
    public float getMaxVis() {
        return this.maxVis;
    }

    @Override
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

    @Override
    public int getVisSuction(HelperLocation loc) {
        return this.getSuction(loc);
    }

    @Override
    public void setVisSuction(int suction) {}

    @Override
    public int getTaintSuction(HelperLocation loc) {
        return this.getSuction(loc);
    }

    @Override
    public void setTaintSuction(int suction) {}

    @Override
    public void setSuction(int suction) {}

    @Override
    public int getSuction(HelperLocation loc) {
        if (loc == null) {
            loc = new HelperLocation(this, this.orientation);
            loc.moveForwards(1.0);
        }

        if (this.gettingPower()) {
            return 0;
        } else {
            int bellows = 0;

            TileBellows.getBellows(
                this.worldObj,
                this.xCoord,
                this.yCoord,
                this.zCoord,
                new ForgeDirection[] { ForgeDirection.NORTH,
                                       ForgeDirection.EAST,
                                       ForgeDirection.SOUTH,
                                       ForgeDirection.WEST }
            );

            HelperLocation me = new HelperLocation(this, this.orientation);
            me.moveForwards(1.0);
            if (loc.equals(me)) {
                return 20 + bellows * 10;
            } else {
                return 0;
            }
        }
    }

    // TODO: IRotatable?
    //@Override
    public boolean rotate() {
        ++this.orientation;
        if (this.orientation > 5) {
            this.orientation -= 6;
        }

        return true;
    }

    public boolean gettingPower() {
        return /*super.worldObj.isBlockGettingPowered(
                   super.xCoord, super.yCoord, super.zCoord
               )
            || */
            super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            );
    }
}
