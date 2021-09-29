package dev.moonlight.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public abstract class PacketEvent extends Event {

    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public static class Send extends PacketEvent {

        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {

        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
