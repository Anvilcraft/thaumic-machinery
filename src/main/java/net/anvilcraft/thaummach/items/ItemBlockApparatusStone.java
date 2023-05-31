package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.blocks.BlockApparatusStone.MetaVals;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBlockApparatusStone extends ItemBlockApparatus {
    public ItemBlockApparatusStone(Block block) {
        super(block);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    @Override
    public String getTypeString() {
        return "stone";
    }

    @Override
    public String getNameExtension(int meta_) {
        MetaVals meta = MetaVals.get(meta_);
        if (meta == null)
            return null;
        return meta.toString().toLowerCase();
    }
}
