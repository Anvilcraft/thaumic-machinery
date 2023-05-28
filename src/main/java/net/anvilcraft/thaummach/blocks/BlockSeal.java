package net.anvilcraft.thaummach.blocks;

import net.anvilcraft.thaummach.TMTab;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSeal extends BlockContainer {
    public BlockSeal() {
        super(Material.rock);
        this.setHardness(0.5F);
        this.setResistance(15.0F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockName("thaummach:seal");
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        // TODO: add texture for this
        this.blockIcon = ir.registerIcon("thaummach:seal");
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
        if (world.isRemote)
            return true;

        TileEntity te = world.getTileEntity(i, j, k);
        if (te != null && ((TileSeal) te).runes[0] == 0
            && ((TileSeal) te).runes[1] == 1) {
            ++((TileSeal) te).portalWindow;
            world.playSoundEffect(
                (double) i + 0.5,
                (double) j + 0.5,
                (double) k + 0.5,
                "thaummach:pclose",
                0.2F,
                1.0F + world.rand.nextFloat() * 0.2F
            );
            return true;
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
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
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileSeal();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return this.createNewTileEntity(world, metadata);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean
    isSideSolid(IBlockAccess world, int i, int j, int k, ForgeDirection side) {
        //int md = world.getBlockMetadata(i, j, k);
        //return md != 0 && md != 3 && md != 4 && md != 7;
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
        if (l == 0 && world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN)) {
            return true;
        } else if (l == 1 && world.isSideSolid(i, j - 1, k, ForgeDirection.UP)) {
            return true;
        } else if (l == 2 && world.isSideSolid(i, j, k + 1, ForgeDirection.NORTH)) {
            return true;
        } else if (l == 3 && world.isSideSolid(i, j, k - 1, ForgeDirection.SOUTH)) {
            return true;
        } else if (l == 4 && world.isSideSolid(i + 1, j, k, ForgeDirection.EAST)) {
            return true;
        } else {
            return l == 5 && world.isSideSolid(i - 1, j, k, ForgeDirection.WEST);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        if (world.isSideSolid(i - 1, j, k, ForgeDirection.EAST)) {
            return true;
        } else if (world.isSideSolid(i + 1, j, k, ForgeDirection.WEST)) {
            return true;
        } else if (world.isSideSolid(i, j, k - 1, ForgeDirection.SOUTH)) {
            return true;
        } else if (world.isSideSolid(i, j, k + 1, ForgeDirection.NORTH)) {
            return true;
        } else {
            return world.isSideSolid(i, j - 1, k, ForgeDirection.UP)
                ? true
                : world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN);
        }
    }

    @Override
    public void onPostBlockPlaced(
        World world,
        int x,
        int y,
        int z,
        int l
    ) {
        int orientation = -1;
        if (world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)) {
            orientation = 0;
        } else if (world.isSideSolid(x, y - 1, z, ForgeDirection.UP)) {
            orientation = 1;
        } else if (world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH)) {
            orientation = 2;
        } else if (world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH)) {
            orientation = 3;
        } else if (world.isSideSolid(x + 1, y, z, ForgeDirection.WEST)) {
            orientation = 4;
        } else if (world.isSideSolid(x - 1, y, z, ForgeDirection.EAST)) {
            orientation = 5;
        }

        TileSeal ts = (TileSeal) world.getTileEntity(x, y, z);
        ts.orientation = (short) orientation;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int
    isProvidingStrongPower(IBlockAccess iblockaccess, int i, int j, int k, int meta) {
        TileSeal ts = (TileSeal) iblockaccess.getTileEntity(i, j, k);
        if (ts != null) {
            return ts.isPowering ? 15 : 0;
        }

        return 0;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int meta) {
        return this.isProvidingStrongPower(world, x, y, z, meta);
    }

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
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        super.onNeighborBlockChange(world, i, j, k, l);
        if (this.checkIfAttachedToBlock(world, i, j, k)) {
            TileSeal tes = (TileSeal) world.getTileEntity(i, j, k);
            if (tes != null) {
                int i1 = tes.orientation;
                boolean flag = false;
                if (!world.isSideSolid(i - 1, j, k, ForgeDirection.EAST) && i1 == 5) {
                    flag = true;
                }

                if (!world.isSideSolid(i + 1, j, k, ForgeDirection.WEST) && i1 == 4) {
                    flag = true;
                }

                if (!world.isSideSolid(i, j, k - 1, ForgeDirection.SOUTH) && i1 == 3) {
                    flag = true;
                }

                if (!world.isSideSolid(i, j, k + 1, ForgeDirection.NORTH) && i1 == 2) {
                    flag = true;
                }

                if (!world.isSideSolid(i, j - 1, k, ForgeDirection.UP) && i1 == 1) {
                    flag = true;
                }

                if (!world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN) && i1 == 0) {
                    flag = true;
                }

                if (flag) {
                    world.setBlockToAir(i, j, k);
                }
            }
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
