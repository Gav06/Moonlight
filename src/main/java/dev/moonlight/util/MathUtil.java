package dev.moonlight.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class MathUtil {
    public static double square(double input) {
        return input * input;
    }

    public static float normalize(float value, float min, float max) {
        return 1.0f - ((value - min) / (max - min));
    }

    public static double normalize(double value, double min, double max) {
        return 1.0d - ((value - min) / (max - min));
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(max, Math.max(min, value));
    }

    public static double clampedNormalize(double value, double min, double max) {
        return clamp(normalize(value, min, max), 0.0, 1.0);
    }

    public static float clampedNormalize(float value, float min, float max) {
        return clamp(normalize(value, min, max), 0.0f, 1.0f);
    }

    public static double lerp(double then, double now, double delta) {
        return then + (now - then) * delta;
    }

    public static float lerp(float then, float now, float delta) {
        return then + (now - then) * delta;
    }

    public static double safeLerp(double then, double now, double delta) {
        delta = clamp(delta, 0.0, 1.0);
        return lerp(then, now, delta);
    }

    public static float safeLerp(float then, float now, float delta) {
        delta = clamp(delta, 0.0f, 1.0f);
        return lerp(then, now, delta);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float ticks) {
        return interpolateEntity(entity, ticks).subtract(Minecraft.getMinecraft().getRenderManager().viewerPosX, Minecraft.getMinecraft().getRenderManager().viewerPosY, Minecraft.getMinecraft().getRenderManager().viewerPosZ);
    }

    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
}
