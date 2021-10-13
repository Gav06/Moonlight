package dev.moonlight.module.mods.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.DeathEvent;
import dev.moonlight.event.events.TotemPopEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Module.Info(
        name = "PopCounter",
        desc = "Counts player pops.",
        category = Module.Category.World
)
public class PopCounter extends Module {

    public BoolSetting self = new BoolSetting("Self", true, false);
    public BoolSetting onDeath = new BoolSetting("OnDeath", true, false);

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e.equals(mc.player) && !self.getValue()) return;
            if(event.getPopCount() == 0) {
                MessageUtil.sendRemovableMessage(ChatFormatting.GREEN + event.getEntity().getName() + ChatFormatting.RESET + " has just popped a totem ez.", event.getEntity().getEntityId());
            }else if(event.getPopCount() > 0) {
                MessageUtil.sendRemovableMessage(ChatFormatting.GREEN + event.getEntity().getName() + ChatFormatting.RESET + " has popped " + event.getPopCount() + " ez.", event.getEntity().getEntityId());
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
                    }
                }
            }
        }
    }
}
