package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

public final class BoolSetting extends Setting {

    private final boolean defaultValue;
    private boolean parent;
    private boolean value;

    public BoolSetting(String name, boolean defaultValue, boolean parent) {
        super(name);
        this.parent = parent;
        this.defaultValue = this.value = defaultValue;
    }

    public BoolSetting(String name, boolean defaultValue, boolean parent, Visibility visible) {
        super(name, visible);
        this.parent = parent;
        this.defaultValue = this.value = defaultValue;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean isParent() {
        return parent;
    }

    public void toggle() {
        this.value = !this.value;
    }
}
