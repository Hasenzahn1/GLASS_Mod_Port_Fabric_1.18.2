package me.hasenzahn1.glass_fabric_port.gui.screen_handler;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class GlassTerminalScreenHandler extends ScreenHandler {

    private String channel;
    private BlockPos pos;

    private final PlayerInventory inv;

    public GlassTerminalScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf){
        super(GLASSMod.GLASS_TERMINAL_SCREEN_HANDLER, syncId);
        channel = buf.readString();
        pos = buf.readBlockPos();
        this.inv = inv;
    }

    public GlassTerminalScreenHandler(int syncId, PlayerInventory inventory){
        super(GLASSMod.GLASS_TERMINAL_SCREEN_HANDLER, syncId);
        this.inv = inventory;
    }

    public String getChannel() {
        return channel;
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getPlayerName(){
        return inv.player.getName().getString();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
