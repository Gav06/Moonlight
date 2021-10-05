package dev.moonlight.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleDisableEvent extends Event {
    String moduleName;

    public ModuleDisableEvent(String moduleName) {
        this.moduleName = moduleName;
    }

}
