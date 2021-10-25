package dev.moonlight.mixin.mixins;

import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.misc.ShulkerCeption;
import net.minecraft.inventory.SlotShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlotShulkerBox.class)
public class MixinSlotShulkerBox {

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void isItemValid$Inject$HEAD(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Moonlight.INSTANCE.getModuleManager().isModuleEnabled(ShulkerCeption.class))
            cir.setReturnValue(true);
    }
}