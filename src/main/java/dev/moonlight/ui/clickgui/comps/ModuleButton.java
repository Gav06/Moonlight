package dev.moonlight.ui.clickgui.comps;

import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ModuleButton extends AbstractComponent {
    Module module;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) module.toggle();
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName(), x, y, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
