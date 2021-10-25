package dev.moonlight.module.mods.misc;

import dev.moonlight.event.events.DeathEvent;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.util.MessageUtil;
import dev.moonlight.util.Timer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "AutoKit",
        desc = "Automatically ",
        category = Module.Category.Misc
)
public class AutoKit extends Module {

    public Timer timer = new Timer();
    public boolean hasUsedKit = false;

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(nullCheck()) return;
        if(mc.player.isEntityAlive() && mc.player.inventory.isEmpty() && !hasUsedKit || !mc.player.isDead && mc.player.inventory.isEmpty() && !hasUsedKit) {
            mc.player.sendChatMessage("/kit autoKit");
            MessageUtil.sendMessage("Used autoKit.");
            hasUsedKit = true;
        }
        if(hasUsedKit && timer.passedMs(2000)) {
            hasUsedKit = false;
            timer.reset();
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if(event.getEntity() == mc.player) {
            if(hasUsedKit) hasUsedKit = false;
        }
    }

    @Override
    public void onEnable() {
        MessageUtil.sendMessage("Make a kit called autoKit.");
    }

    @Override
    public void onDisable() {
        hasUsedKit = false;
    }
}
