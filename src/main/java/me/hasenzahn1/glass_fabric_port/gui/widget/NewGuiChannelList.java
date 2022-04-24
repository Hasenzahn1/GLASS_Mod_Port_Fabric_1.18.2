package me.hasenzahn1.glass_fabric_port.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

public class NewGuiChannelList extends EntryListWidget<NewGuiChannelList.ChannelListEntry> {

    private boolean widgetFocused;
    private TextRenderer renderer;

    public NewGuiChannelList(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.renderer = client.textRenderer;
        setRenderHeader(false, 0);
        System.out.println(getScrollAmount());
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        this.widgetFocused = super.changeFocus(lookForwards);
        if (this.widgetFocused) {
            this.ensureVisible(this.getFocused());
        }

        return this.widgetFocused;
    }

    public void addElement(String test){
        int index = addEntry(new ChannelListEntry(this));
        ensureVisible(children().get(index));
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public static class ChannelListEntry extends Entry<NewGuiChannelList.ChannelListEntry>{

        protected final NewGuiChannelList parent;

        public ChannelListEntry(NewGuiChannelList parent){
            this.parent = parent;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            drawCenteredText(matrices, parent.renderer, "Test" + index, x, y, 0xff0000);
            System.out.println("Render: " + index + " " + x + " " + y + " " + entryWidth + " " + entryHeight + " " + mouseX + " " + mouseY + " " + hovered + " " + tickDelta);
        }
    }
}
