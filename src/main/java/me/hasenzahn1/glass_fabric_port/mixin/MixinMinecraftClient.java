package me.hasenzahn1.glass_fabric_port.mixin;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClientGame;onLeaveGameSession()V"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
    public void onDisconnect(Screen screen, CallbackInfo ci){
        GLASSMod.instance.eventHandlerClient.terminalLocations.clear();
        System.out.println("Disconnect");
    }

}
