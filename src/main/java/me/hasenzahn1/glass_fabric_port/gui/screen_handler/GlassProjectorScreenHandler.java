package me.hasenzahn1.glass_fabric_port.gui.screen_handler;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class GlassProjectorScreenHandler extends ScreenHandler {

    private String channel;
    private final PlayerInventory inv;
    private BlockPos pos;

    public GlassProjectorScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf){
        super(GLASSMod.GLASS_PROJECTOR_SCREEN_HANDLER, syncId);
        channel = buf.readString();
        pos = buf.readBlockPos();
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

    public BlockPos getPos(){
        return pos;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
