package dev.moonlight.ui.clickgui.settings;

import dev.moonlight.settings.impl.ModeSetting;
import dev.moonlight.ui.clickgui.SettingComponent;
import dev.moonlight.ui.clickgui.api.Rect;
import net.minecraft.client.gui.Gui;

public final class ModeComponent extends SettingComponent {

    private final ModeSetting setting;

    public ModeComponent(ModeSetting modeSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = modeSetting;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                setting.increase();
            } else if (mouseButton == 1) {
                setting.decrease();
            }
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isInside(mouseX, mouseY)) {
            Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
        }

        cfont.drawStringWithShadow(setting.getName() + ": " + setting.getValueEnum().toString(), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
