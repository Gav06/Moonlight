package dev.moonlight.misc;

public final class MathUtil {

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
        return clamp(normalize(value, min, max), min, max);
    }

    public static float clampedNormalize(float value, float min, float max) {
        return clamp(normalize(value, min, max), min, max);
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
}
