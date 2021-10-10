package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.ui.clickgui.hud.WatermarkComponent;

@Module.Info(
        name = "Watermark",
        desc = "Thing",
        category = Module.Category.HUD
)
public class Watermark extends HUDModule {

    public BoolSetting csgo = new BoolSetting("CSGO", true, false);
    public FloatSetting lineWidth = new FloatSetting("LineWidth", 3.0f, 0.1f, 5.0f, () -> csgo.getValue());
    public BoolSetting version = new BoolSetting("Version", true, false);

    public Watermark() { super(new WatermarkComponent(1, 1, 1, 1)); }
}
