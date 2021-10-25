package dev.moonlight.ui.clickgui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.module.mods.client.HUD;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.module.mods.hud.IP;
import dev.moonlight.ui.clickgui.api.HUDComponent;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class IPComponent extends HUDComponent {
    public IPComponent(int x, int y, int width, int height) { super(x, y, width, height); }

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
        int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).r.getValue();
        int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).g.getValue();
        int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).b.getValue();
        int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).a.getValue();
        String ip = ChatFormatting.GRAY + "IP:" + ChatFormatting.RESET + (Moonlight.INSTANCE.getModuleManager().getModule(IP.class).censorIp.getValue() ? "Censored" : (Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP != null ? Minecraft.getMinecraft().getCurrentServerData().serverIP : "SinglePlayer"));
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        this.width = Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled() ? Moonlight.INSTANCE.getFontRenderer().getStringWidth(ip) : Minecraft.getMinecraft().fontRenderer.getStringWidth(ip);
        this.height = Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled() ? Moonlight.INSTANCE.getFontRenderer().getStringHeight(ip) : Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
            Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(ip, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ip, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
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
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
