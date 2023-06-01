package net.anvilcraft.thaummach.gui;

import java.util.Random;

import net.anvilcraft.thaummach.ThaumicMachinery;
import net.anvilcraft.thaummach.container.ContainerEnchanter;
import net.anvilcraft.thaummach.packets.PacketEnchanterStart;
import net.anvilcraft.thaummach.tiles.TileEnchanter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GuiEnchanter extends GuiContainer {
    private static ModelBook bookModel = new ModelBook();
    private Random field_40230_x = new Random();
    public int field_40227_h;
    public float field_40229_i;
    public float field_40225_j;
    public float field_40226_k;
    public float field_40223_l;
    public float field_40224_m;
    public float field_40221_n;
    ItemStack field_40222_o;
    public TileEnchanter te;

    public GuiEnchanter(InventoryPlayer inventoryplayer, TileEnchanter tile) {
        super(new ContainerEnchanter(inventoryplayer, tile));
        this.te = tile;
        super.ySize = 182;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(/* useless parameters: */ int alec1, int alec2) {
        super.fontRendererObj.drawString("Thaumic Enchanter", 12, 6, 0x404040);
        super.fontRendererObj.drawString("Inventory", 8, super.ySize - 96 + 2, 0x404040);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.doStuff();
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int l = (super.width - super.xSize) / 2;
        int i1 = (super.height - super.ySize) / 2;

        for (int j1 = 0; j1 < 3; ++j1) {
            int k1 = i - (l + 60);
            int l1 = j - (i1 + 22 + 19 * j1);
            if (k1 >= 0 && l1 >= 0 && k1 < 108 && l1 < 19 && this.te.enchantLevels[j1] > 0
                && this.te.getStackInSlot(0) != null) {
                ThaumicMachinery.channel.sendToServer(
                    new PacketEnchanterStart(this.te.xCoord, this.te.yCoord, this.te.zCoord, j1)
                );
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/enchanter.png")
        );
        int l = (super.width - super.xSize) / 2;
        int i1 = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, super.xSize, super.ySize);
        int p1 = this.te.getProgressScaled(25);
        this.drawTexturedModalRect(l + 21, i1 + 71, 176, 0, p1, 3);
        GL11.glPushMatrix();
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        ScaledResolution scaledresolution
            = new ScaledResolution(super.mc, super.mc.displayWidth, super.mc.displayHeight);
        GL11.glViewport(
            (scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(),
            (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(),
            320 * scaledresolution.getScaleFactor(),
            240 * scaledresolution.getScaleFactor()
        );
        GL11.glTranslatef(-0.34F, 0.28F, 0.0F);
        GLU.gluPerspective(90.0F, 1.333333F, 9.0F, 80.0F);
        float f1 = 1.0F;
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        RenderHelper.enableStandardItemLighting();
        GL11.glTranslatef(0.0F, 3.3F, -16.0F);
        GL11.glScalef(f1, f1, f1);
        float f2 = 5.0F;
        GL11.glScalef(f2, f2, f2);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/models/book.png")
        );
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        float f3 = this.field_40221_n + (this.field_40224_m - this.field_40221_n) * f;
        GL11.glTranslatef((1.0F - f3) * 0.2F, (1.0F - f3) * 0.1F, (1.0F - f3) * 0.25F);
        GL11.glRotatef(-(1.0F - f3) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        float f4 = this.field_40225_j + (this.field_40229_i - this.field_40225_j) * f + 0.25F;
        float f5 = this.field_40225_j + (this.field_40229_i - this.field_40225_j) * f + 0.75F;
        f4 = (f4 - (float) MathHelper.truncateDoubleToInt((double) f4)) * 1.6F - 0.3F;
        f5 = (f5 - (float) MathHelper.truncateDoubleToInt((double) f5)) * 1.6F - 0.3F;
        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f5 < 0.0F) {
            f5 = 0.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        GL11.glEnable(32826);
        bookModel.render((Entity) null, 0.0F, f4, f5, f3, 0.0F, 0.0625F);
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glMatrixMode(5889);
        GL11.glViewport(0, 0, super.mc.displayWidth, super.mc.displayHeight);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("thaummach", "textures/guis/enchanter.png")
        );
        EnchantmentNameParts.instance.reseedRandomGenerator(this.te.nameSeed);

        for (int j1 = 0; j1 < 3; ++j1) {
            String s = EnchantmentNameParts.instance.generateNewRandomName();
            super.zLevel = 0.0F;
            this.mc.renderEngine.bindTexture(
                new ResourceLocation("thaummach", "textures/guis/enchanter.png")
            );
            int k1 = this.te.enchantLevels[j1] * 2 * (this.te.enchantLevels[j1] / 2);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.te.enchantLevels[j1] > 0) {
                String s1 = "" + k1;
                String s2 = "" + this.te.enchantLevels[j1];
                FontRenderer fontrenderer = super.mc.standardGalacticFontRenderer;
                int l1 = 6839882;
                int i2 = i - (l + 60);
                int j2 = j - (i1 + 22 + 19 * j1);
                if (i2 >= 0 && j2 >= 0 && i2 < 108 && j2 < 19) {
                    this.drawTexturedModalRect(l + 60, i1 + 22 + 19 * j1, 0, 220, 108, 19);
                    l1 = 16777088;
                } else {
                    this.drawTexturedModalRect(l + 60, i1 + 22 + 19 * j1, 0, 182, 108, 19);
                }

                if (this.te.enchantmentChoice == j1 && this.te.enchantmentCost > 0
                    && this.te.progress < (float) this.te.enchantmentCost) {
                    this.drawTexturedModalRect(l + 60, i1 + 22 + 19 * j1, 0, 201, 108, 19);
                }

                fontrenderer.drawSplitString(s, l + 62, i1 + 24 + 19 * j1, 104, l1);
                fontrenderer = super.mc.fontRenderer;
                l1 = 10764287;
                fontrenderer.drawStringWithShadow(
                    s1, l + 166 - fontrenderer.getStringWidth(s1), i1 + 24 + 19 * j1 + 8, l1
                );
                l1 = 8453920;
                fontrenderer.drawStringWithShadow(
                    s2, l + 166 - fontrenderer.getStringWidth(s2), i1 + 24 + 19 * j1 - 1, l1
                );
            }
        }
    }

    /**
     * No one knows exactly what this function does.
     */
    public void doStuff() {
        ItemStack itemstack = super.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual(itemstack, this.field_40222_o)) {
            this.field_40222_o = itemstack;

            do {
                this.field_40226_k
                    += (float) (this.field_40230_x.nextInt(4) - this.field_40230_x.nextInt(4));
            } while (this.field_40229_i <= this.field_40226_k + 1.0F
                     && this.field_40229_i >= this.field_40226_k - 1.0F);
        }

        ++this.field_40227_h;
        this.field_40225_j = this.field_40229_i;
        this.field_40221_n = this.field_40224_m;
        boolean flag = false;

        for (int i = 0; i < 3; ++i) {
            if (this.te.enchantLevels[i] != 0) {
                flag = true;
            }
        }

        if (flag) {
            this.field_40224_m += 0.2F;
        } else {
            this.field_40224_m -= 0.2F;
        }

        if (this.field_40224_m < 0.0F) {
            this.field_40224_m = 0.0F;
        }

        if (this.field_40224_m > 1.0F) {
            this.field_40224_m = 1.0F;
        }

        float f = (this.field_40226_k - this.field_40229_i) * 0.4F;
        float f1 = 0.2F;
        if (f < -f1) {
            f = -f1;
        }

        if (f > f1) {
            f = f1;
        }

        this.field_40223_l += (f - this.field_40223_l) * 0.9F;
        this.field_40229_i += this.field_40223_l;
    }
}
