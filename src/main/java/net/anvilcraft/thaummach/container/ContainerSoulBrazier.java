package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.InventorySlot;
import net.anvilcraft.thaummach.tiles.TileSoulBrazier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSoulBrazier extends Container {
    private TileSoulBrazier brazier;

    public ContainerSoulBrazier(
        InventoryPlayer inventoryplayer, TileSoulBrazier soulbrazier
    ) {
        this.brazier = soulbrazier;
        this.addSlotToContainer(new InventorySlot(soulbrazier, 0, 80, 41));

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
        return this.brazier.isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
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
