package net.anvilcraft.thaummach;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.items.ItemCrystallineBell;
import net.anvilcraft.thaummach.items.ItemDetector;
import net.anvilcraft.thaummach.items.ItemFocus;
import net.anvilcraft.thaummach.items.ItemRunicEssence;
import net.anvilcraft.thaummach.items.ItemSingularity;
import net.anvilcraft.thaummach.items.ItemSoulFragment;
import net.anvilcraft.thaummach.items.ItemUpgrade;
import net.anvilcraft.thaummach.items.ItemVoidBracelet;
import net.minecraft.item.Item;

public class TMItems {
    public static Item crystallineBell;
    public static Item detector;
    public static Item focus0;
    public static Item focus1;
    public static Item focus2;
    public static Item focus3;
    public static Item focus4;
    public static Item runicEssence;
    public static Item singularity;
    public static Item soulFragment;
    public static Item upgrade;
    public static Item voidBracelet;

    public static void init() {
        GameRegistry.registerItem(crystallineBell = new ItemCrystallineBell(), "crystalline_bell");

        GameRegistry.registerItem(detector = new ItemDetector(), "detector");

        GameRegistry.registerItem(focus0 = new ItemFocus(0), "focus0");
        GameRegistry.registerItem(focus1 = new ItemFocus(1), "focus1");
        GameRegistry.registerItem(focus2 = new ItemFocus(2), "focus2");
        GameRegistry.registerItem(focus3 = new ItemFocus(3), "focus3");
        GameRegistry.registerItem(focus4 = new ItemFocus(4), "focus4");

        GameRegistry.registerItem(runicEssence = new ItemRunicEssence(), "runic_essence");

        GameRegistry.registerItem(singularity = new ItemSingularity(), "singularity");

        GameRegistry.registerItem(soulFragment = new ItemSoulFragment(), "soul_fragment");

        GameRegistry.registerItem(upgrade = new ItemUpgrade(), "upgrade");

        GameRegistry.registerItem(voidBracelet = new ItemVoidBracelet(), "voidBracelet");
    }
}
