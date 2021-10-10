package dev.moonlight.module.hudMods;

import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.hud.CoordinatesComponent;

@Module.Info(
        name = "Coordinates",
        desc = "Shows the players coordinates.",
        category = Module.Category.HUD
)
public class Coordinates extends HUDModule {
    public BoolSetting nether = new BoolSetting("Nether", true, false);

    public Coordinates() {
        super(new CoordinatesComponent(2, 2, 30, 10));
    }
}
