package dev.moonlight.ui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.FPSHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class HUD {

    private final Moonlight moonlight;

    public HUD(Moonlight moonlight) {
        this.moonlight = moonlight;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        moonlight.getFontRenderer().drawStringWithShadow(String.format("FPS: %.3f", FPSHelper.INSTANCE.getFpsAverage()), 2f, 2f, -1);
    }
}
