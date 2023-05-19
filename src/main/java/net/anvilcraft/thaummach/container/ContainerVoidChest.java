package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.tiles.TileVoidChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVoidChest extends Container {
    private TileVoidChest vchest;

    public ContainerVoidChest(InventoryPlayer inventoryplayer, TileVoidChest vc) {
        this.vchest = vc;

        vc.openInventory();

        int j;
        int k;
        for (j = 0; j < 8; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(vc, k + j * 9, 8 + k * 18, 9 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                    new Slot(inventoryplayer, k + j * 9 + 9, 8 + k * 18, 158 + j * 18)
                );
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 216));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 72) {
                if (!this.mergeItemStack(itemstack1, 72, 108, true)) {
                    return null;
                }
            } else if (i >= 72 && i <= 108) {
                if (!this.mergeItemStack(itemstack1, 0, 72, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 72, 108, false)) {
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
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.vchest.closeInventory();
    }
}
