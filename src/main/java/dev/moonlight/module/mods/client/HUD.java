package dev.moonlight.module.mods.client;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;

@Module.Info(
        name = "HUD",
        desc = "Shit",
        category = Module.Category.Client,
        enabled = true
)
public class HUD extends Module {

    public BoolSetting clientSync = new BoolSetting("ClientSync", true, false);
    public BoolSetting xyDescription = new BoolSetting("XYDescription", true, false);
}
