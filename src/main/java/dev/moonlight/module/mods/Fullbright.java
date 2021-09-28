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

    public float brightnessLevel = 0f;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (isEnabled()) {
            if (brightnessLevel < 24f) {
                brightnessLevel += 1 * mc.getRenderPartialTicks();
            }
        } else {
            if (brightnessLevel > 0f) {
                brightnessLevel -= 1 * mc.getRenderPartialTicks();
            }
        }

        brightnessLevel = MathHelper.clamp(brightnessLevel, 0f, 24f);
    }

}
