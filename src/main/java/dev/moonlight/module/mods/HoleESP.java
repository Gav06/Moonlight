package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.util.BlockUtil;
import dev.moonlight.util.MathUtil;
import dev.moonlight.util.RenderUtil;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import io.netty.util.internal.ConcurrentSet;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// TODO: fix frustum check lmao
@Module.Info(
        name = "HoleESP",
        desc = "Highlights holes for safety in crystal combat",
        category = Module.Category.Render
)
public final class HoleESP extends Module {

    private final FloatSetting distance = new FloatSetting("Distance", 8.0f, 2.0f, 32.0f);
    private final FloatSetting updateDelay = new FloatSetting("Update Delay", 2f, 1f, 20f);
    //render
    private final BoolSetting gradient = new BoolSetting("Gradient", true, false);
    private final BoolSetting distanceFade = new BoolSetting("Distance Fade", false, false);
    private final BoolSetting self = new BoolSetting("Self", false, false);
    public BoolSetting safeColorParent = new BoolSetting("SafeColor", false, true);
    public final FloatSetting rSafe = new FloatSetting("RSafe", 255, 0, 255, () -> safeColorParent.getValue());
    public final FloatSetting gSafe = new FloatSetting("GSafe", 255, 0, 255, () -> safeColorParent.getValue());
    public final FloatSetting bSafe = new FloatSetting("BSafe", 255, 0, 255, () -> safeColorParent.getValue());
    public final FloatSetting aSafe = new FloatSetting("ASafe", 255, 0, 255, () -> safeColorParent.getValue());
    public BoolSetting unSafeColorParent = new BoolSetting("UnSafeColor", false, true);
    public final FloatSetting rUnSafe = new FloatSetting("RUnSafe", 255, 0, 255, () -> unSafeColorParent.getValue());
    public final FloatSetting gUnSafe = new FloatSetting("GUnSafe", 255, 0, 255, () -> unSafeColorParent.getValue());
    public final FloatSetting bUnSafe = new FloatSetting("BUnSafe", 255, 0, 255, () -> unSafeColorParent.getValue());
    public final FloatSetting aUnSafe = new FloatSetting("AUnSafe", 255, 0, 255, () -> unSafeColorParent.getValue());

    private final HoleFinderCallable callable = new HoleFinderCallable();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    // so we dont get ConcurrentModificationExceptions when we modify these lists in other threads
    private final ConcurrentSet<BlockPos> unSafePositions = new ConcurrentSet<>();
    private final ConcurrentSet<BlockPos> safePositions = new ConcurrentSet<>();

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(isEnabled()) {
            for (BlockPos pos : safePositions) {
                renderHole(pos, new Color(rSafe.getValue(), gSafe.getValue(), bSafe.getValue()));
            }

            for (BlockPos pos : unSafePositions) {
                renderHole(pos, new Color(rUnSafe.getValue(), gUnSafe.getValue(), bUnSafe.getValue()));
            }
        }
    }

    private float getDistanceAlpha(BlockPos pos) {
        return (float) (MathUtil.clampedNormalize(mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()), 0.5d, distance.getValue()));
    }

    private void renderHole(BlockPos pos, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

        int bottomAlpha = 128;
        int topAlpha = 28;

        if (distanceFade.getValue()) {
            topAlpha *= getDistanceAlpha(pos);
            bottomAlpha *= getDistanceAlpha(pos);
        }

        final Color topColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), topAlpha);
        final Color bottomColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), bottomAlpha);

        GlStateManager.color(bottomColor.getRed() / 255.0f, bottomColor.getGreen() / 255.0f, bottomColor.getBlue() / 255.0f, bottomColor.getAlpha() / 255.0f);
        RenderUtil.quad3d(
                GL11.GL_LINE_LOOP,
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY(), pos.getZ() + 1,
                pos.getX(), pos.getY(), pos.getZ() + 1
        );
        if (gradient.getValue()) {
            final AxisAlignedBB bb = new AxisAlignedBB(pos);
            if (!mc.player.getEntityBoundingBox().intersects(bb) || self.getValue())
                drawSimpleGradientBB(bb, topColor, bottomColor);
        }

        GlStateManager.popMatrix();
    }

    private int ticksPassed = 0;

    @SuppressWarnings("unchecked")
    @ApiCall
    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        if(isEnabled()) {
            if (ticksPassed >= updateDelay.getValue()) {
                executor.submit(callable);
                ticksPassed = 0;
            }

            ticksPassed++;
        }
    }

    @SuppressWarnings("rawtypes")
    private class HoleFinderCallable implements Callable {
        private final Set<BlockPos> safeBlocks = new HashSet<>();
        private final Set<BlockPos> unsafeBlocks = new HashSet<>();
        @Override
        public Object call() {

            for (BlockPos pos : BlockUtil.getSurroundingPositions(Math.round(distance.getValue()), mc.player, true)) {
                // where we are checking if it is a hole or not
                if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() != Blocks.AIR) {
                    if (isHoleType(pos, Blocks.BEDROCK)) {
                        safeBlocks.add(pos);
                    } else if (isHoleType(pos, Blocks.OBSIDIAN, Blocks.BEDROCK)) {
                        unsafeBlocks.add(pos);
                    }
                }
            }

            // updating the sets
            safePositions.clear();
            unSafePositions.clear();
            safePositions.addAll(safeBlocks);
            unSafePositions.addAll(unsafeBlocks);
            safeBlocks.clear();
            unsafeBlocks.clear();
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