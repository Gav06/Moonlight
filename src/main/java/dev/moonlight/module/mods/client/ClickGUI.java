package dev.moonlight.module.mods.client;

import dev.moonlight.module.Module;

@Module.Info(name = "ClickGUI", desc = "Displays the ClickGUI.", category = Module.Category.Client)
public class ClickGUI extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(moonlight.getClickGUI());
        toggle();
    }
}
