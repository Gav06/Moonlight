package dev.moonlight.module.mods;

import dev.moonlight.events.PacketEvent;
import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BindSetting;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.settings.impl.ModeSetting;
import dev.moonlight.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;

/**
 * @Author zPrestige_
 * @Since 05/10/21
 */

@Module.Info(name = "AutoCrystal", category = Module.Category.Combat, desc = "obliterates kids with ender crystals.")
public class AutoCrystal extends Module {

    public FloatSetting placeRange = new FloatSetting("PlaceRange", 5, 0, 6);
    public FloatSetting breakRange = new FloatSetting("BreakRange", 5, 0, 6);
    public FloatSetting targetRange = new FloatSetting("TargetRange", 10, 0, 15);

    public FloatSetting minimumDamage = new FloatSetting("MinimumDamage", 6, 0, 12);
    public FloatSetting maximumSelfDamage = new FloatSetting("MaximumSelfDamage", 8, 0, 12);
    public BoolSetting antiSuicide = new BoolSetting("AntiSuicide", false, false);

    public BoolSetting packetBreak = new BoolSetting("PacketBreak", false, false);
    public BoolSetting breakPredict = new BoolSetting("BreakPredict", false, false);
    public BoolSetting soundPredict = new BoolSetting("SoundPredict", false, false);
    public BoolSetting placePredict = new BoolSetting("PlacePredict", false, false);

    public FloatSetting placeDelay = new FloatSetting("PlaceDelay", 100, 0, 500);
    public FloatSetting breakDelay = new FloatSetting("BreakDelay", 100, 0, 500);

    public BoolSetting silentSwitch = new BoolSetting("SilentSwitch", false, false);
    public BoolSetting antiWeakness = new BoolSetting("AntiWeakness", false, false, () -> silentSwitch.getValue());

    public BoolSetting placeSwing = new BoolSetting("PlaceSwing", false, false);
    public ModeSetting placeSwingHand = new ModeSetting("PlaceSwingHand", PlaceSwingHand.MAINHAND, () -> placeSwing.getValue());

    public enum PlaceSwingHand {MAINHAND, OFFHAND}

    public BoolSetting breakSwing = new BoolSetting("BreakSwing", false, false);
    public ModeSetting breakSwingHand = new ModeSetting("BreakSwingHand", BreakSwingHand.MAINHAND, () -> breakSwing.getValue());

    public enum BreakSwingHand {MAINHAND, OFFHAND}

    public ModeSetting facePlaceMode = new ModeSetting("facePlaceMode", FacePlaceMode.Never);

    public enum FacePlaceMode {Never, Health, Bind, Always}

    public FloatSetting facePlaceHp = new FloatSetting("FacePlaceHp", 15, 0, 36, () -> facePlaceMode.getValueEnum().equals(FacePlaceMode.Health));
    public BindSetting facePlaceBind = new BindSetting("FacePlaceBind", Keyboard.KEY_NONE, () -> facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind));

    EntityPlayer targetPlayer;
    BlockPos finalPos;
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        targetPlayer = DamageUtil.getTarget(targetRange.getValue());

        if (targetPlayer == null)
            return;

        if (placeTimer.passedMs((long) placeDelay.getValue()))
            doPlace();

        if (breakTimer.passedMs((long) breakDelay.getValue()))
            doBreak();

    }

    void doPlace() {
        List<BlockPos> sphere = BlockUtil.getSphere(placeRange.getValue());
        int slot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
        int oldSlot = mc.player.inventory.currentItem;
        for (int size = sphere.size(), s = 0; s < size; ++s) {

            BlockPos pos = sphere.get(s);
            float selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            float selfDamage = DamageUtil.calculatePosDamage(pos, mc.player);
            float targetDamage = DamageUtil.calculatePosDamage(pos, targetPlayer);
            float targetHealth = targetPlayer.getHealth() + targetPlayer.getAbsorptionAmount();
            float minimumDamageValue = minimumDamage.getValue();

            if (BlockUtil.isPosValidForCrystal(pos)) {

                if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && targetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                    minimumDamageValue = 2;

                if (antiSuicide.getValue() && selfDamage > selfHealth)
                    return;

                if (selfDamage > maximumSelfDamage.getValue())
                    return;

                if (targetDamage < minimumDamageValue)
                    return;

                finalPos = pos;

            }
        }
        if (finalPos != null) {
            if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL))
                InventoryUtil.silentSwitchToSlot(slot);

            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(finalPos, EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));

            if (placeSwing.getValue())
                mc.player.swingArm(placeSwingHand.getValueEnum().equals(PlaceSwingHand.MAINHAND) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);

            if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)) {
                mc.player.inventory.currentItem = oldSlot;
                mc.playerController.updateController();
            }
        }
    }

    void doBreak() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                float selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                float selfDamage = DamageUtil.calculateEntityDamage((EntityEnderCrystal) entity, mc.player);
                float targetDamage = DamageUtil.calculateEntityDamage((EntityEnderCrystal) entity, targetPlayer);
                float targetHealth = targetPlayer.getHealth() + targetPlayer.getAbsorptionAmount();
                float minimumDamageValue = minimumDamage.getValue();
                int sword = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
                int oldSlot = mc.player.inventory.currentItem;

                if (entity.getDistance(mc.player) > MathUtil.square(breakRange.getValue()))
                    return;

                if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && targetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                    minimumDamageValue = 2;

                if (antiSuicide.getValue() && selfDamage > selfHealth)
                    return;

                if (selfDamage > maximumSelfDamage.getValue())
                    return;

                if (targetDamage < minimumDamageValue)
                    return;

                if (silentSwitch.getValue() && antiWeakness.getValue() && (mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD) && mc.player.getActivePotionEffects().equals(Potion.getPotionById(18)))
                    InventoryUtil.silentSwitchToSlot(sword);

                if (packetBreak.getValue()) {
                    mc.getConnection().sendPacket(new CPacketUseEntity(entity));
                } else {
                    mc.playerController.attackEntity(mc.player, entity);
                }

                if (silentSwitch.getValue() && antiWeakness.getValue() && (mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD) && mc.player.getActivePotionEffects().equals(Potion.getPotionById(18))) {
                    mc.player.inventory.currentItem = oldSlot;
                    mc.playerController.updateController();
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion)
            event.setCanceled(true);


        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity velocity;
            velocity = (SPacketEntityVelocity) event.getPacket();

            if (velocity.getEntityID() == mc.player.getEntityId())
                event.setCanceled(true);
        }

        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal) {
                        BlockPos predictedCrystalPos = new BlockPos(entity.posX, entity.posY - 1, entity.posZ);
                        float selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                        float selfDamage = DamageUtil.calculatePosDamage(predictedCrystalPos, mc.player);
                        float targetDamage = DamageUtil.calculatePosDamage(predictedCrystalPos, targetPlayer);
                        float targetHealth = targetPlayer.getHealth() + targetPlayer.getAbsorptionAmount();
                        float minimumDamageValue = minimumDamage.getValue();
                        int slot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
                        int oldSlot = mc.player.inventory.currentItem;

                        if (soundPredict.getValue() && entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= breakRange.getValue())
                            entity.setDead();

                        if (placePredict.getValue()) {

                            if (entity.getDistance(mc.player) > MathUtil.square(breakRange.getValue()))
                                return;

                            if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && targetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                                minimumDamageValue = 2;

                            if (antiSuicide.getValue() && selfDamage > selfHealth)
                                return;

                            if (selfDamage > maximumSelfDamage.getValue())
                                return;

                            if (targetDamage < minimumDamageValue)
                                return;

                            if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL))
                                InventoryUtil.silentSwitchToSlot(slot);

                            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(predictedCrystalPos, EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));

                            if (placeSwing.getValue())
                                mc.player.swingArm(placeSwingHand.getValueEnum().equals(PlaceSwingHand.MAINHAND) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);

                            if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)) {
                                mc.player.inventory.currentItem = oldSlot;
                                mc.playerController.updateController();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onEnable() {
        finalPos = null;
    }

    @Override
    protected void onDisable() {
        finalPos = null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (finalPos != null) {
            RenderUtil.drawBox(finalPos, new Color(255, 255, 255, 120));
        }
    }
}
