package dev.moonlight.module.mods.render;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Module.Info(
        name = "StrictLimbs",
        desc = "Stops limb movement so players look sexy.",
        category = Module.Category.Render
)
public class StrictLimbs extends Module {

    public BoolSetting self = new BoolSetting("Self", true, false);
    public BoolSetting rotateHead = new BoolSetting("RotateHead", true, false);

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if(nullCheck()) return;
        for(Entity entity : mc.world.loadedEntityList) {
            if(!(entity instanceof EntityPlayer)) return;
            if(entity.equals(mc.player) && !self.getValue()) return;
            ((EntityPlayer) entity).prevLimbSwingAmount = 0;
            ((EntityPlayer) entity).limbSwingAmount = 0;
            ((EntityPlayer) entity).limbSwing = 0;
            if(!rotateHead.getValue()) {
                ((EntityPlayer) entity).prevCameraPitch = 0;
                ((EntityPlayer) entity).prevCameraYaw = 0;
                ((EntityPlayer) entity).cameraPitch = 0;
                ((EntityPlayer) entity).cameraYaw = 0;
            }
        }
    }
}
