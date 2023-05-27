package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.ThaumicMachinery;
import net.anvilcraft.thaummach.container.ContainerDuplicator;
import net.anvilcraft.thaummach.packets.PacketDuplicatorSetRepeat;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDuplicator extends GuiContainer {
    private TileDuplicator duplicatorInventory;

    public GuiDuplicator(InventoryPlayer inventoryplayer, TileDuplicator tileEntity) {
        super(new ContainerDuplicator(inventoryplayer, tileEntity));
        this.duplicatorInventory = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        super.fontRendererObj.drawString("Duplicator", 8, 5, 0x404040);
        super.fontRendererObj.drawString("Inventory", 8, super.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/duplicator.png")
        );
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int i1;
        if (this.duplicatorInventory.isCooking()) {
            i1 = this.duplicatorInventory.getCookProgressScaled(25);
            this.drawTexturedModalRect(j + 54, k + 34, 176, 15, i1 + 1, 16);
        }

        if (!this.duplicatorInventory.repeat) {
            this.drawTexturedModalRect(j + 62, k + 48, 176, 0, 10, 10);
        } else {
            this.drawTexturedModalRect(j + 62, k + 48, 186, 0, 10, 10);
        }

        if (this.duplicatorInventory.boost > 0) {
            i1 = this.duplicatorInventory.getBoostScaled();
            this.drawTexturedModalRect(j + 157, k + 45 - i1, 208, 30 - i1, 7, i1);
        }

        super.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
        if (this.duplicatorInventory.getUpgrades()[0] >= 0) {
            this.drawTexturedModalRect(
                j + 152, k + 60, 16 * this.duplicatorInventory.getUpgrades()[0], 32, 16, 16
            );
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int sx = (super.width - super.xSize) / 2;
        int sy = (super.height - super.ySize) / 2;
        int k1 = i - (sx + 62);
        int l1 = j - (sy + 48);
        if (k1 >= 0 && l1 >= 0 && k1 < 10 && l1 <= 10) {
            Minecraft.getMinecraft().getSoundHandler().playSound(
                PositionedSoundRecord.func_147674_a(new ResourceLocation("random.orb"), 1.0f)
            );
            ThaumicMachinery.channel.sendToServer(new PacketDuplicatorSetRepeat(
                this.duplicatorInventory.xCoord,
                this.duplicatorInventory.yCoord,
                this.duplicatorInventory.zCoord,
                !this.duplicatorInventory.repeat
            ));
        }
    }
}
