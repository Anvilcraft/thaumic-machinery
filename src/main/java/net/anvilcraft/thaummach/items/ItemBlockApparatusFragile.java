package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.blocks.BlockApparatusFragile.MetaVals;
import net.minecraft.block.Block;

public class ItemBlockApparatusFragile extends ItemBlockApparatus {
    public ItemBlockApparatusFragile(Block block) {
        super(block);
    }

    @Override
    public String getTypeString() {
        return "fragile";
    }

    @Override
    public String getNameExtension(int meta_) {
        MetaVals meta = MetaVals.get(meta_);
        if (meta == null)
            return null;
        return meta.toString().toLowerCase();
    }
}
