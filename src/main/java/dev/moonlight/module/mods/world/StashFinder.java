package dev.moonlight.module.mods.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.MessageUtil;
import net.minecraft.client.settings.KeyBinding;
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

    public BoolSetting walk = new BoolSetting("Walk", false, false);
    public BoolSetting storageParent = new BoolSetting("Storage", false, true);
    public BoolSetting shulker = new BoolSetting("Shulker", true, false, () -> storageParent.getValue());
    public BoolSetting chest = new BoolSetting("Chest", true, false, () -> storageParent.getValue());
    public BoolSetting eChest = new BoolSetting("EChest", true, false, () -> storageParent.getValue());
    public BoolSetting hopper = new BoolSetting("Hopper", true, false, () -> storageParent.getValue());

    public List<BlockPos> foundBlocks = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(walk.getValue() && !mc.gameSettings.keyBindForward.isKeyDown()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        for(TileEntity e : mc.world.loadedTileEntityList) {
            if(e instanceof TileEntityShulkerBox && !foundBlocks.contains(new BlockPos(e.getPos())) && shulker.getValue()) {
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.GREEN) + " Found a " + ChatFormatting.LIGHT_PURPLE + "Shulker" + ChatFormatting.RESET + " at " + ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET + e.getPos().getZ());
                foundBlocks.add(new BlockPos(e.getPos()));
            }
            if(e instanceof TileEntityChest && !foundBlocks.contains(new BlockPos(e.getPos())) && chest.getValue()) {
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.GREEN) + " Found a " + ChatFormatting.GOLD + "Chest" + ChatFormatting.RESET + " at " + ChatFormatting.GRAY + "X:" + ChatFormatting.RESET + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.RESET + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.RESET + e.getPos().getZ());
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

    @Override
    public void onDisable() {
        if(walk.getValue()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        if(!foundBlocks.isEmpty()) foundBlocks.clear();
    }
}
