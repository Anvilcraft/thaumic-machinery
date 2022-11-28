package net.anvilcraft.thaummach;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.tiles.TileConduit;
import net.anvilcraft.thaummach.tiles.TileConduitPump;
import net.anvilcraft.thaummach.tiles.TileConduitTank;
import net.anvilcraft.thaummach.tiles.TileConduitValve;
import net.anvilcraft.thaummach.tiles.TileConduitValveAdvanced;
import net.anvilcraft.thaummach.tiles.TileCrucible;
import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.anvilcraft.thaummach.tiles.TileFilter;
import net.anvilcraft.thaummach.tiles.TilePurifier;

public class CommonProxy {
    public void preInit() {}

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileConduit.class, "conduit");
        GameRegistry.registerTileEntity(TileConduitPump.class, "conduit_pump");
        GameRegistry.registerTileEntity(TileConduitTank.class, "conduit_tank");
        GameRegistry.registerTileEntity(TileConduitValve.class, "conduit_valve");
        GameRegistry.registerTileEntity(
            TileConduitValveAdvanced.class, "conduit_valve_advanced"
        );
        GameRegistry.registerTileEntity(TileCrucible.class, "crucible");
        GameRegistry.registerTileEntity(TileCrystallizer.class, "crystallizer");
        GameRegistry.registerTileEntity(TileFilter.class, "filter");
        GameRegistry.registerTileEntity(TilePurifier.class, "purifier");
    }
}
