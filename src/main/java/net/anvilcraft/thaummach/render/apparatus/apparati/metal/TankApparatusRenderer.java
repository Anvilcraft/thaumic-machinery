package net.anvilcraft.thaummach.render.apparatus.apparati.metal;

import net.anvilcraft.thaummach.blocks.BlockApparatusMetal;
import net.anvilcraft.thaummach.render.apparatus.AbstractTankApparatusRenderer;
import net.minecraft.util.IIcon;

public class TankApparatusRenderer
    extends AbstractTankApparatusRenderer<BlockApparatusMetal> {
    public static TankApparatusRenderer INSTANCE = new TankApparatusRenderer();

    @Override
    public IIcon getBottomIcon(BlockApparatusMetal block) {
        return block.iconTankBottom;
    }

    @Override
    public IIcon getSideIcon(BlockApparatusMetal block) {
        return block.iconTankSide;
    }
}
