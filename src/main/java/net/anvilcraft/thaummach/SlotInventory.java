package net.anvilcraft.thaummach;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInventory extends Slot {
    public SlotInventory(IInventory inv, int idx, int x, int y) {
        super(inv, idx, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.inventory.isItemValidForSlot(this.slotNumber, stack);
    }
}
