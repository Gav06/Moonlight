package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.BlockHelper;
import dev.moonlight.util.MathUtil;
import dev.moonlight.util.RenderUtil;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// TODO: fix frustum check lmao
@Module.Info(
        name = "HoleESP",
        desc = "Highlights holes for safety in crystal combat",
        category = Module.Category.Render
)
public final class HoleESP extends Module {
//
//    private final ICamera camera = new Frustum();
//
//    private final BoolSetting frustumCheck = new BoolSetting("Frustum Check", true);

    private final FloatSetting distance = new FloatSetting("Distance", 8.0f, 2.0f, 32.0f);
    private final BoolSetting distanceFade = new BoolSetting("Distance Fade", false, false);
    private final BoolSetting self = new BoolSetting("Self", false, false);

    private final HoleFinderCallable callable = new HoleFinderCallable();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    // so we dont get ConcurrentModificationExceptions when we modify these lists in other threads
    private final ConcurrentLinkedQueue<BlockPos> unSafePositions = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<BlockPos> safePositions = new ConcurrentLinkedQueue<>();

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (BlockPos pos : safePositions) {
            renderHole(pos, Color.GREEN);
        }

        for (BlockPos pos : unSafePositions) {
            renderHole(pos, Color.RED);
        }
    }

    private float getDistanceAlpha(BlockPos pos, int normalOpacity) {
        return (float) (MathUtil.clampedNormalize(mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()), 0.0d, normalOpacity / 255.0));
    }

    private void renderHole(BlockPos pos, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

        int bottomAlpha = 128;


        GlStateManager.popMatrix();
    }

    private int ticksPassed = 0;

    @SuppressWarnings("unchecked")
    @ApiCall
    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        if (ticksPassed >= 2) {
            safePositions.clear();
            unSafePositions.clear();

            executor.submit(callable);
            ticksPassed = 0;
        }

        ticksPassed++;
    }

    @SuppressWarnings("rawtypes")
    private class HoleFinderCallable implements Callable {

        @Override
        public Object call() {
            for (BlockPos pos : BlockHelper.getSurroundingPositions(8, mc.player, true)) {
//                if (frustumCheck.getValue()) {
//                    camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
//                    if (!camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
//                        return null;
//                    }
//                }

                // where we are checking if it is a hole or not
                if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() != Blocks.AIR) {
                    if (isHoleType(pos, Blocks.BEDROCK)) {
                        safePositions.add(pos);
                    } else if (isHoleType(pos, Blocks.OBSIDIAN, Blocks.BEDROCK)) {
                        unSafePositions.add(pos);
                    }
                }
            }

            return null;
        }
    }

    private synchronized boolean isHoleType(BlockPos pos, Block... types) {
        int typeCount = 0;
        for (BlockPos blockPos : this.surroundingPositions(pos)) {
            for (Block block : types) {
                if (mc.world.getBlockState(blockPos).getBlock() == block) {
                    typeCount++;
                    break;
                }
            }
        }

        return typeCount == 5;
    }

    private synchronized BlockPos[] surroundingPositions(BlockPos pos) {
        return new BlockPos[] {
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west(),
                pos.down()
        };
    }

    private void drawSimpleGradientBB(AxisAlignedBB bb, Color topColor, Color bottomColor) {
        RenderUtil.prepareRender();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        final float[] topColors = new float[] {
                topColor.getRed() / 255.0f,
                topColor.getGreen() / 255.0f,
                topColor.getBlue() / 255.0f,
                topColor.getAlpha() / 255.0f
        };
        final float[] bottomColors = new float[] {
                bottomColor.getRed() / 255.0f,
                bottomColor.getGreen() / 255.0f,
                bottomColor.getBlue() / 255.0f,
                bottomColor.getAlpha() / 255.0f
        };
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        tessellator.draw();
        RenderUtil.releaseRender();
    }
}