package me.hasenzahn1.glass_fabric_port;

import me.hasenzahn1.glass_fabric_port.blocks.ModBlocks;
import me.hasenzahn1.glass_fabric_port.blocks.blockentity.GlassExtensionBlockEntity;
import me.hasenzahn1.glass_fabric_port.blocks.blockentity.GlassProjectorBlockEntity;
import me.hasenzahn1.glass_fabric_port.blocks.blockentity.GlassTerminalBlockEntity;
import me.hasenzahn1.glass_fabric_port.blocks.blockentity.IInventoryBlock;
import me.hasenzahn1.glass_fabric_port.blocks.renderer.GlassExtensionBlockRenderer;
import me.hasenzahn1.glass_fabric_port.blocks.renderer.GlassProjectorBlockRenderer;
import me.hasenzahn1.glass_fabric_port.blocks.renderer.GlassTerminalBlockRenderer;
import me.hasenzahn1.glass_fabric_port.gui.screen.GlassProjectorScreen;
import me.hasenzahn1.glass_fabric_port.gui.screen.GlassTerminalScreen;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassProjectorScreenHandler;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassTerminalScreenHandler;
import me.hasenzahn1.glass_fabric_port.handler.EventHandlerClient;
import me.hasenzahn1.glass_fabric_port.packet.PacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLASSMod implements ModInitializer, ClientModInitializer {

	public static final String MOD_ID = "glass";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static GLASSMod instance;

	public static BlockEntityType<GlassProjectorBlockEntity> GLASS_PROJECTOR_ENTITY;
	public static BlockEntityType<GlassTerminalBlockEntity> GLASS_TERMINAL_ENTITY;
	public static BlockEntityType<GlassExtensionBlockEntity> GLASS_EXTENSION_ENTITY;

	public static final ScreenHandlerType<GlassTerminalScreenHandler> GLASS_TERMINAL_SCREEN_HANDLER;
	public static final ScreenHandlerType<GlassProjectorScreenHandler> GLASS_PROJECTOR_SCREEN_HANDLER;

	public static final Identifier GLASS_TERMINAL_IDENTIFIER = new Identifier(MOD_ID, "glass_terminal");
	public static final Identifier GLASS_PROJECTOR_IDENTIFIER = new Identifier(MOD_ID, "glass_projector");

	public EventHandlerClient eventHandlerClient;
	private PacketHandler packetHandler;

	static {
		GLASS_PROJECTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, GLASS_PROJECTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(GlassProjectorBlockEntity::new, ModBlocks.GLASS_PROJECTOR).build(null));
		GLASS_TERMINAL_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, GLASS_TERMINAL_IDENTIFIER, FabricBlockEntityTypeBuilder.create(GlassTerminalBlockEntity::new, ModBlocks.GLASS_TERMINAL).build(null));
		GLASS_EXTENSION_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":glass_extension_block_entity", FabricBlockEntityTypeBuilder.create(GlassExtensionBlockEntity::new, ModBlocks.GLASS_EXTENSION).build(null));


		GLASS_TERMINAL_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(GLASS_TERMINAL_IDENTIFIER, GlassTerminalScreenHandler::new);
		GLASS_PROJECTOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(GLASS_PROJECTOR_IDENTIFIER, GlassProjectorScreenHandler::new);
	}

	@Override
	public void onInitialize() {
		packetHandler = new PacketHandler();
		if(instance == null)
			instance = this;

		ModBlocks.registerModBlocks();
		eventHandlerClient = new EventHandlerClient();

		packetHandler.registerServerPacketReceive();
	}

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GLASS_PROJECTOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GLASS_EXTENSION, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GLASS_WIRELESS_EXTENSION, RenderLayer.getCutout());

		BlockEntityRendererRegistry.register(GLASS_PROJECTOR_ENTITY, GlassProjectorBlockRenderer::new);
		BlockEntityRendererRegistry.register(GLASS_TERMINAL_ENTITY, GlassTerminalBlockRenderer::new);
		BlockEntityRendererRegistry.register(GLASS_EXTENSION_ENTITY, GlassExtensionBlockRenderer::new);

		ScreenRegistry.register(GLASS_TERMINAL_SCREEN_HANDLER, GlassTerminalScreen::new);
		ScreenRegistry.register(GLASS_PROJECTOR_SCREEN_HANDLER, GlassProjectorScreen::new);

		if(packetHandler == null){
			packetHandler = new PacketHandler();
		}
		packetHandler.registerClientPacketReceive();
	}
}
