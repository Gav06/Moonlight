package dev.moonlight.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil {
    public static final String messagePrefix = ChatFormatting.GRAY + "<" + Moonlight.DISPLAY_MOD_NAME + ChatFormatting.GRAY + "> " + ChatFormatting.RESET;
    public static final String errorPrefix = ChatFormatting.DARK_RED + "<" + Moonlight.MOD_NAME + "> " + ChatFormatting.RESET;

    public static void sendRawMessage(String message) {
        if(Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
        }
    }

    public static void sendMessage(String message) {
        sendRawMessage(messagePrefix + message);
    }

    public static void sendError(String message) {
        sendRawMessage(errorPrefix + message);
    }

    public static void sendRemovableMessage(String message, int id) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(messagePrefix + message), id);
    }
}
