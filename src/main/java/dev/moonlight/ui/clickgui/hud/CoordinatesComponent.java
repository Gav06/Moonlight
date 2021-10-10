package dev.moonlight.ui.clickgui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.hudMods.Coordinates;
import dev.moonlight.module.hudMods.HUD;
import dev.moonlight.module.mods.GUI;
import dev.moonlight.ui.clickgui.HUDComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

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
        String coordinates = moduleManager.getModule(Coordinates.class).nether.getValue() && Minecraft.getMinecraft().player.dimension == 0 ? "X:" + pos.getX() + ChatFormatting.RED + "[" + (pos.getX() / 8) + "]" + ChatFormatting.RESET + "Y:" + pos.getY() + ChatFormatting.RED + "[" + (pos.getY() / 8) + "]" + ChatFormatting.RESET + "Z:" +  pos.getZ() + ChatFormatting.RED + "[" + (pos.getZ() / 8) + "]" + ChatFormatting.RESET : "X:" + pos.getX() + "Y:" + pos.getY() + "Z:" +  pos.getZ();
        this.width = Moonlight.INSTANCE.getFontRenderer().getStringWidth(coordinates);
        this.height = Moonlight.INSTANCE.getFontRenderer().getStringHeight(coordinates);
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(coordinates, x, y, moduleManager.getModule(HUD.class).clientSync.getValue() ? new Color(moduleManager.getModule(GUI.class).r.getValue() / 255f, moduleManager.getModule(GUI.class).g.getValue() / 255f, moduleManager.getModule(GUI.class).b.getValue() / 255f).getRGB() : -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) { }
}
