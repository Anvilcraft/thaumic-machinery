package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.OutputSlot;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDuplicator extends Container {
    private TileDuplicator duplicator;

    public ContainerDuplicator(InventoryPlayer inventoryplayer, TileDuplicator tileEntity) {
        this.duplicator = tileEntity;
        this.addSlotToContainer(new Slot(tileEntity, 9, 23, 34));

        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 3; ++k) {
                this.addSlotToContainer(
                    new OutputSlot(tileEntity, k + j * 3, 90 + k * 18, 17 + j * 18)
                );
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                    new Slot(inventoryplayer, k + j * 9 + 9, 8 + k * 18, 84 + j * 18)
                );
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }
    }

    @Override
    public void updateProgressBar(int i, int j) {
        if (i == 0) {
            this.duplicator.duplicatorCopyTime = (float) j;
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 11) {
                if (!this.mergeItemStack(itemstack1, 11, 37, true)) {
                    return null;
                }
            } else if (i >= 11 && i < 37) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 11, 37, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer pl) {
        return true;
    }
}
