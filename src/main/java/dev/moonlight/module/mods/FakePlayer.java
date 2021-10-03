package dev.moonlight.module.mods;

import com.mojang.authlib.GameProfile;
import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.util.TickTimer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;
import java.util.Timer;

@Module.Info(
        name = "FakePlayer",
        desc = "Spawns a fake entity for testing configs.",
        category = Module.Category.World
)
public class FakePlayer extends Module {

    public BoolSetting moving = new BoolSetting("Moving", false, false);
    public FloatSetting distanceToMove = new FloatSetting("DistanceToMove", 5, 1, 20, () -> moving.getValue());
    public BoolSetting autoRespawn = new BoolSetting("AutoRespawn", true, false);
    public FloatSetting distanceToRespawn = new FloatSetting("DistanceToRespawn", 10, 1, 30, () -> autoRespawn.getValue());

    public EntityOtherPlayerMP fakePlayer;

    public Random random = new Random();

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
        if(moving.getValue()) {
            TickTimer timer = new TickTimer();
            if(timer.hasTicksPassed((long) distanceToRespawn.getValue())) {
                fakePlayer.posX += random.nextDouble();
                if(!mc.world.getBlockState(new BlockPos(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ)).getBlock().equals(Blocks.AIR))
                    fakePlayer.posY += 1D;
                fakePlayer.posZ += random.nextDouble();
                timer.reset();
            }
        }
        if(autoRespawn.getValue() && fakePlayer != null) {
            if(fakePlayer.getDistance(mc.player) >= distanceToRespawn.getValue()) {
                fakePlayer.attemptTeleport(mc.player.posX, mc.player.posY, mc.player.posZ);
            }
        }
    }

    @Override
    public void onDisable() {
        if(mc.world == null && fakePlayer != null) mc.world.removeEntity(fakePlayer);
    }
}
