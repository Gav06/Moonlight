package dev.moonlight.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class TotemPopEvent extends Event {

    private final Entity entity;

    public TotemPopEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
