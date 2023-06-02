package net.anvilcraft.thaummach;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.alec.jalec.factories.AlecUnexpectedRuntimeErrorExceptionFactory;
import net.anvilcraft.thaummach.entities.EntitySingularity;
import net.anvilcraft.thaummach.gui.GuiArcaneFurnace;
import net.anvilcraft.thaummach.gui.GuiBore;
import net.anvilcraft.thaummach.gui.GuiCondenser;
import net.anvilcraft.thaummach.gui.GuiCrystallizer;
import net.anvilcraft.thaummach.gui.GuiDuplicator;
import net.anvilcraft.thaummach.gui.GuiEnchanter;
import net.anvilcraft.thaummach.gui.GuiGenerator;
import net.anvilcraft.thaummach.gui.GuiRepairer;
import net.anvilcraft.thaummach.gui.GuiSoulBrazier;
import net.anvilcraft.thaummach.gui.GuiVoidChest;
import net.anvilcraft.thaummach.gui.GuiVoidInterface;
import net.anvilcraft.thaummach.render.BlockApparatusRenderer;
import net.anvilcraft.thaummach.render.entity.EntitySingularityRenderer;
import net.anvilcraft.thaummach.render.tile.TileBoreRenderer;
import net.anvilcraft.thaummach.render.tile.TileCondenserRenderer;
import net.anvilcraft.thaummach.render.tile.TileConduitPumpRenderer;
import net.anvilcraft.thaummach.render.tile.TileCrystallizerRenderer;
import net.anvilcraft.thaummach.render.tile.TileDuplicatorRenderer;
import net.anvilcraft.thaummach.render.tile.TileEnchanterRenderer;
import net.anvilcraft.thaummach.render.tile.TileGeneratorRenderer;
import net.anvilcraft.thaummach.render.tile.TileRepairerRenderer;
import net.anvilcraft.thaummach.render.tile.TileSealRenderer;
import net.anvilcraft.thaummach.render.tile.TileVoidInterfaceRenderer;
import net.anvilcraft.thaummach.render.tile.TileVoidRenderer;
import net.anvilcraft.thaummach.tiles.TileArcaneFurnace;
import net.anvilcraft.thaummach.tiles.TileBore;
import net.anvilcraft.thaummach.tiles.TileCondenser;
import net.anvilcraft.thaummach.tiles.TileConduit;
import net.anvilcraft.thaummach.tiles.TileConduitPump;
import net.anvilcraft.thaummach.tiles.TileConduitTank;
import net.anvilcraft.thaummach.tiles.TileConduitValve;
import net.anvilcraft.thaummach.tiles.TileConduitValveAdvanced;
import net.anvilcraft.thaummach.tiles.TileCrucible;
import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.anvilcraft.thaummach.tiles.TileEnchanter;
import net.anvilcraft.thaummach.tiles.TileFilter;
import net.anvilcraft.thaummach.tiles.TileGenerator;
import net.anvilcraft.thaummach.tiles.TileMonolith;
import net.anvilcraft.thaummach.tiles.TilePurifier;
import net.anvilcraft.thaummach.tiles.TileRepairer;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.anvilcraft.thaummach.tiles.TileSoulBrazier;
import net.anvilcraft.thaummach.tiles.TileVoidChest;
import net.anvilcraft.thaummach.tiles.TileVoidCube;
import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.anvilcraft.thaummach.tiles.TileVoidLock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(
            EntitySingularity.class, new EntitySingularityRenderer()
        );

        FMLCommonHandler.instance().bus().register(new RenderTicker());

        BlockApparatusRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockApparatusRenderer());
    }

    @Override
    public void registerTileEntities() {
        // clang-format off
        GameRegistry.registerTileEntity(TileArcaneFurnace.class, "arcane_furnace");
        GameRegistry.registerTileEntity(TileConduit.class, "conduit");
        GameRegistry.registerTileEntity(TileConduitTank.class, "conduit_tank");
        GameRegistry.registerTileEntity(TileConduitValve.class, "conduit_valve");
        GameRegistry.registerTileEntity(TileConduitValveAdvanced.class, "conduit_valve_advanced");
        GameRegistry.registerTileEntity(TileCrucible.class, "crucible");
        GameRegistry.registerTileEntity(TileFilter.class, "filter");
        GameRegistry.registerTileEntity(TilePurifier.class, "purifier");
        GameRegistry.registerTileEntity(TileSoulBrazier.class, "soul_brazier");

        ClientRegistry.registerTileEntity(TileBore.class, "bore", new TileBoreRenderer());
        ClientRegistry.registerTileEntity(TileCondenser.class, "condenser", new TileCondenserRenderer());
        ClientRegistry.registerTileEntity(TileConduitPump.class, "conduit_pump", new TileConduitPumpRenderer());
        ClientRegistry.registerTileEntity(TileCrystallizer.class, "crystallizer", new TileCrystallizerRenderer());
        ClientRegistry.registerTileEntity(TileDuplicator.class, "duplicator", new TileDuplicatorRenderer());
        ClientRegistry.registerTileEntity(TileEnchanter.class, "enchanter", new TileEnchanterRenderer());
        ClientRegistry.registerTileEntity(TileGenerator.class, "generator", new TileGeneratorRenderer());
        ClientRegistry.registerTileEntity(TileMonolith.class, "monolith", new TileVoidRenderer());
        ClientRegistry.registerTileEntity(TileRepairer.class, "repairer", new TileRepairerRenderer());
        ClientRegistry.registerTileEntity(TileSeal.class, "seal", new TileSealRenderer());
        ClientRegistry.registerTileEntity(TileVoidChest.class, "void_chest", new TileVoidRenderer());
        ClientRegistry.registerTileEntity(TileVoidCube.class, "void_cube", new TileVoidRenderer());
        ClientRegistry.registerTileEntity(TileVoidInterface.class, "void_interface", new TileVoidInterfaceRenderer());
        ClientRegistry.registerTileEntity(TileVoidLock.class, "void_lock", new TileVoidRenderer());
        // clang-format on
    }

    @Override
    public Object
    getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        switch (GuiID.get(id)) {
            case ARCANE_FURNACE:
                return new GuiArcaneFurnace(player.inventory, (TileArcaneFurnace) te);

            case BORE:
                return new GuiBore(player.inventory, (TileBore) te);

            case CONDENSER:
                return new GuiCondenser(player.inventory, (TileCondenser) te);

            case CRYSTALLIZER:
                return new GuiCrystallizer(player.inventory, (TileCrystallizer) te);

            case DUPLICATOR:
                return new GuiDuplicator(player.inventory, (TileDuplicator) te);

            case ENCHANTER:
                return new GuiEnchanter(player.inventory, (TileEnchanter) te);

            case GENERATOR:
                return new GuiGenerator((TileGenerator) te);

            case REPAIRER:
                return new GuiRepairer(player.inventory, (TileRepairer) te);

            case SOUL_BRAZIER:
                return new GuiSoulBrazier(player.inventory, (TileSoulBrazier) te);

            case VOID_CHEST:
                return new GuiVoidChest(player.inventory, (TileVoidChest) te);

            case VOID_INTERFACE:
                return new GuiVoidInterface(player.inventory, (TileVoidInterface) te);

            default:
                throw AlecUnexpectedRuntimeErrorExceptionFactory.PLAIN.createAlecException(
                    "invalid GUI ID"
                );
        }
    }
}
