package me.hasenzahn1.glass_fabric_port.blocks.blockentity;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import me.hasenzahn1.glass_fabric_port.gui.screen.GlassProjectorScreen;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassTerminalScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GlassTerminalBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, IInventoryBlock {

    public String channel = "";

    public GlassTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(GLASSMod.GLASS_TERMINAL_ENTITY, pos, state);
        //channel = "";
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        System.out.println("ToUpdatePacket");
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void markRemoved() {
        if (GLASSMod.instance.eventHandlerClient.terminalLocations.containsKey(channel) && GLASSMod.instance.eventHandlerClient.terminalLocations.get(channel) == getPos()){
            GLASSMod.instance.eventHandlerClient.terminalLocations.remove(channel);
        }

        super.markRemoved();
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            markDirtyInWorld(this.world, this.pos, this.getCachedState());
        }
    }

    public void markDirtyInWorld(World world, BlockPos pos, BlockState state){
        world.markDirty(pos);

        if(!world.isClient){
            ((ServerWorld) world).getChunkManager().markForUpdate(pos);
        }
    }

    public void update(String channel){


        if(!this.channel.isEmpty()){
            if (GLASSMod.instance.eventHandlerClient.terminalLocations.containsKey(this.channel) && GLASSMod.instance.eventHandlerClient.terminalLocations.get(this.channel) == getPos()){
                GLASSMod.instance.eventHandlerClient.terminalLocations.remove(this.channel);
            }
        }

        this.channel = channel;

        if(!channel.isEmpty()) GLASSMod.instance.eventHandlerClient.terminalLocations.put(channel, getPos());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        System.out.println("initial Chunk data");
        if(!channel.isEmpty()) GLASSMod.instance.eventHandlerClient.terminalLocations.put(channel, getPos());
        return createNbt();
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GlassTerminalScreenHandler(syncId, inv);
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

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(channel);
        buf.writeBlockPos(pos);
    }
}
