package dev.moonlight.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleEnableEvent extends Event {
    String moduleName;

    public ModuleEnableEvent(String moduleName) {
        this.moduleName = moduleName;
    }
}
