package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.OutputSlot;
import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCrystallizer extends Container {
    private TileCrystallizer crystallizer;
    private int lastCookTime = 0;

    public ContainerCrystallizer(
        InventoryPlayer inventoryplayer, TileCrystallizer tileCrystallizer
    ) {
        this.crystallizer = tileCrystallizer;
        this.addSlotToContainer(new Slot(tileCrystallizer, 6, 80, 70));

        // Air
        this.addSlotToContainer(new OutputSlot(tileCrystallizer, 0, 131, 41));

        // Fire
        this.addSlotToContainer(new OutputSlot(tileCrystallizer, 1, 131, 100));

        // Water
        this.addSlotToContainer(new OutputSlot(tileCrystallizer, 2, 30, 41));

        // Earth
        this.addSlotToContainer(new OutputSlot(tileCrystallizer, 3, 30, 100));

        // Magic
        this.addSlotToContainer(new OutputSlot(tileCrystallizer, 4, 80, 12));

        // Taint
        this.addSlotToContainer(new OutputSlot(tileCrystallizer, 5, 80, 129));

        int j;
        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                    new Slot(inventoryplayer, k + j * 9 + 9, 8 + k * 18, 158 + j * 18)
                );
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 216));
        }
    }

    // TODO: WTF
    //@Override
    //public void updateCraftingResults() {
    //    super.updateCraftingResults();

    //    for (int i = 0; i < super.inventorySlots.size(); ++i) {
    //        ICrafting icrafting = (ICrafting) super.inventorySlots.get(i);
    //        if ((float) this.lastCookTime != this.crystallizer.crystalTime) {
    //            icrafting.updateCraftingInventoryInfo(
    //                this, 0, Math.round(this.crystallizer.crystalTime)
    //            );
    //        }
    //    }

    //    this.lastCookTime = Math.round(this.crystallizer.crystalTime);
    //}

    @Override
    public void updateProgressBar(int i, int j) {
        if (i == 0) {
            this.crystallizer.crystalTime = (float) j;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.crystallizer.canInteractWith(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 7) {
                if (!this.mergeItemStack(itemstack1, 7, 34, true)) {
                    return null;
                }
            } else if (i >= 7 && i <= 34) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return null;
                }
            } else if (i > 34 && i <= 43) {
                if (!this.mergeItemStack(itemstack1, 7, 34, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 7, 43, false)) {
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
