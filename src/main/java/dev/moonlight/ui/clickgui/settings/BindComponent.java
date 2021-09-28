package dev.moonlight.ui.clickgui.settings;

import dev.moonlight.misc.Bind;
import dev.moonlight.ui.clickgui.SettingComponent;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class BindComponent extends SettingComponent {

    private final Bind bind;
    private boolean listening;

    public BindComponent(Bind bind, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.bind = bind;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            listening = !listening;
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isInside(mouseX, mouseY)) {
            Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
        }
        final String text = listening ? "Press a key..." : "Keybind [" + Keyboard.getKeyName(bind.getBind()) + "]";
        cfont.drawStringWithShadow(text, x + 2f, y + 2f, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) {
        if (listening) {
            if (keyCode == Keyboard.KEY_DELETE) {
                bind.setBind(0);
            } else {
                bind.setBind(keyCode);
            }

            listening = false;
        }
    }
}
