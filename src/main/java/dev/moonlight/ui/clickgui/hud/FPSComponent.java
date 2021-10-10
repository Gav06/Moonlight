package dev.moonlight.ui.clickgui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.FPSHelper;
import dev.moonlight.ui.clickgui.HUDComponent;

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
        String fps = String.format("FPS: %.3f", FPSHelper.INSTANCE.getFpsAverage());
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(fps);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(fps);
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(fps, x, y, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
