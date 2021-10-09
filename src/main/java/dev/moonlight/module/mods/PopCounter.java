package dev.moonlight.module.mods;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.events.DeathEvent;
import dev.moonlight.events.TotemPopEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Module.Info(
        name = "PopCounter",
        desc = "Counts pops kinda...",
        category = Module.Category.World
)
public class PopCounter extends Module {

    public BoolSetting self = new BoolSetting("Self", true, false);
    public BoolSetting onDeath = new BoolSetting("OnDeath", true, false);

    public HashMap<Entity, Integer> entityPops = new HashMap<>();

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e.equals(mc.player) && !self.getValue()) return;
            if(!entityPops.containsKey(event.getEntity())) {
                MessageUtil.sendRemovableMessage(ChatFormatting.GREEN + event.getEntity().getName() + ChatFormatting.RESET + " has just popped a totem ez.", event.getEntity().getEntityId());
                entityPops.put(event.getEntity(), + 1);
            }else if(entityPops.containsKey(event.getEntity())) {
                MessageUtil.sendRemovableMessage(ChatFormatting.GREEN + event.getEntity().getName() + ChatFormatting.RESET + " has popped " + entityPops.entrySet() + " ez.", event.getEntity().getEntityId());
                entityPops.put(event.getEntity(), + 1);
            }
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e.equals(mc.player) && !self.getValue()) return;
            if(e instanceof EntityPlayer) {
                if(onDeath.getValue()) {
                    if(((EntityPlayer) e).getHealth() <= 0 || e.isDead || !e.isEntityAlive()) {
                        MessageUtil.sendRemovableMessage(ChatFormatting.GREEN + event.getEntity().getName() + ChatFormatting.RESET + event.getEntity().getName() + " has just died he is so ez.", event.getEntity().getEntityId());
                        entityPops.remove(event.getEntity(), 0);
                    }
                }
            }
        }
    }
}
