package dev.moonlight.module.mods.player;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BindSetting;
import dev.moonlight.settings.impl.BoolSetting;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "Suicide",
        desc = "Automatically kills the player.",
        category = Module.Category.Player
)
public class Suicide extends Module {

    public BoolSetting suicideBomb = new BoolSetting("SuicideBomb", true, false);
    public BindSetting bindToNuke = new BindSetting("BindToNuke", Keyboard.KEY_N, () -> suicideBomb.getValue());

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        if(suicideBomb.getValue()) return;
        mc.player.sendChatMessage("/kill");
        toggle();
    }
}
