package net.anvilcraft.thaummach.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemSeal extends ItemBlock {
    public ItemSeal(Block b) {
        super(b);
        super.maxStackSize = 16;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("thaummach:seal");
    }
}
