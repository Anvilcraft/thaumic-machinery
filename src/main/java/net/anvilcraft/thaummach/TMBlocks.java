package net.anvilcraft.thaummach;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.items.ItemBlockApparatusFragile;
import net.anvilcraft.thaummach.items.ItemBlockApparatusMetal;
import net.minecraft.block.Block;

public class TMBlocks {
    public static Block apparatusFragile;
    public static Block apparatusMetal;

    public static void init() {
        apparatusFragile = new BlockApparatusFragile();
        apparatusMetal = new BlockApparatusMetal();

        GameRegistry.registerBlock(apparatusFragile, ItemBlockApparatusFragile.class, "apparatus_fragile");
        GameRegistry.registerBlock(apparatusMetal, ItemBlockApparatusMetal.class, "apparatus_metal");
    }
}
