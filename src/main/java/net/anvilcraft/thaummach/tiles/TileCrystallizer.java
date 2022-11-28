package net.anvilcraft.thaummach.tiles;

import java.util.stream.IntStream;

import dev.tilera.auracore.api.machine.IUpgradable;
import dev.tilera.auracore.api.machine.TileVisUser;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;

public class TileCrystallizer
    extends TileVisUser implements ISidedInventory, IUpgradable {
    private ItemStack[] crystalizerItemStacks = new ItemStack[10];
    public float crystalTime = 0.0F;
    public float maxTime = 30.0F;
    public float sucked = 0.0F;
    public int boost = 0;
    private byte[] upgrades = new byte[] { -1 };
    int boostDelay = 20;

    // TODO: GUIs
    //public GuiScreen getGui(EntityPlayer player) {
    //    return new GuiCrystalizer(player.inventory, this);
    //}

    @Override
    public int getSizeInventory() {
        return this.crystalizerItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.crystalizerItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.crystalizerItemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.crystalizerItemStacks[i].stackSize <= j) {
                itemstack1 = this.crystalizerItemStacks[i];
                this.crystalizerItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.crystalizerItemStacks[i].splitStack(j);
                if (this.crystalizerItemStacks[i].stackSize == 0) {
                    this.crystalizerItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.crystalizerItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.crystalizerItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1
                = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotCrystalizer");
            if (byte0 >= 0 && byte0 < this.crystalizerItemStacks.length) {
                this.crystalizerItemStacks[byte0]
                    = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.crystalTime = nbttagcompound.getFloat("Time");
        this.upgrades = nbttagcompound.getByteArray("upgrades");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("Time", this.crystalTime);
        nbttagcompound.setByteArray("upgrades", this.upgrades);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.crystalizerItemStacks.length; ++i) {
            if (this.crystalizerItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotCrystalizer", (byte) i);
                this.crystalizerItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public int getCookProgressScaled(int i) {
        return Math.round(this.crystalTime / this.maxTime * (float) i);
    }

    public int getBoostScaled() {
        return Math.round(0.1F + (float) this.boost / 2.0F) * 6;
    }

    public boolean isCooking() {
        return this.crystalTime > 0.0F;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!super.worldObj.isRemote) {
            this.maxTime = this.hasUpgrade((byte) 1) ? 25.0F : 30.0F;
            if (this.crystalTime > 0.0F
                && !super.worldObj.isBlockIndirectlyGettingPowered(
                    super.xCoord, super.yCoord, super.zCoord
                )) {
                float sa = 0.025F + 0.0025F * (float) this.boost
                    + (this.hasUpgrade((byte) 0) ? 0.025F : 0.0F);
                this.sucked = this.getAvailablePureVis(sa);
                this.crystalTime -= this.sucked;
            } else {
                this.sucked = 0.0F;
            }

            if (this.crystalTime > 0.0F
                && (this.crystalizerItemStacks[6] == null
                    || this.crystalizerItemStacks[6].getItem() != ConfigItems.itemShard
                )) {
                super.worldObj.playSoundEffect(
                    (double) super.xCoord + 0.5,
                    (double) super.yCoord + 0.5,
                    (double) super.zCoord + 0.5,
                    "random.fizz",
                    1.0F,
                    1.6F
                );
                this.crystalTime = 0.0F;
            }

            if (this.crystalTime < 0.0F && this.crystalizerItemStacks[6] != null
                && this.crystalizerItemStacks[6].getItem() == ConfigItems.itemShard) {
                // TODO: WTF
                //this.addCrystal(ThaumCraftCore.getCrystalByBiome(
                //    super.worldObj,
                //    super.xCoord,
                //    super.zCoord,
                //    this.hasUpgrade((byte) 3) ? 3 : 0
                //));
                this.crystalTime = 0.0F;
                AuraManager.addFluxToClosest(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    new AspectList().add(Aspect.CRYSTAL, 5)
                );
            }

            if (this.crystalTime == 0.0F && this.crystalizerItemStacks[6] != null
                && this.crystalizerItemStacks[6].getItem() == ConfigItems.itemShard) {
                if (this.crystalizerItemStacks[6].isItemEqual(
                        // TODO: definetely wrong meta
                        new ItemStack(ConfigItems.itemShard, 1, 6)
                    )) {
                    this.crystalTime = this.maxTime;
                } else {
                    this.crystalTime = this.maxTime * 2.0F / 3.0F;
                }
            }

            if (this.boostDelay <= 0 || this.boostDelay == 10) {
                // TODO: magic boost
                //ac = (SIAuraChunk) mod_ThaumCraft.AuraHM.get(Arrays.asList(
                //    auraX, auraZ, ThaumCraftCore.getDimension(super.worldObj)
                //));
                //if (ac != null && this.boost < 10 && ac.boost > 0) {
                //    ++this.boost;
                //    --ac.boost;
                //}
            }

            if (this.boostDelay <= 0) {
                if (this.boost > 0) {
                    --this.boost;
                }

                this.boostDelay = 20;
            } else {
                --this.boostDelay;
            }
        }
    }

    private void addCrystal(int type) {
        ItemStack itemstack = new ItemStack(ConfigItems.itemShard, 1, type);
        if (this.crystalizerItemStacks[type] == null) {
            this.crystalizerItemStacks[type] = itemstack.copy();
        } else if (this.crystalizerItemStacks[type].isItemEqual(itemstack) && this.crystalizerItemStacks[type].stackSize < itemstack.getMaxStackSize()) {
            ItemStack var10000 = this.crystalizerItemStacks[type];
            var10000.stackSize += itemstack.stackSize;
        }

        --this.crystalizerItemStacks[6].stackSize;
        if (this.crystalizerItemStacks[6].stackSize <= 0) {
            this.crystalizerItemStacks[6] = null;
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if (super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord)
            != this) {
            return false;
        } else {
            return entityplayer.getDistanceSq(
                       (double) super.xCoord + 0.5,
                       (double) super.yCoord + 0.5,
                       (double) super.zCoord + 0.5
                   )
                <= 64.0;
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public boolean getConnectable(ForgeDirection face) {
        return face != ForgeDirection.UP;
    }

    @Override
    public boolean canAcceptUpgrade(byte upgrade) {
        if (upgrade != 0 && upgrade != 1 && upgrade != 3) {
            return false;
        } else {
            return !this.hasUpgrade(upgrade);
        }
    }

    @Override
    public int getUpgradeLimit() {
        return 1;
    }

    @Override
    public byte[] getUpgrades() {
        return this.upgrades;
    }

    @Override
    public boolean hasUpgrade(byte upgrade) {
        if (this.upgrades.length < 1) {
            return false;
        } else {
            for (int a = 0; a < this.getUpgradeLimit(); ++a) {
                if (this.upgrades[a] == upgrade) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean setUpgrade(byte upgrade) {
        for (int a = 0; a < this.getUpgradeLimit(); ++a) {
            if (this.upgrades[a] < 0 && this.canAcceptUpgrade(upgrade)) {
                this.upgrades[a] = upgrade;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean clearUpgrade(int index) {
        if (this.upgrades[index] >= 0) {
            this.upgrades[index] = -1;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.crystalizerItemStacks[var1] != null) {
            ItemStack var2 = this.crystalizerItemStacks[var1];
            this.crystalizerItemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    @Override
    public String getInventoryName() {
        return "thaummach:crystallizer";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        // TODO: WTF
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side != 0 && side != 1) {
            return IntStream.rangeClosed(0, 5).toArray();
        } else {
            return new int[] { 6 };
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        // TODO: WTF
        return true;
    }
}
