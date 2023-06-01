package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.anvilcraft.thaummach.tiles.TileEnchanter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketEnchanterStart implements IMessage {
    int x;
    int y;
    int z;
    int idx;

    public PacketEnchanterStart() {}

    public PacketEnchanterStart(int x, int y, int z, int idx) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.idx = idx;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.idx = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.idx);
    }

    public static class Handler implements IMessageHandler<PacketEnchanterStart, IMessage> {
        @Override
        public IMessage onMessage(PacketEnchanterStart pkt, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            TileEntity te = world.getTileEntity(pkt.x, pkt.y, pkt.z);

            if (te instanceof TileEnchanter) {
                ((TileEnchanter) te).startEnchantingItem(pkt.idx);
            }

            return null;
        }
    }
}
