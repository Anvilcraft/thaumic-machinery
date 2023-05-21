package net.anvilcraft.thaummach;

import java.util.Collection;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import dev.tilera.auracore.client.AuraManagerClient;
import dev.tilera.auracore.client.AuraManagerClient.NodeStats;
import net.anvilcraft.thaummach.render.PortalRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTicker {
    @SubscribeEvent
    public void onRenderTickEvent(RenderTickEvent ev) {
        if (Minecraft.getMinecraft().theWorld == null || ev.phase != Phase.END)
            return;

        for (PortalRenderer ren : PortalRenderer.INSTANCES) {
            // TODO: optimize
            ren.createPortalView();
        }

        EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
        if (pl == null)
            // WTF
            return;

        int foundDetectorType = -1;

        for (int i = 0; i < pl.inventory.getSizeInventory(); i++) {
            ItemStack stack = pl.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == TMItems.detector) {
                foundDetectorType = stack.getItemDamage();
                break;
            }
        }

        if (foundDetectorType >= 0) {
            GuiDetector.INSTANCE.render(foundDetectorType);
        }
    }

    public static class GuiDetector extends Gui {
        public static final GuiDetector INSTANCE = new GuiDetector();

        public NodeStats findNode() {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            NodeStats closest = null;
            double closestDistance = Double.MAX_VALUE;
            Collection<NodeStats> col = AuraManagerClient.auraClientList.values();
            for (NodeStats stats : col) {
                int dim = stats.dimension;
                if (player.dimension != dim)
                    continue;
                double px = stats.x;
                double py = stats.y;
                double pz = stats.z;
                double xd = px - player.posX;
                double yd = py - player.posY;
                double zd = pz - player.posZ;
                double distSq = xd * xd + yd * yd + zd * zd;
                if (!(distSq < closestDistance))
                    continue;
                closestDistance = distSq;
                closest = stats;
            }
            return closest;
        }

        public void render(int type) {
            NodeStats aura = this.findNode();
            if (aura == null)
                return;

            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution scaledresolution
                = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            mc.entityRenderer.setupOverlayRendering();
            GL11.glEnable(3042);
            GL11.glEnable(32826);
            RenderHelper.enableStandardItemLighting();
            RenderHelper.disableStandardItemLighting();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(
                new ResourceLocation("thaummach", "textures/guis/detector.png")
            );
            super.zLevel = -90.0F;
            int sv = 48 * (aura.base * 2 - aura.level) / (aura.base * 2);
            int st = 48 * (aura.base * 2 - aura.taint) / (aura.base * 2);
            if (type == 0 || type == 2 || type >= 3) {
                //if (aura.goodVibes > 0) {
                //    this.drawTexturedModalRect(k - 34, l - 17, 0, 72, 16, 16);
                //}

                this.drawTexturedModalRect(k - 30, l - 67 + sv, 0, 0 + sv, 8, 48 - sv);

                this.drawTexturedModalRect(k - 31, l - 71, 23, 0, 10, 74);
            }

            if (type == 1 || type == 2 || type >= 3) {
                //if (aura.badVibes > 0) {
                //    this.drawTexturedModalRect(k - 19, l - 17, 0, 72, 16, 16);
                //}

                this.drawTexturedModalRect(k - 15, l - 67 + st, 8, 0 + st, 8, 48 - st);

                this.drawTexturedModalRect(k - 16, l - 71, 39, 0, 10, 74);
            }

            GL11.glDisable(32826);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
