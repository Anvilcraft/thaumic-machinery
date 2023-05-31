package net.anvilcraft.thaummach.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.anvilcraft.alec.jalec.factories.AlecUnexpectedRuntimeErrorExceptionFactory;
import net.anvilcraft.thaummach.render.BlockApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.SimpleBlockApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockApparatusStone extends BlockApparatus {
    public IIcon iconEldritchStone;

    public BlockApparatusStone() {
        super(Material.rock);
        this.setHardness(2.0F);
        this.setResistance(15.0F);
        this.setStepSound(Block.soundTypeStone);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        Function<String, IIcon> reg = (s) -> register.registerIcon("thaummach:" + s);

        this.iconEldritchStone = reg.apply("eldritch_stone");
    }

    @Override
    public IApparatusRenderer getApparatusRenderer(int meta_) {
        MetaVals meta = MetaVals.get(meta_);
        switch (meta) {
            case ELDRITCH_STONE:
                return SimpleBlockApparatusRenderer.INSTANCE;

            default:
                return null;
        }
    }

    @Override
    public boolean onBlockActivated(
        World world,
        int i,
        int j,
        int k,
        EntityPlayer entityplayer,

        // useless parameters
        int alec1,
        float alec2,
        float alec3,
        float alec4
    ) {
        TileEntity te = world.getTileEntity(i, j, k);
        int meta = world.getBlockMetadata(i, j, k);
        //if (meta == 8 && entityplayer.inventory.getCurrentItem() != null) {
        //    boolean filled = false;
        //    if (entityplayer.inventory.getCurrentItem().isItemEqual(new ItemStack(Items.bucket)
        //        )) {
        //        entityplayer.inventory.setInventorySlotContents(
        //            entityplayer.inventory.currentItem, new ItemStack(Items.water_bucket)
        //        );
        //        filled = true;
        //    } else if (entityplayer.inventory.getCurrentItem().isItemEqual(
        //                   new ItemStack(Items.glass_bottle)
        //               )) {
        //        if (entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1)
        //            == null) {
        //            entityplayer.inventory.setInventorySlotContents(
        //                entityplayer.inventory.currentItem, new ItemStack(Items.potionitem)
        //            );
        //        } else if (!entityplayer.inventory.addItemStackToInventory(new
        //        ItemStack(Item.potionitem
        //                   ))) {
        //            entityplayer.dropPlayerItem(new ItemStack(Item.potion.shiftedIndex, 1, 0));
        //        }

        //        filled = true;
        //    } else {
        //        ItemStack filler = this.getOtherWaterContainer(entityplayer.inventory);
        //        if (filler != null) {
        //            entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
        //            if (!entityplayer.inventory.addItemStackToInventory(
        //                    new ItemStack(filler.getItem(), 1, filler.getItemDamage())
        //                )) {
        //                entityplayer.dropPlayerItem(
        //                    new ItemStack(filler.getItem(), 1, filler.getItemDamage())
        //                );
        //            }

        //            filled = true;
        //        }
        //    }

        //    if (filled) {
        //        int auraX = i >> 4;
        //        int auraZ = k >> 4;
        //        SIAuraChunk ac = (SIAuraChunk) mod_ThaumCraft.AuraHM.get(
        //            Arrays.asList(auraX, auraZ, ThaumCraftCore.getDimension(world))
        //        );
        //        if (ac != null) {
        //            ++ac.badVibes;
        //        }

        //        return true;
        //    }

        //}

        return super.onBlockActivated(world, i, j, k, entityplayer, alec1, alec2, alec3, alec4);
    }

    // TODO: WTF
    //private ItemStack getOtherWaterContainer(InventoryPlayer inventory) {
    //    if (ItemInterface.getItem("waxCapsule") != null
    //        && inventory.getCurrentItem().isItemEqual(ItemInterface.getItem("waxCapsule"))) {
    //        return ItemInterface.getItem("waxCapsuleWater");
    //    } else if (ItemInterface.getItem("refractoryEmpty") != null &&
    //    inventory.getCurrentItem().isItemEqual(ItemInterface.getItem("refractoryEmpty"))) {
    //        return ItemInterface.getItem("refractoryWater");
    //    } else if (ItemInterface.getItem("canEmpty") != null &&
    //    inventory.getCurrentItem().isItemEqual(ItemInterface.getItem("canEmpty"))) {
    //        return ItemInterface.getItem("canWater");
    //    } else {
    //        return Items.getItem("cell") != null
    //                && inventory.getCurrentItem().isItemEqual(Items.getItem("cell"))
    //            ? ItemInterface.getItem("waterCell")
    //            : null;
    //    }
    //}

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        MetaVals meta = MetaVals.get(world.getBlockMetadata(x, y, z));
        return meta == MetaVals.ELDRITCH_STONE ? 10.0f : super.getBlockHardness(world, x, y, z);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int md) {
        MetaVals meta = MetaVals.get(md);

        switch (meta) {
            case ELDRITCH_STONE:
                return null;

            default:
                throw AlecUnexpectedRuntimeErrorExceptionFactory.PLAIN.createAlecException(
                    "Invalid meta!"
                );
        }

        //if (md == 1) {
        //    return new TileInfuser();
        //} else if (md == 2) {
        //    return new TileInfuserDark();
        //} else if (md == 3) {
        //    return new TileEnchanter();
        //} else if (md == 4) {
        //    return new TileResearcher();
        //} else if (md == 6) {
        //    return new TileDarknessGenenerator();
        //} else if (md == 7) {
        //    return new TileEnchanterAdvanced();
        //} else {
        //    return (TileEntity) (md == 8 ? new TileUrn() : super.getBlockEntity(md));
        //}
    }

    @Override
    public int getRenderType() {
        return BlockApparatusRenderer.RI;
    }

    // TODO: WTF
    //public int getBlockTextureFromSide(int i) {
    //    return 31;
    //}

    @Override
    public IIcon getIcon(int i, int j) {
        MetaVals meta = MetaVals.get(j);

        switch (meta) {
            case ELDRITCH_STONE:
                return this.iconEldritchStone;

            default:
                return null;
        }

        //if (j == 0) {
        //    return 46;
        //} else if (j == 4) {
        //    return 93;
        //} else if (j == 6) {
        //    return i < 2 ? 105 : 108;
        //} else if (j == 8) {
        //    if (i == 0) {
        //        return 159;
        //    } else {
        //        return i == 1 ? 158 : 157;
        //    }
        //} else {
        //    return super.getBlockTextureFromSideAndMetadata(i, j);
        //}
    }

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        int meta = iblockaccess.getBlockMetadata(i, j, k);
        //if (meta == 3) {
        //    if (side == 0) {
        //        return 162;
        //    } else {
        //        return side == 1 ? 161 : 160;
        //    }
        //} else if (meta == 7) {
        //    if (side == 0) {
        //        return 162;
        //    } else {
        //        return side == 1 ? 164 : 163;
        //    }
        //} else {
        //    TileInfuser ti;
        //    HelperLocation loc;
        //    TileEntity te;
        //    if (meta == 1) {
        //        if (side == 0) {
        //            return 53;
        //        } else if (side == 1) {
        //            return 54;
        //        } else {
        //            ti = (TileInfuser) iblockaccess.getBlockTileEntity(i, j, k);
        //            loc = new HelperLocation(ti);
        //            switch (side) {
        //                case 2:
        //                    loc.facing = HelperFacing.NEGZ;
        //                    break;
        //                case 3:
        //                    loc.facing = HelperFacing.POSZ;
        //                    break;
        //                case 4:
        //                    loc.facing = HelperFacing.NEGX;
        //                    break;
        //                case 5:
        //                    loc.facing = HelperFacing.POSX;
        //            }

        //            if (!ti.getConnectable(loc.facing)) {
        //                return 55;
        //            } else {
        //                te = loc.getConnectableTile(iblockaccess);
        //                return te != null ? 56 : 55;
        //            }
        //        }
        //    } else if (meta == 2) {
        //        if (side == 0) {
        //            return 59;
        //        } else if (side == 1) {
        //            return 60;
        //        } else {
        //            ti = (TileInfuser) iblockaccess.getBlockTileEntity(i, j, k);
        //            loc = new HelperLocation(ti);
        //            switch (side) {
        //                case 2:
        //                    loc.facing = HelperFacing.NEGZ;
        //                    break;
        //                case 3:
        //                    loc.facing = HelperFacing.POSZ;
        //                    break;
        //                case 4:
        //                    loc.facing = HelperFacing.NEGX;
        //                    break;
        //                case 5:
        //                    loc.facing = HelperFacing.POSX;
        //            }

        //            if (!ti.getConnectable(loc.facing)) {
        //                return 61;
        //            } else {
        //                te = loc.getConnectableTile(iblockaccess);
        //                return te != null ? 62 : 61;
        //            }
        //        }
        //    } else if (meta == 4) {
        //        if (side == 0) {
        //            return 93;
        //        } else {
        //            return side == 1 ? 91 : 92;
        //        }
        //    } else {
        //        return super.getBlockTexture(iblockaccess, i, j, k, side);
        //    }
        //}
        return this.getIcon(side, meta);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(Item arg0, CreativeTabs arg1, List list) {
        Arrays.stream(MetaVals.values())
            .map((m) -> new ItemStack(this, 1, m.ordinal()))
            .forEach(list::add);
    }

    @Override
    public void addCollisionBoxesToList(
        World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity
    ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0) {
            TileEntity te = world.getTileEntity(x, y, z);
            int l = 1;
            if (te != null && te instanceof TileSeal) {
                l = ((TileSeal) te).orientation;
            }

            float thickness = 0.0625F;
            if (l == 0) {
                this.setBlockBounds(0.3F, 1.0F - thickness, 0.3F, 0.7F, 1.0F, 0.7F);
            }

            if (l == 1) {
                this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, thickness, 0.7F);
            }

            if (l == 2) {
                this.setBlockBounds(0.3F, 0.3F, 1.0F - thickness, 0.7F, 0.7F, 1.0F);
            }

            if (l == 3) {
                this.setBlockBounds(0.3F, 0.3F, 0.0F, 0.7F, 0.7F, thickness);
            }

            if (l == 4) {
                this.setBlockBounds(1.0F - thickness, 0.3F, 0.3F, 1.0F, 0.7F, 0.7F);
            }

            if (l == 5) {
                this.setBlockBounds(0.0F, 0.3F, 0.3F, thickness, 0.7F, 0.7F);
            }

            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        } else if (meta == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        } else if (meta == 4) {
            this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.375F, 0.8F);
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        } else if (meta != 1 && meta != 2) {
            if (meta == 8) {
                this.setBlockBounds(0.3125F, 0.5625F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.5625F, 0.875F);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            } else {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                this.setBlockBoundsForItemRender();
            }
        } else {
            float w1 = 0.0625F;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F - w1, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        int md = iblockaccess.getBlockMetadata(i, j, k);
        if (md == 0) {
            if (iblockaccess.getTileEntity(i, j, k) == null) {
                return;
            }

            int l = ((TileSeal) ((TileSeal) iblockaccess.getTileEntity(i, j, k))).orientation;
            float thickness = 0.0625F;
            if (l == 0) {
                this.setBlockBounds(0.3F, 1.0F - thickness, 0.3F, 0.7F, 1.0F, 0.7F);
            }

            if (l == 1) {
                this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, thickness, 0.7F);
            }

            if (l == 2) {
                this.setBlockBounds(0.3F, 0.3F, 1.0F - thickness, 0.7F, 0.7F, 1.0F);
            }

            if (l == 3) {
                this.setBlockBounds(0.3F, 0.3F, 0.0F, 0.7F, 0.7F, thickness);
            }

            if (l == 4) {
                this.setBlockBounds(1.0F - thickness, 0.3F, 0.3F, 1.0F, 0.7F, 0.7F);
            }

            if (l == 5) {
                this.setBlockBounds(0.0F, 0.3F, 0.3F, thickness, 0.7F, 0.7F);
            }
        } else if (md == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        } else {
            float w1;
            if (md != 1 && md != 2) {
                if (md == 6) {
                    w1 = 0.0625F;
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F - w1, 1.0F);
                } else if (md == 4) {
                    this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
                } else if (md == 8) {
                    this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.5625F, 0.875F);
                } else {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            } else {
                w1 = 0.0625F;
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F - w1, 1.0F);
            }
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        int md = w.getBlockMetadata(i, j, k);
        if (md == 3) {
            AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
        } else {
            float w1;
            if (md != 1 && md != 2) {
                if (md == 4) {
                    AxisAlignedBB.getBoundingBox(0.0, 0.375, 0.0, 1.0, 0.625, 1.0);
                } else if (md == 6) {
                    w1 = 0.0625F;
                    AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, (double) (0.5F - w1), 1.0);
                } else if (md == 8) {
                    AxisAlignedBB.getBoundingBox(0.3125, 0.5625, 0.3125, 0.6875, 1.0, 0.6875);
                    AxisAlignedBB.getBoundingBox(0.125, 0.0, 0.125, 0.875, 0.5625, 0.875);
                } else {
                    AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                }
            } else {
                w1 = 0.0625F;
                AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, (double) (1.0F - w1), 1.0);
            }
        }

        return super.getSelectedBoundingBoxFromPool(w, i, j, k);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void randomDisplayTick(World w, int i, int j, int k, Random r) {
        //int meta = w.getBlockMetadata(i, j, k);
        //if ((meta == 3 || meta == 4 || meta == 7) && r.nextBoolean()) {
        //    for (int ll = i - 2; ll <= i + 2; ++ll) {
        //        for (int i1 = k - 2; i1 <= k + 2; ++i1) {
        //            if (ll > i - 2 && ll < i + 2 && i1 == k - 1) {
        //                i1 = k + 2;
        //            }

        //            for (int j1 = j; j1 <= j + 1; ++j1) {
        //                if ((w.getBlockId(ll, j1, i1) == Block.bookShelf.blockID
        //                     || w.getBlockId(ll, j1, i1) == mod_ThaumCraft.blockAppFragile.blockID
        //                         && w.getBlockMetadata(ll, j1, i1) == 4)
        //                    && (w.getBlockId(ll, j1, i1) != Block.bookShelf.blockID
        //                        || r.nextInt(16) == 0)
        //                    && (w.getBlockId(ll, j1, i1) != mod_ThaumCraft.blockAppFragile.blockID
        //                        || r.nextInt(8) == 0)) {
        //                    if (!w.isAirBlock((ll - i) / 2 + i, j1, (i1 - k) / 2 + k)) {
        //                        break;
        //                    }

        //                    w.spawnParticle(
        //                        "enchantmenttable",
        //                        (double) i + 0.5,
        //                        (double) j + 2.0,
        //                        (double) k + 0.5,
        //                        (double) ((float) (ll - i) + r.nextFloat()) - 0.5,
        //                        (double) ((float) (j1 - j) - r.nextFloat() - 1.0F),
        //                        (double) ((float) (i1 - k) + r.nextFloat()) - 0.5
        //                    );
        //                }
        //            }
        //        }
        //    }
        //}
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(x, y, z));
        switch (md) {
            case ENCHANTER:
            case URN:
                return false;

            default:
                return true;
        }
    }

    //public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
    //    if (l == 0 && world.isBlockSolidOnSide(i, j + 1, k, 0)) {
    //        return true;
    //    } else if (l == 1 && world.isBlockSolidOnSide(i, j - 1, k, 1)) {
    //        return true;
    //    } else if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
    //        return true;
    //    } else if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
    //        return true;
    //    } else if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
    //        return true;
    //    } else {
    //        return l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5);
    //    }
    //}

    //public boolean canPlaceBlockAt(World world, int i, int j, int k) {
    //    if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
    //        return true;
    //    } else if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
    //        return true;
    //    } else if (world.isBlockSolidOnSide(i, j, k - 1, 3)) {
    //        return true;
    //    } else if (world.isBlockSolidOnSide(i, j, k + 1, 2)) {
    //        return true;
    //    } else {
    //        return world.isBlockSolidOnSide(i, j - 1, k, 1)
    //            ? true
    //            : world.isBlockSolidOnSide(i, j + 1, k, 0);
    //    }
    //}

    private boolean checkIfAttachedToBlock(World world, int i, int j, int k) {
        if (!this.canPlaceBlockAt(world, i, j, k)) {
            this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getLightValue(IBlockAccess iba, int i, int j, int k) {
        MetaVals md = MetaVals.get(iba.getBlockMetadata(i, j, k));
        return md == MetaVals.ELDRITCH_STONE ? 12 : super.getLightValue(iba, i, j, k);
    }

    @Override
    public float getExplosionResistance(
        Entity exploder, World world, int x, int y, int z, double srcX, double srcY, double srcZ
    ) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(x, y, z));
        return md == MetaVals.ELDRITCH_STONE ? 100.0F : 15.0F;
    }

    public static enum MetaVals {
        __SEAL, // 0
        INFUSER, // 1
        INFUSER_DARK, // 2
        ENCHANTER, // 3
        __RESEARCHER, // 4
        ELDRITCH_STONE, // 5
        DARKNESS_GENERATOR, // 6
        ENCHANTER_ADVANCED, // 7
        URN; // 8

        public static MetaVals get(int meta) {
            if (meta < 0 || meta >= MetaVals.values().length)
                return null;

            return MetaVals.values()[meta];
        }
    }
}
