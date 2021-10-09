package dev.moonlight.module.mods;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.settings.impl.ModeSetting;
import dev.moonlight.util.ColorUtil;
import dev.moonlight.util.InventoryUtil;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "KillAura",
        desc = "Automatically attacks entities (doesnt have rotations yet).",
        category = Module.Category.Combat
)
public class KillAura extends Module {

    public FloatSetting range = new FloatSetting("Range", 4, 1, 6);
    //delay
    public BoolSetting delayParent = new BoolSetting("Delay", false, true);
    public BoolSetting attackDelay = new BoolSetting("AttackDelay", true, false, () -> delayParent.getValue());
    public FloatSetting attackSpeed = new FloatSetting("AttackSpeed", 10, 1, 18, () -> delayParent.getValue());
    //targets
    public BoolSetting targetParent = new BoolSetting("Targets", false, true);
    public BoolSetting players = new BoolSetting("Players", true, false, () -> targetParent.getValue());
    public BoolSetting mobs = new BoolSetting("Mobs", true, false, () -> targetParent.getValue());
    public BoolSetting animals = new BoolSetting("Animals", true, false, () -> targetParent.getValue());
    //render
    public BoolSetting renderParent = new BoolSetting("Render", false, true);
    public BoolSetting render = new BoolSetting("Render", true, false, () -> renderParent.getValue());
    public ModeSetting renderMode = new ModeSetting("RenderMode", RenderMode.Both, () -> renderParent.getValue() && render.getValue());
    public FloatSetting r = new FloatSetting("R", 255, 0, 255, () -> renderParent.getValue() && render.getValue());
    public FloatSetting g = new FloatSetting("G", 255, 0, 255, () -> renderParent.getValue() && render.getValue());
    public FloatSetting b = new FloatSetting("B", 255, 0, 255, () -> renderParent.getValue() && render.getValue());
    public FloatSetting a = new FloatSetting("A", 125, 0, 255, () -> renderParent.getValue() && render.getValue());
    public FloatSetting lineWidth = new FloatSetting("LineWidth", 1, 0, 3, () -> renderParent.getValue() && render.getValue());
    public BoolSetting rainbow = new BoolSetting("Rainbow", true, false, () -> renderParent.getValue() && render.getValue());
    //misc
    public BoolSetting miscParent = new BoolSetting("Misc", false, true);
    public BoolSetting switchToSword = new BoolSetting("SwitchToSword", true, false, () -> miscParent.getValue());
    public BoolSetting swordOnly = new BoolSetting("SwordOnly", false, false, () -> miscParent.getValue());

    public enum RenderMode {
        Both,
        Fill,
        Outline
    }

    public Entity target = null;

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + range.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        int slot = InventoryUtil.getItemSlot(Items.DIAMOND_SWORD);
        for (Entity e : mc.world.loadedEntityList) {
            if (shouldAttack(e)) {
                if (slot != -1 && switchToSword.getValue())
                    InventoryUtil.switchToSlot(slot);
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && swordOnly.getValue()) {
                    attack(e);
                } else if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !swordOnly.getValue()) {
                    attack(e);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        boolean fill = false;
        boolean outline = false;
        if (renderMode.getValueEnum() == RenderMode.Both) {
            fill = true;
            outline = true;
        } else if (renderMode.getValueEnum() == RenderMode.Fill) {
            fill = true;
            outline = false;
        } else if (renderMode.getValueEnum() == RenderMode.Outline) {
            fill = false;
            outline = true;
        }
        if (render.getValue() && target != null) {
            AxisAlignedBB bb = target.getEntityBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepareRender();
            if (fill)
                RenderGlobal.renderFilledBox(bb, (rainbow.getValue() ? ColorUtil.rainbow(6).getRed() : r.getValue()) / 255f, (rainbow.getValue() ? ColorUtil.rainbow(6).getGreen() : g.getValue()) / 255f, (rainbow.getValue() ? ColorUtil.rainbow(6).getBlue() : b.getValue()) / 255f, a.getValue() / 255f);
            if (outline) {
                GlStateManager.glLineWidth(lineWidth.getValue());
                RenderGlobal.drawSelectionBoundingBox(bb, (rainbow.getValue() ? ColorUtil.rainbow(6).getRed() : r.getValue()) / 255f, (rainbow.getValue() ? ColorUtil.rainbow(6).getGreen() : g.getValue()) / 255f, (rainbow.getValue() ? ColorUtil.rainbow(6).getBlue() : b.getValue()) / 255f, a.getValue() / 255f);
            }
            RenderUtil.releaseRender();
        }
    }

    @Override
    public void onDisable() {
        target = null;
    }

    public boolean shouldAttack(Entity entity) {
        if (entity.equals(mc.player)) return false;
        if (!(entity instanceof EntityLivingBase)) return false;
        if (entity.isDead || !entity.isEntityAlive() || ((EntityLivingBase) entity).getHealth() < 0) return false;
        if ((entity instanceof EntityPlayer) && !players.getValue()) return false;
        if ((entity instanceof EntityMob || entity instanceof EntitySlime) && !mobs.getValue()) return false;
        if ((entity instanceof EntityAnimal) && !animals.getValue()) return false;
        return entity.getDistance(mc.player) <= range.getValue();
    }

    public void attack(Entity e) {
        if (attackDelay.getValue()) {
            if (mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                mc.playerController.attackEntity(mc.player, e);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                target = e;
            }
        } else {
            if (mc.player.ticksExisted % attackSpeed.getValue() == 0.0) {
                mc.playerController.attackEntity(mc.player, e);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                target = e;
            }
        }
    }
}
