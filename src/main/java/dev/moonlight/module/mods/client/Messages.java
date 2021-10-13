package dev.moonlight.module.mods.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.ModuleToggleEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "Messages",
        desc = "Sends messages for the client.",
        category = Module.Category.Client
)
public class Messages extends Module {

    public BoolSetting waterMark = new BoolSetting("WaterMark", true, false);
    public BoolSetting toggleMessages = new BoolSetting("ToggleMessages", true, false);

    @SubscribeEvent
    public void onEnable(ModuleToggleEvent.Enable event) {
        if(toggleMessages.getValue()) {
            if(event.getModule().getName().equalsIgnoreCase("gui")) return;
            if(waterMark.getValue()) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.GREEN + ChatFormatting.BOLD + " ENABLED");
            }else {
                MessageUtil.sendRawMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.GREEN + ChatFormatting.BOLD + " ENABLED");
            }
        }
    }

    @SubscribeEvent
    public void onDisable(ModuleToggleEvent.Disable event) {
        if(toggleMessages.getValue()) {
            if(event.getModule().getName().equalsIgnoreCase("gui")) return;
            if(waterMark.getValue()) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RED + ChatFormatting.BOLD + " DISABLED");
            }else {
                MessageUtil.sendRawMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RED + ChatFormatting.BOLD + " DISABLED");
            }
        }
    }
}
