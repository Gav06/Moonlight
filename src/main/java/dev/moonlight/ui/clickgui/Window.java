package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.settings.Setting;
import dev.moonlight.settings.impl.*;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import dev.moonlight.ui.clickgui.api.ContentPane;
import dev.moonlight.ui.clickgui.api.DragComponent;
import dev.moonlight.ui.clickgui.api.SettingComponent;
import dev.moonlight.ui.clickgui.settings.*;
import dev.moonlight.util.MessageUtil;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


// class for holding all of the code for the main window
public final class Window extends AbstractComponent {

    private final WindowHeader header;

    private final ArrayList<ContentPane<?>> panes;
    private final GUI moonlightGui;

    private final ContentPane<ModuleButton> modulePane;
    private final ContentPane<SettingComponent> settingPane;

    private final HashMap<Module.Category, List<ModuleButton>> moduleButtonCache;

    public Window(GUI moonlightGui, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new WindowHeader(x, y, width, moonlightGui.getMoonlight().getFontRenderer().getHeight() + 4);
        this.panes = new ArrayList<>();
        this.moonlightGui = moonlightGui;
        this.moduleButtonCache = new HashMap<>();

        // used for accessing the different panes to perform actions
        ContentPane<CategoryButton> categoryPane;
        panes.add(categoryPane = new ContentPane<>(x, y, 100, height));
        panes.add(this.modulePane = new ContentPane<>(x, y, width / 2 - 100, height));
        panes.add(this.settingPane = new ContentPane<>(x, y, width / 2, height));

        for (Module.Category category : Module.Category.values()) {
            moduleButtonCache.put(category, new ArrayList<>());
            for (Module module : moonlightGui.getMoonlight().getModuleManager().getCategoryModules(category)) {
                final ModuleButton button = new ModuleButton(module, 0, 0, width / 2 - 100, 14);
                moduleButtonCache.get(category).add(button);
            }
        }

        // category buttons initialization
        for (Module.Category category : Module.Category.values()) {
            categoryPane.getComponents().add(new CategoryButton(category, 0, 0, categoryPane.width, categoryPane.height / category.getClass().getEnumConstants().length));
        }
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
        int lbgr = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).lbgr.getValue();
        int lbgg = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).lbgg.getValue();
        int lbgb = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).lbgb.getValue();
        int lbga = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).lbga.getValue();
        int rbgr = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).rbgr.getValue();
        int rbgg = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).rbgg.getValue();
        int rbgb = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).rbgb.getValue();
        int rbga = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).rbga.getValue();
        int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).r.getValue();
        int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).g.getValue();
        int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).b.getValue();
        int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).a.getValue();

        moonlightGui.getMoonlight().getFontRenderer().drawStringWithShadow(Moonlight.MOD_NAME + " v" + Moonlight.VERSION, x + 2, header.y + 1, -1);

        //Right Rect
        Gui.drawRect(x + 100, y, x + width, y + height, new Color(rbgr, rbgg, rbgb, rbga).getRGB());
        //Left Rect
        Gui.drawRect(x, y, x + 100, y + height, new Color(lbgr, lbgg, lbgb, lbga).getRGB());
        //Middle
        Gui.drawRect(x + 99, y, x + 100, y + height, new Color(r, g, b, a).getRGB());

        RenderUtil.outline2d(x, y - header.height, x + width, y + height, new Color(r, g, b, a).getRGB());


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

    private static class WindowHeader extends DragComponent {
        public WindowHeader(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            super.draw(mouseX, mouseY, partialTicks);
            int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).r.getValue();
            int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).g.getValue();
            int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).b.getValue();
            int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).a.getValue();
            Gui.drawRect(x, y, x + width, y + height, new Color(r, g, b, a).getRGB());
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
                settingPane.getComponents().clear();
                settingPane.metaTags.remove("module");
                for (ModuleButton button : moduleButtonCache.get(category)) {
                    modulePane.getComponents().add(button);
                }
                modulePane.metaTags.put("category", category);
            }
        }

        @Override
        public void release(int mouseX, int mouseY, int mouseButton) {
        }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            if (isInside(mouseX, mouseY)) {
                Gui.drawRect(x, y, x + width, y + height, 0x50ffffff);
            }
            Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0x90000000);
            StringBuilder sb = new StringBuilder();
            if (modulePane.metaTags.get("category") == category) {
                sb.append("> ");
            }
            sb.append(category.name());
            moonlightGui.getMoonlight().getFontRenderer().drawCenteredStringWithShadow(sb.toString(), x + width / 2f, y + height / 2f - moonlightGui.getMoonlight().getFontRenderer().getHeight() / 2f, -1);
        }

        @Override
        public void typed(char keyChar, int keyCode) {
        }
    }

    private class ModuleButton extends AbstractComponent {
        private final CopyOnWriteArrayList<SettingComponent> settingComponents;
        private final Module module;

        public ModuleButton(Module module, int x, int y, int width, int height) {
            super(x, y, width, height);
            this.module = module;
            this.settingComponents = new CopyOnWriteArrayList<>();

            final int height_ = 16;
            for (Setting setting : module.getSettings()) {
                if (setting != null) {
                    if (setting instanceof BoolSetting) {
                        settingComponents.add(new BoolComponent((BoolSetting) setting, 0, 0, settingPane.width, height_));
                    } else if (setting instanceof ModeSetting) {
                        settingComponents.add(new ModeComponent((ModeSetting) setting, 0, 0, settingPane.width, height_));
                    } else if (setting instanceof FloatSetting) {
                        settingComponents.add(new SliderComponent((FloatSetting) setting, 0, 0, settingPane.width, height_));
                    } else if (setting instanceof ColorSetting) {
                        settingComponents.add(new ColorComponent((ColorSetting) setting, 0, 0, settingPane.width, height_));
                    } else if (setting instanceof BindSetting) {
                        settingComponents.add(new SetBindComponent((BindSetting) setting, 0, 0, settingPane.width, height_));
                    } else if (setting instanceof StringSetting) {
                        settingComponents.add(new StringComponent((StringSetting) setting, 0, 0, settingPane.width, height_));
                    }
                }
            }

            settingComponents.add(new BindComponent(module, 0, 0, settingPane.width, height_));
        }

        @Override
        public void click(int mouseX, int mouseY, int mouseButton) {
            if (isInside(mouseX, mouseY)) {
                if (mouseButton == 0) {
                    module.toggle();
                } else {
                    if (settingPane.metaTags.get("module") != module.getName()) {
                        settingPane.getComponents().clear();
                        for (SettingComponent component : settingComponents) {
                            settingPane.getComponents().add(component);
                        }
                        settingPane.metaTags.put("module", module.getName());
                    } else {
                        settingPane.getComponents().clear();
                        settingPane.metaTags.remove("module");
                    }
                }
                if(mouseButton == 2) {
                    module.setVisible();
                    MessageUtil.sendMessage(module.getName() + "'s visibility in the arrayList has been set to " + module.isVisible());
                }
            }
        }

        @Override
        public void release(int mouseX, int mouseY, int mouseButton) {
        }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).r.getValue();
            int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).g.getValue();
            int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).b.getValue();
            int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).a.getValue();
            Moonlight moonlight = Moonlight.INSTANCE;
            if (isInside(mouseX, mouseY)) {
                Gui.drawRect(x, y, x + width, y + height, 0x50ffffff);
            }
            Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0x90000000);
            if (!settingPane.getComponents().isEmpty())
                Gui.drawRect(settingPane.x, settingPane.y, settingPane.x + settingPane.width - 1, settingPane.y + settingPane.height, 0x90000000);
            StringBuilder sb = new StringBuilder();
            if (settingPane.metaTags.get("module") == module.getName())
                sb.append("> ");
            sb.append(module.getName());
            int color = 0x777777;
            if (module.isEnabled())
                color = -1;
            moonlightGui.getMoonlight().getFontRenderer().drawCenteredStringWithShadow(sb.toString(), x + width / 2f, y + height / 2f - moonlightGui.getMoonlight().getFontRenderer().getHeight() / 2f, color);
            if (isInside(mouseX, mouseY)) {
                if (moonlight.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).descriptions.getValue()) {
                    Gui.drawRect(mouseX + 1, mouseY - 6, mouseX + 5 + moonlightGui.getMoonlight().getFontRenderer().getStringWidth(module.getDesc()), mouseY - 3 + moonlightGui.getMoonlight().getFontRenderer().getStringHeight(module.getDesc()), 0x90000000);
                    moonlightGui.getMoonlight().getFontRenderer().drawStringWithShadow(module.getDesc(), mouseX + 3, mouseY - 4, -1);
                    RenderUtil.outline2d(mouseX + 1, mouseY - 6, mouseX + 5 + moonlightGui.getMoonlight().getFontRenderer().getStringWidth(module.getDesc()), mouseY - 2 + moonlightGui.getMoonlight().getFontRenderer().getStringHeight(module.getDesc()), new Color(r, g, b, a).getRGB());
                }
            }
        }

        @Override
        public void typed(char keyChar, int keyCode) {
        }

        public Module getModule() {
            return module;
        }
    }
}