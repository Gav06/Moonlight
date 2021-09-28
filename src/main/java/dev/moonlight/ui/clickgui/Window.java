package dev.moonlight.ui.clickgui;

import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import dev.moonlight.ui.clickgui.api.ContentPane;
import dev.moonlight.ui.clickgui.api.DragComponent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Window extends AbstractComponent {

    private final WindowHeader header;

    private final ArrayList<ContentPane> panes;
    private final GUI moonlightGui;

//    private final HashMap<Module.Category, List<ModuleButton>> moduleButtonCache;

    public Window(GUI moonlightGui, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new WindowHeader(x, y, width, 8);
        this.panes = new ArrayList<>();
        this.moonlightGui = moonlightGui;
//        this.moduleButtonCache = new HashMap<>();

        final ContentPane categoryPane = new ContentPane(x, y, 100, height);

        for (Module.Category category : Module.Category.values()) {
            moonlightGui.registerComponentToPane(
                    new CategoryButton(
                            category,
                            0,
                            0,
                            categoryPane.width,
                            categoryPane.height / category.getClass().getEnumConstants().length),
                    categoryPane,
                    "category_button_" + category.name().toLowerCase());
        }

        final ContentPane modulesPane = new ContentPane(x, y, width / 2 - 100, height);
        final ContentPane settingsPane = new ContentPane(x, y, width / 2 - 100, height);

        moonlightGui.registerPaneToWindow(categoryPane, this, "category_pane");
        moonlightGui.registerPaneToWindow(modulesPane, this, "modules_pane");
        moonlightGui.registerPaneToWindow(settingsPane, this, "settings_pane");
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        header.click(mouseX, mouseY, mouseButton);
        for (ContentPane pane : panes) {
            pane.click(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        header.release(mouseX, mouseY, mouseButton);
        for (ContentPane pane : panes) {
            pane.release(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        header.draw(mouseX, mouseY, partialTicks);
        this.x = header.x;
        this.y = header.y + header.height;

        Gui.drawRect(x, y, x + width, y + height, 0x60000000);

        final int color = 0xC81c4791;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        float a = (float)(color >> 24 & 255) / 255.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(1f);
        GL11.glColor4f(r, g, b, a);
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        buffer.pos(x, y - header.height, 0.0).endVertex();
        buffer.pos(x + width, y - header.height, 0.0).endVertex();
        buffer.pos(x + width, y + height, 0.0).endVertex();;
        buffer.pos(x, y + height, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();


        int x = 0;
        for (ContentPane pane : panes) {
            pane.x = this.x + x;
            pane.y = y;
            x += pane.width;
            pane.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) {
        for (ContentPane pane : panes) {
            pane.typed(keyChar, keyCode);
        }
    }

    public ArrayList<ContentPane> getContentPanes() {
        return panes;
    }

    private class WindowHeader extends DragComponent {
        public WindowHeader(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            super.draw(mouseX, mouseY, partialTicks);
            Gui.drawRect(x, y, x + width, y + height, 0xC81c4791);
        }
    }

    private class CategoryButton extends AbstractComponent {

        private final Module.Category category;

        public CategoryButton(Module.Category category, int x, int y, int width, int height) {
            super(x, y, width, height);
            this.category = category;
        }

        @Override
        public void click(int mouseX, int mouseY, int mouseButton) {
            final ContentPane modulePane = (ContentPane) moonlightGui.getComponentByTag("modules_pane");
            if (modulePane != null) {
//                modulePane.getComponents().clear();
//                modulePane.getComponents()
            }
        }

        @Override
        public void release(int mouseX, int mouseY, int mouseButton) { }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            if (isInside(mouseX, mouseY)) {
                Gui.drawRect(x, y, x + width, y + height, 0x50ffffff);
            }
            Gui.drawRect(x + 4, y + 4, x + width - 4, y + height - 4, 0x90000000);
            moonlightGui.getMoonlight().getFontRenderer().drawCenteredStringWithShadow(category.name(), x + width / 2f, y + height / 2f - moonlightGui.getMoonlight().getFontRenderer().getHeight() / 2f, -1);
        }

        @Override
        public void typed(char keyChar, int keyCode) { }
    }
//
//    private static class ModuleButton extends AbstractComponent {
//
//    }
}