package net.anvilcraft.thaummach.tiles;

import java.util.stream.IntStream;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import dev.tilera.auracore.api.machine.IUpgradable;
import dev.tilera.auracore.api.machine.TileVisUser;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.anvilcraft.thaummach.RecipesCrucible;
import net.anvilcraft.thaummach.ThaumicMachinery;
import net.anvilcraft.thaummach.packets.PacketDuplicatorPress;
import net.anvilcraft.thaummach.particles.FXWisp;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

// TODO: make rotatable using AC IWandable
public class TileDuplicator extends TileVisUser implements ISidedInventory, IUpgradable, ITileGui {
    private ItemStack[] duplicatorItemStacks = new ItemStack[10];
    public float duplicatorCopyTime = 0.0F;
    public float currentItemCopyCost;
    public float sucked = 0.0F;
    public boolean repeat = false;
    public int boost = 0;
    private byte[] upgrades = new byte[] { -1 };
    public int orientation = -1;
    public float press = 0.0F;
    public boolean doPress = false;
    private int pressDelay = 0;
    int boostDelay = 20;

    @Override
    public GuiID getGuiID() {
        return GuiID.DUPLICATOR;
    }

    @Override
    public int getSizeInventory() {
        return this.duplicatorItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.duplicatorItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.duplicatorItemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.duplicatorItemStacks[i].stackSize <= j) {
                itemstack1 = this.duplicatorItemStacks[i];
                this.duplicatorItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.duplicatorItemStacks[i].splitStack(j);
                if (this.duplicatorItemStacks[i].stackSize == 0) {
                    this.duplicatorItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.duplicatorItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return "Thaumic Duplicator";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.duplicatorItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotDuplicator");
            if (byte0 >= 0 && byte0 < this.duplicatorItemStacks.length) {
                this.duplicatorItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.duplicatorCopyTime = nbttagcompound.getFloat("CopyTime");
        this.currentItemCopyCost = nbttagcompound.getFloat("CopyCost");
        this.repeat = nbttagcompound.getBoolean("Repeat");
        this.upgrades = nbttagcompound.getByteArray("upgrades");
        this.orientation = nbttagcompound.getShort("orientation");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("CopyTime", this.duplicatorCopyTime);
        nbttagcompound.setFloat("CopyCost", this.currentItemCopyCost);
        nbttagcompound.setBoolean("Repeat", this.repeat);
        nbttagcompound.setByteArray("upgrades", this.upgrades);
        nbttagcompound.setShort("orientation", (short) this.orientation);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.duplicatorItemStacks.length; ++i) {
            if (this.duplicatorItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotDuplicator", (byte) i);
                this.duplicatorItemStacks[i].writeToNBT(nbttagcompound1);
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
        return Math.round(this.duplicatorCopyTime / this.currentItemCopyCost * (float) i);
    }

    public int getBoostScaled() {
        return Math.round(0.1F + (float) this.boost / 2.0F) * 6;
    }

    public boolean isCooking() {
        return this.duplicatorCopyTime > 0.0F;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!super.worldObj.isRemote) {
            boolean flag1 = false;
            boolean flag = this.duplicatorCopyTime > 0.0F;
            if (this.canProcess() && this.currentItemCopyCost > 0.0F
                && !super.worldObj.isBlockIndirectlyGettingPowered(
                    super.xCoord, super.yCoord, super.zCoord
                )) {
                float sa
                    = 0.5F + 0.05F * (float) this.boost + (this.hasUpgrade((byte) 0) ? 0.5F : 0.0F);
                this.sucked = this.getAvailablePureVis(sa);
                this.duplicatorCopyTime += this.sucked;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            } else {
                this.sucked = 0.0F;
            }
            if (this.duplicatorCopyTime >= this.currentItemCopyCost && flag) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                AuraManager.addFluxToClosest(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    new AspectList().add(
                        Aspect.TAINT, Math.max(1, (int) (this.currentItemCopyCost / 20.0f))
                    )
                );

                this.addProcessedItem();
                ThaumicMachinery.channel.sendToAllAround(
                    new PacketDuplicatorPress(this.xCoord, this.yCoord, this.zCoord),
                    new TargetPoint(
                        this.worldObj.provider.dimensionId,
                        this.xCoord,
                        this.yCoord,
                        this.zCoord,
                        64
                    )
                );
                this.duplicatorCopyTime = 0.0F;
                this.currentItemCopyCost = 0.0F;
                this.markDirty();
            }

            if (this.currentItemCopyCost != 0.0F
                && this.currentItemCopyCost != (float) this.getCopyCost()) {
                this.duplicatorCopyTime = 0.0F;
                this.currentItemCopyCost = 0.0F;
                super.worldObj.playSoundEffect(
                    (double) super.xCoord + 0.5,
                    (double) super.yCoord + 0.5,
                    (double) super.zCoord + 0.5,
                    "random.fizz",
                    1.0F,
                    1.6F
                );
            }

            if (this.duplicatorCopyTime == 0.0F && this.canProcess()) {
                this.currentItemCopyCost = (float) this.getCopyCost();
                this.markDirty();
            }

            if (flag != this.duplicatorCopyTime > 0.0F) {
                flag1 = true;
            }

            if (flag1) {
                this.markDirty();
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

            if (this.boostDelay <= 0) {
                if (this.boost > 0) {
                    --this.boost;
                }

                this.boostDelay = 20;
            } else {
                --this.boostDelay;
            }
        } else {
            if (this.press > 0.0F && !this.doPress) {
                this.press *= 0.97F;
            }

            if (this.press < 0.125F && !this.doPress) {
                this.press = 0.125F;
            }

            if (this.press < 0.1875F && this.doPress) {
                this.press *= 1.1F;
            }

            if (this.press >= 0.1875F && this.doPress && this.pressDelay <= 0) {
                this.pressDelay = 12;
                this.press = 0.1875F;
            }

            if (this.pressDelay > 0) {
                --this.pressDelay;
            }

            if (this.pressDelay <= 0 && this.doPress && this.press >= 0.1875F) {
                this.doPress = false;
            }
        }
    }

    public int getCopyCost() {
        if (this.duplicatorItemStacks[9] != null
            && this.duplicatorItemStacks[9].getRarity() == EnumRarity.common) {
            if (this.duplicatorItemStacks[9].getItem()
                == Item.getItemFromBlock(Blocks.cobblestone)) {
                return 2;
            } else {
                float t = RecipesCrucible.smelting().getSmeltingResult(
                    this.duplicatorItemStacks[9], true, false
                );
                if (t > 0.0F) {
                    int tr = Math.round(t * (float) (this.hasUpgrade((byte) 1) ? 4 : 5));
                    return tr;
                } else {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

    private boolean canProcess() {
        if (this.duplicatorItemStacks[9] == null) {
            return false;
        } else {
            for (int j = 0; j < 9; ++j) {
                if (this.duplicatorItemStacks[j] == null) {
                    return true;
                }

                if (this.duplicatorItemStacks[j].isItemEqual(this.duplicatorItemStacks[9])) {
                    int st = this.duplicatorItemStacks[j].stackSize + 1;
                    if (!this.repeat) {
                        ++st;
                    }

                    if (st <= this.getInventoryStackLimit()
                        && st <= this.duplicatorItemStacks[j].getMaxStackSize()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private void addProcessedItem() {
        if (this.canProcess()) {
            ItemStack itemstack = new ItemStack(
                this.duplicatorItemStacks[9].getItem(),
                1,
                this.duplicatorItemStacks[9].getItemDamage()
            );
            int repeats = 1;
            if (!this.repeat) {
                repeats = 2;
            }

            for (int q = 0; q < repeats; ++q) {
                for (int j = 0; j < 9; ++j) {
                    if (this.duplicatorItemStacks[j] == null) {
                        this.duplicatorItemStacks[j] = itemstack.copy();
                        break;
                    }

                    if (this.duplicatorItemStacks[j].isItemEqual(itemstack)
                        && this.duplicatorItemStacks[j].stackSize < itemstack.getMaxStackSize()) {
                        ItemStack var10000 = this.duplicatorItemStacks[j];
                        var10000.stackSize += itemstack.stackSize;
                        break;
                    }
                }
            }

            if (!this.repeat) {
                --this.duplicatorItemStacks[9].stackSize;
                if (this.duplicatorItemStacks[9].stackSize <= 0) {
                    this.duplicatorItemStacks[9] = null;
                }
            }
        }
    }

    //@Override
    //public int getStartInventorySide(int side) {
    //    return side != 0 && side != 1 ? 0 : 9;
    //}

    //@Override
    //public int getSizeInventorySide(int side) {
    //    return side != 0 && side != 1 ? 9 : 1;
    //}

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public boolean getConnectable(ForgeDirection face) {
        switch (face) {
            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
                return true;

            default:
                return false;
        }
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

    // TODO: IWandable
    //@Override
    //public boolean rotate() {
    //    ++this.orientation;
    //    if (this.orientation > 3) {
    //        this.orientation -= 4;
    //    }

    //    return true;
    //}

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.duplicatorItemStacks[var1] != null) {
            ItemStack var2 = this.duplicatorItemStacks[var1];
            this.duplicatorItemStacks[var1] = null;
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
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        // output slot for top/bottom
        switch (side) {
            case 0:
            case 1:
                return new int[] { 9 };

            default:
                return IntStream.range(0, 9).toArray();
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack otherStack, int side) {
        ItemStack thisStack = this.duplicatorItemStacks[slot];

        return thisStack != null && thisStack.isItemEqual(otherStack)
            && thisStack.stackSize >= otherStack.stackSize;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("duplicatorCopyTime", this.duplicatorCopyTime);
        nbt.setFloat("currentItemCopyCost", this.currentItemCopyCost);
        nbt.setFloat("sucked", this.sucked);
        nbt.setBoolean("repeat", this.repeat);
        nbt.setInteger("boost", this.boost);
        nbt.setByteArray("upgrades", this.upgrades);
        nbt.setInteger("orientation", this.orientation);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.duplicatorCopyTime = nbt.getFloat("duplicatorCopyTime");
        this.currentItemCopyCost = nbt.getFloat("currentItemCopyCost");
        this.sucked = nbt.getFloat("sucked");
        this.repeat = nbt.getBoolean("repeat");
        this.boost = nbt.getInteger("boost");
        this.upgrades = nbt.getByteArray("upgrades");
        this.orientation = nbt.getInteger("orientation");
    }
}
