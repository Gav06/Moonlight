package dev.moonlight.module.mods.world;

import dev.moonlight.module.Module;

@Module.Info(name = "Gerald0mcSimulator", desc = "Im fucking quitting coding fuck this shit.", category = Module.Category.World)
public class GeraldZeroMcSimulator extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("This is the consequences of gerald0mc's absolute retard ability to code. Fuck this shit. Trash ass coding.");
        toggle();
    }
}
