package dev.moonlight.module.mods.combat;

import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.util.InventoryUtil;
import dev.moonlight.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.TreeMap;

@Module.Info(name = "Flatten", category = Module.Category.Combat, desc = "op zprestige module")
public class Flatten extends Module {
    public FloatSetting placeDelay = new FloatSetting("PlaceDelay", 100, 0, 1000);
    public FloatSetting targetRange = new FloatSetting("TargetRange", 5.0f, 0.0f, 10.0f);

    Target target;
    Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        target = getTarget(targetRange.getValue());
        int obbySlot = InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN));
        int currentItem = mc.player.inventory.currentItem;

        if (target == null)
            return;

        BlockPos targetPos = target.getTargetPos().down();

        if (!target.getTarget().onGround)
            return;

        if (obbySlot == -1)
            return;
        if (mc.world.getBlockState(targetPos.north()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            //place block north
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
            timer.reset();
        }

        if (mc.world.getBlockState(targetPos.east()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            //place block east
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
            timer.reset();
        }

        if (mc.world.getBlockState(targetPos.south()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            //place block south
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
            timer.reset();
        }

        if (mc.world.getBlockState(targetPos.west()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            //place block west
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
            timer.reset();
        }
    }

    public Target getTarget(float range) {
        TreeMap<Double, Target> map = new TreeMap<>();
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (entity.getDistance(mc.player) > range)
                continue;

            map.put(entity.getDistanceSq(mc.player), new Target(entity, entity.getDistanceSq(mc.player)));
        }
        if (!map.isEmpty())
            return map.lastEntry().getValue();

        return null;
    }

    static class Target {
        EntityPlayer target;
        Double distance;

        public Target(EntityPlayer target, Double distance) {
            this.target = target;
            this.distance = distance;
        }

        public EntityPlayer getTarget() {
            return target;
        }

        public BlockPos getTargetPos() {
            return new BlockPos(Math.floor(target.posX), Math.floor(target.posY), Math.floor(target.posZ));
        }

        public Double getDistance() {
            return distance;
        }
    }

}
