package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.ColorSetting;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Module.Info(
        name = "BurrowESP",
        desc = "Renders faggots.",
        category = Module.Category.Render
)
public class BurrowESP extends Module {
    public BoolSetting self = new BoolSetting("Self", true, false);
    public BoolSetting targetParent = new BoolSetting("Blocks", false, true);
    public BoolSetting obsidian = new BoolSetting("Obsidian", true, false, () -> targetParent.getValue());
    public BoolSetting eChest = new BoolSetting("EChest", true, false, () -> targetParent.getValue());
    public BoolSetting anvil = new BoolSetting("Anvil", true, false, () -> targetParent.getValue());
    public BoolSetting skull = new BoolSetting("Skull", true, false, () -> targetParent.getValue());
    public ColorSetting color = new ColorSetting("Color", 255, 255, 255, 255);

    public List<BlockPos> burrowedPlayers = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        for(Entity entity : mc.world.loadedEntityList) {
            if(isBurrowed(entity)) {
                burrowedPlayers.add(entity.getPosition());
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (BlockPos pos : burrowedPlayers) {
            AxisAlignedBB bb = new AxisAlignedBB(pos);
            RenderUtil.prepareRender();
            RenderGlobal.drawSelectionBoundingBox(bb, color.r.getValue(), color.g.getValue(), color.b.getValue(), color.a.getValue());
            RenderUtil.releaseRender();
        }
    }

    public boolean isBurrowed(Entity entity) {
        if(entity == mc.player && !self.getValue()) return false;
        if(mc.world.getBlockState(new BlockPos(entity.posX, entity.posY + 0.2D, entity.posZ)).getBlock() == Blocks.OBSIDIAN && obsidian.getValue()) return true;
        if(mc.world.getBlockState(new BlockPos(entity.posX, entity.posY + 0.2D, entity.posZ)).getBlock() == Blocks.ENDER_CHEST && eChest.getValue()) return true;
        if(mc.world.getBlockState(new BlockPos(entity.posX, entity.posY + 0.2D, entity.posZ)).getBlock() == Blocks.ANVIL && anvil.getValue()) return true;
        if(mc.world.getBlockState(new BlockPos(entity.posX, entity.posY + 0.2D, entity.posZ)).getBlock() == Blocks.SKULL && skull.getValue()) return true;
        return false;
    }
}
