package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.ModeSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "Sprint",
        desc = "Makes you sprint without having to hold shift.",
        category = Module.Category.Movement
)
public class Sprint extends Module {

    public ModeSetting mode = new ModeSetting("Mode", Mode.Legit);

    public enum Mode {
        Legit,
        Rage
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(mode.getValueEnum() == Mode.Legit && mc.player.moveForward > 0 && !mc.player.isSneaking() && !mc.player.isHandActive() && !mc.player.collidedHorizontally) {
            if (!mc.player.isSprinting())
                mc.player.setSprinting(true);
        }else if(mode.getValueEnum() == Mode.Rage){
            mc.player.setSprinting(false);
        }
    }
}
