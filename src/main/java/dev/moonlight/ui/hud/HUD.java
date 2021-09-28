package dev.moonlight.ui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import net.minecraft.client.Minecraft;
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
//        moonlight.getFontRenderer().drawStringWithShadow(Moonlight.MOD_NAME + " v" + Moonlight.VERSION, 2, 2, -1);
    }
}
