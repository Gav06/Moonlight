package dev.moonlight.module.hudMods;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;

@Module.Info(
        name = "HUD",
        desc = "Shit",
        category = Module.Category.Client
)
public class HUD extends Module {

    public BoolSetting clientSync = new BoolSetting("ClientSync", true, false);
}
