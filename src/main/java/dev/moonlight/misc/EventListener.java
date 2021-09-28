package dev.moonlight.misc;

import dev.moonlight.Moonlight;
import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public final class EventListener {

    private final Moonlight moonlight;

    public EventListener(Moonlight instance) {
        this.moonlight = instance;
    }

    @ApiCall
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        final int eventKey = Keyboard.getEventKey();
        if (Keyboard.getEventKeyState()) {
            // on key press
            for (Module module : moonlight.getModuleManager().getModuleList()) {
                if (module.getBind() == eventKey) {
                    module.toggle();
                }
            }
        } else {
            // on key release
        }
    }

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event) {

    }
}
