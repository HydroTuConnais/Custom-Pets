// src/main/java/net/hydrotuconnais/custompets/network/OpenPetsMenuPacket.java
package net.hydrotuconnais.custompets.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.hydrotuconnais.custompets.screen.PetsScreen;

import java.util.function.Supplier;

public class OpenPetsMenuPacket {
    public OpenPetsMenuPacket() {}

    public static OpenPetsMenuPacket decode(FriendlyByteBuf buf) {
        return new OpenPetsMenuPacket();
    }

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft.getInstance().setScreen(new PetsScreen());
            });
        });
        ctx.get().setPacketHandled(true);
    }
}