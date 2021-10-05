package dev.moonlight.util;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.Objects;

public class DamageUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    // this is the one util yall once sent in the gc

    public static EntityPlayer getTarget(float range) {
        EntityPlayer entity = null;
        for (int size = mc.world.playerEntities.size(), s = 0; s < size; ++s) {
            EntityPlayer player = mc.world.playerEntities.get(s);
            if (!isEntityValid(player, range)) {
                if (entity == null) {
                    entity = player;
                } else if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(entity)) {
                    entity = player;
                }
            }
        }
        return entity;
    }

    public static boolean isEntityValid(Entity entity, double range) {
        return !(((EntityLivingBase) entity).getHealth() > 0.0f) || entity.isDead || entity.equals(mc.player) || entity instanceof EntityPlayer && mc.player.getDistanceSq(entity) > range * range;
    }

    public static float calculateEntityDamage(EntityEnderCrystal crystal, EntityPlayer player) {
        return calculatePosDamage(crystal.posX, crystal.posY, crystal.posZ, player);
    }

    public static float calculatePosDamage(BlockPos position, EntityPlayer player) {
        return calculatePosDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, player);
    }

    public static float calculatePosDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleSize = 12.0F;
        double size = entity.getDistance(posX, posY, posZ) / (double) doubleSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double value = (1.0D - size) * blockDensity;
        float damage = (float) ((int) ((value * value + value) / 2.0D * 7.0D * (double) doubleSize + 1.0D));
        double finalDamage = 1.0D;

        if (entity instanceof EntityLivingBase) {
            finalDamage = getBlastReduction((EntityLivingBase) entity, getMultipliedDamage(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finalDamage;
    }

    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            final DamageSource source = DamageSource.causeExplosionDamage(explosion);

            damage = CombatRules.getDamageAfterAbsorb(damage, (float) player.getTotalArmorValue(), (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), source), 0.0f, 20.0f) / 25.0f;

            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) damage -= damage / 4.0f;
            return damage;
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getMultipliedDamage(float damage) {
        return damage * (mc.world.getDifficulty().getId() == 0 ? 0.0F : (mc.world.getDifficulty().getId() == 2 ? 1.0F : (mc.world.getDifficulty().getId() == 1 ? 0.5F : 1.5F)));
    }
}