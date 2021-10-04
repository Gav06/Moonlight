package dev.moonlight.module.mods;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.ColorSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "ESP",
        desc = "Renders esp around entities.",
        category = Module.Category.Render
)
public class ESP extends Module {

    public BoolSetting distance = new BoolSetting("Distance", true, false);
    public FloatSetting distanceToRender = new FloatSetting("DistanceToRender", 20, 1, 100, () -> distance.getValue());
    public BoolSetting targetParent = new BoolSetting("Targets", false, true);
    public BoolSetting players = new BoolSetting("Players", true, false, () -> targetParent.getValue());
    public BoolSetting mobs = new BoolSetting("Mob", true, false, () -> targetParent.getValue());
    public BoolSetting animals = new BoolSetting("Animals", true, false, () -> targetParent.getValue());
    public BoolSetting items = new BoolSetting("Items", true, false, () -> targetParent.getValue());
    public ColorSetting color = new ColorSetting("Color", 125, 0, 125, 255);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (Entity e : mc.world.loadedEntityList) {
            if(distance.getValue() && e.getDistance(mc.player) >= distanceToRender.getValue()) return;
            AxisAlignedBB bb = e.getEntityBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepareRender();
            if(e instanceof EntityItem && items.getValue())
                RenderGlobal.drawSelectionBoundingBox(bb, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, color.getA() / 255f);
            if(e instanceof EntityPlayer && players.getValue())
                RenderGlobal.drawSelectionBoundingBox(bb, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, color.getA() / 255f);
            if(e instanceof EntityAnimal && animals.getValue())
                RenderGlobal.drawSelectionBoundingBox(bb, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, color.getA() / 255f);
            if((e instanceof EntityMob || e instanceof EntitySlime) && mobs.getValue())
                RenderGlobal.drawSelectionBoundingBox(bb, color.getR() / 255f, color.getG() / 255f, color.getB() / 255f, color.getA() / 255f);
            RenderUtil.releaseRender();
        }
    }
}
