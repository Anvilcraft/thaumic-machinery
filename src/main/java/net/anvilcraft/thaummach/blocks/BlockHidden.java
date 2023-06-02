package net.anvilcraft.thaummach.blocks;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.thaummach.particles.FXWisp;
import net.anvilcraft.thaummach.tiles.TileMonolith;
import net.anvilcraft.thaummach.tiles.TileVoidCube;
import net.anvilcraft.thaummach.tiles.TileVoidLock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.fx.WRVector3;
import thaumcraft.client.fx.bolt.FXLightningBolt;

public class BlockHidden extends BlockContainer {
    public IIcon iconDefault;
    public IIcon iconEmpty;
    public IIcon iconMonolithBottom;
    public IIcon iconMonolithEnd;
    public IIcon iconMonolithMiddle;
    public IIcon iconMonolithTop;

    public BlockHidden() {
        super(Material.rock);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockName("tcbhidden");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setTickRandomly(true);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        Function<String, IIcon> reg = (n) -> register.registerIcon("thaummach:" + n);

        this.iconDefault = reg.apply("eldritch_stone");
        this.iconEmpty = reg.apply("empty");
        this.iconMonolithBottom = reg.apply("monolith_bottom");
        this.iconMonolithEnd = reg.apply("monolith_end");
        this.iconMonolithMiddle = reg.apply("monolith_middle");
        this.iconMonolithTop = reg.apply("monolith_top");
    }

    @Override
    public int getLightValue() {
        return 1;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void getSubBlocks(Item arg0, CreativeTabs arg1, List arg2) {}

    @Override
    public float getBlockHardness(World w, int x, int y, int z) {
        int meta = w.getBlockMetadata(x, y, z);
        return meta == 4 ? 10.0F : super.getBlockHardness(w, x, y, z);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int md) {
        switch (md) {
            case 2:
                return new TileMonolith();

            case 3:
            case 5:
                return new TileVoidCube();

            case 4:
                return new TileVoidLock();
        }
        return null;
        //if (md != 3 && md != 5) {
        //    if (md == 1) {
        //        return new TileVoidHole();
        //    } else if (md == 2) {
        //        return new TileMonolith();
        //    } else {
        //        return (TileEntity) (md == 4 ? new TileVoidLock() : super.getBlockEntity(md));
        //    }
        //} else {
        //    return new TileVoidCube();
        //}
    }

    @Override
    public void breakBlock(
        World world,
        int x,
        int y,
        int z,
        // useless parameters
        Block alec1,
        int alec2
    ) {
        //if (md == 5) {
        //    SISpecialTile pd = new SISpecialTile(
        //        i,
        //        j,
        //        k,
        //        (byte) 0,
        //        (byte) ModLoader.getMinecraftInstance().thePlayer.dimension,
        //        (byte) 2
        //    );
        //    mod_ThaumCraft.DeleteSpecialTileFromList(pd);
        //}

        super.breakBlock(world, x, y, z, alec1, alec2);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        switch (world.getBlockMetadata(x, y, z)) {
            case 1:
            case 2:
                return false;

            default:
                return true;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        super.onNeighborBlockChange(world, i, j, k, l);
        int md = world.getBlockMetadata(i, j, k);
        if (md == 5) {
            try {
                TileVoidCube tvc = (TileVoidCube) world.getTileEntity(i, j, k);
                boolean[] test = new boolean[] { false, false, false, false };

                int a;
                for (a = 0; a < 4; ++a) {
                    int xx = 0;
                    int zz = 0;
                    switch (a) {
                        case 0:
                            xx = 1;
                            break;
                        case 1:
                            xx = -1;
                            break;
                        case 2:
                            zz = 1;
                            break;
                        case 3:
                            zz = -1;
                    }

                    TileEntity tvc0 = world.getTileEntity(i + xx, j, k + zz);
                    if (tvc0 != null && ((TileVoidCube) tvc0).placed != -1
                        && ((TileVoidCube) tvc0).placed != tvc.runes[a]) {
                        ((TileVoidCube) tvc0).placed = -1;
                        world.newExplosion(
                            (Entity) null,
                            (double) ((float) tvc0.xCoord + 0.5F),
                            (double) ((float) tvc0.yCoord + 1.5F),
                            (double) ((float) tvc0.zCoord + 0.5F),
                            1.0F,
                            false,
                            false
                        );
                    } else if (tvc0 != null && ((TileVoidCube)tvc0).placed != -1 &&
                    ((TileVoidCube)tvc0).placed == tvc.runes[a]) {
                        test[a] = true;
                    }
                }

                if (test[0] && test[1] && test[2] && test[3]) {
                    world.playSoundEffect(
                        (double) ((float) i + 0.5F),
                        (double) ((float) j + 0.5F),
                        (double) ((float) k + 0.5F),
                        "thaummach:rumble",
                        4.0F,
                        1.0F
                    );

                    for (a = 0; a < 50; ++a) {
                        FXWisp ef = new FXWisp(
                            world,
                            (double) ((float) i + world.rand.nextFloat()),
                            (double) ((float) j - world.rand.nextFloat() * 2.0F),
                            (double) ((float) k + world.rand.nextFloat()),
                            (double) ((float) i + world.rand.nextFloat()),
                            (double) ((float) j + world.rand.nextFloat() * 2.0F + 4.0F),
                            (double) ((float) k + world.rand.nextFloat()),
                            0.5F,
                            5
                        );
                        ef.shrink = true;
                        Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                    }
                    tvc.generate();
                    world.setBlockToAir(i, j, k);
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        int md = world.getBlockMetadata(i, j, k);
        TileVoidCube tvc;
        if (md == 3) {
            try {
                tvc = (TileVoidCube) world.getTileEntity(i, j, k);
                tvc.runes[0] = (byte) world.rand.nextInt(6);
            } catch (Exception var12) {}
        }

        if (md == 5) {
            try {
                tvc = (TileVoidCube) world.getTileEntity(i, j, k);
                TileVoidCube tvc0 = (TileVoidCube) world.getTileEntity(i + 1, j, k);
                TileVoidCube tvc1 = (TileVoidCube) world.getTileEntity(i - 1, j, k);
                TileVoidCube tvc2 = (TileVoidCube) world.getTileEntity(i, j, k + 1);
                TileVoidCube tvc3 = (TileVoidCube) world.getTileEntity(i, j, k - 1);
                tvc.runes[0] = tvc0.runes[0];
                tvc.runes[1] = tvc1.runes[0];
                tvc.runes[2] = tvc2.runes[0];
                tvc.runes[3] = tvc3.runes[0];
            } catch (Exception var11) {}
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
        return false;
    }

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int md = iblockaccess.getBlockMetadata(i, j, k);
        if (md == 0) {
            return this.iconDefault;
        } else if (md == 3 && l != 1) {
            return this.iconDefault;
        } else {
            return md == 4 && l < 2 ? this.iconDefault : this.iconEmpty;
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            return this.iconDefault;
        } else if (meta == 3) {
            return side != 1 ? this.iconDefault : this.iconEmpty;
        } else if (meta == 4) {
            return side < 2 ? this.iconDefault : this.iconEmpty;
        } else {
            return this.iconEmpty;
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void addCollisionBoxesToList(
        World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity alec
    ) {
        int md = world.getBlockMetadata(x, y, z);
        if (md != 1) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, alec);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            int md = world.getBlockMetadata(x, y, z);
            if (md == 1 || md == 2) {
                AuraManager.addFluxToClosest(world, x, y, z, new AspectList().add(Aspect.TAINT, 1));
            }
        }
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        int md = world.getBlockMetadata(i, j, k);
        if (md == 2 && world.rand.nextInt(5) == 0) {
            FXLightningBolt bolt;
            if (world.getBlock(i, j - 1, k) != this) {
                bolt = new FXLightningBolt(
                    world,
                    new WRVector3((double) i + 0.5, (double) j + 0.25, (double) k + 0.5),
                    new WRVector3(
                        (double) i + 0.5
                            + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 2.0F),
                        (double) (j - 2),
                        (double) k + 0.5
                            + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 2.0F)
                    ),
                    world.rand.nextLong()
                );
                bolt.setMultiplier(4.0F);
                bolt.defaultFractal();
                bolt.setType(5);
                bolt.finalizeBolt();
            }

            if (world.getBlock(i, j + 1, k) != this) {
                bolt = new FXLightningBolt(
                    world,
                    new WRVector3((double) i + 0.5, (double) j + 0.75, (double) k + 0.5),
                    new WRVector3(
                        (double) i + 0.5
                            + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 2.0F),
                        (double) (j + 3),
                        (double) k + 0.5
                            + (double) ((world.rand.nextFloat() - world.rand.nextFloat()) * 2.0F)
                    ),
                    world.rand.nextLong()
                );
                bolt.setMultiplier(4.0F);
                bolt.defaultFractal();
                bolt.setType(5);
                bolt.finalizeBolt();
            }
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
