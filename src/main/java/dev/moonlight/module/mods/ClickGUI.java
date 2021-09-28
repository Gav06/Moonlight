package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "ClickGUI",
        desc = "Opens the clickGui",
        category = Module.Category.Client,
        bind = Keyboard.KEY_GRAVE
)
public final class ClickGUI extends Module {

    public final BoolSetting background = new BoolSetting("Background", false);

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(moonlight.getGui());
        disable();
    }
}
