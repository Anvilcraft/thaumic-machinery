package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.container.ContainerSoulBrazier;
import net.anvilcraft.thaummach.tiles.TileSoulBrazier;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSoulBrazier extends GuiContainer {
    private TileSoulBrazier brazier;

    public GuiSoulBrazier(InventoryPlayer inventoryplayer, TileSoulBrazier soulbrazier) {
        super(new ContainerSoulBrazier(inventoryplayer, soulbrazier));
        this.brazier = soulbrazier;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Soul Brazier", 57, 5, 0xf0f0f0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/soul_brazier.png")
        );
        int l = (super.width - super.xSize) / 2;
        int i1 = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, super.xSize, super.ySize);
        int k1 = (int) ((float) (16 * this.brazier.burnTime) / 6000.0F);
        this.drawTexturedModalRect(l + 80, i1 + 35 - k1, 176, 16 - k1, 16, k1);
        int moon = super.mc.theWorld.getMoonPhase();
        this.drawTexturedModalRect(l + 84, i1 + 66, 200, moon * 8, 8, 8);
    }
}
