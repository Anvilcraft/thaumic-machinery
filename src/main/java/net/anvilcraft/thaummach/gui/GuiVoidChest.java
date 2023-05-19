package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.container.ContainerVoidChest;
import net.anvilcraft.thaummach.tiles.TileVoidChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiVoidChest extends GuiContainer {
    public GuiVoidChest(InventoryPlayer inventoryplayer, TileVoidChest vc) {
        super(new ContainerVoidChest(inventoryplayer, vc));
        super.ySize = 239;
    }

    protected void drawGuiContainerForegroundLayer() {}

    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/voidchest.png")
        );
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
    }
}
