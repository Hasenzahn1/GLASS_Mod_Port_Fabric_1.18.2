package me.hasenzahn1.glass_fabric_port.blocks.renderer;

import me.hasenzahn1.glass_fabric_port.blocks.custom.GlassTerminal;
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

public class GlassTerminalBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer {

    public static ItemStack stack = new ItemStack(Items.GLASS, 1);

    public GlassTerminalBlockRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();



        Direction facing = entity.getCachedState().get(GlassTerminal.FACING);

        if(facing == Direction.NORTH) matrices.translate(.5, .5, .25);
        else if(facing == Direction.SOUTH) matrices.translate(.5, .5, .75);
        else if(facing == Direction.EAST) matrices.translate(.75, .5, .5);
        else if(facing == Direction.WEST) matrices.translate(.25, .5, .5);

        matrices.multiply(facing.getRotationQuaternion());
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));


        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);

        matrices.pop();
    }
}
