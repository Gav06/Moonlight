package dev.moonlight.misc;

import dev.moonlight.Moonlight;
import dev.moonlight.events.PacketEvent;
import dev.moonlight.events.TotemPopEvent;
import dev.moonlight.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.logging.Logger;

public final class EventListener {

    private final Moonlight moonlight;

    public EventListener(Moonlight instance) {
        this.moonlight = instance;
    }

    @ApiCall
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            System.out.println("im an alabama nigger and i wanna be free");
            for (Module module : moonlight.getModuleManager().getModuleList()) {
                if (module.getBind() == Keyboard.getEventKey()) {
                    System.out.println("Module is being toggled");
                    module.toggle();
                }
            }
        }
    }

    @ApiCall
    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event) {

    }

    @ApiCall
    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus statusPacket = (SPacketEntityStatus) event.getPacket();

            if (statusPacket.getOpCode() == 35) {
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent(statusPacket.getEntity(Minecraft.getMinecraft().world)));
            }
        }
    }

    @ApiCall
    @SubscribeEvent
    public void onEntityTotemPop(TotemPopEvent event) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("entity popped totem (" + event.getEntity().getName() + ")"));
    }
}
