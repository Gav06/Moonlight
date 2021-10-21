package dev.moonlight.module.mods.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.MessageUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "ChatFilter",
        desc = "Filters chat.",
        category = Module.Category.World
)
public class ChatFilter extends Module {

    public BoolSetting deleteMessage = new BoolSetting("DeleteMessage", false, false);
    public BoolSetting ads = new BoolSetting("Ads", true, false);

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.getMessage().getUnformattedComponentText().contains("discord") && ads.getValue()) {
            if (deleteMessage.getValue()) {
                event.setCanceled(true);
            } else {
                event.setMessage(new TextComponentString(MessageUtil.getModulePrefix(this, ChatFormatting.DARK_PURPLE) + " Advertisement censored by " + Moonlight.DISPLAY_MOD_NAME));
            }
        }
    }
}
