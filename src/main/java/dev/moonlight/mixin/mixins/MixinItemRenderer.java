package dev.moonlight.mixin.mixins;

import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.render.OldAnimations;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @ModifyVariable(method = "updateEquippedItem", at = @At("STORE"), index = 4)
    private float updateEquippedItem$ModifyVariable$STORE$F4(float original) {
        final OldAnimations oldAnimations = Moonlight.INSTANCE.getModuleManager().getModule(OldAnimations.class);
        if (oldAnimations != null) {
            return Moonlight.INSTANCE.getModuleManager().isModuleEnabled(OldAnimations.class) ? 1f : original;
        } else {
            return original;
        }
    }
}