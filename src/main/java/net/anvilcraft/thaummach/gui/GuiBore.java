package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.container.ContainerBore;
import net.anvilcraft.thaummach.tiles.TileBore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBore extends GuiContainer {
    private TileBore arcaneBore;

    public GuiBore(InventoryPlayer inventoryplayer, TileBore tileBore) {
        super(new ContainerBore(inventoryplayer, tileBore));
        this.arcaneBore = tileBore;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Arcane Bore", 54, 5, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/bore.png")
        );
        int l = (super.width - super.xSize) / 2;
        int i1 = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, super.xSize, super.ySize);
        if (this.arcaneBore.duration > 0) {
            int q = (int
            ) ((float) this.arcaneBore.duration / (float) this.arcaneBore.maxDuration
               * 46.0F);
            this.drawTexturedModalRect(l + 103, i1 + 66 - q, 176, 46 - q, 9, q + 1);
        }
    }
}
