package dev.moonlight.module.mods.combat;

import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.util.InventoryUtil;
import dev.moonlight.util.RenderUtil;
import dev.moonlight.util.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.TreeMap;

@Module.Info(name = "Flatten", category = Module.Category.Combat, desc = "op zprestige module")
public class Flatten extends Module {
    public FloatSetting placeDelay = new FloatSetting("PlaceDelay", 100, 0, 1000);
    public FloatSetting targetRange = new FloatSetting("TargetRange", 5.0f, 0.0f, 10.0f);

    Target target;
    BlockPos renderPos;
    Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(nullCheck()) return;
        target = getTarget(targetRange.getValue());
        int obbySlot = InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN));
        int currentItem = mc.player.inventory.currentItem;

        if(renderPos != null)
            renderPos = null;

        if (target == null)
            return;

        BlockPos targetPos = target.getTargetPos().down();

        if (!target.getTarget().onGround)
            return;

        if (obbySlot == -1)
            return;

        if (mc.world.getBlockState(targetPos.north()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            renderPos = new BlockPos(targetPos.north());
            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos.north(), EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            mc.player.inventory.currentItem = currentItem;
            renderPos = null;
            mc.playerController.updateController();
            timer.reset();
        }

        if (mc.world.getBlockState(targetPos.east()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            renderPos = new BlockPos(targetPos.east());
            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos.east(), EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            mc.player.inventory.currentItem = currentItem;
            renderPos = null;
            mc.playerController.updateController();
            timer.reset();
        }

        if (mc.world.getBlockState(targetPos.south()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            renderPos = new BlockPos(targetPos.south());
            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos.south(), EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            mc.player.inventory.currentItem = currentItem;
            renderPos = null;
            mc.playerController.updateController();
            timer.reset();
        }

        if (mc.world.getBlockState(targetPos.west()).getBlock().equals(Blocks.AIR) && timer.passedMs((long) placeDelay.getValue())) {
            InventoryUtil.switchToSlot(obbySlot);
            renderPos = new BlockPos(targetPos.west());
            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos.west(), EnumFacing.UP, mc.player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            mc.player.inventory.currentItem = currentItem;
            renderPos = null;
            mc.playerController.updateController();
            timer.reset();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(renderPos == null) return;
        RenderUtil.prepareRender();
        RenderGlobal.renderFilledBox(new AxisAlignedBB(renderPos), 1f, 1f, 1f, 1f);
        RenderUtil.releaseRender();
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
