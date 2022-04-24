package me.hasenzahn1.glass_fabric_port.blocks.renderer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class GlassExtensionBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer {

    public GlassExtensionBlockRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

    }
}
