package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "GUI",
        desc = "Opens the GUI.",
        category = Module.Category.Client,
        bind = Keyboard.KEY_GRAVE
)
public final class GUI extends Module {

    public final BoolSetting background = new BoolSetting("Background", false, false);
    public final BoolSetting descriptions = new BoolSetting("Descriptions", true, false);
    public final FloatSetting r = new FloatSetting("R", 255, 0, 255);
    public final FloatSetting g = new FloatSetting("G", 0, 0, 255);
    public final FloatSetting b = new FloatSetting("B", 0, 0, 255);
    public final FloatSetting a = new FloatSetting("A", 255, 0, 255);
    public final FloatSetting backgroundA = new FloatSetting("WindowA", 255, 0, 255);

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(moonlight.getGui());
        disable();
    }
}
