package me.hasenzahn1.glass_fabric_port.gui.widget;

import com.google.common.collect.ImmutableList;
import me.hasenzahn1.glass_fabric_port.gui.screen.GlassProjectorScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class GuiChannelList extends ElementListWidget<GuiChannelList.ChannelEntry> {

    private int left;
    private GlassProjectorScreen parent;

    public GuiChannelList(GlassProjectorScreen parent, int width, int height, int top, int botton, int entryHeight, int left) {
        super(MinecraftClient.getInstance(), width, height, top, botton, entryHeight);
        this.parent = parent;
        this.left = left;
        this.setRenderHeader(false, 0);
        setRenderBackground(false);
        setRenderHorizontalShadows(false);
    }

    public void addSingleOptionEntry(String text) {
        this.addEntry(ChannelEntry.create(parent, this.width, itemHeight, left, text, client.textRenderer));
    }

    public void addAll(String[] texts) {
        for(int i = 0; i < texts.length; i += 2) {
            this.addSingleOptionEntry(texts[i]);
        }
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    public Optional<ClickableWidget> getHoveredButton(double mouseX, double mouseY) {
        Iterator var5 = this.children().iterator();

        while(var5.hasNext()) {
            ChannelEntry buttonEntry = (ChannelEntry)var5.next();
            Iterator var7 = buttonEntry.buttons.iterator();

            while(var7.hasNext()) {
                ClickableWidget clickableWidget = (ClickableWidget)var7.next();
                if (clickableWidget.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(clickableWidget);
                }
            }
        }

        return Optional.empty();
    }

    @Environment(EnvType.CLIENT)
    protected static class ChannelEntry extends Entry<ChannelEntry> {
        final List<ClickableWidget> buttons;
        private TextRenderer renderer;
        private int width, height, left;
        private GlassProjectorScreen parent;

        private ChannelEntry(List<ClickableWidget> optionsToButtons, GlassProjectorScreen parent, TextRenderer renderer, int width, int height, int left) {
            this.buttons = ImmutableList.copyOf(optionsToButtons);
            this.renderer = renderer;
            this.parent = parent;
            this.width = width;
            this.height = height;
            this.left = left;
        }

        public static ChannelEntry create(GlassProjectorScreen parent, int width, int height, int left, String text, TextRenderer renderer) {
            return new ChannelEntry(ImmutableList.of(new TextFieldWidget(renderer, 0, 0, width, height, new LiteralText(text))), parent, renderer, width, height, left); //option.createButton(options, width / 2 - 155, 0, 310)));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            for (ClickableWidget widget : buttons){
                matrices.push();
                String name = renderer.trimToWidth(widget.getMessage().getString().substring(widget.getMessage().getString().indexOf(":") + 1), (entryWidth - 10) * 2);
                if(widget.getMessage().getString().startsWith("public:"))
                {
                    name = I18n.translate("glass.gui.public") + " " + name;
                }
                drawCenteredText(matrices, renderer, new LiteralText(name), parent.width / 2, y, index % 2 == 0 ? 0xFFFFFF : 0xAAAAAA);
                matrices.pop();

                //widget.render(matrices, mouseX, mouseY, tickDelta);
            }

            /*this.buttons.forEach((button) -> {
                button.y = y;
                button.x = (int)((entryWidth + 2) * 1.12);
                button.render(matrices, mouseX, mouseY, tickDelta);
            });

             */
        }

        public List<? extends Element> children() {
            return this.buttons;
        }

        public List<? extends Selectable> selectableChildren() {
            return this.buttons;
        }
    }
}


/*
public class GuiChannelList extends EntryListWidget<GuiChannelList.ChannelDisplay> {

    public GuiChannelList(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        setRenderHorizontalShadows(false);
        //setRenderBackground(false);

        addEntry(new ChannelDisplay("test", minecraftClient.textRenderer));
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }


    protected static class ChannelDisplay extends ElementListWidget.Entry<ChannelDisplay>{

        private String channelName;
        private TextRenderer renderer;

        public ChannelDisplay(String name, TextRenderer renderer){
            this.channelName = name;
            this.renderer = renderer;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return null;
        }

        @Override
        public List<? extends Element> children() {
            return null;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            matrices.push();
            drawTextWithShadow(matrices, renderer, new LiteralText(channelName), x, y, 0xffffff);
            matrices.pop();
            System.out.println("Render");
        }
    }
}

 */
