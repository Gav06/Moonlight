package dev.moonlight.module.mods.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "Step",
        desc = "Moves you up blocks instead of having to jump.",
        category = Module.Category.Movement
)
public class Step extends Module {

    public FloatSetting stepHeight = new FloatSetting("StepHeight", 1.0f, 0.5f, 2.5f);

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + stepHeight.getValue() + ChatFormatting.RESET + "]";
    }

    @Override
    public void onDisable() {
        if (mc.player != null)
            mc.player.stepHeight = 0.5f;
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(nullCheck()) return;
        mc.player.stepHeight = stepHeight.getValue();
    }
}
