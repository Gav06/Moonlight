package dev.moonlight.module.mods.hud;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.hud.FPSComponent;

@Module.Info(
        name = "FPS",
        desc = "EZ",
        category = Module.Category.HUD
)
public class FPS extends HUDModule {
    public FPS() { super(new FPSComponent(1, 1, 1, 1)); }
}
