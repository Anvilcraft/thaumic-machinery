package net.anvilcraft.thaummach;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.items.ItemCrystallineBell;
import net.anvilcraft.thaummach.items.ItemFocus;
import net.anvilcraft.thaummach.items.ItemRunicEssence;
import net.anvilcraft.thaummach.items.ItemSingularity;
import net.minecraft.item.Item;

public class TMItems {
    public static Item crystallineBell;
    public static Item focus0;
    public static Item focus1;
    public static Item focus2;
    public static Item focus3;
    public static Item focus4;
    public static Item runicEssence;
    public static Item singularity;

    public static void init() {
        crystallineBell = new ItemCrystallineBell();

        focus0 = new ItemFocus(0);
        focus1 = new ItemFocus(1);
        focus2 = new ItemFocus(2);
        focus3 = new ItemFocus(3);
        focus4 = new ItemFocus(4);

        runicEssence = new ItemRunicEssence();

        singularity = new ItemSingularity();

        GameRegistry.registerItem(crystallineBell, "crystalline_bell");

        GameRegistry.registerItem(focus0, "focus0");
        GameRegistry.registerItem(focus1, "focus1");
        GameRegistry.registerItem(focus2, "focus2");
        GameRegistry.registerItem(focus3, "focus3");
        GameRegistry.registerItem(focus4, "focus4");

        GameRegistry.registerItem(runicEssence, "runic_essence");

        GameRegistry.registerItem(singularity, "singularity");
    }
}
