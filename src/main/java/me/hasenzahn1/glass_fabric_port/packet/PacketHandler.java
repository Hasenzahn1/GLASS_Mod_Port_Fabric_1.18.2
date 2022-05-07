package me.hasenzahn1.glass_fabric_port.packet;

import me.hasenzahn1.glass_fabric_port.GLASSMod;
import me.hasenzahn1.glass_fabric_port.blocks.blockentity.IInventoryBlock;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PacketHandler {


    public static final Identifier SET_CHANNEL_PACKET = new Identifier(GLASSMod.MOD_ID, "set_packet");
    public static final Identifier INIT_CHANNEL_PACKET = new Identifier(GLASSMod.MOD_ID, "init_channel");

    public void registerServerPacketReceive() {
        //Server Set Channel Receiver
        ServerPlayNetworking.registerGlobalReceiver(PacketHandler.SET_CHANNEL_PACKET, (server, player, handler, buff, sender) -> {
            System.out.println("SERVER set Channel Packet Receive: " + buff);
            PacketByteBuf buf_copy = PacketByteBufs.copy(buff);
            BlockPos pos = buff.readBlockPos();
            String channel = buff.readString();
            server.execute(() -> {
                //Change Block
                BlockEntity be = player.getWorld().getBlockEntity(pos);
                if(be instanceof IInventoryBlock) ((IInventoryBlock) be).update(channel);
                player.getWorld().setBlockState(pos, player.getWorld().getBlockState(pos), 3);
                if(be != null) be.markDirty();

                //Send Packet to all Players
                server.getWorlds().forEach((world) -> {
                    for(ServerPlayerEntity p : world.getPlayers()){
                        ServerPlayNetworking.send(p, PacketHandler.SET_CHANNEL_PACKET, buf_copy);
                    }
                });
            });
        });

        //Server Init Channel Receive
        ServerPlayNetworking.registerGlobalReceiver(INIT_CHANNEL_PACKET, (server, player, handler, buff, sender) -> {
            server.execute(() -> {
                System.out.println("SERVER init Channel Receive: " + GLASSMod.instance.eventHandlerClient.terminalLocations.keySet());
                /*PacketByteBuf buf = PacketByteBufs.create();
                int i = 0;
                System.out.println(GLASSMod.instance.eventHandlerClient.terminalLocations.keySet());
                for(String channel : GLASSMod.instance.eventHandlerClient.terminalLocations.keySet()){
                    PacketByteBuf pair = PacketByteBufs.create();
                    pair.writeBlockPos(GLASSMod.instance.eventHandlerClient.terminalLocations.get(channel));
                    pair.writeString(channel);
                    buf.setBytes(i, pair);
                    i++;
                }
                ServerPlayNetworking.send(player, INIT_CHANNEL_PACKET, buf);

                 */
                for(String channel : GLASSMod.instance.eventHandlerClient.terminalLocations.keySet()){
                    sendPlayerSetChannelPacket(player, channel);
                }
            });
        });
    }

    public void sendPlayerSetChannelPacket(ServerPlayerEntity player, String channel){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(GLASSMod.instance.eventHandlerClient.terminalLocations.get(channel));
        buf.writeString(channel);

        ServerPlayNetworking.send(player, SET_CHANNEL_PACKET, buf);
    }

    public void registerClientPacketReceive() {
        //Client Set Channel Receive
        ClientPlayNetworking.registerGlobalReceiver(PacketHandler.SET_CHANNEL_PACKET, (client, handler, buff, sender) -> {
            System.out.println("CLIENT set Channel Receive: " + buff);
            BlockPos pos = buff.readBlockPos();
            String channel = buff.readString();

            client.execute(() -> {
                if(client.player == null) return;
                BlockEntity be = client.player.getWorld().getBlockEntity(pos);
                if(be instanceof IInventoryBlock){
                    ((IInventoryBlock) be).update(channel);
                }
            });
        });
    }
}
