package net.anvilcraft.thaummach.tiles;

import dev.tilera.auracore.api.AuraNode;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.anvilcraft.thaummach.TMItems;
import net.anvilcraft.thaummach.ThaumicMachinery;
import net.anvilcraft.thaummach.packets.PacketFXSparkle;
import net.anvilcraft.thaummach.packets.PacketFXWisp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;

public class TileSoulBrazier extends TileEntity implements ISidedInventory, ITileGui {
    private ItemStack stack = null;
    private int delay;
    public int burnTime;
    private boolean previousLight;
    private int lightingDelay;

    public static ItemStack VALID_ITEM = new ItemStack(TMItems.soul_fragment);

    @Override
    public GuiID getGuiID() {
        return GuiID.SOUL_BRAZIER;
    }

    public boolean isWorking() {
        return this.burnTime > 0
            && !super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            );
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote)
            return;

        if (this.lightingDelay <= 0 && this.isWorking() != this.previousLight) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            super.worldObj.updateLightByType(
                EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord
            );
            this.lightingDelay = 10;
            this.previousLight = this.isWorking();
        }

        --this.lightingDelay;
        if (!super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            )) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            if (this.burnTime <= 0 && this.stack != null
                && this.stack.isItemEqual(VALID_ITEM)) {
                this.burnTime = 6000;
                this.lightingDelay = 0;
                super.worldObj.updateLightByType(
                    EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord
                );
                --this.stack.stackSize;
                if (this.stack.stackSize == 0) {
                    this.stack = null;
                }
            }

            if (this.burnTime > 0) {
                --this.burnTime;
                int q = 5;
                if (this.burnTime % q == 0) {
                    ThaumicMachinery.sendFXPacket(
                        this.worldObj,
                        new PacketFXWisp(
                            (double) ((float) super.xCoord + 0.5F),
                            (double) (super.yCoord + 1),
                            (double) ((float) super.zCoord + 0.5F),
                            0.6F,
                            5,
                            true,
                            -0.03f
                        )
                    );

                    ThaumicMachinery.sendFXPacket(
                        this.worldObj,
                        new PacketFXWisp(
                            (double) ((float) super.xCoord + 0.5F),
                            (double) ((float) super.yCoord + 0.8F),
                            (double) ((float) super.zCoord + 0.5F),
                            0.2F,
                            6,
                            false,
                            -0.015f
                        )
                    );

                    ThaumicMachinery.sendFXPacket(
                        this.worldObj,
                        new PacketFXSparkle(

                            (double
                            ) ((float) super.xCoord + 0.5F
                               + (super.worldObj.rand.nextFloat()
                                  - super.worldObj.rand.nextFloat())
                                   / 5.0F),
                            (double
                            ) ((float) (super.yCoord + 1)
                               + (super.worldObj.rand.nextFloat()
                                  - super.worldObj.rand.nextFloat())
                                   / 5.0F),
                            (double
                            ) ((float) super.zCoord + 0.5F
                               + (super.worldObj.rand.nextFloat()
                                  - super.worldObj.rand.nextFloat())
                                   / 5.0F),
                            0.65F,
                            6,
                            3,
                            -0.03F
                        )
                    );
                }

                if (this.delay > 0) {
                    --this.delay;
                    return;
                }

                this.delay = 90 - (3 + Math.abs(super.worldObj.getMoonPhase() - 4)) * 10;

                int closestId = AuraManager.getClosestAuraWithinRange(
                    this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16
                );
                if (closestId < 0)
                    return;
                AuraNode closest = AuraManager.getNode(closestId);

                int secondClosestId = -1;
                synchronized (AuraManager.saveLock) {
                    AuraManager.auraNodes.remove(closestId);
                    secondClosestId = AuraManager.getClosestAuraWithinRange(
                        this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1024
                    );
                    AuraManager.auraNodes.put(closestId, closest);
                }
                if (secondClosestId < 0)
                    return;

                AuraNode secondClosest = AuraManager.getNode(secondClosestId);

                if (this.worldObj.rand.nextBoolean() && secondClosest.level > 0) {
                    AuraManager.queueNodeChanges(
                        secondClosestId, -1, 0, false, null, 0, 0, 0
                    );
                    AuraManager.queueNodeChanges(closestId, 1, 0, false, null, 0, 0, 0);
                } else if (secondClosest.taint > 0) {
                    AuraManager.queueNodeChanges(
                        secondClosestId, 0, 0, -1, false, null, 0, 0, 0
                    );
                    AuraManager.queueNodeChanges(
                        closestId, 0, 0, 1, false, null, 0, 0, 0
                    );
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.burnTime = nbttagcompound.getInteger("burnTime");
        if (nbttagcompound.hasKey("stack"))
            this.stack
                = ItemStack.loadItemStackFromNBT(nbttagcompound.getCompoundTag("stack"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("burnTime", this.burnTime);

        if (this.stack != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            this.stack.writeToNBT(stackTag);
            nbttagcompound.setTag("stack", stackTag);
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.stack != null) {
            ItemStack itemstack1;
            if (this.stack.stackSize <= j) {
                itemstack1 = this.stack;
                this.stack = null;
                return itemstack1;
            } else {
                itemstack1 = this.stack.splitStack(j);
                if (this.stack.stackSize == 0) {
                    this.stack = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.stack = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        ItemStack stack = this.stack;
        this.stack = null;
        return stack;
    }

    @Override
    public String getInventoryName() {
        return "Brazier of Souls";
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
    public boolean isItemValidForSlot(int slot, ItemStack otherStack) {
        return this.canInsertItem(slot, otherStack, 0);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack otherStack, int side) {
        return this.stack == null ? otherStack.getItem() == VALID_ITEM.getItem()
                                  : (this.stack.isItemEqual(otherStack))
                && otherStack.stackSize + this.stack.stackSize
                    <= this.stack.getMaxStackSize();
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack otherStack, int side) {
        return this.stack != null && otherStack.isItemEqual(this.stack)
            && otherStack.stackSize <= this.stack.stackSize;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("delay", this.delay);
        nbt.setInteger("burnTime", this.burnTime);
        nbt.setBoolean("previousLight", this.previousLight);
        nbt.setInteger("lightingDelay", this.lightingDelay);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();
        this.delay = nbt.getInteger("delay");
        this.burnTime = nbt.getInteger("burnTime");
        this.previousLight = nbt.getBoolean("previousLight");
        this.lightingDelay = nbt.getInteger("lightingDelay");

        // schedule light update
        this.worldObj.func_147451_t(this.xCoord, this.yCoord, this.zCoord);
    }
}
