package net.anvilcraft.thaummach.render.apparatus.apparati.fragile;

import net.anvilcraft.thaummach.blocks.BlockApparatusFragile;
import net.anvilcraft.thaummach.render.apparatus.AbstractTankApparatusRenderer;
import net.minecraft.util.IIcon;

public class ConduitTankApparatusRenderer
    extends AbstractTankApparatusRenderer<BlockApparatusFragile> {
    public static final ConduitTankApparatusRenderer INSTANCE
        = new ConduitTankApparatusRenderer();

    @Override
    public IIcon getBottomIcon(BlockApparatusFragile block) {
        return block.iconTankBottom;
    }

    @Override
    public IIcon getSideIcon(BlockApparatusFragile block) {
        return block.iconTankSide;
    }
}
