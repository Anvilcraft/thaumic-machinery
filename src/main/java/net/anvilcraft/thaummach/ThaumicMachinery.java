package net.anvilcraft.thaummach;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.anvilcraft.thaummach.entities.EntitySingularity;

@Mod(modid = "thaummach")
public class ThaumicMachinery {
    @SidedProxy(
        modId = "thaummach",
        serverSide = "net.anvilcraft.thaummach.CommonProxy",
        clientSide = "net.anvilcraft.thaummach.ClientProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        proxy.registerTileEntities();
        TMBlocks.init();
        TMItems.init();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
        int entId = 0;
        EntityRegistry.registerModEntity(
            EntitySingularity.class, "singularity", entId++, this, 64, 2, true
        );
        proxy.init();
    }
}
