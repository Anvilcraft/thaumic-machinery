package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;

abstract class AbstractFXPacketHandler<Req extends IPacketFX>
    implements IMessageHandler<Req, IMessage> {
    public abstract EntityFX readFX(Req message);

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(Req message, MessageContext ctx) {
        Minecraft.getMinecraft().effectRenderer.addEffect(this.readFX(message));
        return null;
    }
}
