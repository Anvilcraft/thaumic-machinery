package net.anvilcraft.thaummach.render.apparatus;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * IApparatusRenderer is an interface that provides an interface for rendering an
 * apparatus block.
 *
 * It is an attempt to debullshit azanor's rendering code.
 */
public interface IApparatusRenderer {
    public void renderApparatus(
        IBlockAccess w,
        RenderBlocks rb,
        int x,
        int y,
        int z,
        Block block,
        int meta,
        boolean inv
    );
}
