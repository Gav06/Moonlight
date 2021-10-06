package dev.moonlight.module.mods;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "ArrayList",
        desc = "Displays enabled modules.",
        category = Module.Category.Client
)
public class ArrayList extends Module {

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        int yOffset = 2;
        for(Module module : Moonlight.INSTANCE.getModuleManager().getModuleList()) {
            if(module.isEnabled() && module.isVisible()) {
                float y = yOffset;
                moonlight.getFontRenderer().drawStringWithShadow(module.getName() + module.getMetaData(), 1, y, -1);
                yOffset += moonlight.getFontRenderer().getStringHeight(module.getName()) + 2;
            }
        }
    }
}
