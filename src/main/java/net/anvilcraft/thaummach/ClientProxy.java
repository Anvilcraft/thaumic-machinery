package net.anvilcraft.thaummach;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.entities.EntitySingularity;
import net.anvilcraft.thaummach.render.BlockApparatusRenderer;
import net.anvilcraft.thaummach.render.TileSealRenderer;
import net.anvilcraft.thaummach.render.entity.EntitySingularityRenderer;
import net.anvilcraft.thaummach.render.tile.TileBoreRenderer;
import net.anvilcraft.thaummach.render.tile.TileConduitPumpRenderer;
import net.anvilcraft.thaummach.render.tile.TileCrystallizerRenderer;
import net.anvilcraft.thaummach.tiles.TileBore;
import net.anvilcraft.thaummach.tiles.TileConduit;
import net.anvilcraft.thaummach.tiles.TileConduitPump;
import net.anvilcraft.thaummach.tiles.TileConduitTank;
import net.anvilcraft.thaummach.tiles.TileConduitValve;
import net.anvilcraft.thaummach.tiles.TileConduitValveAdvanced;
import net.anvilcraft.thaummach.tiles.TileCrucible;
import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.anvilcraft.thaummach.tiles.TileFilter;
import net.anvilcraft.thaummach.tiles.TilePurifier;
import net.anvilcraft.thaummach.tiles.TileSeal;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        BlockApparatusRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockApparatusRenderer());
    }

    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(
            EntitySingularity.class, new EntitySingularityRenderer()
        );

        FMLCommonHandler.instance().bus().register(new RenderTicker());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileConduit.class, "conduit");
        GameRegistry.registerTileEntity(TileConduitTank.class, "conduit_tank");
        GameRegistry.registerTileEntity(TileConduitValve.class, "conduit_valve");
        GameRegistry.registerTileEntity(
            TileConduitValveAdvanced.class, "conduit_valve_advanced"
        );
        GameRegistry.registerTileEntity(TileCrucible.class, "crucible");
        GameRegistry.registerTileEntity(TileFilter.class, "filter");
        GameRegistry.registerTileEntity(TilePurifier.class, "purifier");

        ClientRegistry.registerTileEntity(TileBore.class, "bore", new TileBoreRenderer());
        ClientRegistry.registerTileEntity(
            TileConduitPump.class, "conduit_pump", new TileConduitPumpRenderer()
        );
        ClientRegistry.registerTileEntity(
            TileCrystallizer.class, "crystallizer", new TileCrystallizerRenderer()
        );
        ClientRegistry.registerTileEntity(TileSeal.class, "seal", new TileSealRenderer());
    }
}
