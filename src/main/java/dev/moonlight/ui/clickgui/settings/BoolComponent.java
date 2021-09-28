package dev.moonlight.ui.clickgui.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.SettingComponent;
import net.minecraft.client.gui.Gui;

public final class BoolComponent extends SettingComponent {

    private final BoolSetting boolSetting;

    public BoolComponent(BoolSetting boolSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.boolSetting = boolSetting;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            boolSetting.toggle();
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isInside(mouseX, mouseY)) {
            Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
        }

        final StringBuilder sb = new StringBuilder(boolSetting.getName() + ":");

        if (boolSetting.getValue()) {
            sb.append(ChatFormatting.GREEN + " true");
        } else {
            sb.append(ChatFormatting.RED + " false");
        }

        cfont.drawStringWithShadow(sb.toString(), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
