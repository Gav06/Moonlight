package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.settings.impl.StringSetting;
import dev.moonlight.ui.clickgui.hud.WatermarkComponent;

@Module.Info(
        name = "Watermark",
        desc = "Thing",
        category = Module.Category.HUD,
        enabled = true
)
public class Watermark extends HUDModule {

    public BoolSetting custom = new BoolSetting("Custom", false, false);
    public StringSetting customWatermark = new StringSetting("CustomWatermark", "Zori");
    public BoolSetting version = new BoolSetting("Version", true, false);

    public Watermark() { super(new WatermarkComponent(1, 1, 1, 1)); }
}
