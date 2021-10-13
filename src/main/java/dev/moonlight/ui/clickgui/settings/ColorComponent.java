package dev.moonlight.ui.clickgui.settings;

import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.settings.impl.ColorSetting;
import dev.moonlight.ui.clickgui.api.SettingComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class ColorComponent extends SettingComponent {
    private final ArrayList<SliderComponent> sliderComponents;
    public ColorSetting setting;
    private boolean open;

    public ColorComponent(ColorSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        sliderComponents = new ArrayList<>();
        sliderComponents.add(new SliderComponent(setting.r, x, y, width, height));
        sliderComponents.add(new SliderComponent(setting.g, x, y, width, height));
        sliderComponents.add(new SliderComponent(setting.b, x, y, width, height));
        sliderComponents.add(new SliderComponent(setting.a, x, y, width, height));
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            open = !open;
        }
        if (open) {
            for (SliderComponent component : sliderComponents) {
                component.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (SliderComponent component : sliderComponents) {
                component.release(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color((int) setting.r.getValue(), (int) setting.g.getValue(), (int) setting.b.getValue()).getRGB());
        final StringBuilder sb = new StringBuilder(setting.getName() + " [" + setting.r.getValue() + "," + setting.g.getValue() + "," + setting.b.getValue() + "," + setting.a.getValue() + "]");
        if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
            cfont.drawStringWithShadow(String.valueOf(sb), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(String.valueOf(sb), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
        }
        if (open) {
            int yOffset = y + height;
            for (SliderComponent component : sliderComponents) {
                component.x = x;
                component.y = yOffset;
                component.draw(mouseX, mouseY, partialTicks);
                yOffset += component.height;
            }
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) { }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
