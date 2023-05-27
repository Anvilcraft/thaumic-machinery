package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketDuplicatorSetRepeat implements IMessage {
    int x;
    int y;
    int z;
    boolean repeat;

    public PacketDuplicatorSetRepeat() {}

    public PacketDuplicatorSetRepeat(int x, int y, int z, boolean repeat) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.repeat = repeat;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.repeat = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBoolean(this.repeat);
    }

    public static class Handler implements IMessageHandler<PacketDuplicatorSetRepeat, IMessage> {
        @Override
        public IMessage onMessage(PacketDuplicatorSetRepeat msg, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;

            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);

            if (te instanceof TileDuplicator) {
                ((TileDuplicator) te).repeat = msg.repeat;
                world.markBlockForUpdate(msg.x, msg.y, msg.z);
            }

            return null;
        }
    }
}
