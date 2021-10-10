package dev.moonlight.module.mods.movement;

import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "AutoWalk",
        desc = "Walks for you.",
        category = Module.Category.Movement
)
public class AutoWalk extends Module {

    public BoolSetting autoJump = new BoolSetting("AutoJump", true, false);

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(!mc.gameSettings.keyBindForward.isKeyDown() || !mc.gameSettings.keyBindBack.isKeyDown()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
            if(mc.player.collidedHorizontally && autoJump.getValue()) {
                mc.player.jump();
            }
        }
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
    }
}
