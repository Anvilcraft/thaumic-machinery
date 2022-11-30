package net.anvilcraft.thaummach.blocks;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import dev.tilera.auracore.client.FXSparkle;
import net.anvilcraft.thaummach.AuraUtils;
import net.anvilcraft.thaummach.render.BlockApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.metal.ArcaneFurnaceApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.metal.BoreApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.metal.CrucibleApparatusRenderer;
import net.anvilcraft.thaummach.render.apparatus.apparati.metal.CrystallizerApparatusRenderer;
import net.anvilcraft.thaummach.tiles.TileArcaneFurnace;
import net.anvilcraft.thaummach.tiles.TileBore;
import net.anvilcraft.thaummach.tiles.TileConduitTank;
import net.anvilcraft.thaummach.tiles.TileCrucible;
import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.tiles.TileBellows;

public class BlockApparatusMetal extends BlockApparatus {
    private int delay = 0;

    public IIcon iconArcaneFurnaceBottom;
    public IIcon iconArcaneFurnaceInside;
    public IIcon iconArcaneFurnaceSide;
    public IIcon iconArcaneFurnaceTop;
    public IIcon iconCrystallizerBottom;
    public IIcon iconCrystallizerSide;
    public IIcon iconCrystallizerTop;
    public IIcon iconGenerator1;
    public IIcon iconGenerator2;
    public IIcon iconGenerator3;
    public IIcon iconSoulBrazierBottom;
    public IIcon iconSoulBrazierSide;
    public IIcon iconSoulCrucibleBottom;
    public IIcon iconSoulCrucibleFace0;
    public IIcon iconSoulCrucibleFace1;
    public IIcon iconSoulCrucibleFace2;
    public IIcon iconSoulCrucibleFace3;
    public IIcon iconSoulCrucibleTop;
    public IIcon iconSoulCrucibleTopInv;
    public IIcon iconVoidChestBottom;
    public IIcon iconVoidChestSide;
    public IIcon iconVoidChestSideTransparent;
    public IIcon iconVoidChestTop;
    public IIcon iconVoidInterfaceBottom;
    public IIcon iconVoidInterfaceSide;

    public IIcon[] iconsEyesCrucible;
    public IIcon[] iconsNormalCrucible;
    public IIcon[] iconsThaumiumCrucible;

    public BlockApparatusMetal() {
        super(Material.rock);
        this.setHardness(3.0F);
        this.setResistance(17.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setBlockName("tcbappmetal");
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);

        this.iconArcaneFurnaceBottom
            = reg.registerIcon("thaummach:arcane_furnace_bottom");
        this.iconArcaneFurnaceInside
            = reg.registerIcon("thaummach:arcane_furnace_inside");
        this.iconArcaneFurnaceSide = reg.registerIcon("thaummach:arcane_furnace_side");
        this.iconArcaneFurnaceTop = reg.registerIcon("thaummach:arcane_furnace_top");
        this.iconCrystallizerBottom = reg.registerIcon("thaummach:crystallizer_bottom");
        this.iconCrystallizerSide = reg.registerIcon("thaummach:crystallizer_side");
        this.iconCrystallizerTop = reg.registerIcon("thaummach:crystallizer_top");
        this.iconGenerator1 = reg.registerIcon("thaummach:generator_1");
        this.iconGenerator2 = reg.registerIcon("thaummach:generator_2");
        this.iconGenerator3 = reg.registerIcon("thaummach:generator_3");
        this.iconSoulBrazierBottom = reg.registerIcon("thaummach:soul_brazier_bottom");
        this.iconSoulBrazierSide = reg.registerIcon("thaummach:soul_brazier_side");
        this.iconSoulCrucibleBottom = reg.registerIcon("thaummach:soul_crucible_bottom");
        this.iconSoulCrucibleFace0 = reg.registerIcon("thaummach:soul_crucible_face_0");
        this.iconSoulCrucibleFace1 = reg.registerIcon("thaummach:soul_crucible_face_1");
        this.iconSoulCrucibleFace2 = reg.registerIcon("thaummach:soul_crucible_face_2");
        this.iconSoulCrucibleFace3 = reg.registerIcon("thaummach:soul_crucible_face_3");
        this.iconSoulCrucibleTop = reg.registerIcon("thaummach:soul_crucible_top");
        this.iconSoulCrucibleTopInv = reg.registerIcon("thaummach:soul_crucible_top_inv");
        this.iconVoidChestBottom = reg.registerIcon("thaummach:void_chest_bottom");
        this.iconVoidChestSide = reg.registerIcon("thaummach:void_chest_side");
        this.iconVoidChestSideTransparent
            = reg.registerIcon("thaummach:void_chest_side_transparent");
        this.iconVoidChestTop = reg.registerIcon("thaummach:void_chest_top");
        this.iconVoidInterfaceBottom
            = reg.registerIcon("thaummach:void_interface_bottom");
        this.iconVoidInterfaceSide = reg.registerIcon("thaummach:void_interface_side");

        this.iconsEyesCrucible
            = IntStream.rangeClosed(1, 7)
                  .mapToObj(i -> reg.registerIcon("thaummach:eyes_crucible_" + i))
                  .toArray(IIcon[] ::new);

        this.iconsNormalCrucible
            = IntStream.rangeClosed(1, 7)
                  .mapToObj(
                      i
                      -> i == 5 ? null
                                : reg.registerIcon("thaummach:normal_crucible_" + i)
                  )
                  .toArray(IIcon[] ::new);

        this.iconsThaumiumCrucible
            = IntStream.rangeClosed(1, 7)
                  .mapToObj(i -> reg.registerIcon("thaummach:thaumium_crucible_" + i))
                  .toArray(IIcon[] ::new);
    }

    @Override
    public IApparatusRenderer getApparatusRenderer(int md) {
        MetaVals meta = MetaVals.get(md);

        switch (meta) {
            case NORMAL_CRUCIBLE:
            case EYES_CRUCIBLE:
            case THAUMIUM_CRUCIBLE:
            case SOUL_CRUCIBLE:
                return CrucibleApparatusRenderer.INSTANCE;

            case ARCANE_FURNACE:
                return ArcaneFurnaceApparatusRenderer.INSTANCE;

            case CRYSTALLIZER:
                return CrystallizerApparatusRenderer.INSTANCE;

            case BORE:
                return BoreApparatusRenderer.INSTANCE;

            default:
                return null;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        MetaVals md = MetaVals.get(meta);
        if (md.isCrucible()) {
            TileCrucible tc = new TileCrucible();
            tc.setTier((short) (md.ordinal() + 1));
            return tc;
        } else if (md == MetaVals.ARCANE_FURNACE) {
            //return new TileArcaneFurnace();
        } else if (md == MetaVals.GENERATOR) {
            //return new TileGenerator();
        } else if (md == MetaVals.CRYSTALLIZER) {
            return new TileCrystallizer();
        } else if (md == MetaVals.BORE) {
            return new TileBore();
        } else if (md == MetaVals.VOID_CHEST) {
            //return new TileVoidChest();
        } else if (md == MetaVals.VOID_INTERFACE) {
            //return new TileVoidInterface();
        } else if (md == MetaVals.TANK) {
            return new TileConduitTank();
        } else if (md == MetaVals.SOUL_BRAZIER) {
            //return new TileSoulBrazier();
        }

        return null;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (MetaVals meta : MetaVals.values()) {
            list.add(new ItemStack(this, 1, meta.ordinal()));
        }
    }

    @Override
    public void
    onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(i, j, k));
        if (md == MetaVals.NORMAL_CRUCIBLE || md == MetaVals.EYES_CRUCIBLE
            || md == MetaVals.THAUMIUM_CRUCIBLE) {
            if (entity instanceof EntityItem && entity.posY <= (double) j + 0.7) {
                entity.motionX += (double
                ) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.05F);
                entity.motionY += (double) (world.rand.nextFloat() * 0.1F);
                entity.motionZ += (double
                ) ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.05F);
                ((EntityItem) entity).delayBeforeCanPickup = 10;
                ((EntityItem) entity).age = 0;
            }

            ++this.delay;
            if (this.delay >= 5) {
                this.delay = 0;
                if (entity instanceof EntityLiving
                    && !(entity instanceof EntityThaumicSlime)) {
                    entity.attackEntityFrom(DamageSource.magic, 1);
                    world.playSoundEffect(
                        (double) i,
                        (double) j,
                        (double) k,
                        "random.fizz",
                        0.4F,
                        2.0F + world.rand.nextFloat() * 0.4F
                    );
                }
            }
        }
    }

    @Override
    public int getRenderType() {
        return BlockApparatusRenderer.RI;
    }

    @Override
    public IIcon getIcon(int i, int j) {
        MetaVals meta = MetaVals.get(j);
        if (meta == MetaVals.GENERATOR) {
            return this.iconGenerator2;
        } else if (meta == MetaVals.VOID_CHEST) {
            if (i == 0) {
                return this.iconVoidChestBottom;
            } else {
                return i == 1 ? this.iconVoidChestTop : this.iconVoidChestSide;
            }
        } else if (meta == MetaVals.VOID_INTERFACE) {
            return i <= 1 ? this.iconVoidInterfaceBottom : this.iconVoidInterfaceSide;
        } else if (meta == MetaVals.SOUL_BRAZIER) {
            return i <= 1 ? this.iconSoulBrazierBottom : this.iconSoulBrazierSide;
        } else {
            return super.getIcon(i, j);
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        MetaVals meta = MetaVals.get(iblockaccess.getBlockMetadata(i, j, k));
        TileCrucible tc;
        if (meta == MetaVals.NORMAL_CRUCIBLE || meta == MetaVals.EYES_CRUCIBLE
            || meta == MetaVals.THAUMIUM_CRUCIBLE) {
            if (side == 1) {
                return CrucibleApparatusRenderer.getIcons(this, meta)[0];
            } else if (side == 0) {
                return CrucibleApparatusRenderer.getIcons(this, meta)[3];
            } else {
                tc = (TileCrucible) iblockaccess.getTileEntity(i, j, k);
                return meta != MetaVals.NORMAL_CRUCIBLE && tc != null && tc.isPowering
                    ? CrucibleApparatusRenderer.getIcons(this, meta)[3]
                    : CrucibleApparatusRenderer.getIcons(this, meta)[5];
            }
        } else if (meta == MetaVals.SOUL_CRUCIBLE) {
            if (side == 1) {
                return this.iconSoulCrucibleTop;
            } else if (side == 0) {
                return this.iconSoulCrucibleBottom;
            } else {
                tc = (TileCrucible) iblockaccess.getTileEntity(i, j, k);
                switch (tc == null ? 3 : tc.face) {
                    case 0:
                        return this.iconSoulCrucibleFace0;

                    case 1:
                        return this.iconSoulCrucibleFace1;

                    case 2:
                        return this.iconSoulCrucibleFace2;

                    default:
                        return this.iconSoulCrucibleFace3;
                }
            }
        } else if (meta == MetaVals.ARCANE_FURNACE) {
            if (side == 1) {
                return this.iconArcaneFurnaceTop;
            } else if (side == 0) {
                return this.iconArcaneFurnaceBottom;
            } else {
                return this.iconArcaneFurnaceSide;
            }
        }
        //else if (meta == 5) {
        //    return 144;
        //}
        else if (meta == MetaVals.CRYSTALLIZER) {
            if (side == 1) {
                return this.iconCrystallizerTop;
            } else if (side == 0) {
                return this.iconCrystallizerBottom;
            } else {
                return this.iconCrystallizerSide;
            }
        }
        //else if (meta == 8) {
        //    if (side == 0) {
        //        return 104;
        //    } else {
        //        return side == 1 ? 97 : 255;
        //    }
        //} else if (meta == 10) {
        //    return side <= 1 ? 78 : 79;
        //} else {
        //    return super.getBlockTexture(iblockaccess, i, j, k, side);
        //}
        return null;
    }

    @Override
    public int damageDropped(int i) {
        return i;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void addCollisionBoxesToList(
        World world,
        int i,
        int j,
        int k,
        AxisAlignedBB axisalignedbb,
        List arraylist,
        Entity entity
    ) {
        MetaVals meta = MetaVals.get(world.getBlockMetadata(i, j, k));
        float t4;
        if (meta == MetaVals.NORMAL_CRUCIBLE || meta == MetaVals.EYES_CRUCIBLE
            || meta == MetaVals.THAUMIUM_CRUCIBLE) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
            super.addCollisionBoxesToList(
                world, i, j, k, axisalignedbb, arraylist, entity
            );
            t4 = 0.125F;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, t4, 1.0F, 1.0F);
            super.addCollisionBoxesToList(
                world, i, j, k, axisalignedbb, arraylist, entity
            );
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, t4);
            super.addCollisionBoxesToList(
                world, i, j, k, axisalignedbb, arraylist, entity
            );
            this.setBlockBounds(1.0F - t4, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(
                world, i, j, k, axisalignedbb, arraylist, entity
            );
            this.setBlockBounds(0.0F, 0.0F, 1.0F - t4, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(
                world, i, j, k, axisalignedbb, arraylist, entity
            );
            this.setBlockBoundsForItemRender();
        } else if (meta != MetaVals.GENERATOR && meta != MetaVals.BORE) {
            if (meta == MetaVals.VOID_INTERFACE) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.4375F, 1.0F);
                super.addCollisionBoxesToList(
                    world, i, j, k, axisalignedbb, arraylist, entity
                );
            } else if (meta == MetaVals.TANK) {
                t4 = 0.0625F;
                this.setBlockBounds(t4, 0.0F, t4, 1.0F - t4, 1.0F, 1.0F - t4);
                super.addCollisionBoxesToList(
                    world, i, j, k, axisalignedbb, arraylist, entity
                );
            } else if (meta == MetaVals.SOUL_BRAZIER) {
                t4 = 0.25F;
                this.setBlockBounds(t4, 0.0F, t4, 1.0F - t4, 0.75F, 1.0F - t4);
                super.addCollisionBoxesToList(
                    world, i, j, k, axisalignedbb, arraylist, entity
                );
            } else {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(
                    world, i, j, k, axisalignedbb, arraylist, entity
                );
                this.setBlockBoundsForItemRender();
            }
        } else {
            t4 = 0.125F;
            this.setBlockBounds(t4, t4, t4, 1.0F - t4, 1.0F - t4, 1.0F - t4);
            super.addCollisionBoxesToList(
                world, i, j, k, axisalignedbb, arraylist, entity
            );
        }
    }

    @Override
    public void
    setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        MetaVals md = MetaVals.get(iblockaccess.getBlockMetadata(i, j, k));
        float t4;
        if (md != MetaVals.GENERATOR && md != MetaVals.BORE) {
            if (md == MetaVals.TANK) {
                t4 = 0.0625F;
                this.setBlockBounds(t4, 0.0F, t4, 1.0F - t4, 1.0F, 1.0F - t4);
            } else if (md == MetaVals.SOUL_BRAZIER) {
                t4 = 0.25F;
                this.setBlockBounds(t4, 0.0F, t4, 1.0F - t4, 0.75F, 1.0F - t4);
            } else if (md == MetaVals.VOID_INTERFACE) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.4375F, 1.0F);
            } else {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        } else {
            t4 = 0.125F;
            this.setBlockBounds(t4, t4, t4, 1.0F - t4, 1.0F - t4, 1.0F - t4);
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        MetaVals md = MetaVals.get(w.getBlockMetadata(i, j, k));
        float t4;
        if (md != MetaVals.GENERATOR && md != MetaVals.BORE) {
            if (md == MetaVals.VOID_INTERFACE) {
                AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 0.4375, 1.0);
            } else if (md == MetaVals.TANK) {
                t4 = 0.0625F;
                AxisAlignedBB.getBoundingBox(
                    (double) t4,
                    0.0,
                    (double) t4,
                    (double) (1.0F - t4),
                    1.0,
                    (double) (1.0F - t4)
                );
            } else if (md == MetaVals.SOUL_BRAZIER) {
                t4 = 0.25F;
                AxisAlignedBB.getBoundingBox(
                    (double) t4,
                    0.0,
                    (double) t4,
                    (double) (1.0F - t4),
                    0.75,
                    (double) (1.0F - t4)
                );
            } else {
                AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            }
        } else {
            t4 = 0.125F;
            AxisAlignedBB.getBoundingBox(
                (double) t4,
                (double) t4,
                (double) t4,
                (double) (1.0F - t4),
                (double) (1.0F - t4),
                (double) (1.0F - t4)
            );
        }

        return super.getSelectedBoundingBoxFromPool(w, i, j, k);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void randomDisplayTick(World w, int i, int j, int k, Random r) {
        MetaVals meta = MetaVals.get(w.getBlockMetadata(i, j, k));
        if (meta.isCrucible()) {
            try {
                //TileCrucible data = (TileCrucible) w.getTileEntity(i, j, k);
            } catch (Exception var15) {
                return;
            }

            // TODO: FX
            //ThaumCraftCore.createGreenFlameFX(
            //    w,
            //    (float) i + 0.2F + r.nextFloat() * 0.6F,
            //    (float) j + 0.1F,
            //    (float) k + 0.2F + r.nextFloat() * 0.6F
            //);
        }

        int arcs;
        if (meta == MetaVals.ARCANE_FURNACE) {
            TileArcaneFurnace data;
            try {
                data = (TileArcaneFurnace) w.getTileEntity(i, j, k);
            } catch (Exception var14) {
                return;
            }

            if (!data.isWorking()) {
                return;
            }

            float f1;
            float f2;
            float f3;
            for (arcs = 0; arcs < 3; ++arcs) {
                f1 = r.nextFloat() * 0.6F;
                f2 = r.nextFloat() * 0.5F;
                f3 = r.nextFloat() * 0.6F;
                w.spawnParticle(
                    "smoke",
                    (double) ((float) i + f1 + 0.2F),
                    (double) ((float) j + 0.1F + f2),
                    (double) ((float) k + f3 + 0.2F),
                    0.0,
                    0.06,
                    0.0
                );
                w.spawnParticle(
                    "flame",
                    (double) ((float) i + f1 + 0.2F),
                    (double) ((float) j + 0.1F + f2),
                    (double) ((float) k + f3 + 0.2F),
                    0.0,
                    0.06,
                    0.0
                );
            }

            float f = (float) i + 0.5F;
            f1 = (float) j + 0.3F + r.nextFloat() * 0.4F;
            f2 = (float) k + 0.5F;
            f3 = 0.45F;
            float f4 = r.nextFloat() * 0.6F - 0.3F;

            for (int a = 0; a < 4; ++a) {
                if (a == 0 && !w.getBlock(i - 1, j, k).isOpaqueCube()) {
                    w.spawnParticle(
                        "smoke",
                        (double) (f - f3),
                        (double) f1,
                        (double) (f2 + f4),
                        0.0,
                        0.0,
                        0.0
                    );
                    w.spawnParticle(
                        "flame",
                        (double) (f - f3),
                        (double) f1,
                        (double) (f2 + f4),
                        0.0,
                        0.0,
                        0.0
                    );
                } else if (a == 1 && !w.getBlock(i + 1, j, k).isOpaqueCube()) {
                    w.spawnParticle(
                        "smoke",
                        (double) (f + f3),
                        (double) f1,
                        (double) (f2 + f4),
                        0.0,
                        0.0,
                        0.0
                    );
                    w.spawnParticle(
                        "flame",
                        (double) (f + f3),
                        (double) f1,
                        (double) (f2 + f4),
                        0.0,
                        0.0,
                        0.0
                    );
                } else if (a == 2 && !w.getBlock(i, j, k - 1).isOpaqueCube()) {
                    w.spawnParticle(
                        "smoke",
                        (double) (f + f4),
                        (double) f1,
                        (double) (f2 - f3),
                        0.0,
                        0.0,
                        0.0
                    );
                    w.spawnParticle(
                        "flame",
                        (double) (f + f4),
                        (double) f1,
                        (double) (f2 - f3),
                        0.0,
                        0.0,
                        0.0
                    );
                } else if (a == 3 && !w.getBlock(i, j, k + 1).isOpaqueCube()) {
                    w.spawnParticle(
                        "smoke",
                        (double) (f + f4),
                        (double) f1,
                        (double) (f2 + f3),
                        0.0,
                        0.0,
                        0.0
                    );
                    w.spawnParticle(
                        "flame",
                        (double) (f + f4),
                        (double) f1,
                        (double) (f2 + f3),
                        0.0,
                        0.0,
                        0.0
                    );
                }
            }
        }

        if (meta == MetaVals.GENERATOR) {
            // TODO: generator
            //TileGenerator tg = (TileGenerator) w.getTileEntity(i, j, k);
            //int arcs = 20;
            //if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics
            //    || Config.lowGfx) {
            //    arcs = 10;
            //}

            //arcs = arcs * tg.storedEnergy / tg.energyMax;
            //if (w.rand.nextInt(20) < arcs) {
            //    LightningBolt bolt = new LightningBolt(
            //        w,
            //        (double) i + 0.5,
            //        (double) j + 0.5,
            //        (double) k + 0.5,
            //        (double) i + 0.1 + (double) w.rand.nextFloat() * 0.8,
            //        (double) j + 0.1 + (double) w.rand.nextFloat() * 0.8,
            //        (double) k + 0.1 + (double) w.rand.nextFloat() * 0.8,
            //        w.rand.nextLong(),
            //        6,
            //        9.0F
            //    );
            //    bolt.defaultFractal();
            //    bolt.setType(0);
            //    bolt.setNonLethal();
            //    bolt.finalizeBolt();
            //}
        }

        if (meta == MetaVals.CRYSTALLIZER) {
            FXSparkle ef2 = new FXSparkle(
                w,
                (double) ((float) i + 0.1F + w.rand.nextFloat() * 0.8F),
                (double) ((float) j + 0.6F + w.rand.nextFloat() * 0.6F),
                (double) ((float) k + 0.1F + w.rand.nextFloat() * 0.8F),
                1.0F,
                w.rand.nextInt(5),
                3
            );
            Minecraft.getMinecraft().effectRenderer.addEffect(ef2);
        }

        if (meta == MetaVals.VOID_INTERFACE) {
            // TODO: void interface
            //TileVoidInterface tvi = (TileVoidInterface) w.getTileEntity(i, j, k);
            //if (tvi != null && tvi.linked && w.rand.nextInt(10) == 0) {
            //    LightningBolt bolt = new LightningBolt(
            //        w,
            //        new WRVector3((double) i + 0.5, (double) j + 0.75, (double) k +
            //        0.5), new WRVector3(
            //            (double) i + 0.5 + (double) w.rand.nextFloat()
            //                - (double) w.rand.nextFloat(),
            //            (double) (j + 2),
            //            (double) k + 0.5 + (double) w.rand.nextFloat()
            //                - (double) w.rand.nextFloat()
            //        ),
            //        w.rand.nextLong()
            //    );
            //    bolt.setMultiplier(4.0F);
            //    bolt.defaultFractal();
            //    bolt.setType(5);
            //    bolt.setNonLethal();
            //    bolt.finalizeBolt();
            //}
        }
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, Block block, int meta_) {
        MetaVals meta = MetaVals.get(meta_);
        if (meta.isCrucible() || meta == MetaVals.TANK) {
            AuraUtils.spillTaint(world, i, j, k);
        }

        if (meta == MetaVals.VOID_INTERFACE) {
            // TODO: void interface
            //TileVoidInterface ts = (TileVoidInterface) world.getTileEntity(i, j,
            //k); if (ts != null) {
            //    ts.invalidateLinks();
            //    SISpecialTile pd = new SISpecialTile(
            //        i,
            //        j,
            //        k,
            //        ts.network,
            //        (byte) ModLoader.getMinecraftInstance().thePlayer.dimension,
            //        (byte) 1
            //    );
            //    mod_ThaumCraft.DeleteSpecialTileFromList(pd);
            //}

        } else {
            super.breakBlock(world, i, j, k, block, meta_);
        }
    }

    @Override
    public boolean
    isSideSolid(IBlockAccess world, int i, int j, int k, ForgeDirection side) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(i, j, k));
        return md != MetaVals.VOID_CHEST && md != MetaVals.VOID_INTERFACE;
    }

    @Override
    public void onBlockPlacedBy(
        World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack is
    ) {
        MetaVals md = MetaVals.get(world.getBlockMetadata(i, j, k));
        if (md == MetaVals.VOID_INTERFACE) {
            // TODO: void interface
            //if (world.getBlock(i, j - 1, k) == this
            //    && world.getBlockMetadata(i, j - 1, k) == 8) {
            //    TileVoidInterface tvi
            //        = (TileVoidInterface) world.getTileEntity(i, j, k);
            //    if (tvi != null) {
            //        tvi.network = (byte) world.rand.nextInt(6);
            //        SISpecialTile pd = new SISpecialTile(
            //            i,
            //            j,
            //            k,
            //            tvi.network,
            //            (byte) ModLoader.getMinecraftInstance().thePlayer.dimension,
            //            (byte) 1
            //        );
            //        mod_ThaumCraft.AddSpecialTileToList(pd);
            //        tvi.invalidateLinks();
            //        tvi.establishLinks();
            //    }
            //} else {
            //    this.dropBlockAsItem(world, i, j, k, md, 0);
            //    world.setBlock(i, j, k, 0);
            //}
        } else if (md == MetaVals.BORE) {
            TileBore tb = (TileBore) world.getTileEntity(i, j, k);
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
                int l = MathHelper.floor_double(
                            (double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5
                        )
                    & 3;
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

        super.onBlockPlacedBy(world, i, j, k, entityliving, is);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int
    isProvidingStrongPower(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        MetaVals meta = MetaVals.get(iblockaccess.getBlockMetadata(i, j, k));
        if (meta == MetaVals.EYES_CRUCIBLE || meta == MetaVals.THAUMIUM_CRUCIBLE) {
            // TODO: crucibles
            //TileCrucible data = (TileCrucible) iblockaccess.getTileEntity(i, j, k);
            //if (l == 1) {
            //    TileEntity below = iblockaccess.getTileEntity(i, j - 1, k);
            //    if (below != null && below instanceof TileArcaneFurnace) {
            //        return 0;
            //    }
            //}

            //if (data.isPowering) {
            //    return 15;
            //}
        }

        return 0;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int i, int j, int k, int l) {
        return this.isProvidingStrongPower(world, i, j, k, l);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        super.onNeighborBlockChange(world, i, j, k, l);
        MetaVals meta = MetaVals.get(world.getBlockMetadata(i, j, k));
        int bellows = 0;
        if (world.getTileEntity(i + 1, j, k) instanceof TileBellows) {
            ++bellows;
        }

        if (world.getTileEntity(i - 1, j, k) instanceof TileBellows) {
            ++bellows;
        }

        if (world.getTileEntity(i, j, k + 1) instanceof TileBellows) {
            ++bellows;
        }

        if (world.getTileEntity(i, j, k - 1) instanceof TileBellows) {
            ++bellows;
        }

        if (meta.isCrucible()) {
            // TODO: crucibles
            //TileCrucible data = (TileCrucible) world.getTileEntity(i, j, k);
            //data.bellows = bellows;
        } else if (meta == MetaVals.ARCANE_FURNACE) {
            // TODO: arcane furnace
            //TileArcaneFurnace data
            //    = (TileArcaneFurnace) world.getTileEntity(i, j, k);
            //data.bellows = bellows;
        } else {
            if (meta == MetaVals.VOID_INTERFACE
                && (world.getBlock(i, j - 1, k) != this
                    || MetaVals.get(world.getBlockMetadata(i, j - 1, k))
                        != MetaVals.VOID_CHEST)) {
                this.dropBlockAsItem(world, i, j, k, meta.ordinal(), 0);
                world.setBlockToAir(i, j, k);
            }
        }
    }

    @Override
    public int getLightValue(IBlockAccess iba, int i, int j, int k) {
        MetaVals md = MetaVals.get(iba.getBlockMetadata(i, j, k));
        if (md.isCrucible()) {
            return 5;
        } else {
            TileEntity tsb;
            if (md == MetaVals.ARCANE_FURNACE) {
                // TODO: arcane furnace
                //tsb = iba.getTileEntity(i, j, k);
                //return tsb != null && tsb instanceof TileArcaneFurnace
                //        && ((TileArcaneFurnace) tsb).isWorking()
                //    ? 13
                //    : 0;
            } else if (md == MetaVals.SOUL_BRAZIER) {
                // TODO: soul brazier
                //tsb = iba.getTileEntity(i, j, k);
                //return tsb != null && tsb instanceof TileSoulBrazier
                //        && ((TileSoulBrazier) tsb).isWorking()
                //    ? 15
                //    : 0;
            } else {
                return super.getLightValue(iba, i, j, k);
            }
        }
        return 0;
    }

    //@Override
    //public boolean renderAppMetalBlock(
    //    World w, RenderBlocks rb, int i, int j, int k, Block block, boolean inv, int md
    //) {
    //    if (md == -9) {
    //        md = w.getBlockMetadata(i, j, k);
    //    }

    //    switch (md) {
    //        case 0:
    //        case 1:
    //        case 2:
    //        case 3:
    //            ThaumCraftRenderer.renderBlockCrucible(w, rb, i, j, k, block, md, inv);
    //            return true;
    //        case 4:
    //            ThaumCraftRenderer.renderBlockArcaneFurnace(
    //                w, rb, i, j, k, block, md, inv
    //            );
    //            return true;
    //        case 5:
    //            ThaumCraftRenderer.renderBlockGenerator(w, rb, i, j, k, block, md, inv);
    //            return true;
    //        case 6:
    //            ThaumCraftRenderer.renderBlockCrystalizer(w, rb, i, j, k, block, md,
    //            inv); return true;
    //        case 7:
    //            ThaumCraftRenderer.renderBlockBore(w, rb, i, j, k, block, md, inv);
    //            return true;
    //        case 8:
    //            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    //            if (block.getRenderBlockPass() == 0 && !inv) {
    //                rb.renderStandardBlock(block, i, j, k);
    //            } else if (inv) {
    //                ThaumCraftRenderer.DrawFaces(
    //                    rb, block, 97, 104, 103, 103, 103, 103, false
    //                );
    //            }

    //            return true;
    //        case 9:
    //            ThaumCraftRenderer.renderBlockVoidInterface(
    //                w, rb, i, j, k, block, md, inv
    //            );
    //            return true;
    //        case 10:
    //            ThaumCraftRenderer.renderBlockTank(w, rb, i, j, k, block, md, inv);
    //            return true;
    //        case 11:
    //            ThaumCraftRenderer.renderBlockSoulBrazier(w, rb, i, j, k, block, md,
    //            inv); return true;
    //        default:
    //            return false;
    //    }
    //}

    public static enum MetaVals {
        NORMAL_CRUCIBLE, // 0
        EYES_CRUCIBLE, // 1
        THAUMIUM_CRUCIBLE, // 2
        SOUL_CRUCIBLE, // 3
        ARCANE_FURNACE, // 4
        GENERATOR, // 5
        CRYSTALLIZER, // 6
        BORE, // 7
        VOID_CHEST, // 8
        VOID_INTERFACE, // 9
        TANK, // 10
        SOUL_BRAZIER; // 11

        public static MetaVals get(int meta) {
            if (meta >= 0 && meta < MetaVals.values().length) {
                return MetaVals.values()[meta];
            }

            return null;
        }

        public boolean isCrucible() {
            return this == NORMAL_CRUCIBLE || this == EYES_CRUCIBLE
                || this == THAUMIUM_CRUCIBLE || this == SOUL_CRUCIBLE;
        }
    }
}
