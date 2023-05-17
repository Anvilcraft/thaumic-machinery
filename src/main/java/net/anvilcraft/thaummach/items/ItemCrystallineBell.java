package net.anvilcraft.thaummach.items;

import dev.tilera.auracore.api.ICrystal;
import net.anvilcraft.thaummach.TMTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;

public class ItemCrystallineBell extends Item implements IRepairable {
    public ItemCrystallineBell() {
        super();
        super.maxStackSize = 1;
        this.setMaxDamage(100);
        this.setUnlocalizedName("thaummach:crystalline_bell");
        this.setCreativeTab(TMTab.INSTANCE);
        this.canRepair = false;
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = reg.registerIcon("thaummach:crystalline_bell");
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 5;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean onItemUse(
        ItemStack itemstack,
        EntityPlayer player,
        World world,
        int i,
        int j,
        int k,
        int l,
        // useless parameters
        float alec1,
        float alec2,
        float alec3
    ) {
        if (itemstack.stackSize == 0)
            return false;

        int meta = world.getBlockMetadata(i, j, k);
        TileEntity te = world.getTileEntity(i, j, k);
        if (te != null && te instanceof ICrystal) {
            if (((ICrystal) te).getCrystalCount(meta) == 1)
                return false;
            world.playSoundEffect(
                (double) ((float) i + 0.5F),
                (double) ((float) j + 0.5F),
                (double) ((float) k + 0.5F),
                "random.orb",
                0.5F,
                0.8F + (float) ((ICrystal) te).getCrystalCount(meta) * 0.1F
            );

            ((ICrystal) te).harvestShard(player);

            itemstack.damageItem(1, player);
            world.markBlockForUpdate(i, j, k);
            return true;
        }
        return super.onItemUse(itemstack, player, world, i, j, k, l, alec1, alec2, alec3);
    }

    // TODO: WTF
    //@Override
    //public float visRepairCost() {
    //    return 0.5F;
    //}
}
