package dev.moonlight.module.mods.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "Log",
        desc = "Logs when you reach a certain health.",
        category = Module.Category.Player
)
public class Log extends Module {

    public FloatSetting healthToLog = new FloatSetting("Health", 6, 0, 36);

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(mc.player.getHealth() <= healthToLog.getValue()) {
            mc.player.connection.sendPacket(new SPacketDisconnect(new TextComponentString("Logged at" + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + mc.player.getHealth() + ChatFormatting.GRAY + "]")));
            toggle();
        }
    }
}
