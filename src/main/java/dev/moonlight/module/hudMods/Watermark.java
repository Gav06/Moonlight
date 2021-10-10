package dev.moonlight.module.hudMods;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.hud.WatermarkComponent;

@Module.Info(
        name = "Watermark",
        desc = "Thing",
        category = Module.Category.HUD
)
public class Watermark extends HUDModule {

    public BoolSetting version = new BoolSetting("Version", true, false);

    public Watermark() {
        super(new WatermarkComponent(2, 2, 30, 10));
    }
}
