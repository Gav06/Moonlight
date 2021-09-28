package dev.moonlight.mixin.mixins;

import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.Fullbright;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @ModifyVariable(method = "updateLightmap", at = @At("STORE"), index = 16)
    private float updateLightmap$ModifyVariable$STORE$F16(float original) {
        return Math.max(Moonlight.INSTANCE.getModuleManager().getModule(Fullbright.class).brightnessLevel, original);
    }
}
