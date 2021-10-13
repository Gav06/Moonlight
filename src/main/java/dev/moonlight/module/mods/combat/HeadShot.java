package dev.moonlight.module.mods.combat;

import dev.moonlight.event.events.PacketEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(name = "HeadShot", desc = "Basically 32K bow.", category = Module.Category.Combat)
public class HeadShot extends Module {
    public BoolSetting bows = new BoolSetting("Bows", true, false);
    public BoolSetting pearls = new BoolSetting("Pearls", true, false);
    public BoolSetting eggs = new BoolSetting("Eggs", true, false);
    public BoolSetting snowballs = new BoolSetting("Snowballs", true, false);
    public FloatSetting timeOut = new FloatSetting( "Timeout", 5000, 100, 2000);
    public FloatSetting spoofs = new FloatSetting("Spoofs", 10, 1, 300);
    public BoolSetting bypass = new BoolSetting("Bypass", false, false);

    private long lastShootTime;

    @Override
    public void onEnable() {
        lastShootTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();

            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                ItemStack handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);

                if (!handStack.isEmpty() && handStack.getItem() != null && handStack.getItem() instanceof ItemBow && bows.getValue()) {
                    doSpoofs();
                }
            }

        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
            CPacketPlayerTryUseItem packet2 = (CPacketPlayerTryUseItem) event.getPacket();

            if (packet2.getHand() == EnumHand.MAIN_HAND) {
                ItemStack handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);

                if (!handStack.isEmpty() && handStack.getItem() != null) {
                    if (handStack.getItem() instanceof ItemEgg && eggs.getValue()) {
                        doSpoofs();
                    } else if (handStack.getItem() instanceof ItemEnderPearl && pearls.getValue()) {
                        doSpoofs();
                    } else if (handStack.getItem() instanceof ItemSnowball && snowballs.getValue()) {
                        doSpoofs();
                    }
                }
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - lastShootTime >= timeOut.getValue()) {
            lastShootTime = System.currentTimeMillis();
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int index = 0; index < spoofs.getValue(); ++index) {
                if (bypass.getValue()) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1e-10, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1e-10, mc.player.posZ, true));
                } else {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1e-10, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1e-10, mc.player.posZ, false));
                }
            }
        }
    }
}
