package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.TMItems;
import net.anvilcraft.thaummach.container.ContainerCrystallizer;
import net.anvilcraft.thaummach.tiles.TileCrystallizer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCrystallizer extends GuiContainer {
    private TileCrystallizer crystallizerInventory;

    public GuiCrystallizer(
        InventoryPlayer inventoryplayer, TileCrystallizer tileInfuser
    ) {
        super(new ContainerCrystallizer(inventoryplayer, tileInfuser));
        this.crystallizerInventory = tileInfuser;
        super.ySize = 239;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Crystallizer", 5, 5, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/crystallizer.png")
        );
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int i1;
        if (this.crystallizerInventory.isCooking()) {
            i1 = this.crystallizerInventory.getCookProgressScaled(46);
            this.drawTexturedModalRect(j + 160, k + 151 - i1, 176, 46 - i1, 9, i1);
        }

        if (this.crystallizerInventory.boost > 0) {
            i1 = this.crystallizerInventory.getBoostScaled();
            this.drawTexturedModalRect(j + 161, k + 38 - i1, 192, 30 - i1, 7, i1);
        }

        if (this.crystallizerInventory.getUpgrades()[0] >= 0) {
            IIcon itemIcon = TMItems.upgrade.getIconFromDamage(
                this.crystallizerInventory.getUpgrades()[0]
            );

            this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);

            this.drawTexturedModelRectFromIcon(j + 8, k + 128, itemIcon, 16, 16);
        }
    }
}
