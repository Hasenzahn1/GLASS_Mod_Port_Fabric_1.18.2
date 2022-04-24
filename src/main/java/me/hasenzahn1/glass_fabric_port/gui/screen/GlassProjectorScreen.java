package me.hasenzahn1.glass_fabric_port.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassProjectorScreenHandler;
import me.hasenzahn1.glass_fabric_port.gui.widget.NewGuiChannelList;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GlassProjectorScreen extends HandledScreen<GlassProjectorScreenHandler> implements ScreenHandlerListener {

    private static final Identifier BACKGROUND_TEXTURE = new Identifier("glass", "textures/gui/glass_projector.png");

    public int xSize = 93;
    public int ySize = 75;

    private NewGuiChannelList channelList;


    public GlassProjectorScreen(GlassProjectorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        /*RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (width - 93) / 2;
        int y = (height - 75) / 2;
        drawTexture(matrices, x, y, 0, 0, 93, 75);

         */
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        channelList.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        handler.addListener(this);

        channelList = new NewGuiChannelList(client, 1000, 1000, 20, 1200, 20);
        channelList.addElement("test1");
        channelList.addElement("test2");
        addSelectableChild(channelList);

        client.keyboard.setRepeatEvents(true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!channelList.mouseClicked(mouseX, mouseY, button))
            return super.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public void removed() {
        super.removed();
        client.keyboard.setRepeatEvents(false);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {}

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {}

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
}