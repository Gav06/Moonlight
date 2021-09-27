package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "ClickGUI",
        desc = "Opens the clickGui",
        category = Module.Category.Other,
        bind = Keyboard.KEY_GRAVE
)
public final class ClickGUI extends Module {

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(moonlight.getGui());
        disable();
    }
}
