package net.anvilcraft.thaummach;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.anvilcraft.thaummach.render.PortalRenderer;
import net.minecraft.client.Minecraft;

public class RenderTicker {
    @SubscribeEvent
    public void onRenderTickEvent(RenderTickEvent ev) {
        if (Minecraft.getMinecraft().theWorld == null)
            return;

        for (PortalRenderer ren : PortalRenderer.INSTANCES) {
            // TODO: optimize
            ren.createPortalView();
        }
    }
}
