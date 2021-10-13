package dev.moonlight.ui.clickgui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.module.mods.client.HUD;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.ui.clickgui.api.HUDComponent;
import net.minecraft.client.Minecraft;

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
        String ip = ChatFormatting.GRAY + "IP:" + ChatFormatting.RESET + (Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP != null ? Minecraft.getMinecraft().getCurrentServerData() : "SinglePlayer");
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(ip);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(ip);
        if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
            Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(ip, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ip, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
