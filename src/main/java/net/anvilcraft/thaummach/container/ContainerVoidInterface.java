package net.anvilcraft.thaummach.container;

import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVoidInterface extends Container {
    public TileVoidInterface vinter;

    public ItemStack[] clientItems = new ItemStack[72];

    public ContainerVoidInterface(InventoryPlayer inventoryplayer, TileVoidInterface vc) {
        this.vinter = vc;

        int j;
        int k;
        for (j = 0; j < 8; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                    new SlotVoidInterface(vc, 72 + k + j * 9, 8 + k * 18, 9 + j * 18)
                );
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

        this.vinter.openInventory();
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
        this.vinter.closeInventory();
    }

    public class SlotVoidInterface extends Slot {
        public SlotVoidInterface(
            IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_
        ) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public ItemStack getStack() {
            if (ContainerVoidInterface.this.vinter.getWorldObj().isRemote)
                return ContainerVoidInterface.this.clientItems[this.slotNumber];

            return ((TileVoidInterface) this.inventory)
                .getStackInSlotForCurrentLink(this.slotNumber);
        }

        @Override
        public ItemStack decrStackSize(int i) {
            if (ContainerVoidInterface.this.vinter.getWorldObj().isRemote) {
                ItemStack stack
                    = ContainerVoidInterface.this.clientItems[this.slotNumber];
                if (stack == null)
                    return null;
                if (stack.stackSize <= i) {
                    ContainerVoidInterface.this.clientItems[this.slotNumber] = null;
                    return stack;
                }

                return stack.splitStack(i);
            }

            return ((TileVoidInterface) this.inventory)
                .decrStackSizeForCurrentLink(this.slotNumber, i);
        }

        @Override
        public void putStack(ItemStack stack) {
            if (ContainerVoidInterface.this.vinter.getWorldObj().isRemote) {
                ContainerVoidInterface.this.clientItems[this.slotNumber] = stack;
                return;
            }

            ((TileVoidInterface) this.inventory)
                .setInventorySlotContentsForCurrentLink(this.slotNumber, stack);
            this.onSlotChanged();
        }
    }
}
