package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.anvilcraft.thaummach.container.ContainerVoidInterface;
import net.minecraft.inventory.Container;

public class PacketChangeVoidInterfaceContainerPage implements IMessage {
    int page;

    public PacketChangeVoidInterfaceContainerPage() {}

    public PacketChangeVoidInterfaceContainerPage(int page) {
        this.page = page;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.page = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.page);
    }

    public static class Handler
        implements IMessageHandler<PacketChangeVoidInterfaceContainerPage, IMessage> {
        @Override
        public IMessage
        onMessage(PacketChangeVoidInterfaceContainerPage msg, MessageContext ctx) {
            Container container = ctx.getServerHandler().playerEntity.openContainer;

            if (!(container instanceof ContainerVoidInterface))
                return null;

            ContainerVoidInterface cvi = (ContainerVoidInterface) container;

            if (msg.page >= 0 && msg.page < cvi.vinter.getLinkedSize()) {
                cvi.vinter.current = msg.page;
                cvi.detectAndSendChanges();
            }

            ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(
                cvi.vinter.xCoord, cvi.vinter.yCoord, cvi.vinter.zCoord
            );

            return null;
        }
    }
}
