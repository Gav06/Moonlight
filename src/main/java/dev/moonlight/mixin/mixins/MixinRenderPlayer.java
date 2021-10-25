package dev.moonlight.mixin.mixins;

import dev.moonlight.Moonlight;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.module.mods.render.ShaderChams;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    @Inject(method =  "renderEntityName" , at =  @At("HEAD") , cancellable = true)
    public void renderEntityNameHook(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (Moonlight.INSTANCE.getModuleManager().getModule(ShaderChams.class).isEnabled()) {
            info.cancel();
        }
    }
}
