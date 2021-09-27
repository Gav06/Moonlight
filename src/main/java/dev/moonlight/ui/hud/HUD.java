package dev.moonlight.ui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class HUD {

    public HUD() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow("Hello!", 0f, 0f, -1);
    }
}
