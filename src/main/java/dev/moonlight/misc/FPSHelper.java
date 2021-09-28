package dev.moonlight.misc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FPSHelper {

    private int currFps;
    private int lastFps;
    private int fpsAvg;

    public static final FPSHelper INSTANCE = new FPSHelper();

    public FPSHelper() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private long startTime = 0L;
    private int frames;

    private int fps = -1;

    public int getFps() {
        return fps;
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (frames == 0) {
            startTime = System.currentTimeMillis();
            return;
        }
        frames++;
//        fps = (int) (frames / (System.currentTimeMillis() - startTime));
        int diff = (int) (System.currentTimeMillis() - startTime);
        System.out.println("------------------------");
        System.out.println(frames);
        System.out.println(diff);
        System.out.println(frames / diff);
    }
}
