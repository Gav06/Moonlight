package dev.moonlight.module.mods.render;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.ModeSetting;
import dev.moonlight.util.MathUtil;
import dev.moonlight.util.shader.*;
import dev.moonlight.util.shader.shaders.AquaShader;
import dev.moonlight.util.shader.shaders.FlowShader;
import dev.moonlight.util.shader.shaders.RedShader;
import dev.moonlight.util.shader.shaders.SmokeShader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Module.Info(name = "ShaderChams", desc = "Europa except I made it not actually dogshit.", category = Module.Category.Render)
public class ShaderChams extends Module {

    public ModeSetting shaderMode = new ModeSetting("ShaderMode", ShaderModes.AQUA);
    public enum ShaderModes {AQUA, RED, SMOKE, FLOW}
    public BoolSetting targetParent = new BoolSetting("Targets", true, true);
    public BoolSetting players = new BoolSetting("PLayers", true, false, () -> targetParent.getValue());
    public BoolSetting crystals = new BoolSetting("Crystals", true, false, () -> targetParent.getValue());
    public BoolSetting mobs = new BoolSetting("Mobs", false, false, () -> targetParent.getValue());
    public BoolSetting animals = new BoolSetting("Animals", false, false, () -> targetParent.getValue());


    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if(nullCheck()) return;
        FramebufferShader framebufferShader = null;
        if(shaderMode.getValueEnum() == ShaderModes.AQUA) {
            framebufferShader = AquaShader.AQUA_SHADER;
        }else if(shaderMode.getValueEnum() == ShaderModes.RED) {
            framebufferShader = RedShader.RED_SHADER;
        }else if(shaderMode.getValueEnum() == ShaderModes.SMOKE) {
            framebufferShader = SmokeShader.SMOKE_SHADER;
        }else if(shaderMode.getValueEnum() == ShaderModes.FLOW) {
            framebufferShader = FlowShader.FLOW_SHADER;
        }
        if(framebufferShader == null) return;
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        framebufferShader.startDraw(event.getPartialTicks());
        for (Entity entity : mc.world.loadedEntityList) {
            if(entity == mc.player || entity == mc.getRenderViewEntity()) continue;
            if(!(entity instanceof EntityPlayer && players.getValue()
                    || entity instanceof EntityEnderCrystal && crystals.getValue()
                    || (entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue()
                    || entity instanceof EntityAnimal && animals.getValue())) continue;
            Vec3d vector = MathUtil.getInterpolatedRenderPos(entity, event.getPartialTicks());
            Objects.requireNonNull(mc.getRenderManager().getEntityRenderObject(entity)).doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
        }
        framebufferShader.stopDraw();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
    }
}