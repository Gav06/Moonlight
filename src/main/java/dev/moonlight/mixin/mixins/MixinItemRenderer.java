package dev.moonlight.mixin.mixins;

import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.OldAnimations;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public final class MixinItemRenderer {

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