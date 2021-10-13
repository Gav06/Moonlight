package dev.moonlight.module.mods.world;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@Module.Info(
        name = "CityESP",
        desc = "Renders when a player is getting citied.",
        category = Module.Category.Render
)
public class CityESP extends Module {

    public BoolSetting self = new BoolSetting("Self", true, false);
    public FloatSetting lineWidth = new FloatSetting("LineWidth", 1.0f, 0.1f, 3.0f);

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e.equals(mc.player) && !self.getValue()) return;
            for(BlockPos pos : getSurroundPositions(e)) {
                RenderUtil.prepareRender();
                RenderUtil.drawBoxESP(pos, new Color(255, 255, 255, 255), false, new Color(255, 255, 255, 255), lineWidth.getValue(), true, false, 125, false);
                RenderUtil.releaseRender();
            }
        }
    }

    public ArrayList<BlockPos> getSurroundPositions(Entity entity) {
        ArrayList<BlockPos> surroundPositions = new ArrayList<>();
        if((mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).north()).getBlock() != Blocks.OBSIDIAN || (mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).north()).getBlock() != Blocks.BEDROCK))) {
            surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).north()));
        }
        if((mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).west()).getBlock() != Blocks.OBSIDIAN || (mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).west()).getBlock() != Blocks.BEDROCK))) {
            surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).west()));
        }
        if((mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).south()).getBlock() != Blocks.OBSIDIAN || (mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).south()).getBlock() != Blocks.BEDROCK))) {
            surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).south()));
        }
        if((mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).east()).getBlock() != Blocks.OBSIDIAN || (mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).east()).getBlock() != Blocks.BEDROCK))) {
            surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).east()));
        }
        return surroundPositions;
    }
}
