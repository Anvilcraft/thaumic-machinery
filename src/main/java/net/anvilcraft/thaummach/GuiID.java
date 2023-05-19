package net.anvilcraft.thaummach;

public enum GuiID {
    BORE,
    VOID_CHEST,
    VOID_INTERFACE;

    public static GuiID get(int id) {
        if (id >= 0 && id < GuiID.values().length) {
            return GuiID.values()[id];
        }

        return null;
    }
}
