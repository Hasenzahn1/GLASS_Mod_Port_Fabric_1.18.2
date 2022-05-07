package me.hasenzahn1.glass_fabric_port.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassTerminalScreenHandler;
import me.hasenzahn1.glass_fabric_port.packet.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class GlassTerminalScreen extends HandledScreen<GlassTerminalScreenHandler> implements ScreenHandlerListener {

    private static final Identifier BACKGROUND_TEXTURE = new Identifier("glass", "textures/gui/glass_terminal.png");

    private int guiLeft, guiTop;
    private boolean isPublicChannel;

    private TextFieldWidget channelNameWidget;
    private ButtonWidget publicButton, confirmButton;

    public GlassTerminalScreen(GlassTerminalScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        isPublicChannel = false;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (width - 93) / 2;
        int y = (height - 75) / 2;
        drawTexture(matrices, x, y, 0, 0, 93, 75);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        //super.drawForeground(matrices, mouseX, mouseY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        RenderSystem.disableBlend();
        channelNameWidget.render(matrices, mouseX, mouseY, delta);

        drawTextWithShadow(matrices, textRenderer, new TranslatableText("glass.gui.channelName"), guiLeft + 8, guiTop + 6, 16777215);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("glass.gui.publicChannel"), guiLeft + 8, guiTop + 37, 16777215);

        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256){
            client.player.closeHandledScreen();
        }

        return !channelNameWidget.keyPressed(keyCode, scanCode, modifiers) && !channelNameWidget.isActive()  ? super.keyPressed(keyCode, scanCode, modifiers) : true;
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        channelNameWidget.tick();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        handler.addListener(this);

        guiLeft = (width - 93) / 2;
        guiTop = (height - 75) / 2;

        client.keyboard.setRepeatEvents(true);

        channelNameWidget = new TextFieldWidget(textRenderer, guiLeft + 7, guiTop + 17, 80, textRenderer.fontHeight + 4, new TranslatableText("glass.gui.channelName"));
        channelNameWidget.setFocusUnlocked(false);
        channelNameWidget.setDrawsBackground(true);
        channelNameWidget.setText(handler.getChannel().split(":").length == 2 ? handler.getChannel().split(":")[1] : handler.getChannel());
        channelNameWidget.setMaxLength(13);
        channelNameWidget.setEditableColor(-1);
        channelNameWidget.setUneditableColor(-1);

        addSelectableChild(channelNameWidget);
        setInitialFocus(channelNameWidget);
        channelNameWidget.setEditable(true);

        isPublicChannel = handler.getChannel().split(":")[0].equalsIgnoreCase("public");
        publicButton = addDrawableChild(new ButtonWidget(guiLeft + 6, guiTop + 47, 82, 20, new TranslatableText(isPublicChannel ? "gui.yes" : "gui.no"), (button) -> {
            isPublicChannel = !isPublicChannel;
            publicButton.setMessage(new TranslatableText(isPublicChannel ? "gui.yes" : "gui.no"));
        }));

        confirmButton = addDrawableChild(new ButtonWidget(guiLeft + 22, guiTop + 77, 50, 20, ScreenTexts.DONE, (button) -> {
            confirm();
        }));
    }

    @Override
    public void removed() {
        super.removed();
        client.keyboard.setRepeatEvents(false);
    }

    public void confirm(){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getPos());
        buf.writeString((isPublicChannel ? "public:" : handler.getPlayerName() + ":") + channelNameWidget.getText());

        ClientPlayNetworking.send(PacketHandler.SET_CHANNEL_PACKET, buf);
        close();
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {

    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {

    }
}
