package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.InventorySlot;
import net.anvilcraft.thaummach.tiles.TileEnchanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnchanter extends Container {
    public TileEnchanter te;

    public ContainerEnchanter(InventoryPlayer inventoryplayer, TileEnchanter tile) {
        this.te = tile;
        this.addSlotToContainer(new InventorySlot(this.te, 0, 25, 47));

        int i1;
        for (i1 = 0; i1 < 3; ++i1) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(
                    new Slot(inventoryplayer, j1 + i1 * 9 + 9, 8 + j1 * 18, 100 + i1 * 18)
                );
            }
        }

        for (i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(inventoryplayer, i1, 8 + i1 * 18, 158));
        }
    }

    // TODO: WTF
    //@Override
    //public void updateCraftingResults() {
    //    super.updateCraftingResults();

    //    for (int i = 0; i < super.crafters.size(); ++i) {
    //        ICrafting icrafting = (ICrafting) super.crafters.get(i);
    //        icrafting.updateCraftingInventoryInfo(this, 0, this.te.enchantLevels[0]);
    //        icrafting.updateCraftingInventoryInfo(this, 1, this.te.enchantLevels[1]);
    //        icrafting.updateCraftingInventoryInfo(this, 2, this.te.enchantLevels[2]);
    //    }
    //}

    @Override
    public void updateProgressBar(int i, int j) {
        if (i >= 0 && i <= 2) {
            this.te.enchantLevels[i] = j;
        } else {
            super.updateProgressBar(i, j);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i != 0) {
                return null;
            }

            if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
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
    public boolean canInteractWith(EntityPlayer arg0) {
        return true;
    }
}
