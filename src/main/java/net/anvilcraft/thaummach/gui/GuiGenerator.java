package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.tiles.TileGenerator;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiGenerator extends GuiScreen {
    private TileGenerator generator;

    public GuiGenerator(TileGenerator tileGenerator) {
        this.generator = tileGenerator;
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        super.drawScreen(i, j, f);
        int xSize = 176;
        int ySize = 82;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/generator.png")
        );
        int l = (super.width - xSize) / 2;
        int i1 = (super.height - ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        int k1 = (int
        ) (46.0F * (float) this.generator.storedEnergy / (float) this.generator.energyMax
        );
        this.drawTexturedModalRect(l + 84, i1 + 68 - k1, 176, 46 - k1, 9, k1);
        int moon = super.mc.theWorld.getMoonPhase();
        this.drawTexturedModalRect(l + 108, i1 + 41, 192, moon * 8, 8, 8);

        super.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
        for (int k = 0; k <= 1; k++) {
            UtilsFX.renderUpgradeIntoGUI(
                this, (int)this.generator.getUpgrades()[k], l + 56, i1 + 25 + 22 * k
            );
        }

        super.fontRendererObj.drawString("Thaumic Generator", l + 42, i1 + 5, 0x404040);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
