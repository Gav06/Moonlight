package dev.moonlight.ui.clickgui;

import dev.moonlight.ui.clickgui.api.DragComponent;
import net.minecraft.client.gui.Gui;

public class TestComponent extends DragComponent {
    public TestComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
    }
}
