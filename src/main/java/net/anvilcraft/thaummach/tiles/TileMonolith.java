package net.anvilcraft.thaummach.tiles;

import net.minecraft.tileentity.TileEntity;

public class TileMonolith extends TileEntity {
    int soundDelay = 0;

    public void updateEntity() {
        super.updateEntity();
        if (this.soundDelay > 0) {
            --this.soundDelay;
        }

        if (this.soundDelay == 0
            /* && super.worldObj.getBlockId(super.xCoord, super.yCoord - 1, super.zCoord)
                != mod_ThaumCraft.blockHidden.blockID */) {
            super.worldObj.playSoundEffect(
                (double) ((float) super.xCoord + 0.5F),
                (double) ((float) super.yCoord + 0.5F),
                (double) ((float) super.zCoord + 0.5F),
                "thaummach:monolith",
                0.4F,
                1.0F
            );
            this.soundDelay = 450 + super.worldObj.rand.nextInt(150);
        }
    }
}
