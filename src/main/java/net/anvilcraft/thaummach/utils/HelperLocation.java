package net.anvilcraft.thaummach.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HelperLocation {
    public double x;
    public double y;
    public double z;
    public HelperFacing facing;

    public HelperLocation(final TileEntity tile) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.facing = HelperFacing.UNKNOWN;
    }

    public HelperLocation(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = HelperFacing.UNKNOWN;
    }

    public HelperLocation(
        final double x, final double y, final double z, final HelperFacing facing
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
    }

    public HelperLocation(final TileEntity te, final HelperFacing facing) {
        this.x = te.xCoord;
        this.y = te.yCoord;
        this.z = te.zCoord;
        this.facing = facing;
    }

    public HelperLocation(final TileEntity te, final int facing) {
        this.x = te.xCoord;
        this.y = te.yCoord;
        this.z = te.zCoord;
        switch (facing) {
            case 0: {
                this.facing = HelperFacing.NEGY;
                break;
            }
            case 1: {
                this.facing = HelperFacing.POSY;
                break;
            }
            case 2: {
                this.facing = HelperFacing.NEGZ;
                break;
            }
            case 3: {
                this.facing = HelperFacing.POSZ;
                break;
            }
            case 4: {
                this.facing = HelperFacing.NEGX;
                break;
            }
            case 5: {
                this.facing = HelperFacing.POSX;
                break;
            }
        }
    }

    public HelperLocation(final HelperLocation l) {
        this.x = l.x;
        this.y = l.y;
        this.z = l.z;
        this.facing = l.facing;
    }

    public void moveUp(final double amount) {
        switch (this.facing) {
            case POSZ:
            case NEGZ:
            case POSX:
            case NEGX: {
                this.y += amount;
                break;
            }
            default:
                break;
        }
    }

    public void moveDown(final double amount) {
        switch (this.facing) {
            case POSZ:
            case NEGZ:
            case POSX:
            case NEGX: {
                this.y -= amount;
                break;
            }
            default:
                break;
        }
    }

    public void moveRight(final double amount) {
        switch (this.facing) {
            case POSZ: {
                this.x -= amount;
                break;
            }
            case NEGZ: {
                this.x += amount;
                break;
            }
            case POSX: {
                this.z += amount;
                break;
            }
            case NEGX: {
                this.z -= amount;
                break;
            }
            default:
                break;
        }
    }

    public void moveLeft(final double amount) {
        switch (this.facing) {
            case POSZ: {
                this.x += amount;
                break;
            }
            case NEGZ: {
                this.x -= amount;
                break;
            }
            case POSX: {
                this.z -= amount;
                break;
            }
            case NEGX: {
                this.z += amount;
                break;
            }
            default:
                break;
        }
    }

    public void moveForwards(final double amount) {
        switch (this.facing) {
            case POSY: {
                this.y += amount;
                break;
            }
            case NEGY: {
                this.y -= amount;
                break;
            }
            case POSZ: {
                this.z += amount;
                break;
            }
            case NEGZ: {
                this.z -= amount;
                break;
            }
            case POSX: {
                this.x += amount;
                break;
            }
            case NEGX: {
                this.x -= amount;
                break;
            }
            default:
                break;
        }
    }

    public void moveBackwards(final double amount) {
        switch (this.facing) {
            case POSY: {
                this.y -= amount;
                break;
            }
            case NEGY: {
                this.y += amount;
                break;
            }
            case POSZ: {
                this.z -= amount;
                break;
            }
            case NEGZ: {
                this.z += amount;
                break;
            }
            case POSX: {
                this.x -= amount;
                break;
            }
            case NEGX: {
                this.x += amount;
                break;
            }
            default:
                break;
        }
    }

    // TODO: waiting on auracore update
    //public TileEntity getConnectableTile(final World w) {
    //    this.moveForwards(1.0);
    //    final TileEntity te
    //        = w.getTileEntity((int) this.x, (int) this.y, (int) this.z);
    //    if (te instanceof IConnection
    //        && ((IConnection) te).getConnectable(this.facing.turnAround())) {
    //        return te;
    //    }
    //    return null;
    //}

    public TileEntity getFacingTile(final World w) {
        this.moveForwards(1.0);
        final TileEntity te
            = w.getTileEntity((int) this.x, (int) this.y, (int) this.z);
        if (te != null) {
            return te;
        }
        return null;
    }

    //public TileEntity getConnectableTile(final IBlockAccess ibc) {
    //    this.moveForwards(1.0);
    //    final TileEntity te
    //        = ibc.getTileEntity((int) this.x, (int) this.y, (int) this.z);
    //    if (te instanceof IConnection
    //        && ((IConnection) te).getConnectable(this.facing.turnAround())) {
    //        return te;
    //    }
    //    return null;
    //}

    public boolean equals(final HelperLocation loc) {
        return this.x == loc.x && this.y == loc.y && this.z == loc.z;
    }
}
