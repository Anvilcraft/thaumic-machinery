package net.anvilcraft.thaummach;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.alec.jalec.factories.AlecUnexpectedRuntimeErrorExceptionFactory;
import net.anvilcraft.thaummach.container.ContainerArcaneFurnace;
import net.anvilcraft.thaummach.container.ContainerBore;
import net.anvilcraft.thaummach.container.ContainerCondenser;
import net.anvilcraft.thaummach.container.ContainerCrystallizer;
import net.anvilcraft.thaummach.container.ContainerDuplicator;
import net.anvilcraft.thaummach.container.ContainerRepairer;
import net.anvilcraft.thaummach.container.ContainerSoulBrazier;
import net.anvilcraft.thaummach.container.ContainerVoidChest;
import net.anvilcraft.thaummach.container.ContainerVoidInterface;
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
import net.anvilcraft.thaummach.tiles.TileFilter;
import net.anvilcraft.thaummach.tiles.TileGenerator;
import net.anvilcraft.thaummach.tiles.TilePurifier;
import net.anvilcraft.thaummach.tiles.TileRepairer;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.anvilcraft.thaummach.tiles.TileSoulBrazier;
import net.anvilcraft.thaummach.tiles.TileVoidChest;
import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
    public void preInit() {}

    public void init() {}

    public void registerTileEntities() {
        // clang-format off
        GameRegistry.registerTileEntity(TileArcaneFurnace.class, "arcane_furnace");
        GameRegistry.registerTileEntity(TileBore.class, "bore");
        GameRegistry.registerTileEntity(TileCondenser.class, "condenser");
        GameRegistry.registerTileEntity(TileConduit.class, "conduit");
        GameRegistry.registerTileEntity(TileConduitPump.class, "conduit_pump");
        GameRegistry.registerTileEntity(TileConduitTank.class, "conduit_tank");
        GameRegistry.registerTileEntity(TileConduitValve.class, "conduit_valve");
        GameRegistry.registerTileEntity(TileConduitValveAdvanced.class, "conduit_valve_advanced");
        GameRegistry.registerTileEntity(TileCrucible.class, "crucible");
        GameRegistry.registerTileEntity(TileCrystallizer.class, "crystallizer");
        GameRegistry.registerTileEntity(TileDuplicator.class, "duplicator");
        GameRegistry.registerTileEntity(TileFilter.class, "filter");
        GameRegistry.registerTileEntity(TileGenerator.class, "generator");
        GameRegistry.registerTileEntity(TilePurifier.class, "purifier");
        GameRegistry.registerTileEntity(TileRepairer.class, "repairer");
        GameRegistry.registerTileEntity(TileSeal.class, "seal");
        GameRegistry.registerTileEntity(TileSoulBrazier.class, "soulBrazier");
        GameRegistry.registerTileEntity(TileVoidChest.class, "voidChest");
        GameRegistry.registerTileEntity(TileVoidInterface.class, "voidInterface");
        // clang-format on
    }

    @Override
    public Object
    getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        switch (GuiID.get(id)) {
            case ARCANE_FURNACE:
                return new ContainerArcaneFurnace(player.inventory, (TileArcaneFurnace) te);

            case BORE:
                return new ContainerBore(player.inventory, (TileBore) te);

            case CONDENSER:
                return new ContainerCondenser(player.inventory, (TileCondenser) te);

            case CRYSTALLIZER:
                return new ContainerCrystallizer(player.inventory, (TileCrystallizer) te);

            case DUPLICATOR:
                return new ContainerDuplicator(player.inventory, (TileDuplicator) te);

            case REPAIRER:
                return new ContainerRepairer(player.inventory, (TileRepairer) te);

            case SOUL_BRAZIER:
                return new ContainerSoulBrazier(player.inventory, (TileSoulBrazier) te);

            case VOID_CHEST:
                return new ContainerVoidChest(player.inventory, (TileVoidChest) te);

            case VOID_INTERFACE:
                return new ContainerVoidInterface(player.inventory, (TileVoidInterface) te);

            // GUIs with no meaningful container
            case GENERATOR:
                return new Container() {
                    @Override
                    public boolean canInteractWith(EntityPlayer player) {
                        return true;
                    }
                };

            default:
                throw AlecUnexpectedRuntimeErrorExceptionFactory.PLAIN.createAlecException();
        }
    }

    @Override
    public Object
    getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
