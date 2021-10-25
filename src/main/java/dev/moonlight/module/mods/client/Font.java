package dev.moonlight.module.mods.client;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;

@Module.Info(
        name = "Font",
        desc = "Custom font shit.",
        category = Module.Category.Client,
        enabled = true
)
public class Font extends Module {
    public FloatSetting fontSize = new FloatSetting("FontSize", 20, 1, 100);
}
