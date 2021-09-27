package dev.moonlight.ui.clickgui;

import dev.moonlight.misc.ApiCall;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public final class GUI extends GuiScreen {

    @ApiCall
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    @ApiCall
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @ApiCall
    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @ApiCall
    @Override
    public void keyTyped(char keyChar, int keyCode) throws IOException {
        super.keyTyped(keyChar, keyCode);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
