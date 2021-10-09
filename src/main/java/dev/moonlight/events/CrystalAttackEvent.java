package dev.moonlight.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CrystalAttackEvent extends Event {
    int entityId;
    Entity entity;

    public CrystalAttackEvent(int entityId, Entity entity) {
        this.entityId = entityId;
        this.entity = entity;
    }

    public int getEntityId(){
        return entityId;
    }

    public Entity getEntity(){
        return entity;
    }
}
