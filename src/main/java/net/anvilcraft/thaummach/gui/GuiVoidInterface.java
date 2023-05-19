package net.anvilcraft.thaummach.gui;

import net.anvilcraft.thaummach.ThaumicMachinery;
import net.anvilcraft.thaummach.container.ContainerVoidInterface;
import net.anvilcraft.thaummach.packets.PacketChangeVoidInterfaceChannel;
import net.anvilcraft.thaummach.packets.PacketChangeVoidInterfaceContainerPage;
import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiVoidInterface extends GuiContainer {
    TileVoidInterface voidInventory;

    public GuiVoidInterface(InventoryPlayer inventoryplayer, TileVoidInterface vc) {
        super(new ContainerVoidInterface(inventoryplayer, vc));
        this.voidInventory = vc;
        super.ySize = 239;
        super.xSize = 219;
        vc.openInventory();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int alec1, int alec2) {
        if (this.voidInventory.getLinkedSize() > 1) {
            super.fontRendererObj.drawStringWithShadow(
                this.voidInventory.current + 1 + "/" + this.voidInventory.getLinkedSize(),
                195,
                88,
                0x704070
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int qq, int ww) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/void_interface.png")
        );
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        this.drawTexturedModalRect(
            j + 178, k + 24, 224, this.voidInventory.network * 32, 32, 32
        );
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int sx = (super.width - super.xSize) / 2;
        int sy = (super.height - super.ySize) / 2;
        int k1 = i - (sx + 178);
        int l1 = j - (sy + 64);
        if (k1 >= 0 && l1 >= 0 && k1 < 14 && l1 <= 28) {
            this.voidInventory.getWorldObj().playSoundEffect(
                (double) this.voidInventory.xCoord + 0.5,
                (double) this.voidInventory.yCoord + 0.5,
                (double) this.voidInventory.zCoord + 0.5,
                "random.click",
                0.1F,
                0.6F
            );
            if (this.voidInventory.current > 0) {
                --this.voidInventory.current;
                ThaumicMachinery.channel.sendToServer(
                    new PacketChangeVoidInterfaceContainerPage(this.voidInventory.current)
                );
            }
        }

        k1 = i - (sx + 178);
        l1 = j - (sy + 92);
        if (k1 >= 0 && l1 >= 0 && k1 < 14 && l1 <= 28) {
            this.voidInventory.getWorldObj().playSoundEffect(
                (double) this.voidInventory.xCoord + 0.5,
                (double) this.voidInventory.yCoord + 0.5,
                (double) this.voidInventory.zCoord + 0.5,
                "random.click",
                0.1F,
                0.8F
            );

            if (this.voidInventory.current < this.voidInventory.getLinkedSize() - 1) {
                ++this.voidInventory.current;
                ThaumicMachinery.channel.sendToServer(
                    new PacketChangeVoidInterfaceContainerPage(this.voidInventory.current)
                );
            }
        }

        k1 = i - (sx + 178);
        l1 = j - (sy + 24);
        if (k1 >= 0 && l1 >= 0 && k1 < 32 && l1 <= 32) {
            this.voidInventory.getWorldObj().playSoundEffect(
                (double) this.voidInventory.xCoord + 0.5,
                (double) this.voidInventory.yCoord + 0.5,
                (double) this.voidInventory.zCoord + 0.5,
                "random.click",
                0.1F,
                0.8F
            );

            byte newChannel = (byte) (this.voidInventory.network + (k == 0 ? 1 : -1));

            if (newChannel < 0)
                newChannel = 5;
            else if (newChannel >= 6)
                newChannel = 0;

            ThaumicMachinery.channel.sendToServer(new PacketChangeVoidInterfaceChannel(
                this.voidInventory.xCoord,
                this.voidInventory.yCoord,
                this.voidInventory.zCoord,
                newChannel
            ));
        }
    }
}
