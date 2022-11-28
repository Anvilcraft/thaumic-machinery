package net.anvilcraft.thaummach;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TMTab extends CreativeTabs {
    public TMTab() {
        super("thaummach");
    }

    public static TMTab INSTANCE = new TMTab();

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(TMBlocks.apparatusFragile);
    }
}
