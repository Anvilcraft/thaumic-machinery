package net.anvilcraft.thaummach.tiles;

import net.anvilcraft.thaummach.TMBlocks;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.blocks.BlockApparatusStone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileVoidCube extends TileEntity {
    public byte[] runes = new byte[] { -1, -1, -1, -1 };
    public byte placed = -1;
    private int v = 1;
    private int u = 2;
    private int q = 4;
    private int r = 3;
    private int i = -1;
    private int[][][] BP;

    public TileVoidCube() {
        // _____ ___  ____   ___     __        _______ _____
        //|_   _/ _ \|  _ \ / _ \ _  \ \      / /_   _|  ___|
        //  | || | | | | | | | | (_)  \ \ /\ / /  | | | |_
        //  | || |_| | |_| | |_| |_    \ V  V /   | | |  _|
        //  |_| \___/|____/ \___/(_)    \_/\_/    |_| |_|

        // clang-format off
        this.BP = new int[][][] { { { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i }, { this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i }, { this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i }, { this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i }, { this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.r, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.v, 0, 0, 0, this.v, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, 0, 0, 0, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.r, this.v, this.v, 0, this.u, 0, 0, 0, this.u, 0, this.v, this.v, this.r, this.i }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.i, this.r, this.v, this.v, 0, this.u, 0, 0, 0, this.u, 0, this.v, this.v, this.r, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, 0, 0, 0, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.v, 0, 0, 0, this.v, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.r, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.v, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, 5, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, 0, 0, 0, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, this.q, 0, 0, 0, this.q, 0, 0, this.v, this.r, this.i }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.v, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, this.v }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.i, this.r, this.v, 0, 0, this.q, 0, 0, 0, this.q, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, 0, 0, 0, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, 6, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.v, this.r, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, 0, 0, 0, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, 0, 0, 0, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i }, { this.i, this.i, this.r, this.v, 0, 0, 0, 0, 0, 0, 0, this.v, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i }, { this.i, this.r, this.v, this.v, this.v, this.v, this.v, 0, this.v, this.v, this.v, this.v, this.v, this.r, this.i }, { this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.v, this.v, this.v, this.v, this.v, this.v, this.v, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i } }, { { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.r, this.r, this.r, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.r, this.r, this.r, this.i }, { this.i, this.r, this.r, this.r, this.r, this.r, this.v, 0, this.v, this.r, this.r, this.r, this.r, this.r, this.i }, { this.i, this.r, this.r, this.r, this.r, this.r, this.v, this.v, this.v, this.r, this.r, this.r, this.r, this.r, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i }, { this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.r, this.r, this.r, this.i, this.i, this.i, this.i, this.i, this.i }, { this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i, this.i } } };
        // clang-format on
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.runes = nbttagcompound.getByteArray("runes");
        this.placed = nbttagcompound.getByte("placed");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByteArray("runes", this.runes);
        nbttagcompound.setByte("placed", this.placed);
    }

    public void generate() {
        int x = super.xCoord;
        int y = super.yCoord;
        int z = super.zCoord;

        int a;
        int b;
        int c;
        for (a = -1; a <= 1; ++a) {
            for (b = y - 1; b >= 14; --b) {
                for (c = -1; c <= 1; ++c) {
                    if (a == 0 && c == 0) {
                        super.worldObj.setBlockToAir(x + a, b, z + c);
                    } else if (Math.abs(a) == 1 || Math.abs(c) == 1) {
                        super.worldObj.setBlock(
                            x + a,
                            b,
                            z + c,
                            TMBlocks.apparatusStone,
                            BlockApparatusStone.MetaVals.ELDRITCH_STONE.ordinal(),
                            3
                        );
                    }
                }
            }
        }

        for (a = -7; a <= 7; ++a) {
            for (b = 0; b <= 7; ++b) {
                for (c = -7; c <= 7; ++c) {
                    switch (this.BP[b][a + 7][c + 7]) {
                        case 0:
                            super.worldObj.setBlockToAir(x + a, b + 6, z + c);
                            break;
                        case 1:
                            super.worldObj.setBlock(
                                x + a,
                                b + 6,
                                z + c,
                                TMBlocks.apparatusStone,
                                BlockApparatusStone.MetaVals.ELDRITCH_STONE.ordinal(),
                                3
                            );
                            break;
                        case 2:
                            if (super.worldObj.rand.nextInt(4) != 0) {
                                super.worldObj.setBlock(
                                    x + a,
                                    b + 6,
                                    z + c,
                                    TMBlocks.apparatusMetal,
                                    BlockApparatusMetal.MetaVals.VOID_CHEST.ordinal(),
                                    3
                                );
                                generateEldritchChestContents(super.worldObj, x + a, b + 6, z + c);
                            } else {
                                super.worldObj.setBlockToAir(x + a, b + 6, z + c);
                            }
                        case 3:
                            break;
                        case 4:
                            if (!super.worldObj.isAirBlock(x + a, b + 5, z + c)
                                && super.worldObj.rand.nextInt(8) == 0) {
                                super.worldObj.setBlock(
                                    x + a,
                                    b + 6,
                                    z + c,
                                    TMBlocks.apparatusMetal,
                                    BlockApparatusMetal.MetaVals.VOID_INTERFACE.ordinal(),
                                    3
                                );
                                //TileVoidInterface tvi = (TileVoidInterface
                                //) super.worldObj.getTileEntity(x + a, b + 6, z + c);
                                //if (tvi != null) {
                                //    tvi.network = (byte) super.worldObj.rand.nextInt(6);
                                //    SISpecialTile pd = new SISpecialTile(
                                //        x + a,
                                //        b + 6,
                                //        z + c,
                                //        tvi.network,
                                //        (byte)
                                //        ModLoader.getMinecraftInstance().thePlayer.dimension,
                                //        (byte) 1
                                //    );
                                //    mod_ThaumCraft.AddSpecialTileToList(pd);
                                //    tvi.invalidateLinks();
                                //    tvi.establishLinks();
                                //}
                                break;
                            }

                            super.worldObj.setBlockToAir(x + a, b + 6, z + c);
                            break;
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            super.worldObj.setBlock(
                                x + a, b + 6, z + c, TMBlocks.hidden, 4, 3 // TODO: MetaVals
                            );
                            //TileVoidLock tvl = (TileVoidLock
                            //) super.worldObj.getBlockTileEntity(x + a, b + 6, z + c);
                            //tvl.orientation = (byte) (this.BP[b][a + 7][c + 7] - 5);
                    }
                }
            }
        }

        for (a = -1; a <= 1; ++a) {
            for (b = -1; b <= 1; ++b) {
                if (super.worldObj.getBlock(x + a, y, z + b) == TMBlocks.hidden) {
                    // TODO: WTF
                    super.worldObj.setBlockMetadataWithNotify(x + a, y, z + b, 14, 3);
                }

                if (a != 0 || b != 0) {
                    super.worldObj.setBlock(
                        x + a,
                        y,
                        z + b,
                        TMBlocks.apparatusStone,
                        BlockApparatusStone.MetaVals.ELDRITCH_STONE.ordinal(),
                        3
                    );
                }
            }
        }
    }

    public static void generateEldritchChestContents(World w, int x, int y, int z) {
        // TODO: loot
        //Map loot = new HashMap();
        //int count = 0;
        //boolean added = false;

        //int a;
        //int b;
        //for (a = 0; a < 100; ++a) {
        //    for (b = 0; b < 2; ++b) {
        //        loot.put(count, new ItemStack(mod_ThaumCraft.itemArtifactEldritch, 1, b));
        //        ++count;
        //    }
        //}

        //for (a = 0; a < 33; ++a) {
        //    for (b = 2; b < 4; ++b) {
        //        loot.put(count, new ItemStack(mod_ThaumCraft.itemArtifactEldritch, 1, b));
        //        ++count;
        //    }
        //}

        //for (a = 0; a < 11; ++a) {
        //    loot.put(count, new ItemStack(mod_ThaumCraft.itemArtifactEldritch, 1, 4));
        //    ++count;
        //}

        //for (a = 0; a < 5; ++a) {
        //    loot.put(count, new ItemStack(mod_ThaumCraft.itemArtifactEldritch, 1, 5));
        //    ++count;
        //}

        //for (a = 0; a < 17; ++a) {
        //    loot.put(count, new ItemStack(mod_ThaumCraft.itemComponents, 1 + w.rand.nextInt(2), 8));
        //    ++count;
        //}

        //for (a = 0; a < 17; ++a) {
        //    loot.put(
        //        count, new ItemStack(mod_ThaumCraft.itemComponents, 1 + w.rand.nextInt(2), 11)
        //    );
        //    ++count;
        //}

        //for (a = 0; a < 25; ++a) {
        //    loot.put(count, new ItemStack(mod_ThaumCraft.blockAppStone, 1 + w.rand.nextInt(4), 5));
        //    ++count;
        //}

        //TileEntity te = w.getBlockTileEntity(x, y, z);
        //if (te != null && te instanceof TileVoidChest) {
        //    TileVoidChest tec = (TileVoidChest) te;

        //    for (int a = 0; a < tec.getSizeInventory(); ++a) {
        //        if (tec.getStackInSlot(a) == null) {
        //            int chance = w.rand.nextInt(count * 6);
        //            tec.setInventorySlotContents(a, (ItemStack) loot.get(chance));
        //        }
        //    }
        //}
    }
}
