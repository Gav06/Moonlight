package dev.moonlight.ui.clickgui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.hud.ArrayList;
import dev.moonlight.module.mods.client.HUD;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.ui.clickgui.api.HUDComponent;

import java.awt.*;

public class ArrayListComponent extends HUDComponent {
    public ArrayListComponent(int x, int y, int width, int height) {
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
        int yOffset = 2;
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        for(Module module : Moonlight.INSTANCE.getModuleManager().getModuleList()) {
            if(module.isEnabled() && module.isVisible()) {
                if(module.getCategory().equals(Module.Category.HUD) && moduleManager.getModule(ArrayList.class).skipHUDModules.getValue()) continue;
                Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(module.getName() + module.getMetaData(), x, y + yOffset,
                        moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
                yOffset += Moonlight.INSTANCE.getFontRenderer().getStringHeight(module.getName()) + 2;
            }
        }
        this.width = 75;
        this.height = yOffset;
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
