package net.anvilcraft.thaummach;

/**
 * Implemented by Apparatus {@link net.minecraft.tileentity.TileEntity}s which have a GUI that's
 * opened on right-click.
 */
public interface ITileGui {
    /**
     * Get the GUI ID {@link net.anvilcraft.thaummach.blocks.BlockApparatus}
     * will use to determine a GUI to open on right-click.
     */
    public GuiID getGuiID();
}
