package me.hasenzahn1.glass_fabric_port.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hasenzahn1.glass_fabric_port.GLASSMod;
import me.hasenzahn1.glass_fabric_port.gui.screen_handler.GlassProjectorScreenHandler;
import me.hasenzahn1.glass_fabric_port.gui.widget.NewGuiChannelList;
import me.hasenzahn1.glass_fabric_port.packet.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
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

import java.util.ArrayList;
import java.util.Collections;

public class GlassProjectorScreen extends HandledScreen<GlassProjectorScreenHandler> implements ScreenHandlerListener {

    private static final Identifier BACKGROUND_TEXTURE = new Identifier("glass", "textures/gui/glass_projector_2.png");

    private final int xSize = 93 * 2;
    private final int ySize = 75 * 2;
    private int guiLeft, guiTop;

    private NewGuiChannelList channelList;
    private ButtonWidget confirmButton;


    public GlassProjectorScreen(GlassProjectorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        drawTexture(matrices, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        channelList.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);

        matrices.push();
        //matrices.scale(.5f, .5f, .5f);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("glass.gui.availableChannels"), (guiLeft + 3) + 2, (guiTop + 3) + 2 , 16777215);
        //drawTextWithShadow(matrices, textRenderer, new TranslatableText("glass.gui.availableChannels"), (guiLeft + 5) * 2, (guiTop + 5) * 2, 16777215);



        matrices.pop();
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
        handler.addListener(this);

        client.keyboard.setRepeatEvents(true);
        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;

        handler.addListener(this);

        System.out.println(GLASSMod.instance.eventHandlerClient.terminalLocations.keySet());
        channelList = new NewGuiChannelList(client, guiLeft + 5, guiTop + 3, xSize-17, ySize - 22, guiTop + 14, guiTop + ySize-5, 16, width, height);
        ArrayList<String> channels = new ArrayList<>(GLASSMod.instance.eventHandlerClient.terminalLocations.keySet());
        Collections.sort(channels);
        for (String s : channels){
            if(s.split(":")[0].equals(handler.getPlayerName()) || s.split(":")[0].equals("public"))
                channelList.addElement(s);
        }
        String channel = handler.getChannel();

        if (!channel.isEmpty()) channelList.setSelected(channel);


        addSelectableChild(channelList);

        confirmButton = addDrawableChild(new ButtonWidget(guiLeft + (xSize - 50) / 2, guiTop + ySize + 2, 50, 20, new TranslatableText("gui.done"),  (button) -> {
            confirmSelection();
        }));


    }

    public void confirmSelection(){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getPos());
        buf.writeString(channelList.getChannelName());
        ClientPlayNetworking.send(PacketHandler.SET_CHANNEL_PACKET, buf);
        close();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        //System.out.print(mouseX + " | " + mouseY + " | ");
        //System.out.println(channelList.isMouseOver(mouseX, mouseY));
        channelList.debug(mouseX, mouseY, button);
        boolean value = channelList.mouseClicked(mouseX, mouseY, button);
        //System.out.println(value);
        if(!value)
            return super.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public void removed() {
        super.removed();
        client.keyboard.setRepeatEvents(false);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        //System.out.println("Dragged");
        channelList.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        //System.out.println("Released");
        channelList.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        //System.out.println("Scrolled");
        channelList.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {}

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {}

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
}
