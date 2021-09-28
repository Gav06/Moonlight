package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "OldAnimations",
        desc = "Makes the swing animations like 1.8",
        category = Module.Category.Render
)
public final class OldAnimations extends Module {

    /**
     * See MixinItemRenderer -> updateEquippedItem$ModifyVariable$STORE$F4
     */
}
