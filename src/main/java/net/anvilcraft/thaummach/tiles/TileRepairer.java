package net.anvilcraft.thaummach.tiles;

import java.util.stream.IntStream;

import dev.tilera.auracore.api.machine.IUpgradable;
import dev.tilera.auracore.api.machine.TileVisUser;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.IRepairable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TileRepairer extends TileVisUser implements ISidedInventory, IUpgradable, ITileGui {
    private ItemStack[] repairerItemStacks = new ItemStack[12];
    public float sucked = 0.0F;
    public float currentVis = 0.0F;
    public float maxVis = 5.0F;
    public int boost = 0;
    private byte[] upgrades = new byte[] { -1 };
    public boolean worked;
    int boostDelay = 20;
    int soundDelay = 0;

    @Override
    public GuiID getGuiID() {
        return GuiID.REPAIRER;
    }

    @Override
    public int getSizeInventory() {
        return this.repairerItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.repairerItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.repairerItemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.repairerItemStacks[i].stackSize <= j) {
                itemstack1 = this.repairerItemStacks[i];
                this.repairerItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.repairerItemStacks[i].splitStack(j);
                if (this.repairerItemStacks[i].stackSize == 0) {
                    this.repairerItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.repairerItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return "Thaumic Repairer";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.repairerItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1
                = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotDuplicator");
            if (byte0 >= 0 && byte0 < this.repairerItemStacks.length) {
                this.repairerItemStacks[byte0]
                    = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.upgrades = nbttagcompound.getByteArray("upgrades");
        this.currentVis = nbttagcompound.getFloat("currentVis");
        this.maxVis = nbttagcompound.getFloat("maxVis");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByteArray("upgrades", this.upgrades);
        nbttagcompound.setFloat("currentVis", this.currentVis);
        nbttagcompound.setFloat("maxVis", this.maxVis);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.repairerItemStacks.length; ++i) {
            if (this.repairerItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotDuplicator", (byte) i);
                this.repairerItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public int getBoostScaled() {
        return Math.round(0.1F + (float) this.boost / 2.0F) * 6;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.worked = false;
        if (!super.worldObj.isRemote) {
            boolean flag1 = false;

            for (int a = 0; a < 6; ++a) {
                boolean isVisRepair = false;

                try {
                    isVisRepair
                        = this.repairerItemStacks[a].getItem() instanceof IRepairable;
                } catch (Exception var6) {}

                if (this.repairerItemStacks[a] != null
                    && this.repairerItemStacks[a].isItemDamaged()
                    && this.repairerItemStacks[a].getItemDamage() > 0
                    && (this.repairerItemStacks[a].getItem().isRepairable() || isVisRepair
                    )) {
                    float ra = 1.0f;

                    ra *= this.hasUpgrade((byte) 1) ? 0.8F : 1.0F;
                    if (this.repairerItemStacks[a].isItemEnchanted()) {
                        ra *= 1.5F;
                    }

                    if (this.currentVis >= ra) {
                        this.worldObj.markBlockForUpdate(
                            this.xCoord, this.yCoord, this.zCoord
                        );
                        this.currentVis -= ra;
                        flag1 = true;
                        this.repairerItemStacks[a].setItemDamage(
                            this.repairerItemStacks[a].getItemDamage() - 1
                        );
                        this.worked = true;
                        if (!this.hasUpgrade((byte) -1)) {
                            // TODO: FX
                            //switch (super.worldObj.rand.nextInt(4)) {
                            //    case 0:
                            //        ThaumCraftCore.createSmallGreenFlameFX(
                            //            super.worldObj,
                            //            (float) super.xCoord + 0.25F,
                            //            (float) super.yCoord + 1.15F,
                            //            (float) super.zCoord + 0.25F
                            //        );
                            //        break;
                            //    case 1:
                            //        ThaumCraftCore.createSmallGreenFlameFX(
                            //            super.worldObj,
                            //            (float) super.xCoord + 0.25F,
                            //            (float) super.yCoord + 1.15F,
                            //            (float) super.zCoord + 0.75F
                            //        );
                            //        break;
                            //    case 2:
                            //        ThaumCraftCore.createSmallGreenFlameFX(
                            //            super.worldObj,
                            //            (float) super.xCoord + 0.75F,
                            //            (float) super.yCoord + 1.15F,
                            //            (float) super.zCoord + 0.25F
                            //        );
                            //        break;
                            //    case 3:
                            //        ThaumCraftCore.createSmallGreenFlameFX(
                            //            super.worldObj,
                            //            (float) super.xCoord + 0.75F,
                            //            (float) super.yCoord + 1.15F,
                            //            (float) super.zCoord + 0.75F
                            //        );
                            //}
                        }
                    }
                }

                if (this.repairerItemStacks[a] != null
                    && !this.repairerItemStacks[a].isItemDamaged()) {
                    this.moveRepairedItem(a);
                }
            }

            float suckLimit = 0.5F + 0.05F * (float) this.boost
                + (this.hasUpgrade((byte) 0) ? 0.5F : 0.0F);

            for (int i = 0; i < 6; ++i) {
                if (this.repairerItemStacks[i] != null) {
                    if (this.currentVis + suckLimit <= this.maxVis) {
                        this.currentVis += this.getAvailablePureVis(suckLimit);
                    }
                    break;
                }
            }

            if (flag1 && this.soundDelay <= 0) {
                this.soundDelay = 50;
                super.worldObj.playSoundEffect(
                    (double) ((float) super.xCoord + 0.5F),
                    (double) ((float) super.yCoord + 0.5F),
                    (double) ((float) super.zCoord + 0.5F),
                    "thaummach:tinkering",
                    0.5F,
                    1.0F
                );

                AuraManager.addFluxToClosest(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    new AspectList().add(Aspect.TAINT, 1)
                );
            }

            if (this.soundDelay > 0) {
                --this.soundDelay;
            }

            // TODO: magic boost
            //if (this.boostDelay <= 0 || this.boostDelay == 10) {
            //    auraX = super.xCoord >> 4;
            //    auraZ = super.zCoord >> 4;
            //    ac = (SIAuraChunk) mod_ThaumCraft.AuraHM.get(Arrays.asList(
            //        auraX, auraZ, ThaumCraftCore.getDimension(super.worldObj)
            //    ));
            //    if (ac != null && this.boost < 10 && ac.boost > 0) {
            //        ++this.boost;
            //        --ac.boost;
            //    }
            //}
            //
            //if (this.boostDelay <= 0) {
            //    if (this.boost > 0) {
            //        --this.boost;
            //    }

            //    this.boostDelay = 20;
            //} else {
            //    --this.boostDelay;
            //}
        }
    }

    private void moveRepairedItem(int stack) {
        ItemStack itemstack = new ItemStack(
            this.repairerItemStacks[stack].getItem(),
            1,
            this.repairerItemStacks[stack].getItemDamage()
        );

        for (int j = 6; j < 12; ++j) {
            if (this.repairerItemStacks[j] == null) {
                this.repairerItemStacks[j] = itemstack.copy();
                this.repairerItemStacks[j].setTagCompound(
                    this.repairerItemStacks[stack].getTagCompound()
                );
                --this.repairerItemStacks[stack].stackSize;
                if (this.repairerItemStacks[stack].stackSize <= 0) {
                    this.repairerItemStacks[stack] = null;
                }
                break;
            }
        }
    }

    //    public int getStartInventorySide(int side) {
    //        return side != 0 && side != 1 ? 6 : 0;
    //    }
    //
    //    public int getSizeInventorySide(int side) {
    //        return 6;
    //    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public boolean getConnectable(ForgeDirection face) {
        return true;
    }

    @Override
    public boolean canAcceptUpgrade(byte upgrade) {
        if (upgrade != 0 && upgrade != 1) {
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
        if (this.repairerItemStacks[var1] != null) {
            ItemStack var2 = this.repairerItemStacks[var1];
            this.repairerItemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
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
        // output for top/bottom, else input
        int offset = side == 0 || side == 1 ? 6 : 0;

        return IntStream.range(offset, 6 + offset).toArray();
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        ItemStack curStack = this.repairerItemStacks[slot];

        return curStack != null && curStack.isItemEqual(stack)
            && curStack.stackSize - stack.stackSize > 0;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("sucked", this.sucked);
        nbt.setFloat("currentVis", this.currentVis);
        nbt.setFloat("maxVis", this.maxVis);
        nbt.setInteger("boost", this.boost);
        nbt.setByteArray("upgrades", this.upgrades);
        nbt.setBoolean("worked", this.worked);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.sucked = nbt.getFloat("sucked");
        this.currentVis = nbt.getFloat("currentVis");
        this.maxVis = nbt.getFloat("maxVis");
        this.boost = nbt.getInteger("boost");
        this.upgrades = nbt.getByteArray("upgrades");
        this.worked = nbt.getBoolean("worked");
    }
}
