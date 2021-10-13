package dev.moonlight.module.mods.render;

import dev.moonlight.module.Module;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Module.Info(
        name = "Fullbright",
        desc = "Makes the world bright",
        category = Module.Category.Render
)
public final class Fullbright extends Module {

    public Fullbright() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public float brightnessLevel = 0.0f;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
    	if (isEnabled()) {
            if (brightnessLevel < 20.0f) {
                brightnessLevel += 1.0f;
            }
        }else {
    	    if (brightnessLevel > 0.0f) {
                brightnessLevel -= 1.0f;
            }
        }
    }
}
