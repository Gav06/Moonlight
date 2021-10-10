package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.hud.IPComponent;

@Module.Info(
        name = "IP",
        desc = "Shows the active server.",
        category = Module.Category.HUD
)
public class IP extends HUDModule {
    public IP() {
        super(new IPComponent(1, 1, 1, 1));
    }
}
