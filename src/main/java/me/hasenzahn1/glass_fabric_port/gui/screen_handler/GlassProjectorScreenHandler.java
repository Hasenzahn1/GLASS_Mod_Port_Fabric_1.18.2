package me.hasenzahn1.glass_fabric_port.gui.screen_handler;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class GlassProjectorScreenHandler extends ScreenHandler {

    private String channel;
    private final PlayerInventory inv;

    public GlassProjectorScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf){
        super(GLASSMod.GLASS_PROJECTOR_SCREEN_HANDLER, syncId);
        channel = buf.readString();
        this.inv = inv;
    }

    public GlassProjectorScreenHandler(int syncId, PlayerInventory inventory){
        super(GLASSMod.GLASS_PROJECTOR_SCREEN_HANDLER, syncId);
        this.inv = inventory;
    }

    public String getChannel() {
        return channel;
    }

    public String getPlayerName(){
        return inv.player.getName().getString();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
