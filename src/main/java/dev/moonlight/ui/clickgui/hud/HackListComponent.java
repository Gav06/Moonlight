package dev.moonlight.ui.clickgui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.module.mods.hud.HackList;
import dev.moonlight.module.mods.client.HUD;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.ui.clickgui.api.DragComponent;
import dev.moonlight.ui.clickgui.api.HUDComponent;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class HackListComponent extends HUDComponent {
    public HackListComponent(int x, int y, int width, int height) {
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
        int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).r.getValue();
        int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).g.getValue();
        int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).b.getValue();
        int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).a.getValue();
        for(Module module : Moonlight.INSTANCE.getModuleManager().getModuleList()) {
            if(module.isEnabled() && module.isVisible()) {
                if(module.getCategory().equals(Module.Category.HUD) && moduleManager.getModule(HackList.class).skipHUDModules.getValue()) continue;
                if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
                    Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(module.getName() + module.getMetaData(), x, y + yOffset, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
                }else {
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName() + module.getMetaData(), x, y + yOffset, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
                }
                yOffset += Moonlight.INSTANCE.getFontRenderer().getStringHeight(module.getName()) + 2;
            }
        }
        if(isInside(mouseX, mouseY) && moduleManager.getModule(HUD.class).xyDescription.getValue()) {
            String description = "X:" + x + " Y:" + y;
            Gui.drawRect(mouseX + 1, mouseY - 6, mouseX + 5 + (moduleManager.getModule(Font.class).isEnabled() ? Moonlight.INSTANCE.getFontRenderer().getStringWidth(description) : Minecraft.getMinecraft().fontRenderer.getStringWidth(description)), mouseY - 3 + (moduleManager.getModule(Font.class).isEnabled() ? Moonlight.INSTANCE.getFontRenderer().getHeight() : Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0x90000000);
            if(moduleManager.getModule(Font.class).isEnabled()) {
                Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(description, mouseX + 3, mouseY - 4, -1);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(description, mouseX + 3, mouseY - 4, -1);
            }
            RenderUtil.outline2d(mouseX + 1, mouseY - 6, mouseX + 5 + (moduleManager.getModule(Font.class).isEnabled() ? Moonlight.INSTANCE.getFontRenderer().getStringWidth(description) : Minecraft.getMinecraft().fontRenderer.getStringWidth(description)), mouseY - 2 + (moduleManager.getModule(Font.class).isEnabled() ? Moonlight.INSTANCE.getFontRenderer().getHeight() : Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), new Color(r, g, b, a).getRGB());
        }
        this.width = 75;
        this.height = yOffset;
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
