package dev.moonlight.module.mods.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.event.events.CrystalAttackEvent;
import dev.moonlight.event.events.PacketEvent;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.mixin.mixins.ICPacketUseEntityMixin;
import dev.moonlight.module.Module;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.player.AutoSuicide;
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
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * @Author zPrestige_
 * @Since 05/10/21
 */

@Module.Info(
        name = "AutoCrystal",
        desc = "Obliterates kids with ender crystals.",
        category = Module.Category.Combat
)
public class AutoCrystal extends Module {
    public BoolSetting rangesParent = new BoolSetting("Ranges", false, true);
    public FloatSetting placeRange = new FloatSetting("PlaceRange", 5f, 0f, 6f, () -> rangesParent.getValue());
    public FloatSetting breakRange = new FloatSetting("BreakRange", 5f, 0f, 6f, () -> rangesParent.getValue());
    public FloatSetting targetRange = new FloatSetting("TargetRange", 10f, 0f, 15f, () -> rangesParent.getValue());

    public BoolSetting damagesParent = new BoolSetting("Damages", false, true);
    public FloatSetting minimumDamage = new FloatSetting("MinimumDamage", 6f, 0f, 12f, () -> damagesParent.getValue());
    public FloatSetting maximumSelfDamage = new FloatSetting("MaximumSelfDamage", 8f, 0f, 12f, () -> damagesParent.getValue());
    public BoolSetting antiSuicide = new BoolSetting("AntiSuicide", false, false, () -> damagesParent.getValue());

    public BoolSetting predictParent = new BoolSetting("Predicts", false, true);
    public BoolSetting soundPredict = new BoolSetting("SoundPredict", false, false, () -> predictParent.getValue());
    public BoolSetting placePredict = new BoolSetting("PlacePredict", false, false, () -> predictParent.getValue());
    public ModeSetting placePredictMode = new ModeSetting("PlacePredictMode", PlacePredictMode.SOUND, () -> predictParent.getValue() && placePredict.getValue());

    public enum PlacePredictMode {SOUND, EXPLOSION}

    public BoolSetting breakPredict = new BoolSetting("BreakPredict", false, false, () -> predictParent.getValue());
    public BoolSetting breakPredictCalc = new BoolSetting("BreakPredictCalc", false, false, () -> predictParent.getValue() && breakPredict.getValue());

    public BoolSetting delayParent = new BoolSetting("Delays", false, true);
    public FloatSetting placeDelay = new FloatSetting("PlaceDelay", 100, 0, 500, () -> delayParent.getValue());
    public FloatSetting breakDelay = new FloatSetting("BreakDelay", 100, 0, 500, () -> delayParent.getValue());

    public BoolSetting raytraceParent = new BoolSetting("Raytrace", false, true);
    public BoolSetting placeRaytrace = new BoolSetting("PlaceRaytrace", false, false, () -> raytraceParent.getValue());
    public FloatSetting placeRaytraceRange = new FloatSetting("PlaceRaytraceRange", 5f, 0f, 6f, () -> raytraceParent.getValue() && placeRaytrace.getValue());
    public BoolSetting breakRaytrace = new BoolSetting("BreakRaytrace", false, false, () -> raytraceParent.getValue());
    public FloatSetting breakRaytraceRange = new FloatSetting("BreakRaytraceRange", 5f, 0f, 6f, () -> raytraceParent.getValue() && breakRaytrace.getValue());

    public BoolSetting miscParent = new BoolSetting("Misc", false, true);
    public BoolSetting updatedPlacements = new BoolSetting("1.13+Placements", false, false, () -> miscParent.getValue());
    public BoolSetting limitAttack = new BoolSetting("LimitAttack", false, false, () -> miscParent.getValue());
    public BoolSetting packetBreak = new BoolSetting("PacketBreak", false, false, () -> miscParent.getValue());
    public BoolSetting allowCollision = new BoolSetting("AllowCollision", false, false, () -> miscParent.getValue());
    public BoolSetting cancelVelocity = new BoolSetting("CancelVelocity", false, false, () -> miscParent.getValue());
    public BoolSetting cancelExplosion = new BoolSetting("CancelExplosion", false, false, () -> miscParent.getValue());
    public BoolSetting silentSwitch = new BoolSetting("SilentSwitch", false, false, () -> miscParent.getValue());
    public BoolSetting antiWeakness = new BoolSetting("AntiWeakness", false, false, () -> silentSwitch.getValue() && miscParent.getValue());

    public BoolSetting swingParent = new BoolSetting("Swings", false, true);
    public BoolSetting placeSwing = new BoolSetting("PlaceSwing", false, false, () -> swingParent.getValue());
    public ModeSetting placeSwingHand = new ModeSetting("PlaceSwingHand", PlaceSwingHand.MAINHAND, () -> placeSwing.getValue() && swingParent.getValue());
    public enum PlaceSwingHand {MAINHAND, OFFHAND, PACKET}
    public BoolSetting breakSwing = new BoolSetting("BreakSwing", false, false, () -> swingParent.getValue());
    public ModeSetting breakSwingHand = new ModeSetting("BreakSwingHand", BreakSwingHand.MAINHAND, () -> breakSwing.getValue() && swingParent.getValue());
    public enum BreakSwingHand {MAINHAND, OFFHAND, PACKET}

    public BoolSetting facePlaceParent = new BoolSetting("FacePlacing", false, true);
    public ModeSetting facePlaceMode = new ModeSetting("FacePlaceMode", FacePlaceMode.Never, () -> facePlaceParent.getValue());
    public enum FacePlaceMode {Never, Health, Bind, Always}
    public FloatSetting facePlaceHp = new FloatSetting("FacePlaceDelay", 15f, 0f, 36f, () -> facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && facePlaceParent.getValue());
    public BindSetting facePlaceBind = new BindSetting("FacePlaceBind", Keyboard.KEY_NONE, () -> facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && facePlaceParent.getValue());

    public BoolSetting pauseParent = new BoolSetting("Pauses", false, true);
    public BoolSetting pauseOnGapple = new BoolSetting("Gapple", false, false, () -> pauseParent.getValue());
    public BoolSetting pauseOnSword = new BoolSetting("PauseOnSword", false, false, () -> pauseParent.getValue());
    public BoolSetting pauseOnHealth = new BoolSetting("PauseOnHealth", false, false, () -> pauseParent.getValue());
    public FloatSetting pauseHealth = new FloatSetting("PauseHealth", 15f, 0f, 36f, () -> pauseParent.getValue() && pauseOnHealth.getValue());
    public BoolSetting pauseOnExp = new BoolSetting("PauseOnExp", false, false, () -> pauseParent.getValue());

    public BoolSetting renderParent = new BoolSetting("Renders", false, true);
    public BoolSetting render = new BoolSetting("Render", false, false, () -> renderParent.getValue());
    public BoolSetting fade = new BoolSetting("Fade", false, false, () -> render.getValue() && renderParent.getValue());
    public FloatSetting startAlpha = new FloatSetting("StartAlpha", 255, 0, 255, () -> render.getValue() && fade.getValue() && renderParent.getValue());
    public FloatSetting endAlpha = new FloatSetting("EndAlpha", 0, 0, 255, () -> render.getValue() && fade.getValue() && renderParent.getValue());
    public FloatSetting fadeSpeed = new FloatSetting("FadeSpeed", 20, 0, 100, () -> render.getValue() && fade.getValue() && renderParent.getValue());

    public BoolSetting box =new BoolSetting("Box", false, false, () -> render.getValue() && renderParent.getValue());
    public FloatSetting boxRed = new FloatSetting("BoxRed", 255, 0, 255, () -> render.getValue() && box.getValue() && renderParent.getValue());
    public FloatSetting boxGreen = new FloatSetting("BoxGreen", 255, 0, 255, () -> render.getValue() && box.getValue() && renderParent.getValue());
    public FloatSetting boxBlue = new FloatSetting("BoxBlue", 255, 0, 255, () -> render.getValue() && box.getValue() && renderParent.getValue());
    public FloatSetting boxAlpha = new FloatSetting("BoxAlpha", 255, 0, 255, () -> render.getValue() && box.getValue() && renderParent.getValue());

    public BoolSetting outline = new BoolSetting("Outline", false, false, () -> render.getValue() && renderParent.getValue());
    public FloatSetting outlineRed = new FloatSetting("OutlineRed", 255, 0, 255, () -> render.getValue() && outline.getValue() && renderParent.getValue());
    public FloatSetting outlineGreen = new FloatSetting("OutlineGreen", 255, 0, 255, () -> render.getValue() && outline.getValue() && renderParent.getValue());
    public FloatSetting outlineBlue = new FloatSetting("OutlineBlue", 255, 0, 255, () -> render.getValue() && outline.getValue() && renderParent.getValue());
    public FloatSetting outlineAlpha = new FloatSetting("OutlineAlpha", 255, 0, 255, () -> render.getValue() && outline.getValue() && renderParent.getValue());
    public FloatSetting lineWidth = new FloatSetting("LineWidth", 1f, 0f, 5f, () -> render.getValue() && outline.getValue() && renderParent.getValue());

    EntityPlayer targetPlayer;
    BlockPos finalPos;
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();
    HashMap<BlockPos, Integer> possesToFade = new HashMap();
    BestBlockPos bestCrystalPos = new BestBlockPos(BlockPos.ORIGIN, 0);
    HashMap<Integer, Entity> attemptedEntityId = new HashMap();

    float mainTargetDamage;
    float mainTargetHealth;
    float mainMinimumDamageValue;
    float mainSelfHealth;
    float mainSelfDamage;
    int mainSlot;
    int mainOldSlot;

    @Override
    public String getMetaData() {
        return targetPlayer != null ? "[" + ChatFormatting.GRAY + targetPlayer.getDisplayNameString() + ChatFormatting.RESET + "]" : "";
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        ModuleManager moduleManager = Moonlight.INSTANCE.getModuleManager();
        if(moduleManager.getModule(AutoSuicide.class).suicideBomb.getValue() && Keyboard.isKeyDown(moduleManager.getModule(AutoSuicide.class).bindToNuke.getBind()) && moduleManager.getModule(AutoSuicide.class).isEnabled()) {
            targetPlayer = mc.player;
        }else {
            targetPlayer = EntityUtil.getTarget(targetRange.getValue());
        }

        if (targetPlayer == null)
            return;

        if ((pauseOnGapple.getValue() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown())
                || (pauseOnSword.getValue() && mc.player.getHeldItemMainhand().equals(Items.DIAMOND_SWORD))
                || (pauseOnExp.getValue() && mc.player.getHeldItemMainhand().equals(Items.EXPERIENCE_BOTTLE) && mc.gameSettings.keyBindUseItem.isKeyDown()
                || (pauseOnHealth.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() < pauseHealth.getValue())))
                return;

        if (placeTimer.passedMs((long) placeDelay.getValue())) {
            doPlace();
            placeTimer.reset();
        }
        if (breakTimer.passedMs((long) breakDelay.getValue())) {
            doBreak();
            breakTimer.reset();
        }
    }

    public void doPlace() {
        int slot = InventoryUtil.getItemSlot(Items.END_CRYSTAL);
        int oldSlot = mc.player.inventory.currentItem;
        mainSlot = slot;
        mainOldSlot = oldSlot;

        bestCrystalPos = getBestPlacePos();

        if (bestCrystalPos == null)
            return;

        if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL))
            InventoryUtil.switchToSlot(slot);

        mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(bestCrystalPos.getBlockPos(), EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));

        finalPos = bestCrystalPos.getBlockPos();

        if (render.getValue() && fade.getValue())
            possesToFade.put(bestCrystalPos.getBlockPos(), (int) startAlpha.getValue());

        if (placeSwing.getValue())
            swingArm(true);

        if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)) {
            mc.player.inventory.currentItem = oldSlot;
            mc.playerController.updateController();
        }
    }

    public void doBreak() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                float selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                float selfDamage = EntityUtil.calculateEntityDamage((EntityEnderCrystal) entity, mc.player);
                float targetDamage = EntityUtil.calculateEntityDamage((EntityEnderCrystal) entity, targetPlayer);
                float targetHealth = targetPlayer.getHealth() + targetPlayer.getAbsorptionAmount();
                float minimumDamageValue = minimumDamage.getValue();
                int sword = InventoryUtil.getItemSlot(Items.DIAMOND_SWORD);
                int oldSlot = mc.player.inventory.currentItem;

                if (mc.player.getDistance(entity.posX + 0.5f, entity.posY + 1, entity.posZ + 0.5f) > MathUtil.square(breakRange.getValue()))
                    continue;

                if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && targetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && facePlaceBind.getBind() != -1 && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                    minimumDamageValue = 2;

                if (antiSuicide.getValue() && selfDamage > selfHealth)
                    continue;

                if (selfDamage > maximumSelfDamage.getValue())
                    continue;

                if (targetDamage < minimumDamageValue)
                    continue;

                if (limitAttack.getValue() && attemptedEntityId.containsValue(entity))
                    continue;

                if(breakRaytrace.getValue() && !rayTraceCheckPos(new BlockPos(entity.posX, entity.posY, entity.posZ)) && mc.player.getDistance(entity.posX + 0.5f, entity.posY + 1, entity.posZ + 0.5f) > MathUtil.square(breakRaytraceRange.getValue()))
                    continue;

                if (silentSwitch.getValue() && antiWeakness.getValue() && (mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD) && mc.player.getActivePotionEffects().equals(Potion.getPotionById(18)))
                    InventoryUtil.switchToSlot(sword);

                if (packetBreak.getValue()) {
                    mc.getConnection().sendPacket(new CPacketUseEntity(entity));
                } else {
                    mc.playerController.attackEntity(mc.player, entity);
                }

                CrystalAttackEvent event = new CrystalAttackEvent(entity.getEntityId(), entity);
                MinecraftForge.EVENT_BUS.post(event);

                if (breakSwing.getValue())
                    swingArm(false);

                if (silentSwitch.getValue() && antiWeakness.getValue() && (mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD) && mc.player.getActivePotionEffects().equals(Potion.getPotionById(18))) {
                    mc.player.inventory.currentItem = oldSlot;
                    mc.playerController.updateController();
                }
            }
        }
    }

    public BestBlockPos getBestPlacePos() {
        TreeMap<Float, BestBlockPos> posList = new TreeMap<>();
        for (BlockPos pos : BlockUtil.getSphere(placeRange.getValue())) {
            float targetDamage = EntityUtil.calculatePosDamage(pos, targetPlayer);
            float selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            float selfDamage = EntityUtil.calculatePosDamage(pos, mc.player);
            float targetHealth = targetPlayer.getHealth() + targetPlayer.getAbsorptionAmount();
            float minimumDamageValue = minimumDamage.getValue();
            mainTargetDamage = targetDamage;
            mainTargetHealth = targetHealth;
            mainSelfDamage = selfDamage;
            mainSelfHealth = selfHealth;
            mainMinimumDamageValue = minimumDamageValue;
            if (BlockUtil.isPosValidForCrystal(pos, updatedPlacements.getValue())) {
                if (mc.player.getDistance(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f) > MathUtil.square(placeRange.getValue()))
                    continue;

                if (!allowCollision.getValue() && !mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).isEmpty() && mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos).setMaxY(1)).isEmpty())
                    continue;

                if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && targetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && facePlaceBind.getBind() != -1 && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                    minimumDamageValue = 2;

                if (antiSuicide.getValue() && selfDamage > selfHealth)
                    continue;

                if (selfDamage > maximumSelfDamage.getValue())
                    continue;

                if (targetDamage < minimumDamageValue)
                    continue;

                if(placeRaytrace.getValue() && !rayTraceCheckPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ())) && mc.player.getDistance(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f) > MathUtil.square(placeRaytraceRange.getValue()))
                    continue;

                posList.put(targetDamage, new BestBlockPos(pos, targetDamage));
            }
        }
        if (!posList.isEmpty()) {
            return posList.lastEntry().getValue();
        }
        return null;
    }


    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion) {

            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal) {
                    BlockPos predictedCrystalPos = new BlockPos(entity.posX, entity.posY - 1, entity.posZ);
                    if (placePredict.getValue() && placePredictMode.getValueEnum().equals(PlacePredictMode.EXPLOSION) && predictedCrystalPos.equals(bestCrystalPos)) {
                        if (entity.getDistance(mc.player) > MathUtil.square(placeRange.getValue()))
                            continue;

                        if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && mainTargetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && facePlaceBind.getBind() != -1 && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                            mainMinimumDamageValue = 2;

                        if (antiSuicide.getValue() && mainSelfDamage > mainSelfHealth)
                            continue;

                        if (mainSelfDamage > maximumSelfDamage.getValue())
                            continue;

                        if (mainMinimumDamageValue > mainTargetDamage)
                            continue;

                        if (limitAttack.getValue() && attemptedEntityId.containsValue(entity))
                            continue;

                        if(placeRaytrace.getValue() && !rayTraceCheckPos(new BlockPos(predictedCrystalPos.getX(), predictedCrystalPos.getY(), predictedCrystalPos.getZ())) && mc.player.getDistance(predictedCrystalPos.getX() + 0.5f, predictedCrystalPos.getY() + 1, predictedCrystalPos.getZ() + 0.5f) > MathUtil.square(placeRaytraceRange.getValue()))
                            continue;

                        if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL))
                            InventoryUtil.switchToSlot(mainSlot);

                        mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(predictedCrystalPos, EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));

                        if (render.getValue() && fade.getValue())
                            possesToFade.put(predictedCrystalPos, (int) startAlpha.getValue());

                        if (placeSwing.getValue())
                            swingArm(true);

                        if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)) {
                            mc.player.inventory.currentItem = mainOldSlot;
                            mc.playerController.updateController();
                        }
                    }
                }
            }
            if (cancelExplosion.getValue())
                event.setCanceled(true);
        }

        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity velocity = (SPacketEntityVelocity) event.getPacket();

            if (cancelVelocity.getValue() && velocity.getEntityID() == mc.player.getEntityId())
                event.setCanceled(true);
        }
        if (breakPredict.getValue() && event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 51 && finalPos != null && EntityUtil.getTarget(targetRange.getValue()) != null) {
                final CPacketUseEntity predict = new CPacketUseEntity();
                ((ICPacketUseEntityMixin) predict).setEntityIdAccessor(packet.getEntityID());
                ((ICPacketUseEntityMixin) predict).setActionAccessor(CPacketUseEntity.Action.ATTACK);

                if (breakPredictCalc.getValue()) {
                    if (mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > MathUtil.square(breakRange.getValue()))
                        return;

                    if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && mainTargetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && facePlaceBind.getBind() != -1 && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                        mainMinimumDamageValue = 2;

                    if (antiSuicide.getValue() && mainSelfDamage > mainSelfHealth)
                        return;

                    if (mainSelfDamage > maximumSelfDamage.getValue())
                        return;

                    if (mainMinimumDamageValue > mainTargetDamage)
                        return;
                }

                mc.getConnection().sendPacket(predict);
                if (breakSwing.getValue())
                    swingArm(false);
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal) {

                        if (limitAttack.getValue() && attemptedEntityId.containsValue(entity.getEntityId()))
                            attemptedEntityId.remove(entity, entity.getEntityId());

                        BlockPos predictedCrystalPos = new BlockPos(entity.posX, entity.posY - 1, entity.posZ);

                        if (soundPredict.getValue() && entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= breakRange.getValue())
                            entity.setDead();

                        if (placePredict.getValue() && placePredictMode.getValueEnum().equals(PlacePredictMode.SOUND) && predictedCrystalPos.equals(bestCrystalPos)) {

                            if (entity.getDistance(mc.player) > MathUtil.square(placeRange.getValue()))
                                continue;

                            if (BlockUtil.isPlayerSafe(targetPlayer) && (facePlaceMode.getValueEnum().equals(FacePlaceMode.Always) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Health) && mainTargetHealth < facePlaceHp.getValue()) || (facePlaceMode.getValueEnum().equals(FacePlaceMode.Bind) && facePlaceBind.getBind() != -1 && Keyboard.isKeyDown(facePlaceBind.getBind()))))
                                mainMinimumDamageValue = 2;

                            if (antiSuicide.getValue() && mainSelfDamage > mainSelfHealth)
                                continue;

                            if (mainSelfDamage > maximumSelfDamage.getValue())
                                continue;

                            if (mainMinimumDamageValue > mainTargetDamage)
                                continue;

                            if (limitAttack.getValue() && attemptedEntityId.containsValue(entity))
                                continue;

                            if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL))
                                InventoryUtil.switchToSlot(mainSlot);

                            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(predictedCrystalPos, EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));

                            if (render.getValue() && fade.getValue())
                                possesToFade.put(predictedCrystalPos, (int) startAlpha.getValue());

                            if (placeSwing.getValue())
                                swingArm(true);

                            if (silentSwitch.getValue() && (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL || mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)) {
                                mc.player.inventory.currentItem = mainOldSlot;
                                mc.playerController.updateController();
                            }
                        }
                    }
                }
            }
        }
    }

    public void swingArm(boolean place){
        if(place){
            if (PlaceSwingHand.MAINHAND.equals(placeSwingHand.getValueEnum())) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } else if (PlaceSwingHand.OFFHAND.equals(placeSwingHand.getValueEnum())) {
                mc.player.swingArm(EnumHand.OFF_HAND);
            } else if (PlaceSwingHand.PACKET.equals(placeSwingHand.getValueEnum())) {
                mc.player.connection.sendPacket(new CPacketAnimation(mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
            }
        } else {
            if (BreakSwingHand.MAINHAND.equals(breakSwingHand.getValueEnum())) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } else if (BreakSwingHand.OFFHAND.equals(breakSwingHand.getValueEnum())) {
                mc.player.swingArm(EnumHand.OFF_HAND);
            } else if (BreakSwingHand.PACKET.equals(breakSwingHand.getValueEnum())) {
                mc.player.connection.sendPacket(new CPacketAnimation(mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
            }
        }
    }

    @Override
    public void onEnable() {
        targetPlayer = null;
        finalPos = null;
    }

    @Override
    public void onDisable() {
        targetPlayer = null;
        finalPos = null;
    }

    @SubscribeEvent
    public void onCrystalAttacked(CrystalAttackEvent event) {
        if (limitAttack.getValue())
            attemptedEntityId.put(event.getEntityId(), event.getEntity());
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (render.getValue()) {
            if (fade.getValue()) {
                for (Map.Entry<BlockPos, Integer> entry : possesToFade.entrySet()) {
                    possesToFade.put(entry.getKey(), (int) (entry.getValue() - (fadeSpeed.getValue() / 10)));
                    if (entry.getValue() <= endAlpha.getValue()) {
                        possesToFade.remove(entry.getKey());
                        return;
                    }
                    RenderUtil.drawBoxESP(entry.getKey(), new Color(boxRed.getValue() / 255f, boxGreen.getValue() / 255f, boxBlue.getValue() / 255f, entry.getValue() / 255f), true, new Color(outlineRed.getValue() / 255f, outlineGreen.getValue() / 255f, outlineBlue.getValue() / 255f, entry.getValue() / 255f), lineWidth.getValue(), outline.getValue(), box.getValue(), entry.getValue(), true);
                }
            } else if (finalPos != null) {
                RenderUtil.drawBoxESP(finalPos, new Color(boxRed.getValue() / 255f, boxGreen.getValue() / 255f, boxBlue.getValue() / 255f, boxAlpha.getValue() / 255f), true, new Color(outlineRed.getValue() / 255f, outlineGreen.getValue() / 255f, outlineBlue.getValue() / 255f, outlineAlpha.getValue() / 255f), lineWidth.getValue(), outline.getValue(), box.getValue(), (int) boxAlpha.getValue(), true);
            }
        }
    }

    private boolean rayTraceCheckPos(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }

    static class BestBlockPos {
        BlockPos blockPos;
        float targetDamage;

        public BestBlockPos(BlockPos blockPos, float targetDamage) {
            this.blockPos = blockPos;
            this.targetDamage = targetDamage;
        }

        public float getTargetDamage() {
            return targetDamage;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }
    }
}