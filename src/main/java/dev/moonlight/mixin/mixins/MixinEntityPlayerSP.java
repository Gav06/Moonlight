package dev.moonlight.mixin.mixins;

import dev.moonlight.events.PlayerUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public final class MixinEntityPlayerSP {

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void onLivingUpdate$Inject$HEAD(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerUpdateEvent());
    }
}
