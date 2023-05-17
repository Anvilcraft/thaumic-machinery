package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.blocks.BlockApparatusWood.MetaVals;
import net.minecraft.block.Block;

public class ItemBlockApparatusWood extends ItemBlockApparatus {
    public ItemBlockApparatusWood(Block block) {
        super(block);
    }

    @Override
    public String getTypeString() {
        return "wood";
    }

    @Override
    public String getNameExtension(int meta_) {
        MetaVals meta = MetaVals.get(meta_);
        if (meta == null)
            return null;
        return meta.toString().toLowerCase();
    }
}
