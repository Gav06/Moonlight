package dev.moonlight.event;

import dev.moonlight.Moonlight;
import dev.moonlight.event.events.DeathEvent;
import dev.moonlight.event.events.PacketEvent;
import dev.moonlight.event.events.TotemPopEvent;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public final class EventListener {

    private final Moonlight moonlight;

    public EventListener(Moonlight instance) {
        this.moonlight = instance;
    }

    @ApiCall
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
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
    public void onMouse(InputEvent.MouseInputEvent event) { }

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

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().world != null) {
            for (Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                final Entity entity = Minecraft.getMinecraft().world.getEntityByID(e.getEntityId());
                if (entity instanceof EntityPlayer) {
                    final EntityPlayer player = (EntityPlayer) entity;
                    if (player.getHealth() <= 0.0f || player.isDead || !player.isEntityAlive()) {
                        MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
                    }
                }
            }
        }
    }
}
