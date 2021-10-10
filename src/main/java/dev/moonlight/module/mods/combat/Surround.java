package dev.moonlight.module.mods.combat;

import dev.moonlight.module.Module;
import dev.moonlight.util.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

@Module.Info(
        name = "Surround",
        desc = "z",
        category = Module.Category.Combat
)
public class Surround extends Module {

    @Override
    public void onEnable() {
        int slot = InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN));
        for(BlockPos blockPos : getSurroundPositions(mc.player)) {
            if(mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR && mc.world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR) {
                if(slot != -1) {
                    InventoryUtil.switchToSlot(slot);
                }
            }
        }
    }

    public ArrayList<BlockPos> getSurroundPositions(Entity entity) {
        ArrayList<BlockPos> surroundPositions = new ArrayList<>();
        surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).north()));
        surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).west()));
        surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).south()));
        surroundPositions.add((BlockPos) mc.world.getBlockState(new BlockPos(entity.posX, entity.posY, entity.posZ).east()));
        return surroundPositions;
    }
}
