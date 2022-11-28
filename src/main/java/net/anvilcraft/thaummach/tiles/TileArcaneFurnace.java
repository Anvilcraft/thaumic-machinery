package net.anvilcraft.thaummach.tiles;

import java.util.stream.IntStream;

import dev.tilera.auracore.api.machine.TileVisUser;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TileArcaneFurnace extends TileVisUser implements ISidedInventory {
    private ItemStack[] furnaceItemStacks = new ItemStack[19];
    public int furnaceBurnTime = 0;
    public int currentItemBurnTime = 0;
    public int furnaceCookTime = 0;
    public int furnaceMaxCookTime = 180;
    public int bellows = 0;
    public float vis;
    public boolean boost;

    // TODO: GUIs
    //public GuiScreen getGui(EntityPlayer player) {
    //   return new GuiArcaneFurnace(player.inventory, this);
    //}

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    //@Override
    //public int getStartInventorySide(int side) {
    //    if (side == 0) {
    //        return 18;
    //    } else {
    //        return side == 1 ? 0 : 9;
    //    }
    //}

    //@Override
    //public int getSizeInventorySide(int side) {
    //    return side == 0 ? 1 : 9;
    //}

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.furnaceItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.furnaceItemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.furnaceItemStacks[i].stackSize <= j) {
                itemstack1 = this.furnaceItemStacks[i];
                this.furnaceItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.furnaceItemStacks[i].splitStack(j);
                if (this.furnaceItemStacks[i].stackSize == 0) {
                    this.furnaceItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.furnaceItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1
                = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if (byte0 >= 0 && byte0 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[byte0]
                    = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
        this.currentItemBurnTime = nbttagcompound.getShort("MaxBurnTime");
        this.furnaceCookTime = nbttagcompound.getShort("CookTime");
        this.furnaceMaxCookTime = nbttagcompound.getShort("MaxCookTime");
        this.bellows = nbttagcompound.getShort("bellows");
        this.boost = nbttagcompound.getBoolean("boost");
        this.vis = nbttagcompound.getFloat("vis");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short) this.furnaceBurnTime);
        nbttagcompound.setShort("CookTime", (short) this.furnaceCookTime);
        nbttagcompound.setShort("MaxBurnTime", (short) this.currentItemBurnTime);
        nbttagcompound.setShort("MaxCookTime", (short) this.furnaceMaxCookTime);
        nbttagcompound.setShort("bellows", (short) this.bellows);
        nbttagcompound.setBoolean("boost", this.boost);
        nbttagcompound.setFloat("vis", this.vis);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
            if (this.furnaceItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
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
        return this.furnaceCookTime * i / this.furnaceMaxCookTime;
    }

    public int getBurnTimeRemainingScaled(int i) {
        int bt = this.currentItemBurnTime;
        if (bt == 0) {
            bt = 200;
        }

        return this.furnaceBurnTime * i / bt;
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0
            //&& !super.worldObj.isBlockGettingPowered(
            //    super.xCoord, super.yCoord, super.zCoord
            //)
            && !super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            );
    }

    public boolean isWorking() {
        return this.furnaceBurnTime > 0
            && (this.furnaceCookTime > 0 || this.belowHeatableTile())
            //&& !super.worldObj.isBlockGettingPowered(
            //    super.xCoord, super.yCoord, super.zCoord
            //)
            && !super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            );
    }

    public boolean belowHeatableTile() {
        TileEntity te
            = super.worldObj.getTileEntity(super.xCoord, super.yCoord + 1, super.zCoord);
        return te != null && te instanceof TileCrucible;
    }

    @Override
    public void updateEntity() {
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;
        this.setSuction(0);
        if (!super.worldObj.isRemote && this.vis < this.getMaxVis()) {
            this.vis += this.getAvailablePureVis(this.getMaxVis() - this.vis);
        }

        if (!super.worldObj.isRemote
            //&& !super.worldObj.isBlockGettingPowered(
            //    super.xCoord, super.yCoord, super.zCoord
            //)
            && !super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord, super.zCoord
            )) {
            if (this.furnaceBurnTime > 0
                && (this.furnaceCookTime > 0 || this.belowHeatableTile())) {
                --this.furnaceBurnTime;
            }

            if (this.furnaceBurnTime <= 0
                && (this.canSmelt() || this.belowHeatableTile())) {
                this.currentItemBurnTime = this.furnaceBurnTime
                    = getItemBurnTime(this.furnaceItemStacks[18]);
                if (this.furnaceBurnTime > 0) {
                    if (this.vis >= (float) this.currentItemBurnTime / 1600.0F) {
                        this.vis -= (float) this.currentItemBurnTime / 1600.0F;
                        this.currentItemBurnTime
                            = (int) ((float) this.currentItemBurnTime * 1.25F);
                        this.furnaceBurnTime
                            = (int) ((float) this.furnaceBurnTime * 1.25F);
                    }

                    flag1 = true;
                    if (this.furnaceItemStacks[18] != null) {
                        if (this.vis >= 0.25F) {
                            this.vis -= 0.25F;
                            this.furnaceMaxCookTime = 100;
                            this.boost = true;
                        } else {
                            this.furnaceMaxCookTime = 180;
                            this.boost = false;
                        }

                        this.furnaceMaxCookTime = (int
                        ) ((float) this.furnaceMaxCookTime
                           * (1.0F - (float) this.bellows * 0.1F));
                        this.worldObj.markBlockForUpdate(
                            this.xCoord, this.yCoord, this.zCoord
                        );
                        super.worldObj.updateLightByType(
                            EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord
                        );
                        // TODO: WTF
                        //if (this.furnaceItemStacks[18].getItem().func_46056_k()) {
                        //    this.furnaceItemStacks[18] = new ItemStack(
                        //        this.furnaceItemStacks[18].getItem().setFull3D()
                        //    );
                        //} else {
                        --this.furnaceItemStacks[18].stackSize;
                        //}

                        if (this.furnaceItemStacks[18].stackSize == 0) {
                            this.furnaceItemStacks[18] = null;
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.furnaceCookTime;
                if (this.furnaceCookTime >= this.furnaceMaxCookTime) {
                    this.furnaceCookTime = 0;
                    if (this.vis >= 0.25F) {
                        this.vis -= 0.25F;
                        this.furnaceMaxCookTime = 100;
                        this.boost = true;
                    } else {
                        this.furnaceMaxCookTime = 180;
                        this.boost = false;
                    }

                    this.furnaceMaxCookTime = (int
                    ) ((float) this.furnaceMaxCookTime
                       * (1.0F - (float) this.bellows * 0.2F));
                    this.smeltItem();
                    this.worldObj.markBlockForUpdate(
                        this.xCoord, this.yCoord, this.zCoord
                    );
                    super.worldObj.updateLightByType(
                        EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord
                    );
                    flag1 = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }

            if (flag != this.furnaceBurnTime > 0) {
                flag1 = true;
            }
        }

        if (flag1) {
            // TODO: WTF
            //this.onInventoryChanged();
        }
    }

    private boolean canSmelt(int slotIn, int slotOut) {
        if (this.furnaceItemStacks[slotIn] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(
                this.furnaceItemStacks[slotIn]
            );
            if (itemstack == null) {
                return false;
            } else if (this.furnaceItemStacks[slotOut] == null) {
                return true;
            } else if (!this.furnaceItemStacks[slotOut].isItemEqual(itemstack)) {
                return false;
            } else {
                int result
                    = this.furnaceItemStacks[slotOut].stackSize + itemstack.stackSize;
                return result <= this.getInventoryStackLimit()
                    && result <= itemstack.getMaxStackSize();
            }
        }
    }

    private boolean canSmelt() {
        for (int input = 9; input < 18; ++input) {
            for (int output = 0; output < 9; ++output) {
                if (this.canSmelt(input, output)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void smeltItem() {
        for (int input = 9; input < 18; ++input) {
            for (int output = 0; output < 9; ++output) {
                boolean smelted = false;

                boolean tryAgain;
                do {
                    tryAgain = false;
                    if (this.canSmelt(input, output)) {
                        ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(
                            this.furnaceItemStacks[input]
                        );
                        if (this.furnaceItemStacks[output] != null
                            && this.furnaceItemStacks[output].isItemEqual(itemstack)
                            && this.furnaceItemStacks[output].getItemDamage()
                                == itemstack.getItemDamage()) {
                            ItemStack var10000 = this.furnaceItemStacks[output];
                            var10000.stackSize += itemstack.stackSize;
                            smelted = true;
                        } else if (this.furnaceItemStacks[output] == null) {
                            this.furnaceItemStacks[output] = itemstack.copy();
                            smelted = true;
                        }

                        if (smelted
                            && super.worldObj.rand.nextInt(90) < 5 + this.bellows * 7
                            && this.vis >= 0.5F) {
                            this.vis -= 0.5F;
                            tryAgain = true;
                            AuraManager.addFluxToClosest(
                                this.worldObj,
                                this.xCoord,
                                this.yCoord,
                                this.zCoord,
                                new AspectList().add(Aspect.FIRE, 2)
                            );
                        }
                    }
                } while (tryAgain);

                if (smelted) {
                    // TODO: WTF
                    //if (this.furnaceItemStacks[input].getItem().func_46056_k()) {
                    //    this.furnaceItemStacks[input] = new ItemStack(
                    //        this.furnaceItemStacks[input].getItem().setFull3D()
                    //    );
                    //} else {
                    --this.furnaceItemStacks[input].stackSize;
                    //}

                    if (this.furnaceItemStacks[input].stackSize <= 0) {
                        this.furnaceItemStacks[input] = null;
                    }

                    return;
                }
            }
        }
    }

    public static int getItemBurnTime(ItemStack par1ItemStack) {
        return TileEntityFurnace.getItemBurnTime(par1ItemStack);
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
    public boolean getConnectable(ForgeDirection face) {
        switch (face) {
            case DOWN:
            case EAST:
            case SOUTH:
            case WEST:
            case NORTH:
                return true;

            default:
                return false;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.furnaceItemStacks[var1] != null) {
            ItemStack var2 = this.furnaceItemStacks[var1];
            this.furnaceItemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    @Override
    public float getPureVis() {
        return this.vis;
    }

    @Override
    public float getMaxVis() {
        return 5.0F;
    }

    @Override
    public String getInventoryName() {
        return "thaummach:arcane_furnace";
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
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (slot >= 9 && slot <= 17)
            return false;

        if (slot == 18)
            return getItemBurnTime(is) > 0;

        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0)
            return new int[] { 18 };

        if (side == 1)
            return IntStream.rangeClosed(0, 8).toArray();

        return IntStream.rangeClosed(9, 17).toArray();
    }

    @Override
    public boolean
    canInsertItem(int slot, ItemStack is, int side) {
        return this.isItemValidForSlot(slot, is);
    }

    @Override
    public boolean
    canExtractItem(int slot, ItemStack is, int side) {
        return slot >= 9 && slot <= 17;
    }
}
