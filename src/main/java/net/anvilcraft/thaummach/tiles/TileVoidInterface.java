package net.anvilcraft.thaummach.tiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import dev.tilera.auracore.api.HelperLocation;
import net.anvilcraft.thaummach.RuneTileData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileVoidInterface extends TileEntity implements ISidedInventory {
    // TODO: WTF
    public static Set<RuneTileData> VOID_INTERFACES = new HashSet<>();
    public byte network = 0;
    public ArrayList<HelperLocation> links = new ArrayList<>();
    public boolean linked = false;
    public int current = 0;
    public boolean init = false;

    public int getLinkedSize() {
        return this.links.size();
    }

    @Override
    public int getSizeInventory() {
        return Math.max(72, 72 * this.links.size());
    }

    private IInventory getInventory(HelperLocation v3d) {
        TileEntity te = v3d.getFacingTile(this.worldObj);
        return te instanceof TileVoidChest ? (IInventory) te : null;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        int inventory = i / 72;
        int slot = i % 72;

        try {
            return this.getInventory(this.links.get(inventory)).getStackInSlot(slot);
        } catch (Exception var5) {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        int inventory = i / 72;
        int slot = i % 72;
        return this.getInventory(this.links.get(inventory)).getStackInSlotOnClosing(slot);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        int inventory = i / 72;
        int slot = i % 72;
        return this.getInventory(this.links.get(inventory)).decrStackSize(slot, j);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        int inventory = i / 72;
        int slot = i % 72;

        try {
            this.getInventory(this.links.get(inventory))
                .setInventorySlotContents(slot, itemstack);
        } catch (Exception var6) {}
    }

    // TODO: WTF
    //@Override
    //public ItemStack getStackInSlotVirtual(int i) {
    //    try {
    //        return this.getInventory(this.links.get(this.current)).getStackInSlot(i %
    //        72);
    //    } catch (Exception var3) {
    //        return null;
    //    }
    //}

    //@Override
    //public ItemStack decrStackSizeVirtual(int i, int j) {
    //    try {
    //        return this.getInventory(this.links.get(this.current))
    //            .decrStackSize(i % 72, j);
    //    } catch (Exception var4) {
    //        return null;
    //    }
    //}

    //@Override
    //public void setInventorySlotContentsVirtual(int i, ItemStack itemstack) {
    //    try {
    //        this.getInventory(this.links.get(this.current))
    //            .setInventorySlotContents(i % 72, itemstack);
    //    } catch (Exception var4) {}
    //}

    public void establishLinks() {
        this.current = 0;
        this.links.clear();
        ArrayList<RuneTileData> tiles = new ArrayList<>();

        for (RuneTileData rtd : VOID_INTERFACES) {
            if (rtd.rune == this.network
                && rtd.dim == this.worldObj.provider.dimensionId) {
                tiles.add(rtd);
            }
        }

        tiles.sort(
            (a, b)
                -> Double.compare(
                    this.getDistanceFrom(a.x, a.y, a.z),
                    this.getDistanceFrom(b.x, b.y, b.z)
                )
        );

        for (RuneTileData rtd : tiles) {
            TileEntity te = this.worldObj.getTileEntity(rtd.x, rtd.y - 1, rtd.z);
            if (te instanceof TileVoidChest) {
                this.links.add(new HelperLocation(te));
            }
        }

        this.linked = this.links.size() > 1;
    }

    public void invalidateLinks() {
        for (RuneTileData rtd : VOID_INTERFACES) {
            if (rtd.rune != this.network)
                continue;

            TileEntity te = this.worldObj.getTileEntity(rtd.x, rtd.y, rtd.z);
            if (!(te instanceof TileVoidInterface)) {
                VOID_INTERFACES.remove(rtd);
                // TODO: WTF WTF WTF WTF
                this.invalidateLinks();
                break;
            }

            if (te != null) {
                TileVoidInterface tvi = (TileVoidInterface) te;
                tvi.markDirty();
                tvi.clearLinks();
            }
        }
    }

    public void clearLinks() {
        this.current = 0;
        this.links.clear();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.network = nbttagcompound.getByte("network");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("network", this.network);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.init) {
            this.init = true;

            VOID_INTERFACES.add(new RuneTileData(this, this.network));
        }

        if (this.links.size() == 0) {
            this.establishLinks();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public String getInventoryName() {
        return "Void Interface";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return IntStream.range(0, this.getSizeInventory()).toArray();
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack otherStack, int side) {
        ItemStack thisStack = this.getStackInSlot(slot);
        return thisStack == null
            || (thisStack.isItemEqual(otherStack)
                && thisStack.stackSize + otherStack.stackSize
                    <= thisStack.getMaxStackSize());
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack otherStack, int side) {
        ItemStack thisStack = this.getStackInSlot(slot);

        return thisStack != null && thisStack.isItemEqual(otherStack)
            && thisStack.stackSize >= otherStack.stackSize;
    }
}
