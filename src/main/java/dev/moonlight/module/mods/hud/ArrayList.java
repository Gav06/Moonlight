package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.hud.ArrayListComponent;

@Module.Info(
        name = "ArrayList",
        desc = "Draws enabled modules.",
        category = Module.Category.HUD
)
public class ArrayList extends HUDModule {
    public BoolSetting phobosColor = new BoolSetting("PhobosColors", false, true);
    public BoolSetting skipHUDModules = new BoolSetting("SkipHUDModules", true, false);

    public ArrayList() { super(new ArrayListComponent(1, 1, 1, 1)); }
}
