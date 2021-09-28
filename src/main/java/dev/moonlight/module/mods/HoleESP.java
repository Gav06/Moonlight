package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.BlockHelper;
import dev.moonlight.misc.RenderUtil;
import dev.moonlight.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Module.Info(
        name = "HoleESP",
        desc = "Highlights holes for safety in crystal combat",
        category = Module.Category.Render
)
public final class HoleESP extends Module {

    private final HoleFinderCallable callable = new HoleFinderCallable();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final ArrayList<BlockPos> unSafePositions = new ArrayList<>();
    private final ArrayList<BlockPos> safePositions = new ArrayList<>();

    @ApiCall
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (BlockPos pos : safePositions) {
            final BlockPos offsetPos = RenderUtil.offsetBlockPos(pos);
            GL11.glLineWidth(1f);
            GL11.glColor4f(0f, 1f, 0f, 1f);
            RenderUtil.quad3d(GL11.GL_LINE_LOOP,
                    offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(),
                    offsetPos.getX() + 1, offsetPos.getY(), offsetPos.getZ(),
                    offsetPos.getX() + 1, offsetPos.getY(), offsetPos.getZ() + 1,
                    offsetPos.getX(), offsetPos.getY(), offsetPos.getZ() + 1);
            System.out.println("rendering esp block");
        }

        for (BlockPos pos : unSafePositions) {
            final BlockPos offsetPos = RenderUtil.offsetBlockPos(pos);
            GL11.glLineWidth(1f);
            GL11.glColor4f(1f, 0f, 0f, 1f);
            RenderUtil.quad3d(GL11.GL_LINE_LOOP,
                    offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(),
                    offsetPos.getX() + 1, offsetPos.getY(), offsetPos.getZ(),
                    offsetPos.getX() + 1, offsetPos.getY(), offsetPos.getZ() + 1,
                    offsetPos.getX(), offsetPos.getY(), offsetPos.getZ() + 1);
            System.out.println("rendering esp block");
        }
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
        public Object call() throws Exception {
            for (BlockPos pos : BlockHelper.getSurroundingPositions(8, mc.player, true)) {
                // where we are checking if it is a hole or not
                if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() != Blocks.AIR) {
                    if (isHoleType(pos, Blocks.OBSIDIAN, Blocks.BEDROCK)) {
                        System.out.println("found unsafe position");
                        unSafePositions.add(pos);
                    } else if (isHoleType(pos, Blocks.BEDROCK)) {
                        System.out.println("found safe position!");
                        safePositions.add(pos);
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
