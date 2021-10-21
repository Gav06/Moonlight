package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.api.IComponent;
import dev.moonlight.ui.clickgui.comps.CategoryComponent;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

    private final ArrayList<CategoryComponent> categoryComponents = new ArrayList<>();
    private final Moonlight moonlight;

    public ClickGUI(Moonlight moonlight) {
        this.moonlight = moonlight;
        for(Module.Category category : Module.Category.values()) {
            categoryComponents.add(new CategoryComponent(category, 0, 0, width, height));
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        for (IComponent component : categoryComponents) {
            if (component.isVisible()) {
                component.draw(mouseX, mouseY, partialTicks);
            }
        }
    }

    public void click(int mouseX, int mouseY, int mouseButton) {
        for (IComponent component : categoryComponents) {
            if (component.isVisible()) {
                component.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void release(int mouseX, int mouseY, int mouseButton) {
        for (IComponent component : categoryComponents) {
            if (component.isVisible()) {
                component.release(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void type(char keyChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).getBind()) {
            mc.displayGuiScreen(null);
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
            return;
        }

        for (IComponent component : categoryComponents) {
            if (component.isVisible()) {
                component.typed(keyChar, keyCode);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public Moonlight getMoonlight() {
        return moonlight;
    }
}
