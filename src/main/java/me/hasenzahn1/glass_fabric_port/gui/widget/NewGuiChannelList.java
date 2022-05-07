package me.hasenzahn1.glass_fabric_port.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class NewGuiChannelList extends EntryListWidget<NewGuiChannelList.ChannelListEntry> {

    private boolean widgetFocused;
    private TextRenderer renderer;
    private int screenWidth, screenHeight;

    public NewGuiChannelList(MinecraftClient client, int x, int y, int width, int height, int top, int bottom, int itemHeight, int screenWidth, int screenHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.renderer = client.textRenderer;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        setRenderHeader(false, 0);
        setRenderBackground(false);
        setRenderHorizontalShadows(false);
        setRenderSelection(true);
        System.out.println(getScrollAmount());
        left = x;
        right = x + width;
    }


    @Override
    protected void renderBackground(MatrixStack matrices) {
        super.renderBackground(matrices);

        RenderSystem.setShaderTexture(0, new Identifier("textures/gui/recipe_book.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(matrices, left, top, width, bottom - top, 22, 22, 16, 16, 256, 256);
    }

    public String getChannelName(){
        return getFocused() != null ? getFocused().text : "";
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

    public void setSelected(String channel){
        for(ChannelListEntry entry : children()){
            if(entry.text.equals(channel)){
                setSelected(entry);
                break;
            }
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return getRowRight();
    }

    public static class ChannelListEntry extends Entry<NewGuiChannelList.ChannelListEntry>{

        protected final NewGuiChannelList parent;
        protected final String text, drawText;
        private boolean rendered;

        public ChannelListEntry(NewGuiChannelList parent, String text){
            this.parent = parent;
            this.text = text;
            rendered = false;
            drawText = (text.split(":")[0].equals("public") ? "<Public> " : "") +  text.split(":")[1];
        }
        public void test() {
            System.out.println("test");
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if(y < parent.top || y + entryHeight > parent.bottom) {
                rendered = false;
                return;
            }
            rendered = true;
            matrices.push();

            drawStringWithShadow(matrices, parent.renderer, drawText, (x + 2), y + 2, index % 2 == 0 ? 0xFFFFFF : 0XAAAAAA);
            //drawStringWithShadow(matrices, parent.renderer, text, (x + 2) * 2, y * 2, index % 2 == 0 ? 0xFFFFFF : 0XAAAAAA);

            //System.out.println("Render: " + index * 100 + " " + x + " " + y + " " + entryWidth + " " + entryHeight + " " + mouseX + " " + mouseY + " " + hovered + " " + tickDelta);
            matrices.pop();
        }



        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            //System.out.println("Test");
            if(rendered) parent.setSelected(this);
            return true;
        }

    }
}
