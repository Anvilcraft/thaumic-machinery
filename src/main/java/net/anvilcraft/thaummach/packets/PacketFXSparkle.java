package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.api.HelperLocation;
import dev.tilera.auracore.client.FXSparkle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;

public class PacketFXSparkle implements IPacketFX {
    double x;
    double y;
    double z;
    float f;
    int type;
    int m;
    float gravity;

    public PacketFXSparkle() {}

    public PacketFXSparkle(
        double x, double y, double z, float f, int type, int m, float gravity
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;
        this.type = type;
        this.m = m;
        this.gravity = gravity;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.f = buf.readFloat();
        this.type = buf.readInt();
        this.m = buf.readInt();
        this.gravity = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.f);
        buf.writeInt(this.type);
        buf.writeInt(this.m);
        buf.writeFloat(this.gravity);
    }

    @Override
    public HelperLocation getLocation() {
        return new HelperLocation(this.x, this.y, this.z);
    }

    public static class Handler extends AbstractFXPacketHandler<PacketFXSparkle> {
        @Override
        @SideOnly(Side.CLIENT)
        public EntityFX readFX(PacketFXSparkle msg) {
            FXSparkle fx = new FXSparkle(
                Minecraft.getMinecraft().theWorld,
                msg.x,
                msg.y,
                msg.z,
                msg.f,
                msg.type,
                msg.m
            );

            fx.setGravity(msg.gravity);
            return fx;
        }
    }
}
