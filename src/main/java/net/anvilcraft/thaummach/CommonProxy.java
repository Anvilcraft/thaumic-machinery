package net.anvilcraft.thaummach;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.thaummach.container.ContainerArcaneFurnace;
import net.anvilcraft.thaummach.container.ContainerBore;
import net.anvilcraft.thaummach.container.ContainerCrystallizer;
import net.anvilcraft.thaummach.container.ContainerSoulBrazier;
import net.anvilcraft.thaummach.container.ContainerVoidChest;
import net.anvilcraft.thaummach.container.ContainerVoidInterface;
import net.anvilcraft.thaummach.tiles.TileArcaneFurnace;
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
import net.anvilcraft.thaummach.tiles.TileSoulBrazier;
import net.anvilcraft.thaummach.tiles.TileVoidChest;
import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
    public void preInit() {}

    public void init() {}

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileArcaneFurnace.class, "arcane_furnace");
        GameRegistry.registerTileEntity(TileBore.class, "bore");
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
        GameRegistry.registerTileEntity(TileSeal.class, "seal");
        GameRegistry.registerTileEntity(TileVoidChest.class, "voidChest");
        GameRegistry.registerTileEntity(TileVoidInterface.class, "voidInterface");
        GameRegistry.registerTileEntity(TileSoulBrazier.class, "soulBrazier");
    }

    @Override
    public Object
    getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        switch (GuiID.get(id)) {
            case ARCANE_FURNACE:
                return new ContainerArcaneFurnace(
                    player.inventory, (TileArcaneFurnace) te
                );

            case BORE:
                return new ContainerBore(player.inventory, (TileBore) te);

            case CRYSTALLIZER:
                return new ContainerCrystallizer(player.inventory, (TileCrystallizer) te);

            case SOUL_BRAZIER:
                return new ContainerSoulBrazier(player.inventory, (TileSoulBrazier) te);

            case VOID_CHEST:
                return new ContainerVoidChest(player.inventory, (TileVoidChest) te);

            case VOID_INTERFACE:
                return new ContainerVoidInterface(
                    player.inventory, (TileVoidInterface) te
                );

            default:
                throw new IllegalArgumentException("ALEC");
        }
    }

    @Override
    public Object
    getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
