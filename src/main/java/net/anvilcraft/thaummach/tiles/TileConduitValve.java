package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.HelperLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileConduitValve extends TileConduit {
    public boolean open = false;
    private boolean prevPower;

    @Override
    public void updateEntity() {
        if (!super.worldObj.isRemote) {
            this.calculateSuction();
            if (!this.open) {
                this.setSuction(0);
            }

            if (this.getSuction((HelperLocation) null) > 0) {
                this.equalizeWithNeighbours();
            }

            if (super.prevPure != super.pureVis || super.prevTaint != super.taintedVis) {
                super.worldObj.markBlockForUpdate(
                    super.xCoord, super.yCoord, super.zCoord
                );
                super.prevPure = super.pureVis;
                super.prevTaint = super.taintedVis;
            }

            if (this.gettingPower()) {
                this.prevPower = true;
                this.open = false;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                super.worldObj.notifyBlocksOfNeighborChange(
                    super.xCoord, super.yCoord, super.zCoord, this.getBlockType()
                );
            }

            if (!this.gettingPower() && this.prevPower) {
                this.open = true;
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
        this.open = nbttagcompound.getBoolean("open");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("open", this.open);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("open", this.open);
        nbt.setFloat("pureVis", this.pureVis);
        nbt.setFloat("taintedVis", this.taintedVis);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.open = nbt.getBoolean("open");
        this.pureVis = nbt.getFloat("pureVis");
        this.taintedVis = nbt.getFloat("taintedVis");

        System.out.println(this.pureVis + this.taintedVis);

        this.worldObj.markBlockRangeForRenderUpdate(
            this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord
        );
    }
}
