package net.anvilcraft.thaummach.tiles;

import java.util.ArrayList;

import dev.tilera.auracore.api.AuraNode;
import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.api.machine.IConnection;
import dev.tilera.auracore.api.machine.IUpgradable;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigItems;

public class TileCondenser
    extends TileEntity implements ISidedInventory, IUpgradable, IConnection, ITileGui {
    public float angle = 0.0F;
    public float speed;
    public long accTimer;
    public float progress;
    public float currentVis;
    public float currentTaint;
    public int currentType = -1;
    public short maxVis = 10;
    public float degredation;
    private ItemStack[] condenserItemStacks = new ItemStack[2];
    private byte[] upgrades = new byte[] { -1, -1 };

    @Override
    public void updateEntity() {
        if (!super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            )) {
            if (this.worldObj.isRemote) {
                this.angle += this.speed * 5.0F;
                if (this.angle > 360.0F) {
                    this.angle -= 360.0F;
                }

                return;
            }

            if (this.accTimer < System.currentTimeMillis()) {
                this.equalizeWithNeighbours();
                if (this.speed < (this.hasUpgrade((byte) 0) ? 1.25F : 1.0F)) {
                    this.speed += this.hasUpgrade((byte) 0) ? 0.01F : 5.0E-4F;
                    if (this.speed > (this.hasUpgrade((byte) 0) ? 1.25F : 1.0F)) {
                        this.speed = this.hasUpgrade((byte) 0) ? 1.25F : 1.0F;
                    }
                }

                if (this.speed > 0.0F && this.degredation == 0.0F) {
                    this.speed -= 0.005F;
                    if (this.speed < 0.0F) {
                        this.speed = 0.0F;
                    }
                }

                if (!this.hasUpgrade((byte) 3) && this.currentType == 9) {
                    this.progress = 0.0F;
                    this.degredation = 0.0F;
                }

                float moon
                    = (float) (3 + Math.abs(super.worldObj.getMoonPhase() - 4)) * 0.2F;
                if (this.currentType >= 0) {
                    this.progress += this.speed * moon;
                }

                if (this.progress >= (float) (this.hasUpgrade((byte) 1) ? 20 : 25)
                    && this.currentVis <= (float) (this.maxVis - 1)
                    && this.currentTaint <= (float) (this.maxVis - 1)
                    && this.currentType >= 0) {
                    int nodeID = AuraManager.getClosestAuraWithinRange(
                        this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1024
                    );
                    if (nodeID >= 0) {
                        AuraNode node = AuraManager.getNode(nodeID);
                        if (this.currentType != 9) {
                            if (node.level > 0) {
                                AuraManager.queueNodeChanges(
                                    nodeID, -1, 0, false, null, 0, 0, 0
                                );
                                this.progress = 0.0f;
                                ++this.currentVis;
                            } else if (this.hasUpgrade((byte) 3) && node.taint > 0) {
                                AuraManager.queueNodeChanges(
                                    nodeID, 0, 0, -1, false, null, 0, 0, 0
                                );
                                this.progress = 0.0f;
                                ++this.currentTaint;
                            }
                        }

                        this.worldObj.markBlockForUpdate(
                            this.xCoord, this.yCoord, this.zCoord
                        );
                        super.worldObj.updateLightByType(
                            EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord
                        );
                    }
                }

                boolean flag = false;
                if (this.progress < (float) (this.hasUpgrade((byte) 1) ? 20 : 25)) {
                    if (this.degredation > 0.0F) {
                        flag = true;
                    }

                    this.degredation -= Math.max(0.25F, this.speed);
                    if (this.degredation < 0.0F) {
                        this.degredation = 0.0F;
                    }

                    if (this.degredation > 0.0F && (int) (this.degredation % 3.0F) == 0) {
                        // TODO: FX
                        //FXWisp ef = new FXWisp(
                        //    super.worldObj,
                        //    (double
                        //    ) ((float) super.xCoord + 0.5F
                        //       + super.worldObj.rand.nextFloat()
                        //       - super.worldObj.rand.nextFloat()),
                        //    (double
                        //    ) ((float) super.yCoord + 1.5F
                        //       + super.worldObj.rand.nextFloat()
                        //       - super.worldObj.rand.nextFloat()),
                        //    (double
                        //    ) ((float) super.zCoord + 0.5F
                        //       + super.worldObj.rand.nextFloat()
                        //       - super.worldObj.rand.nextFloat()),
                        //    (double) ((float) super.xCoord + 0.5F),
                        //    (double) ((float) super.yCoord + 1.5F),
                        //    (double) ((float) super.zCoord + 0.5F),
                        //    0.1F,
                        //    this.currentType
                        //);
                        //ef.tinkle = true;
                        //ModLoader.getMinecraftInstance().effectRenderer.addEffect(ef);
                    }
                }

                if (this.degredation < 10.0F && flag) {
                    super.worldObj.spawnParticle(
                        "largesmoke",
                        (double) ((float) super.xCoord + 0.5F),
                        (double) ((float) super.yCoord + 1.3F),
                        (double) ((float) super.zCoord + 0.5F),
                        0.0,
                        0.0,
                        0.0
                    );
                }

                if (this.degredation <= 0.0F) {
                    this.worldObj.markBlockForUpdate(
                        this.xCoord, this.yCoord, this.zCoord
                    );
                    if (flag && this.condenserItemStacks[1] != null
                        && this.condenserItemStacks[1].getItem() == ConfigItems.itemShard
                        && this.condenserItemStacks[1].getItemDamage() == 6) {
                        if (this.condenserItemStacks[1].stackSize < 64) {
                            ++this.condenserItemStacks[1].stackSize;
                        } else {
                            EntityItem entityitem = new EntityItem(
                                super.worldObj,
                                (double) ((float) super.xCoord + 0.5F),
                                (double) ((float) super.yCoord + 1.0F),
                                (double) ((float) super.zCoord + 0.5F),
                                new ItemStack(ConfigItems.itemShard, 1, 8)
                            );
                            entityitem.motionY = 0.20000000298023224;
                            super.worldObj.spawnEntityInWorld(entityitem);
                        }
                    }

                    if (flag && this.condenserItemStacks[1] == null) {
                        this.condenserItemStacks[1]
                            = new ItemStack(ConfigItems.itemShard, 1, 8);
                    }

                    if (this.condenserItemStacks[0] == null
                        || this.condenserItemStacks[0].getItem() != ConfigItems.itemShard
                        || this.condenserItemStacks[0].getItemDamage() == 8
                        || this.condenserItemStacks[0].stackSize <= 0
                            && !this.hasUpgrade((byte) 3)) {
                        this.currentType = -1;
                    } else {
                        this.degredation = 4550.0F;
                        this.currentType = this.condenserItemStacks[0].getItemDamage();
                        --this.condenserItemStacks[0].stackSize;
                        if (this.condenserItemStacks[0].stackSize == 0) {
                            this.condenserItemStacks[0] = null;
                        }
                    }
                }

                this.accTimer = System.currentTimeMillis() + 100L;
            }
        }
    }

    @Override
    public GuiID getGuiID() {
        return GuiID.CONDENSER;
    }

    protected void equalizeWithNeighbours() {
        ArrayList<IConnection> neighbours = new ArrayList<>();

        for (int dir = 0; dir < 6; ++dir) {
            // Don't connect on top
            if (dir == 1)
                continue;

            HelperLocation loc = new HelperLocation(this);
            loc.facing = ForgeDirection.VALID_DIRECTIONS[dir];

            TileEntity te = loc.getConnectableTile(super.worldObj);
            if (te != null && te instanceof TileCondenser) {
                IConnection ent = (IConnection) te;
                neighbours.add(ent);
            }
        }

        if (neighbours.size() > 0) {
            float pVis = this.getPureVis();
            float tVis = this.getTaintedVis();

            int a;
            for (a = 0; a < neighbours.size(); ++a) {
                pVis += ((IConnection) neighbours.get(a)).getPureVis();
            }

            for (a = 0; a < neighbours.size(); ++a) {
                tVis += ((IConnection) neighbours.get(a)).getTaintedVis();
            }

            pVis /= (float) (neighbours.size() + 1);
            tVis /= (float) (neighbours.size() + 1);

            for (a = 0; a < neighbours.size(); ++a) {
                ((IConnection) neighbours.get(a)).setPureVis(pVis);
                ((IConnection) neighbours.get(a)).setTaintedVis(tVis);
            }

            this.setPureVis(pVis);
            this.setTaintedVis(tVis);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.condenserItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1
                = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if (byte0 >= 0 && byte0 < this.condenserItemStacks.length) {
                this.condenserItemStacks[byte0]
                    = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.speed = nbttagcompound.getFloat("speed");
        this.progress = nbttagcompound.getFloat("progress");
        this.currentVis = nbttagcompound.getFloat("currentVis");
        this.currentTaint = nbttagcompound.getFloat("currentTaint");
        this.currentType = nbttagcompound.getShort("currentType");
        this.degredation = nbttagcompound.getFloat("taint");
        this.upgrades = nbttagcompound.getByteArray("upgrades");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("speed", this.speed);
        nbttagcompound.setFloat("progress", this.progress);
        nbttagcompound.setFloat("currentVis", this.currentVis);
        nbttagcompound.setFloat("currentTaint", this.currentTaint);
        nbttagcompound.setFloat("taint", this.degredation);
        nbttagcompound.setShort("currentType", (short) this.currentType);
        nbttagcompound.setByteArray("upgrades", this.upgrades);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.condenserItemStacks.length; ++i) {
            if (this.condenserItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.condenserItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public boolean isVisSource() {
        return true;
    }

    @Override
    public boolean isVisConduit() {
        return false;
    }

    @Override
    public float getPureVis() {
        return this.currentVis;
    }

    @Override
    public void setPureVis(float amount) {
        this.currentVis = amount;
    }

    @Override
    public float getTaintedVis() {
        return this.currentTaint;
    }

    @Override
    public float getMaxVis() {
        return (float) this.maxVis;
    }

    @Override
    public void setTaintedVis(float amount) {
        this.currentTaint = amount;
    }

    @Override
    public float[] subtractVis(float amount) {
        float pureAmount = amount / 2.0F;
        float taintAmount = amount / 2.0F;
        float[] result = new float[] { 0.0F, 0.0F };
        if (amount < 0.001F) {
            return result;
        } else {
            if (this.currentVis < pureAmount) {
                pureAmount = this.currentVis;
            }

            if (this.currentTaint < taintAmount) {
                taintAmount = this.currentTaint;
            }

            if (pureAmount < amount / 2.0F && taintAmount == amount / 2.0F) {
                taintAmount = Math.min(amount - pureAmount, this.currentTaint);
            } else if (taintAmount < amount / 2.0F && pureAmount == amount / 2.0F) {
                pureAmount = Math.min(amount - taintAmount, this.currentVis);
            }

            this.currentVis -= pureAmount;
            this.currentTaint -= taintAmount;
            result[0] = pureAmount;
            result[1] = taintAmount;
            return result;
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.condenserItemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.condenserItemStacks[i].stackSize <= j) {
                itemstack1 = this.condenserItemStacks[i];
                this.condenserItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.condenserItemStacks[i].splitStack(j);
                if (this.condenserItemStacks[i].stackSize == 0) {
                    this.condenserItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.condenserItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return "Vis Condenser";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
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
    public int getSizeInventory() {
        return this.condenserItemStacks.length;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        switch (side) {
            // Bottom
            case 0:
                return new int[] { 1 };

            default:
                return new int[] { 0 };
        }
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.condenserItemStacks[i];
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
        return 2;
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
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean clearUpgrade(int index) {
        if (this.upgrades[index] >= 0) {
            this.upgrades[index] = -1;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.condenserItemStacks[var1] != null) {
            ItemStack var2 = this.condenserItemStacks[var1];
            this.condenserItemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    @Override
    public int getVisSuction(HelperLocation loc) {
        return 0;
    }

    @Override
    public void setVisSuction(int suction) {}

    @Override
    public int getTaintSuction(HelperLocation loc) {
        return 0;
    }

    @Override
    public void setTaintSuction(int suction) {}

    @Override
    public void setSuction(int suction) {}

    @Override
    public int getSuction(HelperLocation loc) {
        return 0;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        // output slot
        if (slot == 1)
            return false;

        return stack.getItem() == ConfigItems.itemShard && stack.getItemDamage() != 8;
    }

    @Override
    public boolean getConnectable(ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        ItemStack curStack = this.condenserItemStacks[0];
        if (curStack != null
            && (!curStack.isItemEqual(stack)
                || curStack.stackSize + stack.stackSize > curStack.getMaxStackSize()))
            return false;

        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 1;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("speed", this.speed);
        nbt.setFloat("progress", this.progress);
        nbt.setFloat("currentVis", this.currentVis);
        nbt.setFloat("currentTaint", this.currentTaint);
        nbt.setInteger("currentType", this.currentType);
        nbt.setShort("maxVis", this.maxVis);
        nbt.setFloat("degredation", this.degredation);
        nbt.setByteArray("upgrades", this.upgrades);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.speed = nbt.getFloat("speed");
        this.progress = nbt.getFloat("progress");
        this.currentVis = nbt.getFloat("currentVis");
        this.currentTaint = nbt.getFloat("currentTaint");
        this.currentType = nbt.getInteger("currentType");
        this.maxVis = nbt.getShort("maxVis");
        this.degredation = nbt.getFloat("degredation");
        this.upgrades = nbt.getByteArray("upgrades");

        this.worldObj.markBlockRangeForRenderUpdate(
            this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord
        );
    }
}
