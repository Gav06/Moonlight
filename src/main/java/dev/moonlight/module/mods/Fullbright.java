package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Module.Info(
        name = "Fullbright",
        desc = "Makes the world bright",
        category = Module.Category.Render,
        registerByDefault = false
)
public final class Fullbright extends Module {

    public Fullbright() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public float brightnessLevel = 0.0f;

    @SubscribeEvent
    public void onRenderTick(TickEvent.ClientTickEvent event) {
        if (isEnabled()) {
            if (brightnessLevel < 16.0f) {
                brightnessLevel += 0.5f;
            }
        } else {
            if (brightnessLevel > 0.0f) {
                brightnessLevel -= 0.5f;
            }
        }

        brightnessLevel = MathHelper.clamp(brightnessLevel, 0.0f, 16.0f);
    }
}
