package dev.moonlight.ui.clickgui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.module.mods.hud.Coordinates;
import dev.moonlight.module.mods.client.HUD;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.ui.clickgui.api.HUDComponent;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CoordinatesComponent extends HUDComponent {
    public CoordinatesComponent(int x, int y, int width, int height) {
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
        BlockPos pos = Minecraft.getMinecraft().player.getPosition();
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).r.getValue();
        int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).g.getValue();
        int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).b.getValue();
        int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.client.GUI.class).a.getValue();
        String coordinates = moduleManager.getModule(Coordinates.class).nether.getValue() && Minecraft.getMinecraft().player.dimension == 0 ? ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + pos.getX() + ChatFormatting.RED + "[" + (pos.getX() / 8) + "] " + ChatFormatting.GRAY + "Y:" + ChatFormatting.RESET + pos.getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET +  pos.getZ() + ChatFormatting.RED + "[" + (pos.getZ() / 8) + "]" + ChatFormatting.RESET : ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + pos.getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + pos.getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET +  pos.getZ();
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(coordinates);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(coordinates);
        if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
            Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(coordinates, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(coordinates, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
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
        GlStateManager.popMatrix();
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
