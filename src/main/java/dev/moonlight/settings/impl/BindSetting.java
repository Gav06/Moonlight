package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

public class BindSetting extends Setting {
    public int bind;

    public BindSetting(String name, int bind) {
        super(name);
        this.bind = bind;
    }

    public BindSetting(String name, int bind, Visibility visible) {
        super(name, visible);
        this.bind = bind;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }
}
