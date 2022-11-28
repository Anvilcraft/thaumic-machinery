package net.anvilcraft.thaummach;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
        proxy.preInit();
        proxy.registerTileEntities();
        TMBlocks.init();
    }
}
