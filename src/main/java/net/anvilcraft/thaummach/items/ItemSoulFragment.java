package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.TMTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemSoulFragment extends Item {
    public ItemSoulFragment() {
        super();

        this.setUnlocalizedName("thaummach:soul_fragment");
        this.setCreativeTab(TMTab.INSTANCE);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = reg.registerIcon("thaummach:soul_fragment");
    }
}
