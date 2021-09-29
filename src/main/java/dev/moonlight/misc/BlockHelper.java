package dev.moonlight.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class BlockHelper {

    public static List<BlockPos> getSurroundingPositions(int distance, EntityPlayer player, boolean applyMotion) {
        final List<BlockPos> positions = new ArrayList<>();

        final BlockPos center = new BlockPos(player.posX, player.posY, player.posZ);

        if (applyMotion) {
            center.add(player.motionX, player.motionY, player.motionZ);
        }

        for (int x = -distance; x < distance; x++) {
            for (int y = -distance; y < distance; y++) {
                for (int z = -distance; z < distance; z++) {
                    positions.add(new BlockPos(x, y, z).add(center.getX(), center.getY(), center.getZ()));
                }
            }
        }

        return positions;
    }
}
