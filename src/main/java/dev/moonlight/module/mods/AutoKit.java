package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.util.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "AutoKit",
        desc = "Automatically ",
        category = Module.Category.Player
)
public class AutoKit extends Module {

    @Override
    public void onEnable() {
        MessageUtil.sendMessage("Make a kit called autoKit.");
    }

    public boolean hasUsedKit = false;

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(mc.player.isEntityAlive() && mc.player.inventory.isEmpty() && !hasUsedKit || !mc.player.isDead && mc.player.inventory.isEmpty() && !hasUsedKit) {
            mc.player.sendChatMessage("/kit autoKit");
            hasUsedKit = true;
        }
        if(mc.player.isDead || mc.player.getHealth() <= 0 || !mc.player.isEntityAlive()) {
            hasUsedKit = false;
        }
    }

    @Override
    public void onDisable() {
        hasUsedKit = false;
    }
}
