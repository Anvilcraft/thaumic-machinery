package net.anvilcraft.thaummach;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import dev.tilera.auracore.api.HelperLocation;
import net.anvilcraft.thaummach.entities.EntitySingularity;
import net.anvilcraft.thaummach.packets.IPacketFX;
import net.anvilcraft.thaummach.packets.PacketChangeVoidInterfaceChannel;
import net.anvilcraft.thaummach.packets.PacketChangeVoidInterfaceContainerPage;
import net.anvilcraft.thaummach.packets.PacketDuplicatorPress;
import net.anvilcraft.thaummach.packets.PacketDuplicatorSetRepeat;
import net.anvilcraft.thaummach.packets.PacketEnchanterStart;
import net.anvilcraft.thaummach.packets.PacketFXSparkle;
import net.anvilcraft.thaummach.packets.PacketFXWisp;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.anvilcraft.thaummach.worldgen.TMWorldGenerator;
import net.minecraft.world.World;

@Mod(modid = "thaummach")
public class ThaumicMachinery {
    @Mod.Instance("thaummach")
    public static ThaumicMachinery INSTANCE;

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
        // clang-format off
        channel.registerMessage(new PacketChangeVoidInterfaceChannel.Handler(), PacketChangeVoidInterfaceChannel.class, pktid++, Side.SERVER);
        channel.registerMessage(new PacketChangeVoidInterfaceContainerPage.Handler(), PacketChangeVoidInterfaceContainerPage.class, pktid++, Side.SERVER);
        channel.registerMessage(new PacketDuplicatorPress.Handler(), PacketDuplicatorPress.class, pktid++, Side.CLIENT);
        channel.registerMessage(new PacketDuplicatorSetRepeat.Handler(), PacketDuplicatorSetRepeat.class, pktid++, Side.SERVER);
        channel.registerMessage(new PacketEnchanterStart.Handler(), PacketEnchanterStart.class, pktid++, Side.SERVER);
        channel.registerMessage(new PacketFXSparkle.Handler(), PacketFXSparkle.class, pktid++, Side.CLIENT);
        channel.registerMessage(new PacketFXWisp.Handler(), PacketFXWisp.class, pktid++, Side.CLIENT);
        // clang-format on
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        GameRegistry.registerWorldGenerator(new TMWorldGenerator(), 0);

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

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent ev) {
        TileVoidInterface.VOID_INTERFACES.clear();
        TileSeal.SEALS.clear();
    }

    public static void sendFXPacket(World worldObj, IPacketFX pkt) {
        HelperLocation loc = pkt.getLocation();
        channel.sendToAllAround(
            pkt, new TargetPoint(worldObj.provider.dimensionId, loc.x, loc.y, loc.z, 32)
        );
    }
}
