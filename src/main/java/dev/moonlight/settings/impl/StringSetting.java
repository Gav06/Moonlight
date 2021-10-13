package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

public class StringSetting extends Setting {
    public String value;

    public StringSetting(String name, String value) {
        super(name);
        this.value = value;
    }

    public StringSetting(String name, String value, Visibility visible) {
        super(name, visible);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
