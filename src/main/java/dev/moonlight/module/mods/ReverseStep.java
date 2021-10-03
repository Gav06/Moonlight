package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "ReverseStep",
        desc = "Pulls you down.",
        category = Module.Category.Movement
)
public class ReverseStep extends Module {

    public FloatSetting vanillaSpeed = new FloatSetting("Speed", 9.0f, 0.1f, 9.0f);

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(nullCheck()) return;
        if (mc.player.isInLava() || mc.player.isInWater()) {
            return;
        }
        if (mc.player.onGround) {
            mc.player.motionY -= vanillaSpeed.getValue();
        }
    }
}
