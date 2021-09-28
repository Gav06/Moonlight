package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.BlockHelper;
import dev.moonlight.misc.RenderUtil;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
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

    private final BoolSetting self = new BoolSetting("Self", false);

    private final HoleFinderCallable callable = new HoleFinderCallable();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    // so we dont get ConcurrentModificationExceptions when we modify these lists in other threads
    private final ConcurrentLinkedQueue<BlockPos> unSafePositions = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<BlockPos> safePositions = new ConcurrentLinkedQueue<>();

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {

        for (BlockPos pos : safePositions) {
            renderHole(pos, 0x8000ff00, 0x1a00ff00);
        }

        for (BlockPos pos : unSafePositions) {
            renderHole(pos, 0x80ff0000, 0x1aff0000);
        }

    }

    private void renderHole(BlockPos pos, int color, int clearcolor) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glPushMatrix();
        GL11.glLineWidth(2f);
        final float[] colors = RenderUtil.hexToRGBA(color);
        GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
        GL11.glTranslated(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        RenderUtil.quad3d(GL11.GL_LINE_LOOP,
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY(), pos.getZ() + 1,
                pos.getX(), pos.getY(), pos.getZ() + 1);
        final AxisAlignedBB bb = new AxisAlignedBB(pos);
        if (!(bb.intersects(mc.player.getEntityBoundingBox()) && !self.getValue())) {
            RenderUtil.drawSimpleGradientBB(new AxisAlignedBB(pos), clearcolor, color, true);
        }
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
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
}