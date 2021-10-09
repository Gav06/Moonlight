package dev.moonlight.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isPlayerSafe(EntityPlayer target) {
        BlockPos playerPos = new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
        return (mc.world.getBlockState(playerPos.down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos.down()).getBlock() == Blocks.BEDROCK) &&
                (mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.BEDROCK) &&
                (mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.BEDROCK) &&
                (mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.BEDROCK) &&
                (mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.BEDROCK);
    }

    public static boolean isPosValidForCrystal(BlockPos pos) {
        if (mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(pos).getBlock() != Blocks.OBSIDIAN)
            return false;

        if (mc.world.getBlockState(pos.up()).getBlock() != Blocks.AIR || mc.world.getBlockState(pos.up().up()).getBlock() != Blocks.AIR)
            return false;

        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up()))) {

            if (entity.isDead || entity instanceof EntityEnderCrystal)
                continue;

            return false;
        }

        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up().up()))) {

            if (entity.isDead || entity instanceof EntityEnderCrystal)

                continue;

            return false;
        }

        return true;
    }

    public static List<BlockPos> getSphere(double radius) {
        ArrayList<BlockPos> posList = new ArrayList<>();
        BlockPos pos = new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
        for (int x = pos.getX() - (int) radius; x <= pos.getX() + radius; ++x) {
            for (int y = pos.getY() - (int) radius; y < pos.getY() + radius; ++y) {
                for (int z = pos.getZ() - (int) radius; z <= pos.getZ() + radius; ++z) {
                    double distance = (pos.getX() - x) * (pos.getX() - x) + (pos.getZ() - z) * (pos.getZ() - z) + (pos.getY() - y) * (pos.getY() - y);
                    BlockPos position = new BlockPos(x, y, z);
                    if (distance < radius * radius && !mc.world.getBlockState(position).getBlock().equals(Blocks.AIR)) {
                        posList.add(position);
                    }
                }
            }
        }
        return posList;
    }

    public static List<BlockPos> getSurroundingPositions(int distance, EntityPlayer player, boolean applyMotion, boolean noAir) {
        final List<BlockPos> positions = new ArrayList<>();

        final BlockPos center = new BlockPos(player.posX, player.posY, player.posZ);

        if (applyMotion) {
            center.add(player.motionX, player.motionY, player.motionZ);
        }

        for (int x = -distance; x < distance; x++) {
            for (int y = -distance; y < distance; y++) {
                for (int z = -distance; z < distance; z++) {

                    if(noAir && mc.world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.AIR))
                        continue;

                        positions.add(new BlockPos(x, y, z).add(center.getX(), center.getY(), center.getZ()));

                }
            }
        }

        return positions;
    }
}
