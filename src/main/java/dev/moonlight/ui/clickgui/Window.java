package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.RenderUtil;
import dev.moonlight.module.Module;
import dev.moonlight.settings.Setting;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import dev.moonlight.ui.clickgui.api.ContentPane;
import dev.moonlight.ui.clickgui.api.DragComponent;
import dev.moonlight.ui.clickgui.settings.BoolComponent;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Window extends AbstractComponent {

    private final WindowHeader header;

    private final ArrayList<ContentPane<?>> panes;
    private final GUI moonlightGui;

    // used for accessing the different panes to perform actions
    private final ContentPane<CategoryButton> categoryPane;
    private final ContentPane<ModuleButton> modulePane;
    private final ContentPane<SettingComponent> settingPane;

    private final HashMap<Module.Category, List<ModuleButton>> moduleButtonCache;

    public Window(GUI moonlightGui, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new WindowHeader(x, y, width, moonlightGui.getMoonlight().getFontRenderer().getHeight() + 4);
        this.panes = new ArrayList<>();
        this.moonlightGui = moonlightGui;
        this.moduleButtonCache = new HashMap<>();

        for (Module.Category category : Module.Category.values()) {
            moduleButtonCache.put(category, new ArrayList<>());
            for (Module module : moonlightGui.getMoonlight().getModuleManager().getCategoryModules(category)) {
                final ModuleButton button = new ModuleButton(module, 0, 0, width / 2 - 100, 14);
                moduleButtonCache.get(category).add(button);
            }
        }

        final ContentPane<CategoryButton> categoryPane = new ContentPane<>(x, y, 100, height);

        for (Module.Category category : Module.Category.values()) {
            categoryPane.getComponents().add(
                    new CategoryButton(
                            category,
                            0,
                            0,
                            categoryPane.width,
                            categoryPane.height / category.getClass().getEnumConstants().length));
        }

        final ContentPane<ModuleButton> modulesPane = new ContentPane<>(x, y, width / 2 - 100, height);

        final ContentPane<SettingComponent> settingsPane = new ContentPane<>(x, y, width / 2 - 100, height);

        panes.add(categoryPane);
        this.categoryPane = categoryPane;
        panes.add(modulesPane);
        this.modulePane = modulesPane;
        panes.add(settingsPane);
        this.settingPane = settingsPane;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        header.click(mouseX, mouseY, mouseButton);
        for (ContentPane<?> pane : panes) {
            pane.click(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        header.release(mouseX, mouseY, mouseButton);
        for (ContentPane<?> pane : panes) {
            pane.release(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        header.draw(mouseX, mouseY, partialTicks);
        this.x = header.x;
        this.y = header.y + header.height;

        moonlightGui.getMoonlight().getFontRenderer().drawStringWithShadow(Moonlight.MOD_NAME + " v" + Moonlight.VERSION, x + 2, header.y + 1, -1);

        Gui.drawRect(x, y, x + width, y + height, 0x60000000);

        final int color = 0xC81c4791;
        RenderUtil.outline2d(x, y - header.height, x + width, y + height, color);


        int x = 0;
        for (ContentPane<?> pane : panes) {
            pane.x = this.x + x;
            pane.y = y;
            x += pane.width;
            pane.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) {
        for (ContentPane<?> pane : panes) {
            pane.typed(keyChar, keyCode);
        }
    }

    public ArrayList<ContentPane<?>> getContentPanes() {
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
            if (isInside(mouseX, mouseY)) {

                modulePane.getComponents().clear();
                for (ModuleButton button : moduleButtonCache.get(category)) {
                    modulePane.getComponents().add(button);
                }
                modulePane.metaTags.put("category", category);
            }
        }

        @Override
        public void release(int mouseX, int mouseY, int mouseButton) { }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            if (isInside(mouseX, mouseY)) {
                Gui.drawRect(x, y, x + width, y + height, 0x50ffffff);
            }
            Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0x90000000);
            StringBuilder sb = new StringBuilder();
            if (modulePane.metaTags.get("category") == category)
                sb.append("> ");
            sb.append(category.name());
            moonlightGui.getMoonlight().getFontRenderer().drawCenteredStringWithShadow(sb.toString(), x + width / 2f, y + height / 2f - moonlightGui.getMoonlight().getFontRenderer().getHeight() / 2f, -1);
        }

        @Override
        public void typed(char keyChar, int keyCode) { }
    }

    private class ModuleButton extends AbstractComponent {
        private final CopyOnWriteArrayList<SettingComponent> settingComponents;
        private final Module module;

        public ModuleButton(Module module, int x, int y, int width, int height) {
            super(x, y, width, height);
            this.module = module;
            this.settingComponents = new CopyOnWriteArrayList<>();

            for (Setting setting : module.getSettings()) {
                if (setting instanceof BoolSetting) {
                    settingComponents.add(new BoolComponent((BoolSetting)setting, 0, 0, settingPane.width, 16));
                }
            }
        }

        @Override
        public void click(int mouseX, int mouseY, int mouseButton) {
            if (isInside(mouseX, mouseY)) {
                if (mouseButton == 0) {
                    module.toggle();
                } else {
                    settingPane.getComponents().clear();
                    for (SettingComponent component : settingComponents) {
                        settingPane.getComponents().add(component);
                    }
                }
            }
        }

        @Override
        public void release(int mouseX, int mouseY, int mouseButton) { }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            if (isInside(mouseX, mouseY)) {
                Gui.drawRect(x, y, x + width, y + height, 0x50ffffff);
            }
            Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0x90000000);
            StringBuilder sb = new StringBuilder();
            if (settingPane.getComponents() == settingComponents)
                sb.append("> ");
            sb.append(module.getName());
            int color = 0x666666;
            if (module.isEnabled())
                color = -1;
            moonlightGui.getMoonlight().getFontRenderer().drawCenteredStringWithShadow(sb.toString(), x + width / 2f, y + height / 2f - moonlightGui.getMoonlight().getFontRenderer().getHeight() / 2f, color);
        }

        @Override
        public void typed(char keyChar, int keyCode) { }

        public Module getModule() {
            return module;
        }

        public CopyOnWriteArrayList<SettingComponent> getSettingComponents() {
            return settingComponents;
        }
    }
}