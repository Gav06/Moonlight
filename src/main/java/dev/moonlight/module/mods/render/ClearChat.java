package dev.moonlight.module.mods.render;

import dev.moonlight.module.Module;

@Module.Info(
        name = "ClearChat",
        desc = "Makes chat clear",
        category = Module.Category.Render
)
public class ClearChat extends Module {

    /*
     * See MixinGuiNewChat -> drawChatRedirect
     */
}
