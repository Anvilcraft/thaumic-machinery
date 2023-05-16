package net.anvilcraft.thaummach.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import dev.tilera.auracore.api.HelperLocation;

public interface IPacketFX extends IMessage {
    public HelperLocation getLocation();
}
