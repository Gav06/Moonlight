package dev.moonlight.module.mods.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.MessageUtil;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Module.Info(
        name = "StashFinder",
        desc = "Finds shit.",
        category = Module.Category.World
)
public class StashFinder extends Module {

    public BoolSetting shulker = new BoolSetting("Shulker", true, false);
    public BoolSetting chest = new BoolSetting("Chest", true, false);
    public BoolSetting eChest = new BoolSetting("EChest", true, false);
    public BoolSetting hopper = new BoolSetting("Hopper", true, false);

    public List<BlockPos> foundBlocks = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        for(TileEntity e : mc.world.loadedTileEntityList) {
            if(e instanceof TileEntityShulkerBox && !foundBlocks.contains(new BlockPos(e.getPos())) && shulker.getValue()) {
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.GREEN) + " Found a " + ChatFormatting.LIGHT_PURPLE + "Shulker" + ChatFormatting.RESET + " at " + ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET + e.getPos().getZ());
                foundBlocks.add(new BlockPos(e.getPos()));
            }
            if(e instanceof TileEntityChest && !foundBlocks.contains(new BlockPos(e.getPos())) && chest.getValue()) {
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.GREEN) + " Found a " + ChatFormatting.GREEN + "Chest" + ChatFormatting.RESET + " at " + ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET + e.getPos().getZ());
                foundBlocks.add(new BlockPos(e.getPos()));
            }
            if(e instanceof TileEntityEnderChest && !foundBlocks.contains(new BlockPos(e.getPos())) && eChest.getValue()) {
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.GREEN) + " Found a " + ChatFormatting.DARK_PURPLE + "EChest" + ChatFormatting.RESET + " at " + ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET + e.getPos().getZ());
                foundBlocks.add(new BlockPos(e.getPos()));
            }
            if(e instanceof TileEntityHopper && !foundBlocks.contains(new BlockPos(e.getPos())) && hopper.getValue()) {
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.GREEN) + " Found a " + ChatFormatting.GRAY + "Hopper" + ChatFormatting.RESET + " at " + ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET + e.getPos().getZ());
                foundBlocks.add(new BlockPos(e.getPos()));
            }
        }
    }
}
