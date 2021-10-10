package dev.moonlight.module;

import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.HUDComponent;

public class HUDModule extends Module {

    protected final HUDComponent hudComponent;

    public HUDModule(HUDComponent component) {
        super();
        this.hudComponent = component;
        this.hudComponent.setParent(this);
    }

    public HUDComponent getComponent() {
        return hudComponent;
    }
}
