package dev.moonlight.module.mods.world;

import com.mojang.authlib.GameProfile;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "FakePlayer",
        desc = "Spawns a fake entity for testing configs.",
        category = Module.Category.World
)
public class FakePlayer extends Module {
    public BoolSetting copyInventory = new BoolSetting("CopyInventory", false, false);
    public BoolSetting autoRespawn = new BoolSetting("AutoRespawn", false, false);
    public FloatSetting distanceToRespawn = new FloatSetting("DistanceToRespawn", 10, 1, 30, () -> autoRespawn.getValue());

    public EntityOtherPlayerMP fakePlayer;

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), mc.player.getDisplayNameString()));
        mc.world.addEntityToWorld(fakePlayer.getEntityId(), fakePlayer);
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(autoRespawn.getValue() && fakePlayer != null) {
            if(copyInventory.getValue())
                fakePlayer.inventory = mc.player.inventory;
            if(fakePlayer.getDistance(mc.player) >= distanceToRespawn.getValue()) {
                try {
                    mc.world.removeEntity(fakePlayer);
                    fakePlayer.copyLocationAndAnglesFrom(mc.player);
                    fakePlayer.rotationYawHead = mc.player.rotationYawHead;
                    mc.world.addEntityToWorld(fakePlayer.getEntityId(), fakePlayer);
                }catch (Exception io) {
                    io.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if(mc.world != null && fakePlayer != null) mc.world.removeEntity(fakePlayer);
    }
}
