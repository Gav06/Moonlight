package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

public final class ModeSetting extends Setting {

    private final Enum<?>[] constants;
    private int valueIndex;
    private final int defaultValueIndex;

    public ModeSetting(String name, Enum<?> defaultValue) {
        super(name);
        constants = defaultValue.getDeclaringClass().getEnumConstants();
        this.defaultValueIndex = this.valueIndex = indexOf(defaultValue);
    }

    public ModeSetting(String name, Enum<?> defaultValue, Visibility visible) {
        super(name, visible);
        constants = defaultValue.getDeclaringClass().getEnumConstants();
        this.defaultValueIndex = this.valueIndex = indexOf(defaultValue);
    }

    public void increase() {
        if (valueIndex == constants.length - 1) {
            valueIndex = 0;
        } else {
            valueIndex++;
        }
    }

    public void decrease() {
        if (valueIndex == 0) {
            valueIndex = constants.length - 1;
        } else {
            valueIndex--;
        }
    }

    public Enum<?> getValueEnum() {
        return constants[valueIndex];
    }

    public int getValueIndex() {
        return valueIndex;
    }

    public int getDefaultValueIndex() {
        return defaultValueIndex;
    }

    private int indexOf(Enum<?> value) {
        for (int i = 0; i < constants.length; i++) {
            if (constants[i] == value)
                return i;
        }

        return -1;
    }
}
