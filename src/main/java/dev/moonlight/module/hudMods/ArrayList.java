package dev.moonlight.module.hudMods;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.hud.ArrayListComponent;

@Module.Info(
        name = "ArrayList",
        desc = "Draws enabled modules.",
        category = Module.Category.HUD
)
public class ArrayList extends HUDModule {
    public ArrayList() {
        super(new ArrayListComponent(2, 2, 2, 2));
    }
}
