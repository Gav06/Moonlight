package dev.moonlight.ui.clickgui.settings;

import dev.moonlight.settings.impl.BindSetting;
import dev.moonlight.ui.clickgui.api.SettingComponent;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class SetBindComponent extends SettingComponent {
    private final BindSetting setting;
    private boolean listening;

    public SetBindComponent(BindSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
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
        final String text = listening ? "Press a key..." : setting.getName() + " [" + Keyboard.getKeyName(setting.getBind()) + "]";
        cfont.drawStringWithShadow(text, x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) {
        if (listening) {
            if (keyCode == Keyboard.KEY_DELETE) {
                setting.setBind(0);
            } else {
                setting.setBind(keyCode);
            }

            listening = false;
        }
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
