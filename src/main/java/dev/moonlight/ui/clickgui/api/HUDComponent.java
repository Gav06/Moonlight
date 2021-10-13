package dev.moonlight.ui.clickgui.api;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.font.CFontRenderer;
import dev.moonlight.module.HUDModule;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.ui.clickgui.GUI;
import dev.moonlight.ui.clickgui.api.DragComponent;
import net.minecraft.client.Minecraft;

public abstract class HUDComponent extends DragComponent {
    protected final CFontRenderer cfont = Moonlight.INSTANCE.getFontRenderer();

    private HUDModule parent;

    public HUDComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean isVisible() {
        return parent.isEnabled();
    }

    public void setParent(HUDModule module) {
        parent = module;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(Minecraft.getMinecraft().currentScreen instanceof GUI) {
            GUI.drawRect(x, y, x + width, y + height, isInside(mouseX, mouseY) ? 0x50ffffff : 0x90000000);
        }
        super.draw(mouseX, mouseY, partialTicks);
    }
}
