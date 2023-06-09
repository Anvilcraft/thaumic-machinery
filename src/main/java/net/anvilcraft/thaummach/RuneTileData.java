package net.anvilcraft.thaummach;

import net.anvilcraft.thaummach.tiles.TileSeal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class RuneTileData {
    public int dim;
    public int x;
    public int y;
    public int z;
    public short orientation;
    public byte rune;

    public RuneTileData() {}

    public RuneTileData(TileEntity te, byte rune) {
        this.dim = te.getWorldObj().provider.dimensionId;
        this.x = te.xCoord;
        this.y = te.yCoord;
        this.z = te.zCoord;
        this.rune = rune;
    }

    public RuneTileData(TileSeal seal) {
        this.dim = seal.getWorldObj().provider.dimensionId;

        this.x = seal.xCoord;
        this.y = seal.yCoord;
        this.z = seal.zCoord;

        this.orientation = seal.orientation;

        this.rune = seal.runes[2];
    }

    public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
        nbt.setInteger("dim", this.dim);
        nbt.setInteger("x", this.x);
        nbt.setInteger("y", this.y);
        nbt.setInteger("z", this.z);
        nbt.setShort("orientation", this.orientation);
        nbt.setByte("rune", this.rune);

        return nbt;
    }

    public static RuneTileData readFromNbt(NBTTagCompound nbt) {
        RuneTileData self = new RuneTileData();

        self.dim = nbt.getInteger("dim");
        self.x = nbt.getInteger("x");
        self.y = nbt.getInteger("y");
        self.z = nbt.getInteger("z");
        self.orientation = nbt.getShort("orientation");
        self.rune = nbt.getByte("rune");

        return self;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dim;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        result = prime * result + orientation;
        result = prime * result + rune;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RuneTileData other = (RuneTileData) obj;
        if (dim != other.dim)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        if (orientation != other.orientation)
            return false;
        if (rune != other.rune)
            return false;
        return true;
    }
}
