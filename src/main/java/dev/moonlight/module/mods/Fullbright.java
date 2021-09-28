package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Module.Info(
        name = "Fullbright",
        desc = "Makes the world bright",
        category = Module.Category.Render,
        alwaysRegistered = true
)
public class Fullbright extends Module {

    public float brightnessLevel = 0.0f;

    @SubscribeEvent
    public void onRenderTick(TickEvent.ClientTickEvent event) {
        if (isEnabled()) {
            if (brightnessLevel < 20.0f) {
                brightnessLevel += 1.0f;
            }
        } else {
            if (brightnessLevel > 0.0f) {
                brightnessLevel -= 1.0f;
            }
        }

        brightnessLevel = MathHelper.clamp(brightnessLevel, 0.0f, 20.0f);
    }
}
