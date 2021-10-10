package dev.moonlight.ui.clickgui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.hudMods.HUD;
import dev.moonlight.module.hudMods.Watermark;
import dev.moonlight.module.mods.GUI;
import dev.moonlight.ui.clickgui.HUDComponent;

import java.awt.*;

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
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        String name = Moonlight.INSTANCE.getModuleManager().getModule(Watermark.class).version.getValue() ? Moonlight.DISPLAY_MOD_NAME + " " + Moonlight.VERSION : Moonlight.DISPLAY_MOD_NAME;
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(name);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(name);
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(name, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
