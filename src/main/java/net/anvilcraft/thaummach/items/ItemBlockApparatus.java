package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.TMTab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class ItemBlockApparatus extends ItemBlock {
    public ItemBlockApparatus(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TMTab.INSTANCE);
    }

    public abstract String getTypeString();
    public abstract String getNameExtension(int meta);

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        String metaExt = this.getNameExtension(is.getItemDamage());
        if (metaExt == null)
            return "tile.thaummach:alec";
        return "tile.thaummach:apparatus_" + this.getTypeString() + "_" + metaExt;
    }
}
