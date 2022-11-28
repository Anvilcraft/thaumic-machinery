package net.anvilcraft.thaummach.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileConduitValveAdvanced extends TileConduit {
    public int setting = 0;
    public int prevsetting = 0;
    private boolean prevPower;

    @Override
    public void updateEntity() {
        if (!super.worldObj.isRemote) {
            if (super.prevPure != super.pureVis
                || super.prevTaint != super.taintedVis) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                super.prevPure = super.pureVis;
                super.prevTaint = super.taintedVis;
            }

            this.calculateSuction();
            if (this.setting == 0) {
                this.setSuction(0);
            }

            if (this.setting == 1) {
                this.setTaintSuction(0);
            }

            if (this.setting == 2) {
                this.setVisSuction(0);
            }

            if (this.getSuction(null) > 0) {
                this.equalizeWithNeighbours();
            }

            if (this.gettingPower()) {
                if (!this.prevPower) {
                    this.prevsetting = this.setting;
                }

                this.prevPower = true;
                this.setting = 0;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                super.worldObj.notifyBlocksOfNeighborChange(
                    super.xCoord, super.yCoord, super.zCoord, this.getBlockType()
                );
            }

            if (!this.gettingPower() && this.prevPower) {
                this.setting = this.prevsetting;
                this.prevPower = false;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                super.worldObj.notifyBlocksOfNeighborChange(
                    super.xCoord, super.yCoord, super.zCoord, this.getBlockType()
                );
            }
        }
    }

    protected boolean gettingPower() {
        return super.worldObj.isBlockIndirectlyGettingPowered(
                   super.xCoord, super.yCoord, super.zCoord
               )
            || super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord + 1, super.zCoord
            );
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.setting = nbttagcompound.getInteger("setting");
        this.prevsetting = nbttagcompound.getInteger("prevsetting");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("setting", this.setting);
        nbttagcompound.setInteger("prevsetting", this.prevsetting);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("setting", this.setting);
        nbt.setInteger("prevsetting", this.prevsetting);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.setting = nbt.getInteger("setting");
        this.prevsetting = nbt.getInteger("prevsetting");
    }
}
