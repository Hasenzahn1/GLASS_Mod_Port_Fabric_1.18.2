package me.hasenzahn1.glass_fabric_port.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;

public class NewGuiChannelList extends EntryListWidget<NewGuiChannelList.ChannelListEntry> {

    private boolean widgetFocused;
    private TextRenderer renderer;

    public NewGuiChannelList(MinecraftClient client, int x, int y, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.renderer = client.textRenderer;
        setRenderHeader(false, 0);
        setRenderBackground(false);
        setRenderHorizontalShadows(false);
        setRenderSelection(true);
        System.out.println(getScrollAmount());
        left = x;
        right = x + width;
        System.out.println(left + " " + right + " " + top + " " + bottom);
        MinecraftClient.getInstance().player.sendMessage(new LiteralText(left + " " + right + " " + top + " " + bottom), false);
    }

    @Override
    public int getRowWidth() {
        return width - 4;
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft();
    }

    @Override
    protected int getRowTop(int index) {
        return super.getRowTop(index);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        this.widgetFocused = super.changeFocus(lookForwards);
        if (this.widgetFocused) {
            this.ensureVisible(this.getFocused());
        }

        return this.widgetFocused;
    }

    public void addElement(String text){
        int index = addEntry(new ChannelListEntry(this, text));
        ensureVisible(children().get(index));
    }

    public void debug(double mouseX, double mouseY, int button){
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    protected int getScrollbarPositionX() {
        return getRowRight();
    }

    public static class ChannelListEntry extends Entry<NewGuiChannelList.ChannelListEntry>{

        protected final NewGuiChannelList parent;
        protected final String text;

        public ChannelListEntry(NewGuiChannelList parent, String text){
            this.parent = parent;
            this.text = text;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            matrices.push();
            /*
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            builder.vertex(x, y + entryHeight, 0).color(index * 100, 0, 0, 255).next();
            builder.vertex(x + entryWidth, y + entryHeight, 0).color(index * 100, 0, 0, 255).next();
            builder.vertex(x + entryWidth, y, 0).color(index * 100, 0, 0, 255).next();
            builder.vertex(x, y, 0).color(index * 100, 0, 0, 255).next();
            tessellator.draw();

             */
            float factor = 0.7f;
            matrices.scale(factor, factor, 0);
            drawCenteredText(matrices, parent.renderer, text, x + (entryWidth / 2), y + (entryHeight / 4), 0xff0000);

            //System.out.println("Render: " + index * 100 + " " + x + " " + y + " " + entryWidth + " " + entryHeight + " " + mouseX + " " + mouseY + " " + hovered + " " + tickDelta);
            matrices.pop();
        }



        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            System.out.println("Test");
            parent.setSelected(this);
            return true;
        }

    }
}
