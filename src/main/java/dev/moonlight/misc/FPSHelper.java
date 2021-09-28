package dev.moonlight.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FPSHelper {

    private int currFps;
    private int lastFps;
    private double fpsAvg;

    public static final FPSHelper INSTANCE = new FPSHelper();

    public FPSHelper() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private long startTime = 0L;
    private int frames = 0;

    public double getFpsAverage() {
        return fpsAvg;
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        // calculating fps
        frames++;
        if (System.currentTimeMillis() - startTime > 1000L) {
            lastFps = currFps;
            currFps = frames;
            frames = 0;
            startTime = System.currentTimeMillis();
        }

        // getting interpolated
        fpsAvg = MathHelper.clampedLerp((double) lastFps, (double) currFps, (System.currentTimeMillis() - startTime) / 1000.0) / 2.0;
    }
}
