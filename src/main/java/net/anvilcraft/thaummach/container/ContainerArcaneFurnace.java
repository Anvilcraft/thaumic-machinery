package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.OutputSlot;
import net.anvilcraft.thaummach.tiles.TileArcaneFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerArcaneFurnace extends Container {
    private TileArcaneFurnace arcaneFurnace;

    public ContainerArcaneFurnace(
        InventoryPlayer inventoryplayer, TileArcaneFurnace tileArcaneFurnace
    ) {
        this.arcaneFurnace = tileArcaneFurnace;

        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 3; ++k) {
                this.addSlotToContainer(
                    new Slot(tileArcaneFurnace, 9 + k + j * 3, 17 + k * 18, 17 + j * 18)
                );
            }
        }

        this.addSlotToContainer(new Slot(tileArcaneFurnace, 18, 80, 53));

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 3; ++k) {
                this.addSlotToContainer(new OutputSlot(
                    tileArcaneFurnace, k + j * 3, 107 + k * 18, 17 + j * 18
                ));
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

    //@Override
    //public void updateCraftingResults() {
    //    super.updateCraftingResults();

    //    for (int i = 0; i < super.inventorySlots.size(); ++i) {
    //        ICrafting icrafting = (ICrafting) super.inventorySlots.get(i);
    //        if (this.lastCookTime != this.arcaneFurnace.furnaceCookTime) {
    //            icrafting.updateCraftingInventoryInfo(
    //                this, 0, Math.round((float) this.arcaneFurnace.furnaceCookTime)
    //            );
    //        }
    //    }

    //    this.lastCookTime = Math.round((float) this.arcaneFurnace.furnaceCookTime);
    //}

    @Override
    public void updateProgressBar(int i, int j) {
        if (i == 0) {
            this.arcaneFurnace.furnaceCookTime = j;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.arcaneFurnace.isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 19) {
                if (!this.mergeItemStack(itemstack1, 19, 46, true)) {
                    return null;
                }
            } else if (i >= 19 && i < 46 && !this.mergeItemStack(itemstack1, 0, 9, false)) {
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
