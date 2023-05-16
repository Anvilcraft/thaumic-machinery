package net.anvilcraft.thaummach;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import dev.tilera.auracore.api.HelperLocation;
import net.anvilcraft.thaummach.entities.EntitySingularity;
import net.anvilcraft.thaummach.packets.IPacketFX;
import net.anvilcraft.thaummach.packets.PacketFXSparkle;
import net.anvilcraft.thaummach.packets.PacketFXWisp;
import net.minecraft.world.World;

@Mod(modid = "thaummach")
public class ThaumicMachinery {
    @SidedProxy(
        modId = "thaummach",
        serverSide = "net.anvilcraft.thaummach.CommonProxy",
        clientSide = "net.anvilcraft.thaummach.ClientProxy"
    )
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper channel;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        channel = NetworkRegistry.INSTANCE.newSimpleChannel("thaummach");
        int pktid = 0;
        channel.registerMessage(
            new PacketFXWisp.Handler(), PacketFXWisp.class, pktid++, Side.CLIENT
        );
        channel.registerMessage(
            new PacketFXSparkle.Handler(), PacketFXSparkle.class, pktid++, Side.CLIENT
        );

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

    public static void sendFXPacket(World worldObj, IPacketFX pkt) {
        HelperLocation loc = pkt.getLocation();
        channel.sendToAllAround(
            pkt, new TargetPoint(worldObj.provider.dimensionId, loc.x, loc.y, loc.z, 32)
        );
    }
}
