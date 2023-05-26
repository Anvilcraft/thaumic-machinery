package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.container.ContainerRepairer;
import net.anvilcraft.thaummach.tiles.TileRepairer;
import net.anvilcraft.thaummach.utils.UtilsFX;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiRepairer extends GuiContainer {
    private TileRepairer repairerInventory;

    public GuiRepairer(InventoryPlayer inventoryplayer, TileRepairer tileEntity) {
        super(new ContainerRepairer(inventoryplayer, tileEntity));
        this.repairerInventory = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Restorer", 30, 5, 0x404040);
        super.fontRendererObj.drawString("Inventory", 30, super.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/repairer.png")
        );
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        if (this.repairerInventory.boost > 0) {
            int i1 = this.repairerInventory.getBoostScaled();
            this.drawTexturedModalRect(j + 135, k + 46 - i1, 208, 30 - i1, 7, i1);
        }

        if (this.repairerInventory.getUpgrades()[0] >= 0) {
            super.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
            UtilsFX.renderUpgradeIntoGUI(
                this, this.repairerInventory.getUpgrades()[0], j + 130, k + 54
            );
        }
    }
}
