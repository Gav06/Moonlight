package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

public final class BoolSetting extends Setting {

    private final boolean defaultValue;
    private boolean value;

    public BoolSetting(String name, boolean defaultValue) {
        super(name);
        this.defaultValue = this.value = defaultValue;
    }

    public BoolSetting(String name, boolean defaultValue, Visibility visible) {
        super(name, visible);
        this.defaultValue = this.value = defaultValue;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }
}
