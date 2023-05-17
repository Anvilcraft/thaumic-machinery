package net.anvilcraft.thaummach;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.blocks.BlockApparatusWood;
import net.anvilcraft.thaummach.blocks.BlockSeal;
import net.anvilcraft.thaummach.items.ItemBlockApparatusFragile;
import net.anvilcraft.thaummach.items.ItemBlockApparatusMetal;
import net.anvilcraft.thaummach.items.ItemBlockApparatusWood;
import net.anvilcraft.thaummach.items.ItemSeal;
import net.minecraft.block.Block;

public class TMBlocks {
    public static Block apparatusFragile;
    public static Block apparatusMetal;
    public static Block apparatusWood;
    public static Block seal;

    public static void init() {
        apparatusFragile = new BlockApparatusFragile();
        apparatusMetal = new BlockApparatusMetal();
        apparatusWood = new BlockApparatusWood();
        seal = new BlockSeal();

        GameRegistry.registerBlock(
            apparatusFragile, ItemBlockApparatusFragile.class, "apparatus_fragile"
        );
        GameRegistry.registerBlock(
            apparatusMetal, ItemBlockApparatusMetal.class, "apparatus_metal"
        );
        GameRegistry.registerBlock(
            apparatusWood, ItemBlockApparatusWood.class, "apparatus_wood"
        );
        GameRegistry.registerBlock(seal, ItemSeal.class, "seal");
    }
}
