package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.container.ContainerArcaneFurnace;
import net.anvilcraft.thaummach.tiles.TileArcaneFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiArcaneFurnace extends GuiContainer {
    private TileArcaneFurnace arcaneFurnaceInventory;

    public GuiArcaneFurnace(
        InventoryPlayer inventoryplayer, TileArcaneFurnace tileArcaneFurnace
    ) {
        super(new ContainerArcaneFurnace(inventoryplayer, tileArcaneFurnace));
        this.arcaneFurnaceInventory = tileArcaneFurnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Thaumic Furnace", 48, 5, 4210752);
        super.fontRendererObj.drawString("Inventory", 8, super.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/arcane_furnace.png")
        );
        int l = (super.width - super.xSize) / 2;
        int i1 = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, super.xSize, super.ySize);
        int j1;
        if (this.arcaneFurnaceInventory.isWorking()) {
            j1 = this.arcaneFurnaceInventory.getBurnTimeRemainingScaled(16);
            this.drawTexturedModalRect(l + 80, i1 + 32 + 16 - j1, 176, 16 - j1, 16, j1);
        } else if (this.arcaneFurnaceInventory.furnaceBurnTime > 0) {
            j1 = this.arcaneFurnaceInventory.getBurnTimeRemainingScaled(16);
            this.drawTexturedModalRect(l + 80, i1 + 32 + 16 - j1, 221, 16 - j1, 16, j1);
        }

        j1 = this.arcaneFurnaceInventory.getCookProgressScaled(26);
        this.drawTexturedModalRect(l + 75, i1 + 22, 176, 28, j1, 4);
    }
}
