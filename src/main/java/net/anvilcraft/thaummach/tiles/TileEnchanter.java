package net.anvilcraft.thaummach.tiles;

import java.util.List;
import java.util.Random;

import dev.tilera.auracore.api.machine.TileVisUser;
import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnchanter extends TileVisUser implements ISidedInventory, ITileGui {
    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float field_40061_d;
    public float field_40062_e;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation2;
    public float bookRotationPrev;
    public float bookRotation;
    private static Random RNG = new Random();
    private ItemStack[] itemStacks = new ItemStack[1];
    public int[] enchantLevels = new int[3];
    public long nameSeed;
    private Random rand = new Random();
    public float progress = 0.0F;
    public int enchantmentCost = 0;
    public int enchantmentChoice = -1;

    @Override
    public GuiID getGuiID() {
        return GuiID.ENCHANTER;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getFloat("progress");
        this.enchantmentCost = nbttagcompound.getInteger("enchantmentCost");
        this.enchantmentChoice = nbttagcompound.getInteger("enchantmentChoice");
        this.enchantLevels[0] = nbttagcompound.getInteger("enchantment0");
        this.enchantLevels[1] = nbttagcompound.getInteger("enchantment1");
        this.enchantLevels[2] = nbttagcompound.getInteger("enchantment2");
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.itemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if (byte0 >= 0 && byte0 < this.itemStacks.length) {
                this.itemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("progress", this.progress);
        nbttagcompound.setInteger("enchantmentCost", this.enchantmentCost);
        nbttagcompound.setInteger("enchantmentChoice", this.enchantmentChoice);
        nbttagcompound.setInteger("enchantment0", this.enchantLevels[0]);
        nbttagcompound.setInteger("enchantment1", this.enchantLevels[1]);
        nbttagcompound.setInteger("enchantment2", this.enchantLevels[2]);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (this.itemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.worldObj.isRemote) {
            this.rotateBook();

        } else {
            if (this.progress >= (float) this.enchantmentCost && this.enchantmentChoice != -1) {
                this.enchantItem(this.enchantmentChoice);
                this.progress = (float) (this.enchantmentCost = 0);
                this.enchantmentChoice = -1;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }

            if (this.progress<(float) this.enchantmentCost&& this.enchantmentCost> 0
                && this.enchantmentChoice != -1) {
                this.progress += this.getAvailablePureVis(10.0F);
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    public int getProgressScaled(int i) {
        return (int) (this.progress * (float) i / (float) this.enchantmentCost);
    }

    private void rotateBook() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation2;
        EntityPlayer entityplayer = super.worldObj.getClosestPlayer(
            (double) ((float) super.xCoord + 0.5F),
            (double) ((float) super.yCoord + 0.5F),
            (double) ((float) super.zCoord + 0.5F),
            3.0
        );
        if (entityplayer != null) {
            double d = entityplayer.posX - (double) ((float) super.xCoord + 0.5F);
            double d1 = entityplayer.posZ - (double) ((float) super.zCoord + 0.5F);
            this.bookRotation = (float) Math.atan2(d1, d);
            this.bookSpread += 0.1F;
            if (this.bookSpread < 0.5F || RNG.nextInt(40) == 0) {
                float f3 = this.field_40061_d;

                do {
                    this.field_40061_d += (float) (RNG.nextInt(4) - RNG.nextInt(4));
                } while (f3 == this.field_40061_d);
            }
        } else {
            this.bookRotation += 0.02F;
            this.bookSpread -= 0.1F;
        }

        while (this.bookRotation2 >= 3.141593F) {
            this.bookRotation2 -= 6.283185F;
        }

        while (this.bookRotation2 < -3.141593F) {
            this.bookRotation2 += 6.283185F;
        }

        while (this.bookRotation >= 3.141593F) {
            this.bookRotation -= 6.283185F;
        }

        while (this.bookRotation < -3.141593F) {
            this.bookRotation += 6.283185F;
        }

        float f;
        for (f = this.bookRotation - this.bookRotation2; f >= 3.141593F; f -= 6.283185F) {}

        while (f < -3.141593F) {
            f += 6.283185F;
        }

        this.bookRotation2 += f * 0.4F;
        if (this.bookSpread < 0.0F) {
            this.bookSpread = 0.0F;
        }

        if (this.bookSpread > 1.0F) {
            this.bookSpread = 1.0F;
        }

        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f1 = (this.field_40061_d - this.pageFlip) * 0.4F;
        float f2 = 0.2F;
        if (f1 < -f2) {
            f1 = -f2;
        }

        if (f1 > f2) {
            f1 = f2;
        }

        this.field_40062_e += (f1 - this.field_40062_e) * 0.9F;
        this.pageFlip += this.field_40062_e;
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

    // TODO: AZANOR, WTF
    public void onCraftMatrixChanged(IInventory iinventory) {
        if (this.worldObj.isRemote)
            return;

        this.progress = (float) (this.enchantmentCost = 0);
        this.enchantmentChoice = -1;
        ItemStack itemstack = iinventory.getStackInSlot(0);
        if (itemstack != null && itemstack.isItemEnchantable()) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.nameSeed = this.rand.nextLong();
            float power = 0.0f;

            for (int j = -1; j <= 1; ++j) {
                for (int k = -1; k <= 1; ++k) {
                    if ((j != 0 || k != 0)
                        && this.worldObj.isAirBlock(this.xCoord + k, this.yCoord, this.zCoord + j)
                        && this.worldObj.isAirBlock(
                            this.xCoord + k, this.yCoord + 1, this.zCoord + j
                        )) {
                        power += ForgeHooks.getEnchantPower(
                            this.worldObj, this.xCoord + k * 2, this.yCoord, this.zCoord + j * 2
                        );
                        power += ForgeHooks.getEnchantPower(
                            this.worldObj, this.xCoord + k * 2, this.yCoord + 1, this.zCoord + j * 2
                        );
                        if (k != 0 && j != 0) {
                            power += ForgeHooks.getEnchantPower(
                                this.worldObj, this.xCoord + k * 2, this.yCoord, this.zCoord + j
                            );
                            power += ForgeHooks.getEnchantPower(
                                this.worldObj, this.xCoord + k * 2, this.yCoord + 1, this.zCoord + j
                            );
                            power += ForgeHooks.getEnchantPower(
                                this.worldObj, this.xCoord + k, this.yCoord, this.zCoord + j * 2
                            );
                            power += ForgeHooks.getEnchantPower(
                                this.worldObj, this.xCoord + k, this.yCoord + 1, this.zCoord + j * 2
                            );
                        }
                    }
                }
                if (power > 40) {
                    power = 40;
                }

                for (int l = 0; l < 3; ++l) {
                    this.enchantLevels[l]
                        = calcItemStackEnchantability(this.rand, l, (int) power, itemstack);
                }
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                this.enchantLevels[i] = 0;
            }
        }
    }

    public static int
    calcItemStackEnchantability(Random par0Random, int par1, int par2, ItemStack par3ItemStack) {
        Item var4 = par3ItemStack.getItem();
        int var5 = var4.getItemEnchantability();
        if (var5 <= 0) {
            return 0;
        } else {
            if (par2 > 40) {
                par2 = 40;
            }

            par2 = 1 + (par2 >> 1) + par0Random.nextInt(par2 + 1);
            int var6 = par0Random.nextInt(5) + par2;
            return par1 == 0 ? (var6 >> 1) + 1 : (par1 == 1 ? var6 * 2 / 3 + 1 : var6);
        }
    }

    public boolean startEnchantingItem(int i) {
        ItemStack itemstack = this.getStackInSlot(0);
        if (this.enchantLevels[i] > 0 && itemstack != null) {
            this.enchantmentChoice = i;
            this.enchantmentCost = this.enchantLevels[i] * 2 * (this.enchantLevels[i] / 2);
            this.progress = 0.0F;
            return true;
        } else {
            this.enchantmentChoice = -1;
            return false;
        }
    }

    public boolean enchantItem(int i) {
        ItemStack itemstack = this.itemStacks[0];
        if (this.enchantLevels[i] <= 0 || itemstack == null) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            List list = EnchantmentHelper.buildEnchantmentList(
                this.rand, itemstack, this.enchantLevels[i]
            );
            boolean flag = itemstack.getItem() == Items.book;
            if (list != null) {
                if (flag) {
                    itemstack.func_150996_a(Items.enchanted_book);
                }

                int j = flag && list.size() > 1 ? this.rand.nextInt(list.size()) : -1;

                for (int k = 0; k < list.size(); ++k) {
                    EnchantmentData enchantmentdata = (EnchantmentData) list.get(k);
                    if (!flag || k != j) {
                        if (flag) {
                            Items.enchanted_book.addEnchantment(itemstack, enchantmentdata);
                        } else {
                            itemstack.addEnchantment(
                                enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel
                            );
                        }
                    }
                }

                this.onCraftMatrixChanged(this);
            }
        }

        return true;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        this.markDirty();
        if (this.itemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.itemStacks[i].stackSize <= j) {
                itemstack1 = this.itemStacks[i];
                this.itemStacks[i] = null;

                this.onCraftMatrixChanged(this);
                return itemstack1;
            } else {
                itemstack1 = this.itemStacks[i].splitStack(j);
                if (this.itemStacks[i].stackSize == 0) {
                    this.itemStacks[i] = null;
                }

                this.onCraftMatrixChanged(this);
                return itemstack1;
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.markDirty();
        this.itemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        this.onCraftMatrixChanged(this);
    }

    @Override
    public String getInventoryName() {
        return "Thaumic Enchanter";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return this.itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.itemStacks[i];
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.itemStacks[var1] != null) {
            ItemStack var2 = this.itemStacks[var1];
            this.itemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    @Override
    public void closeInventory() {}

    @Override
    public void openInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return stack.isItemEnchantable();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer arg0) {
        return true;
    }

    @Override
    public boolean canExtractItem(int arg0, ItemStack arg1, int arg2) {
        return arg1.getEnchantmentTagList() != null;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int arg0) {
        return new int[] { 0 };
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("progress", this.progress);
        nbt.setInteger("enchantmentCost", this.enchantmentCost);
        nbt.setInteger("enchantmentChoice", this.enchantmentChoice);
        nbt.setIntArray("enchantLevels", this.enchantLevels);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.progress = nbt.getFloat("progress");
        this.enchantmentCost = nbt.getInteger("enchantmentCost");
        this.enchantmentChoice = nbt.getInteger("enchantmentChoice");
        this.enchantLevels = nbt.getIntArray("enchantLevels");
    }
}
