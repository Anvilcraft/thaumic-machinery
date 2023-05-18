package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.SlotInventory;
import net.anvilcraft.thaummach.items.ItemFocus;
import net.anvilcraft.thaummach.items.ItemSingularity;
import net.anvilcraft.thaummach.tiles.TileBore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBore extends Container {
    private TileBore arcaneBore;

    public ContainerBore(InventoryPlayer inventoryplayer, TileBore tileBore) {
        this.arcaneBore = tileBore;
        this.addSlotToContainer(new SlotInventory(tileBore, 0, 65, 17));
        this.addSlotToContainer(new SlotInventory(tileBore, 1, 65, 55));

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
        return this.arcaneBore.isUseableByPlayer(entityplayer);
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
            } else if (i >= 2 && i < 38) {
                if (itemstack.getItem() instanceof ItemFocus) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        return null;
                } else if (itemstack.getItem() instanceof ItemSingularity) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                        return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            // TODO: WTF
            //slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
