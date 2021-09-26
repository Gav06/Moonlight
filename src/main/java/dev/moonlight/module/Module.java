package dev.moonlight.module;

import net.minecraftforge.common.MinecraftForge;

public abstract class Module {

    private boolean enabled = false;

    public void enable() {
        enabled = true;
        MinecraftForge.EVENT_BUS.register(this);
        onEnable();
    }

    public void disable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        onDisable();
    }

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void onEnable() { }

    protected void onDisable() { }
}
