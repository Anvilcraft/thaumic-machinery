package net.anvilcraft.thaummach.tiles;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.tilera.auracore.api.HelperLocation;
import net.anvilcraft.thaummach.TMItems;
import net.anvilcraft.thaummach.items.ItemFocus;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.client.fx.particles.FXWisp;

public class TileBore extends TileEntity implements ISidedInventory {
    public int orientation = 0;
    public int duration;
    public int maxDuration;
    private int minedelay;
    public int rotation;
    public int focus = -1;
    private int range = 32;
    private int area = 2;
    private int delay = 4;
    private boolean conserve = false;
    private ItemStack[] boreItemStacks;
    private Map<Integer, EntityItem> entities;

    public TileBore() {
        this.orientation = 0;
        this.boreItemStacks = new ItemStack[2];
        this.entities = new HashMap<>();
    }

    // TODO: GUIs
    //public GuiScreen getGui(EntityPlayer player) {
    //    return new GuiBore(player.inventory, this);
    //}

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.duration > 0 && this.gettingPower()) {
            this.rotation += 6 - this.delay;
        }

        if (this.rotation > 360) {
            this.rotation -= 360;
        }

        if (!super.worldObj.isRemote) {
            int a;
            if (this.boreItemStacks[0] != null
                && this.boreItemStacks[0].getItem() instanceof ItemFocus) {
                if (this.boreItemStacks[0] != null) {
                    float f = super.worldObj.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = super.worldObj.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = super.worldObj.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem = new EntityItem(
                        super.worldObj,
                        (double) ((float) super.xCoord + f),
                        (double) ((float) super.yCoord + f1),
                        (double) ((float) super.zCoord + f2),
                        ItemStack.copyItemStack(this.boreItemStacks[0])
                    );
                    float f3 = 0.05F;
                    entityitem.motionX
                        = (double) ((float) super.worldObj.rand.nextGaussian() * f3);
                    entityitem.motionY = (double
                    ) ((float) super.worldObj.rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ
                        = (double) ((float) super.worldObj.rand.nextGaussian() * f3);
                    super.worldObj.spawnEntityInWorld(entityitem);
                    this.boreItemStacks[0] = null;
                }
            }

            this.focus = -1;
            if (this.boreItemStacks[0] != null
                && this.boreItemStacks[0].getItem() == TMItems.focus0) {
                this.range = 40;
                this.area = 2;
                this.delay = 4;
                this.conserve = false;
                this.focus = 0;
            }

            if (this.boreItemStacks[0] != null
                && this.boreItemStacks[0].getItem() == TMItems.focus1) {
                this.range = 40;
                this.area = 3;
                this.delay = 2;
                this.conserve = false;
                this.focus = 1;
            }

            if (this.boreItemStacks[0] != null
                && this.boreItemStacks[0].getItem() == TMItems.focus2) {
                this.range = 80;
                this.area = 3;
                this.delay = 4;
                this.conserve = false;
                this.focus = 2;
            }

            if (this.boreItemStacks[0] != null
                && this.boreItemStacks[0].getItem() == TMItems.focus3) {
                this.range = 40;
                this.area = 5;
                this.delay = 4;
                this.conserve = false;
                this.focus = 3;
            }

            if (this.boreItemStacks[0] != null
                && this.boreItemStacks[0].getItem() == TMItems.focus4) {
                this.range = 40;
                this.area = 3;
                this.delay = 4;
                this.conserve = true;
                this.focus = 4;
            }

            ++this.minedelay;
            if (this.minedelay > this.delay) {
                this.minedelay = 0;
            }

            if (this.duration > 0 && this.gettingPower() && this.minedelay == 0
                && this.boreItemStacks[0] != null) {
                for (a = 0; a < 4; ++a) {
                    if (this.minedBlock()) {
                        this.boreItemStacks[0].setItemDamage(
                            this.boreItemStacks[0].getItemDamage() + 1
                        );
                        if (this.boreItemStacks[0].getItemDamage()
                            > this.boreItemStacks[0].getMaxDamage()) {
                            this.boreItemStacks[0] = null;
                        }
                        break;
                    }
                }

                super.worldObj.playSoundEffect(
                    (double) super.xCoord,
                    (double) super.yCoord,
                    (double) super.zCoord,
                    "mob.slimeattack",
                    0.3F,
                    0.1F + super.worldObj.rand.nextFloat() * 0.3F
                );
                --this.duration;
                HelperLocation hl = new HelperLocation(this, this.orientation);
                HelperLocation hl2 = new HelperLocation(this, this.orientation);
                hl.moveForwards(1.0);
                hl2.moveForwards(5.0);
                FXWisp ef = new FXWisp(
                    super.worldObj,
                    hl.x + 0.5,
                    hl.y + 0.5,
                    hl.z + 0.5,
                    hl2.x + 0.5,
                    hl2.y + 0.5,
                    hl2.z + 0.5,
                    0.6F,
                    this.focus == 0 ? 5 : this.focus
                );
                ef.shrink = true;
                ef.blendmode = 1;
                Minecraft.getMinecraft().effectRenderer.addEffect(ef);
            }

            Collection<EntityItem> c = this.entities.values();

            for (EntityItem ac : c) {
                ac.noClip = false;
                ac.fireResistance = 1;
            }

            this.entities.clear();
            if (this.duration > 0 && this.gettingPower() && this.focus >= 0) {
                this.suckItems();
            }

            if (this.duration == 0 && this.boreItemStacks[0] != null
                && this.boreItemStacks[1] != null && this.gettingPower()
                && this.boreItemStacks[1].isItemEqual(new ItemStack(TMItems.singularity)
                )) {
                this.maxDuration = 250;
                this.duration = this.maxDuration;
                --this.boreItemStacks[1].stackSize;
                if (this.boreItemStacks[1].stackSize == 0) {
                    this.boreItemStacks[1] = null;
                }
            }
        }
    }

    public boolean gettingPower() {
        return super.worldObj.isBlockIndirectlyGettingPowered(
                   super.xCoord, super.yCoord, super.zCoord
               )
            || super.worldObj.isBlockIndirectlyGettingPowered(
                super.xCoord, super.yCoord + 1, super.zCoord
            );
    }

    @SuppressWarnings("unchecked")
    private void suckItems() {
        int xm = 0;
        int xp = 0;
        int ym = 0;
        int yp = 0;
        int zm = 0;
        int zp = 0;
        int radius = this.area + 1;
        if (this.orientation == 0) {
            xm = zm = -radius;
            zp = radius;
            xp = radius;
            ym = -this.range - 1;
        } else if (this.orientation == 1) {
            xm = zm = -radius;
            zp = radius;
            xp = radius;
            yp = this.range + 1;
        } else if (this.orientation == 2) {
            xm = ym = -radius;
            yp = radius;
            xp = radius;
            zm = -this.range - 1;
        } else if (this.orientation == 3) {
            xm = ym = -radius;
            yp = radius;
            xp = radius;
            zp = this.range + 1;
        } else if (this.orientation == 4) {
            zm = ym = -radius;
            yp = radius;
            zp = radius;
            xm = -this.range - 1;
        } else if (this.orientation == 5) {
            zm = ym = -radius;
            yp = radius;
            zp = radius;
            xp = this.range + 1;
        }

        HelperLocation loc = new HelperLocation(this, this.orientation);
        loc.moveForwards(1.0);
        List<EntityItem> list = super.worldObj.getEntitiesWithinAABB(
            EntityItem.class,
            AxisAlignedBB.getBoundingBox(
                (double) (super.xCoord + xm),
                (double) (super.yCoord + ym),
                (double) (super.zCoord + zm),
                (double) super.xCoord + 1.0 + (double) xp,
                (double) super.yCoord + 1.0 + (double) yp,
                (double) super.zCoord + 1.0 + (double) zp
            )
        );

        int a;
        EntityItem entity;
        for (a = 0; a < list.size(); ++a) {
            entity = (EntityItem) list.get(a);
            if (entity instanceof EntityItem && !entity.noClip) {
                double d6 = entity.posX - loc.x - 0.5;
                double d8 = entity.posY - loc.y - 0.5;
                double d10 = entity.posZ - loc.z - 0.5;
                double d11
                    = (double) MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                double d13 = 0.3;
                entity.motionX -= d6 * d13;
                entity.motionY -= d8 * d13;
                entity.motionZ -= d10 * d13;
                if (entity.motionX > 0.35) {
                    entity.motionX = 0.35;
                }

                if (entity.motionX < -0.35) {
                    entity.motionX = -0.35;
                }

                if (entity.motionY > 0.35) {
                    entity.motionY = 0.35;
                }

                if (entity.motionY < -0.35) {
                    entity.motionY = -0.35;
                }

                if (entity.motionZ > 0.35) {
                    entity.motionZ = 0.35;
                }

                if (entity.motionZ < -0.35) {
                    entity.motionZ = -0.35;
                }

                entity.delayBeforeCanPickup = 2;
                entity.fireResistance = 50;
                entity.noClip = true;
                boolean dp = true;

                if (dp) {
                    FXWisp ef = new FXWisp(
                        super.worldObj,
                        (double) ((float) entity.prevPosX),
                        (double) ((float) entity.prevPosY + 0.1F),
                        (double) ((float) entity.prevPosZ),
                        0.4F,
                        this.focus == 0 ? 5 : this.focus
                    );
                    ef.shrink = true;
                    ef.blendmode = 1;
                    Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                }

                if (this.entities.get(entity.getEntityId()) == null) {
                    this.entities.put(entity.getEntityId(), entity);
                }
            }
        }

        list = super.worldObj.getEntitiesWithinAABB(
            EntityItem.class,
            AxisAlignedBB.getBoundingBox(
                loc.x, loc.y, loc.z, loc.x + 1.0, loc.y + 1.0, loc.z + 1.0
            )
        );

        for (a = 0; a < list.size(); ++a) {
            entity = (EntityItem) list.get(a);
            if (entity instanceof EntityItem && !entity.isDead) {
                entity.motionX = 0.0;
                entity.motionY = 0.0;
                entity.motionZ = 0.0;
                entity.noClip = false;
                entity.fireResistance = 1;
                FXWisp ef = new FXWisp(
                    super.worldObj,
                    (double) ((float) entity.prevPosX),
                    (double) ((float) entity.prevPosY + 0.1F),
                    (double) ((float) entity.prevPosZ),
                    1.0F,
                    this.focus == 0 ? 5 : this.focus
                );
                ef.shrink = true;
                Minecraft.getMinecraft().effectRenderer.addEffect(ef);

                switch (this.orientation) {
                    case 0:
                        this.ejectBoreItems(
                            entity,
                            super.xCoord,
                            super.yCoord + 1,
                            super.zCoord,
                            0.0F,
                            0.1F,
                            0.0F
                        );
                        break;
                    case 1:
                        this.ejectBoreItems(
                            entity,
                            super.xCoord,
                            super.yCoord - 1,
                            super.zCoord,
                            0.0F,
                            -0.1F,
                            0.0F
                        );
                        break;
                    case 2:
                        this.ejectBoreItems(
                            entity,
                            super.xCoord,
                            super.yCoord,
                            super.zCoord + 1,
                            0.0F,
                            0.0F,
                            0.1F
                        );
                        break;
                    case 3:
                        this.ejectBoreItems(
                            entity,
                            super.xCoord,
                            super.yCoord,
                            super.zCoord - 1,
                            0.0F,
                            0.0F,
                            -0.1F
                        );
                        break;
                    case 4:
                        this.ejectBoreItems(
                            entity,
                            super.xCoord + 1,
                            super.yCoord,
                            super.zCoord,
                            0.1F,
                            0.0F,
                            0.0F
                        );
                        break;
                    case 5:
                        this.ejectBoreItems(
                            entity,
                            super.xCoord - 1,
                            super.yCoord,
                            super.zCoord,
                            -0.1F,
                            0.0F,
                            0.0F
                        );
                }
            }
        }
    }

    private void
    ejectBoreItems(EntityItem entity, int x, int y, int z, float mx, float my, float mz) {
        super.worldObj.getTileEntity(x, y, z);
        if (this.conserve
            && this.maxDuration - entity.getEntityItem().stackSize >= this.duration) {
            // TODO: vis smelting stuff
            //float val
            //    = RecipesCrucible.smelting().getSmeltingResult(entity.item, true, true);
            //if (val > 0.0F && val < 2.0F) {
            //    this.duration += entity.item.stackSize;
            //    entity.setDead();
            //    return;
            //}
        }

        if (!placeInForgeContainer(super.worldObj, entity, x, y, z, this.orientation)) {
            entity.setLocationAndAngles(
                (double) x + 0.5 - (double) (mx * 3.0F),
                (double) y + 0.5 - (double) (my * 3.0F),
                (double) z + 0.5 - (double) (mz * 3.0F),
                0.0F,
                0.0F
            );
            entity.motionX = (double) mx;
            entity.motionY = (double) my;
            entity.motionZ = (double) mz;
            super.worldObj.spawnParticle(
                "smoke",
                entity.posX,
                entity.posY,
                entity.posZ,
                0.0,
                0.1 * (double) super.worldObj.rand.nextFloat(),
                0.0
            );
        }
    }

    private boolean minedBlock() {
        int xm = 0;
        int ym = 0;
        int zm = 0;
        double xoff = 0.0;
        double yoff = 0.0;
        double zoff = 0.0;
        int radius = this.area;
        if (this.orientation != 0 && this.orientation != 1) {
            if (this.orientation != 2 && this.orientation != 3) {
                if (this.orientation == 4 || this.orientation == 5) {
                    zm = super.worldObj.rand.nextInt(radius)
                        - super.worldObj.rand.nextInt(radius);
                    ym = super.worldObj.rand.nextInt(radius)
                        - super.worldObj.rand.nextInt(radius);
                    if (this.orientation == 4) {
                        xm = -2;
                        xoff = -1.5;
                    } else {
                        xm = 2;
                        xoff = 1.5;
                    }
                }
            } else {
                xm = super.worldObj.rand.nextInt(radius)
                    - super.worldObj.rand.nextInt(radius);
                ym = super.worldObj.rand.nextInt(radius)
                    - super.worldObj.rand.nextInt(radius);
                if (this.orientation == 2) {
                    zm = -2;
                    zoff = -1.5;
                } else {
                    zm = 2;
                    zoff = 1.5;
                }
            }
        } else {
            xm = super.worldObj.rand.nextInt(radius)
                - super.worldObj.rand.nextInt(radius);
            zm = super.worldObj.rand.nextInt(radius)
                - super.worldObj.rand.nextInt(radius);
            if (this.orientation == 0) {
                ym = -2;
                yoff = -1.5;
            } else {
                ym = 2;
                yoff = 1.5;
            }
        }

        do {
            Block id = super.worldObj.getBlock(
                super.xCoord + xm, super.yCoord + ym, super.zCoord + zm
            );
            int meta = super.worldObj.getBlockMetadata(
                super.xCoord + xm, super.yCoord + ym, super.zCoord + zm
            );
            boolean unb = false;
            if (id != Blocks.air) {
                unb = id.getBlockHardness(
                          this.worldObj,
                          this.xCoord + xm,
                          this.yCoord + ym,
                          this.zCoord + zm
                      )
                    == -1.0F;
            }

            if (id != Blocks.air && !unb && id != Blocks.bedrock && id != Blocks.water
                && id != Blocks.flowing_water) {
                id.dropBlockAsItem(
                    super.worldObj,
                    super.xCoord + xm,
                    super.yCoord + ym,
                    super.zCoord + zm,
                    meta,
                    0
                );
                super.worldObj.setBlockToAir(
                    super.xCoord + xm, super.yCoord + ym, super.zCoord + zm
                );
                super.worldObj.spawnParticle(
                    "explode",
                    (double) ((float) (super.xCoord + xm) + 0.5F),
                    (double) ((float) (super.yCoord + ym) + 0.5F),
                    (double) ((float) (super.zCoord + zm) + 0.5F),
                    -0.0,
                    -0.0,
                    -0.0
                );
                super.worldObj.playSoundEffect(
                    (double) (super.xCoord + xm) + 0.5,
                    (double) (super.yCoord + ym) + 0.5,
                    (double) (super.zCoord + zm) + 0.5,
                    "step.gravel",
                    1.0F,
                    1.0F
                );
                return true;
            }

            if (this.orientation == 0) {
                --ym;
            } else if (this.orientation == 1) {
                ++ym;
            } else if (this.orientation == 2) {
                --zm;
            } else if (this.orientation == 3) {
                ++zm;
            } else if (this.orientation == 4) {
                --xm;
            } else if (this.orientation == 5) {
                ++xm;
            }
        } while (super.yCoord + ym >= 0 && super.yCoord + ym <= 255 && xm <= this.range
                 && xm >= -this.range && zm <= this.range && zm >= -this.range
                 && ym <= this.range && ym >= -this.range);

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.orientation = nbttagcompound.getShort("orientation");
        this.duration = nbttagcompound.getShort("duration");
        this.maxDuration = nbttagcompound.getShort("maxDuration");
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.boreItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1
                = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotBore");
            if (byte0 >= 0 && byte0 < this.boreItemStacks.length) {
                this.boreItemStacks[byte0]
                    = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("orientation", (short) this.orientation);
        nbttagcompound.setShort("duration", (short) this.duration);
        nbttagcompound.setShort("maxDuration", (short) this.maxDuration);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.boreItemStacks.length; ++i) {
            if (this.boreItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotBore", (byte) i);
                this.boreItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public boolean rotate() {
        ++this.orientation;
        if (this.orientation > 5) {
            this.orientation -= 6;
        }

        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.boreItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.boreItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.boreItemStacks[i] != null) {
            ItemStack itemstack1;
            if (this.boreItemStacks[i].stackSize <= j) {
                itemstack1 = this.boreItemStacks[i];
                this.boreItemStacks[i] = null;
                return itemstack1;
            } else {
                itemstack1 = this.boreItemStacks[i].splitStack(j);
                if (this.boreItemStacks[i].stackSize == 0) {
                    this.boreItemStacks[i] = null;
                }

                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.boreItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    // TODO: WTF
    //@Override
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.boreItemStacks[var1] != null) {
            ItemStack var2 = this.boreItemStacks[var1];
            this.boreItemStacks[var1] = null;
            return var2;
        } else {
            return null;
        }
    }

    public static boolean placeInForgeContainer(
        World worldObj, EntityItem entity, int x, int y, int z, int orientation
    ) {
        TileEntity te = worldObj.getTileEntity(x, y, z);
        // TODO: buildcraft
        //if (te instanceof ISpecialInventory) {
        //    boolean redo = false;

        //    do {
        //        ISpecialInventory si = (ISpecialInventory) te;
        //        if (si.addItem(entity.item, true, Orientations.Unknown)) {
        //            if (entity.item.stackSize == 0) {
        //                entity.setDead();
        //                return true;
        //            }

        //            redo = true;
        //        }
        //    } while (redo);
        //}

        //if (te instanceof IPipeEntry) {
        //    IPipeEntry pe = (IPipeEntry) te;
        //    if (pe.acceptItems()) {
        //        Orientations or = Orientations.Unknown;
        //        switch (orientation) {
        //            case 0:
        //                or = Orientations.YPos;
        //                --y;
        //                break;
        //            case 1:
        //                or = Orientations.YNeg;
        //                ++y;
        //                break;
        //            case 2:
        //                or = Orientations.ZPos;
        //                --z;
        //                break;
        //            case 3:
        //                or = Orientations.ZNeg;
        //                ++z;
        //                break;
        //            case 4:
        //                or = Orientations.XPos;
        //                --x;
        //                break;
        //            case 5:
        //                or = Orientations.XNeg;
        //                ++x;
        //        }

        //        EntityPassiveItem epi = new EntityPassiveItem(
        //            worldObj, (double) x, (double) y, (double) z, entity.item
        //        );
        //        epi.posX += 0.5;
        //        epi.posY += 0.5;
        //        epi.posZ += 0.5;
        //        pe.entityEntering(epi, or);
        //        entity.setDead();
        //        return true;
        //    }
        //}

        if (te instanceof IInventory) {
            IInventory ii = (IInventory) te;
            if (putIntoChest(x, y, z, ii, entity)) {
                return true;
            }

            for (int xx = -1; xx < 2; ++xx) {
                for (int zz = -1; zz < 2; ++zz) {
                    if ((xx != 0 || zz != 0) && (xx == 0 || zz == 0)) {
                        TileEntity te2 = worldObj.getTileEntity(x + xx, y, z + zz);
                        if (te2 instanceof IInventory) {
                            IInventory ii2 = (IInventory) te2;
                            if (putIntoChest(x + xx, y, z + zz, ii2, entity)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean
    putIntoChest(int x, int y, int z, IInventory ii, EntityItem entity) {
        for (int a = 0; a < ii.getSizeInventory(); ++a) {
            if (ii.getStackInSlot(a) == null) {
                ii.setInventorySlotContents(a, entity.getEntityItem());
                entity.setDead();
                return true;
            }

            if (ii.getStackInSlot(a).isItemEqual(entity.getEntityItem())
                && ii.getStackInSlot(a).getMaxStackSize()
                    >= entity.getEntityItem().stackSize
                        + ii.getStackInSlot(a).stackSize) {
                ItemStack var10000 = entity.getEntityItem();
                var10000.stackSize += ii.getStackInSlot(a).stackSize;
                ii.setInventorySlotContents(a, entity.getEntityItem());
                entity.setDead();
                return true;
            }

            if (ii.getStackInSlot(a).isItemEqual(entity.getEntityItem())
                && ii.getStackInSlot(a).getMaxStackSize() > ii.getStackInSlot(a).stackSize
                && ii.getStackInSlot(a).getMaxStackSize()
                    < entity.getEntityItem().stackSize + ii.getStackInSlot(a).stackSize) {
                int diff = entity.getEntityItem().stackSize
                    + ii.getStackInSlot(a).stackSize
                    - ii.getStackInSlot(a).getMaxStackSize();
                ii.getStackInSlot(a).stackSize = ii.getStackInSlot(a).getMaxStackSize();
                entity.getEntityItem().stackSize = diff;
            }
        }

        return false;
    }

    @Override
    public String getInventoryName() {
        return "thaummach:bore";
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
        // TODO: filtering
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side != 0 && side != 1) {
            return new int[] { 0 };
        } else {
            return new int[] { 1 };
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setShort("orientation", (short) this.orientation);
        nbt.setShort("duration", (short) this.duration);
        nbt.setShort("maxDuration", (short) this.maxDuration);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.orientation = nbt.getShort("orientation");
        this.duration = nbt.getShort("duration");
        this.maxDuration = nbt.getShort("maxDuration");
    }
}
