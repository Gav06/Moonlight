package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.hud.HackListComponent;

@Module.Info(
        name = "HackList",
        desc = "Draws enabled modules.",
        category = Module.Category.HUD
)
public class HackList extends HUDModule {
    public BoolSetting skipHUDModules = new BoolSetting("SkipHUDModules", true, false);

    public HackList() { super(new HackListComponent(1, 1, 1, 1)); }
}
