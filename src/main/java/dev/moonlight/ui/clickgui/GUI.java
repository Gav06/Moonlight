package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import dev.moonlight.ui.clickgui.api.IComponent;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

public final class GUI extends GuiScreen {

    private final Moonlight moonlight;
    private final ArrayList<AbstractComponent> components;

    public GUI(Moonlight moonlight) {
        this.moonlight = moonlight;
        this.components = new ArrayList<>();

        // initialize components here
        this.components.add(new Window(this, 150, 150, 450, 300));
    }

    @ApiCall
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (moonlight.getModuleManager().getModule(dev.moonlight.module.mods.GUI.class).background.getValue())
            drawDefaultBackground();

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
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == moonlight.getModuleManager().getModule(dev.moonlight.module.mods.GUI.class).getBind()) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
            return;
        }

        for (IComponent component : components) {
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
