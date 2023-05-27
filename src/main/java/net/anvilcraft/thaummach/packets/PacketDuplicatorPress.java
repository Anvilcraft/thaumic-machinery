package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.anvilcraft.thaummach.particles.FXWisp;
import net.anvilcraft.thaummach.tiles.TileDuplicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PacketDuplicatorPress implements IMessage {
    int x;
    int y;
    int z;

    public PacketDuplicatorPress() {}

    public PacketDuplicatorPress(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    public static class Handler implements IMessageHandler<PacketDuplicatorPress, IMessage> {
        @Override
        public IMessage onMessage(PacketDuplicatorPress msg, MessageContext ctx) {
            World world = Minecraft.getMinecraft().theWorld;
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TileDuplicator) {
                ((TileDuplicator) te).doPress = true;

                Minecraft.getMinecraft().getSoundHandler().playSound(
                    PositionedSoundRecord.func_147675_a(
                        new ResourceLocation("thaummach", "stomp"), msg.x, msg.y, msg.z
                    )
                );

                int q = 50;
                for (int a = 0; a < q; ++a) {
                    float xx = (float) msg.x + 0.5F
                        - (world.rand.nextFloat() - world.rand.nextFloat()) * 1.5F;
                    float yy = (float) msg.y + 0.5F
                        - (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
                    float zz = (float) msg.z + 0.5F
                        - (world.rand.nextFloat() - world.rand.nextFloat()) * 1.5F;
                    FXWisp ef = new FXWisp(
                        world,
                        (double) ((float) msg.x + 0.5F),
                        (double) ((float) msg.y + 0.5F),
                        (double) ((float) msg.z + 0.5F),
                        (double) xx,
                        (double) yy,
                        (double) zz,
                        0.15F,
                        world.rand.nextInt(5)
                    );
                    Minecraft.getMinecraft().effectRenderer.addEffect(ef);
                }
            }

            return null;
        }
    }
}
