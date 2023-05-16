package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.api.HelperLocation;
import io.netty.buffer.ByteBuf;
import net.anvilcraft.thaummach.particles.FXWisp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;

public class PacketFXWisp implements IPacketFX {
    double x;
    double y;
    double z;
    float f;
    int type;
    boolean shrink;
    float gravity;

    public PacketFXWisp() {}

    public PacketFXWisp(
        double x, double y, double z, float f, int type, boolean shrink, float gravity
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;
        this.type = type;
        this.shrink = shrink;
        this.gravity = gravity;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.f = buf.readFloat();
        this.type = buf.readInt();
        this.shrink = buf.readBoolean();
        this.gravity = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.f);
        buf.writeInt(this.type);
        buf.writeBoolean(this.shrink);
        buf.writeFloat(this.gravity);
    }

    public static class Handler extends AbstractFXPacketHandler<PacketFXWisp> {
        @Override
        @SideOnly(Side.CLIENT)
        public EntityFX readFX(PacketFXWisp msg) {
            FXWisp fx = new FXWisp(
                Minecraft.getMinecraft().theWorld, msg.x, msg.y, msg.z, msg.f, msg.type
            );

            fx.shrink = msg.shrink;
            fx.setGravity(msg.gravity);

            return fx;
        }
    }

    @Override
    public HelperLocation getLocation() {
        return new HelperLocation(this.x, this.y, this.z);
    }
}
