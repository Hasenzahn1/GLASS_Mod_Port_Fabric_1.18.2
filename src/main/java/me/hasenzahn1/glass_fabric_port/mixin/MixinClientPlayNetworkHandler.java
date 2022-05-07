package me.hasenzahn1.glass_fabric_port.mixin;

import me.hasenzahn1.glass_fabric_port.packet.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    public void onPlayerJoin(GameJoinS2CPacket packet, CallbackInfo info){
        ClientPlayNetworking.send(PacketHandler.INIT_CHANNEL_PACKET, PacketByteBufs.create());
        System.out.println("INIT!!! CHANNELS");
    }

}
