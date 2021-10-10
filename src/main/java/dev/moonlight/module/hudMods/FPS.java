package dev.moonlight.module.hudMods;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.hud.FPSComponent;

@Module.Info(
        name = "FPS",
        desc = "EZ",
        category = Module.Category.HUD
)
public class FPS extends HUDModule {
    public FPS() {
        super(new FPSComponent(2, 2, 30, 10));
    }
}
