package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.OutputSlot;
import net.anvilcraft.thaummach.tiles.TileRepairer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRepairer extends Container {
    public ContainerRepairer(InventoryPlayer inventoryplayer, TileRepairer tileEntity) {
        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 2; ++k) {
                this.addSlotToContainer(
                    new Slot(tileEntity, k + j * 2, 37 + k * 18, 16 + j * 18)
                );
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 2; ++k) {
                this.addSlotToContainer(
                    new OutputSlot(tileEntity, 6 + k + j * 2, 90 + k * 18, 16 + j * 18)
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
    public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 12) {
                if (!this.mergeItemStack(itemstack1, 12, 48, true)) {
                    return null;
                }
            } else if (i >= 12 && i < 48) {
                if (!this.mergeItemStack(itemstack1, 0, 6, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 12, 48, false)) {
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
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
