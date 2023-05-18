package net.anvilcraft.thaummach.blocks;

import java.util.Random;

import net.anvilcraft.thaummach.GuiID;
import net.anvilcraft.thaummach.ITileGui;
import net.anvilcraft.thaummach.TMTab;
import net.anvilcraft.thaummach.ThaumicMachinery;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class BlockApparatus extends BlockContainer {
    protected int currentPass;
    public IIcon iconTcubeanim;

    public BlockApparatus(Material m) {
        super(m);
        // TODO: WTF
        //this.setRequiresSelfNotify();
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.currentPass = 1;
        this.setTickRandomly(true);
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.iconTcubeanim = reg.registerIcon("thaummach:tcubeanim");
    }

    public abstract IApparatusRenderer getApparatusRenderer(int meta);

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(
        World world,
        int x,
        int y,
        int z,
        EntityPlayer entityplayer,
        // useless parameters
        int alec1,
        float alec2,
        float alec3,
        float alec4
    ) {
        if (!entityplayer.isSneaking()) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof ITileGui) {
                if (world.isRemote)
                    return true;
                GuiID id = ((ITileGui) te).getGuiID();

                entityplayer.openGui(
                    ThaumicMachinery.INSTANCE, id.ordinal(), world, x, y, z
                );

                return true;
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IInventory) {
            for (int l = 0; l < ((IInventory) te).getSizeInventory(); ++l) {
                ItemStack itemstack = ((IInventory) te).getStackInSlot(l);
                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int i1 = world.rand.nextInt(21) + 10;
                        if (i1 > itemstack.stackSize) {
                            i1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= i1;
                        EntityItem entityitem = new EntityItem(
                            world,
                            (double) ((float) x + f),
                            (double) ((float) y + f1),
                            (double) ((float) z + f2),
                            new ItemStack(
                                itemstack.getItem(), i1, itemstack.getItemDamage()
                            )
                        );
                        float f3 = 0.05F;
                        entityitem.motionX
                            = (double) ((float) world.rand.nextGaussian() * f3);
                        entityitem.motionY
                            = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ
                            = (double) ((float) world.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean canRenderInPass(int n) {
        this.currentPass = n;
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return this.currentPass;
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
        world.setBlockMetadataWithNotify(x, y, z, meta, 3);
        super.onPostBlockPlaced(world, x, y, z, meta);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        world.markBlockForUpdate(x, y, z);
    }
}
