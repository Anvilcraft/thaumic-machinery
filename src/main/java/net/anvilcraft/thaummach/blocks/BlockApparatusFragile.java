package net.anvilcraft.thaummach.blocks;

import java.util.List;
import java.util.Random;

import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.client.FXSparkle;
import net.anvilcraft.thaummach.AuraUtils;
import net.anvilcraft.thaummach.render.BlockApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.ConduitApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.ConduitPumpApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.ConduitTankApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.ConduitValveAdvancedApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.ConduitValveApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.FilterApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.fragile.PurifierApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileConduit;
import net.anvilcraft.thaummach.tiles.TileConduitPump;
import net.anvilcraft.thaummach.tiles.TileConduitTank;
import net.anvilcraft.thaummach.tiles.TileConduitValve;
import net.anvilcraft.thaummach.tiles.TileConduitValveAdvanced;
import net.anvilcraft.thaummach.tiles.TileFilter;
import net.anvilcraft.thaummach.tiles.TilePurifier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.fx.particles.FXWisp;

public class BlockApparatusFragile extends BlockApparatus {
    public IIcon iconConduit;
    public IIcon iconConduitConnection;
    public IIcon iconConduitExtension;
    public IIcon iconConduitInventory;
    public IIcon iconConduitPumpSide;
    public IIcon iconConduitPumpTop;
    public IIcon iconFilterBottom;
    public IIcon iconFilterFront;
    public IIcon iconFilterSide;
    public IIcon iconPurifierFront;
    public IIcon iconPurifierSide;
    public IIcon iconPurifierTop;
    public IIcon iconTankBottom;
    public IIcon iconTankSide;
    public IIcon iconTcubeanim;
    public IIcon iconValveAdvancedOff;
    public IIcon iconValveAdvancedOnTaint;
    public IIcon iconValveAdvancedOnVis;
    public IIcon iconValveOff;
    public IIcon iconValveOn;

    public BlockApparatusFragile() {
        super(Material.wood);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeWood);
        this.setTickRandomly(true);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        this.blockIcon = reg.registerIcon("thaummach:apparatus");

        this.iconConduit = reg.registerIcon("thaummach:conduit");
        this.iconConduitConnection = reg.registerIcon("thaummach:conduit_connection");
        this.iconConduitExtension = reg.registerIcon("thaummach:conduit_extension");
        this.iconConduitInventory = reg.registerIcon("thaummach:conduit_inventory");
        this.iconConduitPumpSide = reg.registerIcon("thaummach:conduit_pump_side");
        this.iconConduitPumpTop = reg.registerIcon("thaummach:conduit_pump_top");
        this.iconFilterBottom = reg.registerIcon("thaummach:filter_bottom");
        this.iconFilterFront = reg.registerIcon("thaummach:filter_front");
        this.iconFilterSide = reg.registerIcon("thaummach:filter_side");
        this.iconPurifierFront = reg.registerIcon("thaummach:purifier_front");
        this.iconPurifierSide = reg.registerIcon("thaummach:purifier_side");
        this.iconPurifierTop = reg.registerIcon("thaummach:purifier_top");
        this.iconTankBottom = reg.registerIcon("thaummach:tank_bottom");
        this.iconTankSide = reg.registerIcon("thaummach:tank_side");
        this.iconTcubeanim = reg.registerIcon("thaummach:tcubeanim");
        this.iconValveAdvancedOff
            = reg.registerIcon("thaummach:conduit_valve_advanced_off");
        this.iconValveAdvancedOnTaint
            = reg.registerIcon("thaummach:conduit_valve_advanced_on_taint");
        this.iconValveAdvancedOnVis
            = reg.registerIcon("thaummach:conduit_valve_advanced_on_vis");
        this.iconValveOff = reg.registerIcon("thaummach:conduit_valve_off");
        this.iconValveOn = reg.registerIcon("thaummach:conduit_valve_on");
    }

    @Override
    public IApparatusRenderer getApparatusRenderer(int meta) {
        switch (MetaVals.get(meta)) {
            case CONDUIT:
                return ConduitApparatusRenderer.INSTANCE;

            case FILTER:
                return FilterApparatusRenderer.INSTANCE;

            case CONDUIT_TANK:
                return ConduitTankApparatusRenderer.INSTANCE;

            case CONDUIT_VALVE:
                return ConduitValveApparatusRenderer.INSTANCE;

            case PURIFIER:
                return PurifierApparatusRenderer.INSTANCE;

            case CONDUIT_VALVE_ADVANCED:
                return ConduitValveAdvancedApparatusRenderer.INSTANCE;

            case CONDUIT_PUMP:
                return ConduitPumpApparatusRenderer.INSTANCE;

            default:
                break;
        }
        return null;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        MetaVals meta = MetaVals.get(world.getBlockMetadata(x, y, z));

        if (meta != MetaVals.CONDUIT && meta != MetaVals.CONDUIT_VALVE
            && meta != MetaVals.CONDUIT_VALVE_ADVANCED) {
            return meta == null ? 0.0F : super.getBlockHardness(world, x, y, z);
        } else {
            return 0.25F;
        }
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List itemList) {
        for (MetaVals meta : MetaVals.values()) {
            itemList.add(new ItemStack(this, 1, meta.ordinal()));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta_) {
        MetaVals meta = MetaVals.get(meta_);

        switch (meta) {
            case CONDUIT:
                return new TileConduit();

            case FILTER:
                return new TileFilter();

            case CONDUIT_TANK:
                return new TileConduitTank();

            case CONDUIT_VALVE:
                return new TileConduitValve();

            case PURIFIER:
                return new TilePurifier();

            case CONDUIT_VALVE_ADVANCED:
                return new TileConduitValveAdvanced();

            case CONDUIT_PUMP:
                return new TileConduitPump();

            default:
                return null;
        }
    }

    @Override
    public void
    setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        MetaVals md = MetaVals.get(iblockaccess.getBlockMetadata(i, j, k));
        float w1;
        if (md != MetaVals.CONDUIT && md != MetaVals.CONDUIT_VALVE
            && md != MetaVals.CONDUIT_VALVE_ADVANCED) {
            if (md != MetaVals.FILTER) {
                if (md == MetaVals.CONDUIT_VALVE) {
                    w1 = 0.0625F;
                    this.setBlockBounds(w1, 0.0F, w1, 1.0F - w1, 1.0F, 1.0F - w1);
                } else if (md == null) {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                } else {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            } else {
                w1 = 0.125F;
                this.setBlockBounds(w1, 0.0F, w1, 1.0F - w1, 1.0F, 1.0F - w1);
            }
        } else {
            w1 = 0.25F;
            this.setBlockBounds(w1, w1, w1, 1.0F - w1, 1.0F - w1, 1.0F - w1);
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        MetaVals md = MetaVals.get(w.getBlockMetadata(i, j, k));
        float w1;
        if (md != MetaVals.CONDUIT && md != MetaVals.CONDUIT_VALVE
            && md != MetaVals.CONDUIT_VALVE_ADVANCED) {
            if (md != MetaVals.FILTER) {
                if (md == MetaVals.CONDUIT_TANK) {
                    w1 = 0.0625F;
                    AxisAlignedBB.getBoundingBox(
                        (double) w1,
                        0.0,
                        (double) w1,
                        (double) (1.0F - w1),
                        1.0,
                        (double) (1.0F - w1)
                    );
                } else {
                    AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                }
            } else {
                w1 = 0.125F;
                AxisAlignedBB.getBoundingBox(
                    (double) w1,
                    0.0,
                    (double) w1,
                    (double) (1.0F - w1),
                    1.0,
                    (double) (1.0F - w1)
                );
            }
        } else {
            w1 = 0.25F;
            AxisAlignedBB.getBoundingBox(
                (double) w1,
                (double) w1,
                (double) w1,
                (double) (1.0F - w1),
                (double) (1.0F - w1),
                (double) (1.0F - w1)
            );
        }

        return super.getSelectedBoundingBoxFromPool(w, i, j, k);
    }

    @Override
    public void onBlockPlacedBy(
        World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack is
    ) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(i, j, k));
        int l = MathHelper.floor_double(
                    (double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5
                )
            & 3;
        if (md == MetaVals.PURIFIER) {
            TilePurifier tp = (TilePurifier) world.getTileEntity(i, j, k);
            tp.orientation = l;
            world.markBlockForUpdate(tp.xCoord, tp.yCoord, tp.zCoord);
        }

        if (md == MetaVals.CONDUIT_PUMP) {
            TileConduitPump tb = (TileConduitPump) world.getTileEntity(i, j, k);
            if (MathHelper.abs((float) entityliving.posX - (float) i) < 1.0F
                && MathHelper.abs((float) entityliving.posZ - (float) k) < 1.0F) {
                double d = entityliving.posY + 1.82 - (double) entityliving.yOffset;
                if (d - (double) j > 2.0) {
                    tb.orientation = 1;
                }

                if ((double) j - d > 0.0) {
                    tb.orientation = 0;
                }
            } else {
                if (l == 0) {
                    tb.orientation = 2;
                }

                if (l == 1) {
                    tb.orientation = 5;
                }

                if (l == 2) {
                    tb.orientation = 3;
                }

                if (l == 3) {
                    tb.orientation = 4;
                }
            }
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
        if (!entityplayer.isSneaking()) {
            MetaVals meta = MetaVals.get(world.getBlockMetadata(i, j, k));
            if (meta == MetaVals.CONDUIT_VALVE) {
                if (entityplayer.isSneaking()) {
                    return false;
                } else {
                    if (world.isRemote)
                        return true;

                    TileConduitValve tileentity
                        = (TileConduitValve) world.getTileEntity(i, j, k);
                    if (tileentity != null) {
                        tileentity.open = !tileentity.open;
                        world.markBlockForUpdate(
                            tileentity.xCoord, tileentity.yCoord, tileentity.zCoord
                        );
                        world.playSoundEffect(
                            (double) i + 0.5,
                            (double) j + 0.5,
                            (double) k + 0.5,
                            "random.click",
                            0.3F,
                            tileentity.open ? 0.6F : 0.5F
                        );
                        world.notifyBlocksOfNeighborChange(i, j, k, this);
                    }

                    return true;
                }
            } else if (meta == MetaVals.CONDUIT_VALVE_ADVANCED) {
                if (entityplayer.isSneaking()) {
                    return false;
                } else {
                    TileConduitValveAdvanced tileentity
                        = (TileConduitValveAdvanced) world.getTileEntity(i, j, k);
                    if (tileentity != null) {
                        ++tileentity.setting;
                        if (tileentity.setting > 2) {
                            tileentity.setting = 0;
                        }

                        world.markBlockForUpdate(
                            tileentity.xCoord, tileentity.yCoord, tileentity.zCoord
                        );
                        world.playSoundEffect(
                            (double) i + 0.5,
                            (double) j + 0.5,
                            (double) k + 0.5,
                            "random.click",
                            0.3F,
                            0.5F
                        );
                        world.notifyBlocksOfNeighborChange(i, j, k, this);
                    }

                    return true;
                }
            } else {
                return super.onBlockActivated(
                    world, i, j, k, entityplayer, alec1, alec2, alec3, alec4
                );
            }
        } else {
            return false;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta_) {
        // TODO: not sure if param 6 is meta
        MetaVals meta = MetaVals.get(meta_);

        if (meta != MetaVals.CONDUIT_PUMP) {
            AuraUtils.spillTaint(world, x, y, z);
        }
        super.breakBlock(world, x, y, z, block, meta_);
    }

    @Override
    public int getRenderType() {
        return BlockApparatusRenderer.RI;
    }

    //public int getBlockTextureFromSide(int i) {
    //    return 15;
    //}

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        MetaVals md = MetaVals.get(iblockaccess.getBlockMetadata(i, j, k));
        if (md == MetaVals.FILTER) {
            if (l <= 1) {
                return this.iconFilterBottom;
            } else {
                TileConduit tf = (TileFilter) iblockaccess.getTileEntity(i, j, k);
                HelperLocation loc = new HelperLocation(tf);
                switch (l) {
                    case 2:
                        loc.facing = ForgeDirection.NORTH;
                        break;
                    case 3:
                        loc.facing = ForgeDirection.SOUTH;
                        break;
                    case 4:
                        loc.facing = ForgeDirection.WEST;
                        break;
                    case 5:
                        loc.facing = ForgeDirection.EAST;
                }

                if (!tf.getConnectable(loc.facing)) {
                    return this.iconFilterSide;
                } else {
                    TileEntity te = loc.getConnectableTile(iblockaccess);
                    return te != null ? this.iconFilterFront : this.iconFilterSide;
                }
            }
        } else if (md == MetaVals.CONDUIT_TANK) {
            return l <= 1 ? this.iconTankBottom : this.iconTankSide;
        } else if (md == MetaVals.PURIFIER) {
            TileEntity te = iblockaccess.getTileEntity(i, j, k);
            if (te != null && te instanceof TilePurifier) {
                TilePurifier tp = (TilePurifier) te;
                if (tp.orientation != 0 && tp.orientation != 2) {
                    return l > 3 ? this.iconPurifierFront : this.iconPurifierSide;
                } else if (l <= 1) {
                    return this.iconPurifierTop;
                } else {
                    return l <= 3 ? this.iconPurifierFront : this.iconPurifierSide;
                }
            } else {
                return this.iconPurifierFront;
            }
        }
        return this.blockIcon;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int i, int j, int k, int side) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(i, j, k));
        //return md > 2 && md != 4;
        return md != MetaVals.CONDUIT && md != MetaVals.FILTER;
    }

    //public int idDropped(int i, Random random, int j) {
    //    return i == 10 ? mod_ThaumCraft.itemComponents.shiftedIndex
    //                   : super.idDropped(i, random, j);
    //}

    @Override
    public int damageDropped(int i) {
        return i;
    }

    public int getLightValue(IBlockAccess iba, int i, int j, int k) {
        int md = iba.getBlockMetadata(i, j, k);
        if (md != 0 && md != 1 && md != 3) {
            if (md == 4) {
                return 8;
            } else {
                return md == 10 ? 14 : super.getLightValue(iba, i, j, k);
            }
        } else {
            return 5;
        }
    }

    //public boolean renderAppFragileBlock(
    //    World w,
    //    RenderBlocks renderblocks,
    //    int i,
    //    int j,
    //    int k,
    //    Block block,
    //    boolean inv,
    //    int md
    //) {
    //    if (md == -9) {
    //        md = w.getBlockMetadata(i, j, k);
    //    }

    //    if (md == 0) {
    //        return ThaumCraftRenderer.renderBlockConduit(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 1) {
    //        return ThaumCraftRenderer.renderBlockFilter(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 2) {
    //        return ThaumCraftRenderer.renderBlockBellows(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 3) {
    //        return ThaumCraftRenderer.renderBlockTank(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 4) {
    //        return ThaumCraftRenderer.renderBlockBrain(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 5) {
    //        return ThaumCraftRenderer.renderBlockValve(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 6) {
    //        return ThaumCraftRenderer.renderBlockPurifier(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 7) {
    //        return ThaumCraftRenderer.renderTrunk(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else if (md == 8) {
    //        return ThaumCraftRenderer.renderBlockValveAdvanced(
    //            w, renderblocks, i, j, k, block, md, inv
    //        );
    //    } else {
    //        return md == 9 ? ThaumCraftRenderer.renderBlockPump(
    //                   w, renderblocks, i, j, k, block, md, inv
    //               )
    //                       : false;
    //    }
    //}

    @Override
    public void randomDisplayTick(World w, int i, int j, int k, Random r) {
        int md = w.getBlockMetadata(i, j, k);
        if (md == 10) {
            FXSparkle ef2 = new FXSparkle(
                w,
                (double) ((float) i + 0.5F),
                (double) ((float) j + 0.5F),
                (double) ((float) k + 0.5F),
                (double) ((float) i + 0.5F + (r.nextFloat() - r.nextFloat()) / 3.0F),
                (double) ((float) j + 0.5F + (r.nextFloat() - r.nextFloat()) / 3.0F),
                (double) ((float) k + 0.5F + (r.nextFloat() - r.nextFloat()) / 3.0F),
                1.0F,
                6,
                3
            );
            ef2.setGravity(0.05F);
            Minecraft.getMinecraft().effectRenderer.addEffect(ef2);
        }
    }

    public void updateTick(World w, int i, int j, int k, Random r) {
        int md = w.getBlockMetadata(i, j, k);
        if (md == 10) {
            w.scheduleBlockUpdate(i, j, k, this, 5 + r.nextInt(3));
            FXWisp ef = new FXWisp(
                w,
                (double) ((float) i + 0.5F),
                (double) ((float) j + 0.5F),
                (double) ((float) k + 0.5F),
                0.5F,
                4
            );
            ef.shrink = true;
            ef.setGravity(-0.03F);
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
            ef = new FXWisp(
                w,
                (double) ((float) i + 0.5F),
                (double) ((float) j + 0.5F),
                (double) ((float) k + 0.5F),
                0.25F,
                1
            );
            ef.setGravity(-0.01F);
            Minecraft.getMinecraft().effectRenderer.addEffect(ef);
        }

        super.updateTick(w, i, j, k, r);
    }

    public static enum MetaVals {
        // original metas in comments
        CONDUIT, // 0
        FILTER, // 1
        CONDUIT_TANK, // 3
        CONDUIT_VALVE, // 5
        PURIFIER, // 6
        CONDUIT_VALVE_ADVANCED, // 8
        CONDUIT_PUMP; // 9

        public static MetaVals get(int meta) {
            if (meta >= 0 && meta < MetaVals.values().length)
                return MetaVals.values()[meta];

            return null;
        }
    }
}
