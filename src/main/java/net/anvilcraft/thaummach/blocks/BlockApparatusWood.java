package net.anvilcraft.thaummach.blocks;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.thaummach.particles.FXWisp;
import net.anvilcraft.thaummach.render.BlockApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.wood.CondenserApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.wood.DuplicatorApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.wood.RepairerApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileCondenser;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.anvilcraft.thaummach.tiles.TileRepairer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockApparatusWood extends BlockApparatus {
    public IIcon iconCondenserPart;
    public IIcon iconCondenserSide;
    public IIcon iconCondenserSpeedUpgrade;
    public IIcon iconCondenserTop;

    public IIcon iconDawnTotemBottom;
    public IIcon[] iconsDawnTotemSide;

    public IIcon iconDuskTotemBottom;
    public IIcon[] iconsDuskTotemSide;

    public IIcon iconDuplicatorBottom;
    public IIcon iconDuplicatorFront;
    public IIcon iconDuplicatorInside;
    public IIcon iconDuplicatorSide;

    public IIcon iconRestorerBottom;
    public IIcon iconRestorerSide;
    public IIcon iconRestorerSidePipes;
    public IIcon iconRestorerTop;

    public BlockApparatusWood() {
        super(Material.wood);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeWood);
        this.setBlockName("tcbappwood");
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        Function<String, IIcon> reg = (s) -> register.registerIcon("thaummach:" + s);

        this.iconCondenserPart = reg.apply("condenser_part");
        this.iconCondenserSide = reg.apply("condenser_side");
        this.iconCondenserSpeedUpgrade = reg.apply("condenser_speed_upgrade");
        this.iconCondenserTop = reg.apply("condenser_top");

        this.iconDawnTotemBottom = reg.apply("dawn_totem_bottom");
        this.iconsDawnTotemSide = IntStream.rangeClosed(1, 6)
                                      .mapToObj((i) -> reg.apply("dawn_totem_side_" + i))
                                      .toArray(IIcon[] ::new);

        this.iconDuskTotemBottom = reg.apply("dusk_totem_bottom");
        this.iconsDuskTotemSide = IntStream.rangeClosed(1, 6)
                                      .mapToObj((i) -> reg.apply("dusk_totem_side_" + i))
                                      .toArray(IIcon[] ::new);

        this.iconDuplicatorBottom = reg.apply("duplicator_bottom");
        this.iconDuplicatorFront = reg.apply("duplicator_front");
        this.iconDuplicatorInside = reg.apply("duplicator_inside");
        this.iconDuplicatorSide = reg.apply("duplicator_side");

        this.iconRestorerBottom = reg.apply("restorer_bottom");
        this.iconRestorerSide = reg.apply("restorer_side");
        this.iconRestorerSidePipes = reg.apply("restorer_side_pipes");
        this.iconRestorerTop = reg.apply("restorer_top");
    }

    @Override
    public IApparatusRenderer getApparatusRenderer(int meta) {
        switch (MetaVals.get(meta)) {
            case CONDENSER:
                return CondenserApparatusRenderer.INSTANCE;

            case REPAIRER:
                return RepairerApparatusRenderer.INSTANCE;

            case DUPLICATOR:
                return DuplicatorApparatusRenderer.INSTANCE;

            default:
                //throw AlecUnexpectedRuntimeErrorExceptionFactory.PLAIN.createAlecException();
                return null;
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List itemList) {
        IntStream.rangeClosed(0, 4)
            .mapToObj((m) -> new ItemStack(this, 1, m))
            .forEach(itemList::add);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        MetaVals md = MetaVals.get(meta);

        switch (md) {
            case CONDENSER:
                return new TileCondenser();

            case REPAIRER:
                return new TileRepairer();

            case DUPLICATOR:
                return new TileDuplicator();

                //case REPAIRER:
                //    return new TileRepairer();

            default:
                return null;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        int md = iblockaccess.getBlockMetadata(i, j, k);
        if (md == 0) {
            float w3 = 0.1875F;
            this.setBlockBounds(w3, 0.0F, w3, 1.0F - w3, 1.0F, 1.0F - w3);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        int md = w.getBlockMetadata(i, j, k);
        if (md == 0) {
            float w3 = 0.1875F;
            AxisAlignedBB.getBoundingBox(
                (double) w3, 0.0, (double) w3, (double) (1.0F - w3), 1.0, (double) (1.0F - w3)
            );
        } else if (md == 10) {
            AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        }

        return super.getSelectedBoundingBoxFromPool(w, i, j, k);
    }

    @Override
    public void onBlockPlacedBy(
        World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack stack
    ) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(x, y, z));
        if (md == MetaVals.DUPLICATOR) {
            int l
                = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5)
                & 3;
            TileDuplicator td = (TileDuplicator) world.getTileEntity(x, y, z);
            td.orientation = l;
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (!world.isRemote) {
            MetaVals l = MetaVals.get(world.getBlockMetadata(i, j, k));
            if (l == MetaVals.DAWN_TOTEM || l == MetaVals.DUSK_TOTEM) {
                // TODO: totems
                //int auraX = i >> 4;
                //int auraZ = k >> 4;
                //SIAuraChunk ac = (SIAuraChunk) mod_ThaumCraft.AuraHM.get(
                //    Arrays.asList(auraX, auraZ, ThaumCraftCore.getDimension(world))
                //);
                //if (ac != null) {
                //    if (l == 3) {
                //        ac.goodVibes = (short) (ac.goodVibes + 1 + random.nextInt(2));
                //        ThaumCraftCore.decreaseTaintedPlantsInArea(world, i, j, k, 2);
                //    } else {
                //        ac.badVibes = (short) (ac.badVibes + 1 + random.nextInt(3));
                //        ThaumCraftCore.increaseTaintedPlants(world, i, j, k);
                //    }
                //}
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        super.onNeighborBlockChange(world, i, j, k, l);
        MetaVals meta = MetaVals.get(world.getBlockMetadata(i, j, k));
        if (meta == MetaVals.CONDENSER) {
            if (!world.isAirBlock(i, j + 1, k)) {
                this.dropBlockAsItem(world, i, j, k, meta.ordinal(), 0);
                world.setBlockToAir(i, j, k);
            }
        }
    }

    @Override
    public int getRenderType() {
        return BlockApparatusRenderer.RI;
    }

    // TODO: WTF
    //@Override
    //public int getBlockTextureFromSide(int i) {
    //    return 15;
    //}

    // TODO: textures
    //@Override
    //public int getBlockTextureFromSideAndMetadata(int i, int j) {
    //    switch (j) {
    //        case 3:
    //            return 127;
    //        case 4:
    //            return 143;
    //        default:
    //            return super.getBlockTextureFromSideAndMetadata(i, j);
    //    }
    //}

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        MetaVals md = MetaVals.get(iblockaccess.getBlockMetadata(i, j, k));
        if (md == MetaVals.CONDENSER) {
            return l <= 1 ? this.iconCondenserTop : this.iconCondenserSide;
        } else if (md == MetaVals.DUPLICATOR) {
            if (l <= 1) {
                return this.iconDuplicatorBottom;
            } else {
                TileEntity te = iblockaccess.getTileEntity(i, j, k);
                if (te != null && te instanceof TileDuplicator) {
                    if (((TileDuplicator) te).orientation == 0 && l == 2) {
                        return this.iconDuplicatorFront;
                    }

                    if (((TileDuplicator) te).orientation == 1 && l == 5) {
                        return this.iconDuplicatorFront;
                    }

                    if (((TileDuplicator) te).orientation == 2 && l == 3) {
                        return this.iconDuplicatorFront;
                    }

                    if (((TileDuplicator) te).orientation == 3 && l == 4) {
                        return this.iconDuplicatorFront;
                    }
                }

                return this.iconDuplicatorSide;
            }
        } else if (md == MetaVals.REPAIRER) {
            return l <= 1 ? this.iconRestorerTop : this.iconRestorerSide;
        }
        //else if (md == 3) {
        //    return l <= 1 ? 127 : 121 + Math.abs((i + j + k) % 6);
        //} else if (md == 4) {
        //    return l <= 1 ? 143 : 137 + Math.abs((i + j + k) % 6);
        //} else {
        //    return 15;
        //}

        return null;
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    @Override
    public int getLightValue(IBlockAccess iba, int i, int j, int k) {
        MetaVals md = MetaVals.get(iba.getBlockMetadata(i, j, k));
        return md == MetaVals.DUPLICATOR ? 5 : super.getLightValue(iba, i, j, k);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World w, int i, int j, int k, Random random) {
        MetaVals md = MetaVals.get(w.getBlockMetadata(i, j, k));
        if (random.nextInt(10) == 0) {
            switch (md) {
                case DAWN_TOTEM:
                    Minecraft.getMinecraft().effectRenderer.addEffect(new FXWisp(
                        w,
                        i + random.nextFloat(),
                        j + random.nextFloat(),
                        k + random.nextFloat(),
                        0.5f,
                        0
                    ));
                    break;

                case DUSK_TOTEM:
                    Minecraft.getMinecraft().effectRenderer.addEffect(new FXWisp(
                        w,
                        i + random.nextFloat(),
                        j + random.nextFloat(),
                        k + random.nextFloat(),
                        0.5f,
                        5
                    ));
                    break;

                default:
                    break;
            }
        }
    }

    public static enum MetaVals {
        CONDENSER, // 0
        DUPLICATOR, // 1
        REPAIRER, // 2
        DAWN_TOTEM, // 3
        DUSK_TOTEM; // 4

        public static MetaVals get(int meta) {
            if (meta >= 0 && meta < MetaVals.values().length) {
                return MetaVals.values()[meta];
            }

            return null;
        }
    }
}
