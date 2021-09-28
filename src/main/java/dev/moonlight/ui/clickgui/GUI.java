package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import dev.moonlight.ui.clickgui.api.ContentPane;
import dev.moonlight.ui.clickgui.api.IComponent;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class GUI extends GuiScreen {

    private final Moonlight moonlight;
    private final ArrayList<AbstractComponent> components;
    private final HashMap<String, AbstractComponent> componentMap;

    public GUI(Moonlight moonlight) {
        this.moonlight = moonlight;
        this.components = new ArrayList<>();
        this.componentMap = new HashMap<>();

        // initialize components here
        registerComponentToScreen(new Window(this, 150, 150, 450, 300), "main_window");
    }

    @ApiCall
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (IComponent component : components) {
            if (component.isVisible()) {
                component.draw(mouseX, mouseY, partialTicks);
            }
        }
    }

    @ApiCall
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (IComponent component : components) {
            if (component.isVisible()) {
                component.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    @ApiCall
    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (IComponent component : components) {
            if (component.isVisible()) {
                component.release(mouseX, mouseY, mouseButton);
            }
        }
    }

    @ApiCall
    @Override
    public void keyTyped(char keyChar, int keyCode) throws IOException {
        for (IComponent component : components) {
            if (component.isVisible()) {
                component.typed(keyChar, keyCode);
            }
        }

        super.keyTyped(keyChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void registerComponentToPane(AbstractComponent component, ContentPane pane, String componentTag) {
        pane.getComponents().add(component);
        componentMap.put(componentTag, component);
    }

    public void registerComponentToScreen(AbstractComponent component, String componentTag) {
        components.add(component);
        componentMap.put(componentTag, component);
    }

    public void registerPaneToWindow(ContentPane pane, Window window, String componentTag) {
        window.getContentPanes().add(pane);
        componentMap.put(componentTag, pane);
    }

    public AbstractComponent getComponentByTag(String tag) {
        return componentMap.get(tag);
    }

    public Moonlight getMoonlight() {
        return moonlight;
    }
}
