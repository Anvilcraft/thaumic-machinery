package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.HelperLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TilePurifier extends TileConduit {
    public int orientation = 0;

    public TilePurifier() {
        super.pureVis = 0.0F;
        super.taintedVis = 0.0F;
        this.orientation = -1;
    }

    @Override
    public void updateEntity() {
        if (!super.worldObj.isRemote) {
            this.calculateSuction();
            if (super.taintSuction < 5) {
                this.setTaintSuction(5);
            }

            if (this.getSuction((HelperLocation) null) > 0) {
                this.equalizeWithNeighbours();
            }

            if ((double) super.taintedVis > 0.01) {
                super.taintedVis -= 0.01F;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.orientation = nbttagcompound.getShort("orientation");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("orientation", (short) this.orientation);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setShort("orientation", (short) this.orientation);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.orientation = nbt.getShort("orientation");
        this.worldObj.markBlockRangeForRenderUpdate(
            this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord
        );
    }

    @Override
    public boolean getConnectable(ForgeDirection face) {
        if (this.orientation != 1 && this.orientation != 3
            || face != ForgeDirection.EAST && face != ForgeDirection.WEST) {
            return (this.orientation == 0 || this.orientation == 2)
                && (face == ForgeDirection.SOUTH || face == ForgeDirection.NORTH);
        } else {
            return true;
        }
    }

    public boolean rotate() {
        ++this.orientation;
        if (this.orientation > 3) {
            this.orientation -= 4;
        }

        return true;
    }
}
