package dev.moonlight.ui.hud;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.module.HUDModule;
import dev.moonlight.module.Module;
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
        for(Module module : moonlight.getModuleManager().getCategoryModules(Module.Category.HUD)) {
            if(module.isEnabled()) {
                final HUDModule hudMod = (HUDModule) module;
                hudMod.getComponent().draw(-1, -1, event.getPartialTicks());
            }
        }
    }
}
