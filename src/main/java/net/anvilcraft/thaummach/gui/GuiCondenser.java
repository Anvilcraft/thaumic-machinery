package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.container.ContainerCondenser;
import net.anvilcraft.thaummach.tiles.TileCondenser;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCondenser extends GuiContainer {
    private TileCondenser arcaneCondenser;

    public GuiCondenser(InventoryPlayer inventoryplayer, TileCondenser tileCondenser) {
        super(new ContainerCondenser(inventoryplayer, tileCondenser));
        this.arcaneCondenser = tileCondenser;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Vis Condenser", 54, 5, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/condenser.png")
        );
        int l = (super.width - super.xSize) / 2;
        int i1 = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, super.xSize, super.ySize);
        int k1 = (int) (35.0F * this.arcaneCondenser.degredation / 4550.0F);
        this.drawTexturedModalRect(l + 78, i1 + 23 + 35 - k1, 176, 35 - k1, 20, k1);
        int moon = super.mc.theWorld.getMoonPhase();
        this.drawTexturedModalRect(l + 84, i1 + 68, 200, moon * 8, 8, 8);

        this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);

        for (int upgIdx = 0; upgIdx <= 1; upgIdx++) {
            int upgMeta = this.arcaneCondenser.getUpgrades()[upgIdx];
            UtilsFX.renderUpgradeIntoGUI(this, upgMeta, l + 56 + 48 * upgIdx, i1 + 56);
        }
    }
}
