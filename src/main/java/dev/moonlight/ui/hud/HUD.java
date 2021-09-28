package dev.moonlight.ui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.FPSHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

public final class HUD {

    private final Moonlight moonlight;

    public HUD(Moonlight moonlight) {
        this.moonlight = moonlight;
        MinecraftForge.EVENT_BUS.register(this);
    }

//    private final float[] frameRates = new float[20];
//
//    private float highestFps = 0;
//    private long lastGraphUpdateTime = 0L;

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        moonlight.getFontRenderer().drawStringWithShadow(String.format("FPS: %.3f", FPSHelper.INSTANCE.getFpsAverage()), 2f, 2f, -1);

//        // fps graph
//        final int graphMin = 0;
//
//        if (System.currentTimeMillis() - lastGraphUpdateTime > 500L) {
//            lastGraphUpdateTime = System.currentTimeMillis();
//            float fps = (float) FPSHelper.INSTANCE.getFpsAverage();
//            if (fps > highestFps)
//                highestFps = fps;
//            frameRates[0] = fps;
//            for (int i = 0; i < frameRates.length; i++) {
//                if (i != 0) {
//                    frameRates[i] = frameRates[i - 1];
//                }
//            }
//
//            System.out.println(Arrays.toString(frameRates));
//        }
//
//        final Tessellator tessellator = Tessellator.getInstance();
//        final BufferBuilder buffer = tessellator.getBuffer();
//        GlStateManager.disableTexture2D();
//        GlStateManager.glLineWidth(1f);
//        GlStateManager.color(1f, 1f, 1f, 1f);
//        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
//        for (int i = 0; i < frameRates.length; i++) {
//            buffer.pos(i * 5, 300, 0.0).endVertex();
//        }
//        tessellator.draw();
//        GlStateManager.enableTexture2D();
    }
}
