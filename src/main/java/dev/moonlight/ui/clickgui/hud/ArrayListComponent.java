package dev.moonlight.ui.clickgui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.module.hudMods.Watermark;
import dev.moonlight.ui.clickgui.HUDComponent;

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
        for(Module module : Moonlight.INSTANCE.getModuleManager().getModuleList()) {
            if(module.isEnabled() && module.isVisible()) {
                Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(module.getName() + module.getMetaData(), x, y + yOffset, -1);
                yOffset += Moonlight.INSTANCE.getFontRenderer().getStringHeight(module.getName()) + 2;
            }
        }
        this.width = 75;
        this.height = yOffset;
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
