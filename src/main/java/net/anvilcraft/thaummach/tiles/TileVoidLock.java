package net.anvilcraft.thaummach.tiles;

import net.anvilcraft.thaummach.TMBlocks;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.blocks.BlockApparatusStone;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileVoidLock extends TileEntity {
    public byte orientation = -1;
    public int unlocked = -1;
    private int v = 1;
    private int u = 2;
    private int m = 3;
    private int i = -1;
    private int[][][] BP;

    public TileVoidLock() {
        // _____ ___  ____   ___     __        _______ _____
        //|_   _/ _ \|  _ \ / _ \ _  \ \      / /_   _|  ___|
        //  | || | | | | | | | | (_)  \ \ /\ / /  | | | |_
        //  | || |_| | |_| | |_| |_    \ V  V /   | | |  _|
        //  |_| \___/|____/ \___/(_)    \_/\_/    |_| |_|

        // clang-format off
        this.BP = new int[][][]{{{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}, {{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, 0, 0, 0, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, this.u, 0, 0, 0, 0, 0, this.u, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, this.u, 0, 0, 0, 0, 0, this.u, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, 0, 0, 0, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}, {{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, this.m, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}, {{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, this.m, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}, {{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, this.m, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}, {{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, 0, 0, 0, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, 0, 0, 0, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}, {{this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.i}, {this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i}}};
        // clang-format on
    }

    @Override
    public void updateEntity() {
        if (this.unlocked > 0) {
            int x = super.xCoord;
            int y = super.yCoord;
            int z = super.zCoord;
            switch (this.orientation) {
                case 0:
                    x -= 7;
                    break;
                case 1:
                    x += 7;
                    break;
                case 2:
                    z -= 7;
                    break;
                case 3:
                    z += 7;
            }

            super.worldObj.playSoundEffect(
                (double) x, (double) y, (double) z, "thaummach:rumble", 4.0F, 1.0F
            );

            int b;
            int alec;
            for (int a = -7; a <= 7; ++a) {
                for (alec = 0; alec <= 6; ++alec) {
                    for (b = -7; b <= 7; ++b) {
                        if (super.worldObj.getBlock(x + a, alec + 6, z + b) == TMBlocks.hidden) {
                            super.worldObj.setBlockMetadataWithNotify(
                                x + a, alec + 6, z + b, 14, 3
                            );
                        }

                        switch (this.BP[a][alec + 7][b + 7]) {
                            case 0:
                                super.worldObj.setBlockToAir(x + a, alec + 6, z + b);
                                break;
                            case 1:
                                super.worldObj.setBlock(
                                    x + a,
                                    alec + 6,
                                    z + b,
                                    TMBlocks.apparatusStone,
                                    BlockApparatusStone.MetaVals.ELDRITCH_STONE.ordinal(),
                                    3
                                );
                                break;
                            case 2:
                                if (super.worldObj.rand.nextInt(3) == 0) {
                                    super.worldObj.setBlock(
                                        x + a,
                                        alec + 6,
                                        z + b,
                                        TMBlocks.apparatusMetal,
                                        BlockApparatusMetal.MetaVals.VOID_CHEST.ordinal(),
                                        3
                                    );
                                    TileVoidCube.generateEldritchChestContents(
                                        super.worldObj, x + a, alec + 6, z + b
                                    );
                                    // TODO: LOOT
                                    //TileEntity te
                                    //    = super.worldObj.getTileEntity(x + a, alec + 6, z + b);
                                    //if (te != null && te instanceof TileVoidChest) {
                                    //    TileVoidChest tec = (TileVoidChest) te;
                                    //    if (super.worldObj.rand.nextInt(4) == 0) {
                                    //        tec.setInventorySlotContents(
                                    //            2 + tec.getSizeInventory() / 2,
                                    //            new ItemStack(mod_ThaumCraft.itemVoidCompass)
                                    //        );
                                    //    }

                                    //    if (super.worldObj.rand.nextInt(4) == 0) {
                                    //        tec.setInventorySlotContents(
                                    //            4 + tec.getSizeInventory() / 2,
                                    //            new ItemStack(mod_ThaumCraft.itemVoidCrusher)
                                    //        );
                                    //    }

                                    //    if (super.worldObj.rand.nextInt(4) == 0) {
                                    //        tec.setInventorySlotContents(
                                    //            6 + tec.getSizeInventory() / 2,
                                    //            new ItemStack(mod_ThaumCraft.itemVoidCutter)
                                    //        );
                                    //    }
                                    //}
                                } else {
                                    super.worldObj.setBlockToAir(x + a, alec + 6, z + b);
                                }
                                break;
                            case 3:
                                super.worldObj.setBlock(
                                    x + a, alec + 6, z + b, TMBlocks.hidden, 2, 3
                                );
                        }
                    }
                }
            }

            x = super.xCoord;
            y = super.yCoord;
            z = super.zCoord;
            boolean tt = false;
            switch (this.orientation) {
                case 0:
                    --x;
                    tt = true;
                    break;
                case 1:
                    ++x;
                    tt = true;
                    break;
                case 2:
                    --z;
                    break;
                case 3:
                    ++z;
            }

            int c;
            for (alec = -2; alec <= 2; ++alec) {
                for (b = -2; b <= 2; ++b) {
                    c = 0;
                    int zs = 0;
                    if (tt) {
                        zs = alec;
                    } else {
                        c = alec;
                    }

                    if (super.worldObj.getBlock(super.xCoord + c, y + b, super.zCoord + zs)
                        == TMBlocks.hidden) {
                        super.worldObj.setBlockMetadataWithNotify(
                            super.xCoord + c, y + b, super.zCoord + zs, 14, 3
                        );
                    }

                    super.worldObj.setBlock(
                        super.xCoord + c, y + b, super.zCoord + zs, TMBlocks.apparatusStone, 5, 3
                    );
                }
            }

            for (alec = -1; alec <= 1; ++alec) {
                for (b = -1; b <= 1; ++b) {
                    for (c = -1; c <= 1; ++c) {
                        if (super.worldObj.getBlock(x + alec, y + b, z + c)
                            == TMBlocks.hidden) {
                            super.worldObj.setBlockMetadataWithNotify(x + alec, y + b, z + c, 14, 3);
                        }

                        super.worldObj.setBlockToAir(x + alec, y + b, z + c);
                        UtilsFX.poofBad(
                            super.worldObj, (float) (x + alec), (float) (y + b), (float) (z + c)
                        );
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.orientation = nbttagcompound.getByte("orientation");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("orientation", this.orientation);
    }
}
