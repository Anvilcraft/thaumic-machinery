package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.InventorySlot;
import net.anvilcraft.thaummach.OutputSlot;
import net.anvilcraft.thaummach.tiles.TileCondenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCondenser extends Container {
    private TileCondenser arcaneCondenser;

    public ContainerCondenser(
        InventoryPlayer inventoryplayer, TileCondenser tileCondenser
    ) {
        this.arcaneCondenser = tileCondenser;
        this.addSlotToContainer(new InventorySlot(tileCondenser, 0, 46, 32));
        this.addSlotToContainer(new OutputSlot(tileCondenser, 1, 114, 32));

        int j;
        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
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
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.arcaneCondenser.isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 2) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return null;
                }
            } else if (this.arcaneCondenser.isItemValidForSlot(0, itemstack1) && !this.mergeItemStack(itemstack1, 0, 1, false)) {
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
}
