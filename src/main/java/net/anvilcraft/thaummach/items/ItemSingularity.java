package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.TMTab;
import net.anvilcraft.thaummach.entities.EntitySingularity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSingularity extends Item {
    public ItemSingularity() {
        super.maxStackSize = 64;
        this.setHasSubtypes(false);
        this.setMaxDamage(0);
        this.setUnlocalizedName("thaummach:singularity");
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = reg.registerIcon("thaummach:singularity");
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    public boolean hasEffect(ItemStack itemstack) {
        return true;
    }

    public ItemStack
    onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (world.isRemote)
            return itemstack;

        world.spawnEntityInWorld(new EntitySingularity(world, entityplayer));
        --itemstack.stackSize;
        return itemstack;
    }
}
