package net.anvilcraft.thaummach.utils;

public enum HelperFacing {
    NEGY,
    POSY,
    NEGZ,
    POSZ,
    NEGX,
    POSX,
    UNKNOWN;

    public HelperFacing turnAround() {
        switch (this) {
            case POSY: {
                return HelperFacing.NEGY;
            }

            case NEGY: {
                return HelperFacing.POSY;
            }
            case POSZ: {
                return HelperFacing.NEGZ;
            }
            case NEGZ: {
                return HelperFacing.POSZ;
            }
            case POSX: {
                return HelperFacing.NEGX;
            }
            case NEGX: {
                return HelperFacing.POSX;
            }
            default: {
                return HelperFacing.UNKNOWN;
            }
        }
    }

    public HelperFacing turnLeft() {
        switch (this) {
            case POSY: {
                return HelperFacing.POSY;
            }
            case NEGY: {
                return HelperFacing.NEGY;
            }
            case POSZ: {
                return HelperFacing.POSX;
            }
            case NEGZ: {
                return HelperFacing.NEGX;
            }
            case POSX: {
                return HelperFacing.NEGZ;
            }
            case NEGX: {
                return HelperFacing.POSZ;
            }
            default: {
                return HelperFacing.UNKNOWN;
            }
        }
    }

    public HelperFacing turnRight() {
        switch (this) {
            case POSY: {
                return HelperFacing.POSY;
            }
            case NEGY: {
                return HelperFacing.NEGY;
            }
            case POSZ: {
                return HelperFacing.NEGX;
            }
            case NEGZ: {
                return HelperFacing.POSX;
            }
            case POSX: {
                return HelperFacing.POSZ;
            }
            case NEGX: {
                return HelperFacing.NEGZ;
            }
            default: {
                return HelperFacing.UNKNOWN;
            }
        }
    }
}
