package me.hasenzahn1.glass_fabric_port.blocks.blockentity;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassProjectorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class GlassProjectorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, IInventoryBlock {

    public String channel;

    public GlassProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(GLASSMod.GLASS_PROJECTOR_ENTITY, pos, state);
        channel = "";
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(channel);
        buf.writeBlockPos(pos);
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GlassProjectorScreenHandler(syncId, inv);
    }

    public void update(String channel){
        this.channel = channel;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        channel = nbt.getString("channel");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("channel", channel);
    }

    public String getChannel() {
        return channel;
    }
}
