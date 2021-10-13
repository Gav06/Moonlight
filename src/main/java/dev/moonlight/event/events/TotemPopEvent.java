package dev.moonlight.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class TotemPopEvent extends Event {

    private final Entity entity;
    private final int popCount;

    public TotemPopEvent(Entity entity, int popCount) {
        this.entity = entity;
        this.popCount = popCount;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getPopCount() {
        return popCount;
    }
}
