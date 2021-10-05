package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.ui.clickgui.api.AbstractComponent;
import dev.moonlight.ui.clickgui.api.IComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

public final class GUI extends GuiScreen {

    private final Moonlight moonlight;
    private final ArrayList<AbstractComponent> components;

    public GUI(Moonlight moonlight) {
        this.moonlight = moonlight;
        components = new ArrayList<>();

        // initialize components here
        components.add(new Window(this, 150, 150, 450, 300));
    }

    @ApiCall
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (moonlight.getModuleManager().getModule(dev.moonlight.module.mods.GUI.class).backgroundMode.getValueEnum().equals(dev.moonlight.module.mods.GUI.BackgroundMode.Darken))
            drawDefaultBackground();

        for (IComponent component : components) {
            if (component.isVisible()) {
                component.draw(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void initGui() {
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer && moonlight.getModuleManager().getModule(dev.moonlight.module.mods.GUI.class).backgroundMode.getValueEnum().equals(dev.moonlight.module.mods.GUI.BackgroundMode.Blur)) {
            if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }

    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null)
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
    }

    @ApiCall
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (IComponent component : components) {
            if (component.isVisible()) {
                component.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    @ApiCall
    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (IComponent component : components) {
            if (component.isVisible()) {
                component.release(mouseX, mouseY, mouseButton);
            }
        }
    }
    @ApiCall
    @Override
    public void keyTyped(char keyChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Moonlight.INSTANCE.getModuleManager().getModule(dev.moonlight.module.mods.GUI.class).getBind()) {
            mc.displayGuiScreen(null);
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
            return;
        }

        for (IComponent component : components) {
            if (component.isVisible()) {
                component.typed(keyChar, keyCode);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public Moonlight getMoonlight() {
        return moonlight;
    }
}
