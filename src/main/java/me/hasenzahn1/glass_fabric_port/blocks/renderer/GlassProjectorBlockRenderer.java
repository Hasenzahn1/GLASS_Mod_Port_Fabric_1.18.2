package me.hasenzahn1.glass_fabric_port.blocks.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import me.hasenzahn1.glass_fabric_port.blocks.custom.GlassProjector;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class GlassProjectorBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

    public static ItemStack stack = new ItemStack(Items.BEACON, 1);

    public GlassProjectorBlockRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();



        Direction facing = entity.getCachedState().get(GlassProjector.FACING);

        if(facing == Direction.NORTH) matrices.translate(.5, .5, 1);
        else if(facing == Direction.SOUTH) matrices.translate(.5, .5, 0);
        else if(facing == Direction.WEST) matrices.translate(1, .5, .5);
        else if(facing == Direction.EAST) matrices.translate(0, .5, .5);
        else if(facing == Direction.UP) matrices.translate(.5, 0, .5);
        else if(facing == Direction.DOWN) matrices.translate(.5, 1, .5);

        matrices.multiply(facing.getRotationQuaternion());
        matrices.scale(1.8f, 1.8f, 1.8f);
        if(entity.getCachedState().get(GlassProjector.POWERED)) matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 16));

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);

        matrices.pop();
    }
}
