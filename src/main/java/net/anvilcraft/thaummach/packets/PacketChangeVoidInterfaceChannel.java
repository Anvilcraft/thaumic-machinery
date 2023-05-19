package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.anvilcraft.thaummach.RuneTileData;
import net.anvilcraft.thaummach.tiles.TileVoidInterface;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketChangeVoidInterfaceChannel implements IMessage {
    int x;
    int y;
    int z;
    byte channel;

    public PacketChangeVoidInterfaceChannel() {}

    public PacketChangeVoidInterfaceChannel(int x, int y, int z, byte channel) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.channel = channel;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.channel = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeByte(this.channel);
    }

    public static class Handler
        implements IMessageHandler<PacketChangeVoidInterfaceChannel, IMessage> {
        @Override
        public IMessage
        onMessage(PacketChangeVoidInterfaceChannel msg, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);

            if (!(te instanceof TileVoidInterface))
                return null;

            TileVoidInterface vi = (TileVoidInterface) te;

            if (msg.channel >= 0 && msg.channel < 6) {
                for (RuneTileData rtd : TileVoidInterface.VOID_INTERFACES) {
                    if (rtd.x == vi.xCoord && rtd.y == vi.yCoord && rtd.z == vi.zCoord
                        && rtd.dim == vi.getWorldObj().provider.dimensionId) {
                        rtd.rune = msg.channel;
                        break;
                    }
                }

                vi.network = msg.channel;
                vi.invalidateLinks();
            }

            world.markBlockForUpdate(msg.x, msg.y, msg.z);

            return null;
        }
    }
}
