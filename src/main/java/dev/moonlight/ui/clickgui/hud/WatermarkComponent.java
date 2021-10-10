package dev.moonlight.ui.clickgui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.module.hudMods.Watermark;
import dev.moonlight.ui.clickgui.HUDComponent;

public class WatermarkComponent extends HUDComponent {
    public WatermarkComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        super.click(mouseX, mouseY, mouseButton);
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        super.release(mouseX, mouseY, mouseButton);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        String name = Moonlight.INSTANCE.getModuleManager().getModule(Watermark.class).version.getValue() ? Moonlight.DISPLAY_MOD_NAME + " " + Moonlight.VERSION : Moonlight.DISPLAY_MOD_NAME;
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(name);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(name);
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(name, x, y, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
