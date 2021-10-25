package dev.moonlight.module.mods.misc;

import dev.moonlight.module.Module;

@Module.Info(name = "Auto32K", desc = "Spawns a 32k sword for you (creative or backdoor only).", category = Module.Category.Misc)
public class AutoThirtyTwoK extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("/give @p minecraft:diamond_sword 1 0 {ench:[{id:16,lvl:32767},{id:19,lvl:10},{id:20,lvl:32767},{id:21,lvl:10},{id:22,lvl:3},{id:34,lvl:32767},{id:70,lvl:1},{id:71,lvl:1}]}");
        toggle();
    }
}
