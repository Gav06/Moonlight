package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.hud.IPComponent;

@Module.Info(
        name = "IP",
        desc = "Shows the active server.",
        category = Module.Category.HUD
)
public class IP extends HUDModule {
    public BoolSetting censorIp = new BoolSetting("CensorIP", false, false);

    public IP() {
        super(new IPComponent(1, 1, 1, 1));
    }
}
