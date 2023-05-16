package net.anvilcraft.thaummach.tiles;

import java.util.stream.IntStream;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileVoidChest extends TileEntity implements ISidedInventory {
    private ItemStack[] vcItemStacks = new ItemStack[72];

    // TODO: GUIs
    //public GuiScreen getGui(EntityPlayer player) {
    //    return new GuiVoidChest(player.inventory, this);
    //}

    @Override
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.vcItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.vcItemStacks[i] != null) {
            ItemStack itemstack1;
            this.markDirty();
            if (this.vcItemStacks[i].stackSize <= j) {
                itemstack1 = this.vcItemStacks[i];
                this.vcItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.vcItemStacks[i].splitStack(j);
                if (this.vcItemStacks[i].stackSize == 0) {
                    this.vcItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.vcItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Void Chest";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.vcItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotVoidChest");
            if (byte0 >= 0 && byte0 < this.vcItemStacks.length) {
                this.vcItemStacks[byte0]
                    = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.vcItemStacks.length; ++i) {
            if (this.vcItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotVoidChest", (byte) i);
                this.vcItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.vcItemStacks[var1] != null) {
            ItemStack var2 = this.vcItemStacks[var1];
            this.vcItemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    @Override
    public void closeInventory() {
        super.worldObj.playSoundEffect(
            (double) ((float) super.xCoord + 0.5F),
            (double) ((float) super.yCoord + 0.5F),
            (double) ((float) super.zCoord + 0.5F),
            "thaummach:stoneclose",
            1.0F,
            1.0F
        );
    }

    @Override
    public void openInventory() {}

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return IntStream.range(0, this.vcItemStacks.length).toArray();
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack otherStack, int side) {
        ItemStack thisStack = this.vcItemStacks[slot];
        return thisStack == null
            || (thisStack.isItemEqual(otherStack)
                && thisStack.stackSize + otherStack.stackSize
                    <= thisStack.getMaxStackSize());
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack otherStack, int side) {
        ItemStack thisStack = this.vcItemStacks[slot];

        return thisStack != null && thisStack.isItemEqual(otherStack)
            && thisStack.stackSize >= otherStack.stackSize;
    }

    @Override
    public int getSizeInventory() {
        return this.vcItemStacks.length;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }
}
