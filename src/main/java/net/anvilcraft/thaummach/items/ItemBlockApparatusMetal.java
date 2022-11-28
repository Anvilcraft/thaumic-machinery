package net.anvilcraft.thaummach.items;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal.MetaVals;
import net.minecraft.block.Block;

public class ItemBlockApparatusMetal extends ItemBlockApparatus {
    public ItemBlockApparatusMetal(Block block) {
        super(block);
    }

    @Override
    public String getTypeString() {
        return "metal";
    }

    @Override
    public String getNameExtension(int meta_) {
        MetaVals meta = MetaVals.get(meta_);
        if (meta == null)
            return null;
        return meta.toString().toLowerCase();
    }
}
