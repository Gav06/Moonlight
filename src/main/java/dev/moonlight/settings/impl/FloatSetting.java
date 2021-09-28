package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;
import net.minecraft.util.math.MathHelper;

public final class FloatSetting extends Setting {

    private final float defaultValue;
    private float value;
    private final float min, max;

    public FloatSetting(String name, float defaultValue, float min, float max) {
        super(name);
        this.defaultValue = this.value = defaultValue;
        this.max = max;
        this.min = min;
    }

    public FloatSetting(String name, float defaultValue, float min, float max, Visibility visible) {
        super(name, visible);
        this.defaultValue = this.value = defaultValue;
        this.min = min;
        this.max = max;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValueClamped(float value) {
        this.value = MathHelper.clamp(value, min, max);
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
