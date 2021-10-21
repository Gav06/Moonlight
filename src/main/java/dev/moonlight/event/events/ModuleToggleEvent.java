package dev.moonlight.event.events;

import dev.moonlight.module.Module;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleToggleEvent extends Event {
    Module module;

    public ModuleToggleEvent(Module moduleIn) {
        this.module = moduleIn;
    }

    public Module getModule() {
        return module;
    }

    public static class Enable extends ModuleToggleEvent {
        public Enable(Module moduleIn) {
            super(moduleIn);
        }
    }

    public static class Disable extends ModuleToggleEvent {
        public Disable(Module moduleIn) {
            super(moduleIn);
        }
    }
}
