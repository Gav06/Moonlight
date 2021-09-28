package dev.moonlight.ui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.FPSHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public final class HUD {

    private final Moonlight moonlight;

    public HUD(Moonlight moonlight) {
        this.moonlight = moonlight;
        MinecraftForge.EVENT_BUS.register(this);
    }

    int[] frameRates = new int[10];

    int highestFps = 0;

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        moonlight.getFontRenderer().drawStringWithShadow(String.format("FPS: %.3f", FPSHelper.INSTANCE.getFpsAverage()), 2f, 2f, -1);

        // fps graph
        final int graphMin = 0;

        frameRates[0] = (int) FPSHelper.INSTANCE.getFpsAverage();
        for (int i = 0; i < frameRates.length; i++) {
            if (i != 0) {
                frameRates[i] = frameRates[i - 1];
            }
        }

        System.out.println(Arrays.toString(frameRates));
    }
}
