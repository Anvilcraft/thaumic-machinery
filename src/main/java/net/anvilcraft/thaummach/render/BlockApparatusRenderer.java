package net.anvilcraft.thaummach.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.anvilcraft.thaummach.blocks.BlockApparatus;
import net.anvilcraft.thaummach.render.apparatus.IApparatusRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class BlockApparatusRenderer implements ISimpleBlockRenderingHandler {
    public static int RI;

    @Override
    public void
    renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (block instanceof BlockApparatus) {
            IApparatusRenderer ren
                = ((BlockApparatus) block).getApparatusRenderer(metadata);
            if (ren != null)
                ren.renderApparatus(null, renderer, 0, 0, 0, block, metadata, true);
        }
    }

    @Override
    public boolean renderWorldBlock(
        IBlockAccess world,
        int x,
        int y,
        int z,
        Block block,
        int modelId,
        RenderBlocks renderer
    ) {
        if (block instanceof BlockApparatus) {
            int meta = world.getBlockMetadata(x, y, z);
            IApparatusRenderer ren
                = ((BlockApparatus) block).getApparatusRenderer(meta);
            if (ren != null)
                ren.renderApparatus(world, renderer, x, y, z, block, meta, false);
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RI;
    }
}
