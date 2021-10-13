package dev.moonlight.module.mods.player;

import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "AutoLog",
        desc = "Losg when you reach a certain health.",
        category = Module.Category.Player
)
public class AutoLog extends Module {

    public FloatSetting healthToLog = new FloatSetting("AutoLog", 6, 0, 36);

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(mc.player.getHealth() <= healthToLog.getValue()) {
            mc.player.connection.sendPacket(new SPacketDisconnect());
            toggle();
        }
    }
}
