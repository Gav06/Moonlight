package dev.moonlight.ui.clickgui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.misc.FPSHelper;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.client.HUD;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.ui.clickgui.api.HUDComponent;

import java.awt.*;

public class FPSComponent extends HUDComponent {
    public FPSComponent(int x, int y, int width, int height) {
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
        String fpsThing = ChatFormatting.GRAY + "FPS:" + ChatFormatting.RESET + "%.3f";
        String fps = String.format(fpsThing, FPSHelper.INSTANCE.getFpsAverage());
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(fps);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(fps);
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(fps, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
