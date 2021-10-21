package dev.moonlight.module.mods.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Module.Info(name = "ShulkerLog", desc = "", category = Module.Category.World)
public class ShulkerLog extends Module {
    public List<TileEntity> tileEntityList = new ArrayList<>();
    public List<Entity> itemEntityList = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        for(TileEntity e : mc.world.loadedTileEntityList) {
            if(tileEntityList.contains(e)) continue;
            if(e instanceof TileEntityShulkerBox) {
                tileEntityList.add(e);
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.AQUA) + " Found a shulker box at " + ChatFormatting.GRAY + "X:" + ChatFormatting.GREEN + e.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.GREEN + e.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.GREEN + e.getPos().getZ() + ChatFormatting.RESET + " and is called " + ChatFormatting.RED + ((TileEntityShulkerBox) e).getName());
            }
        }
        mc.world.loadedEntityList.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).filter(entityItem -> entityItem.ticksExisted > 1).forEach(entityItem -> {
            if(entityItem.getItem().getItem() instanceof ItemShulkerBox && !itemEntityList.contains(entityItem)) {
                itemEntityList.add(entityItem);
                MessageUtil.sendRawMessage(MessageUtil.getModulePrefix(this, ChatFormatting.AQUA) + " Found a shulker box at " + ChatFormatting.GRAY + "X:" + ChatFormatting.GREEN + entityItem.getPosition().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.GREEN + entityItem.getPosition().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.GREEN + entityItem.getPosition().getZ() + ChatFormatting.RESET + " and is called " + ChatFormatting.RED + entityItem.getItem().getDisplayName());
            }
        });
    }

    @Override
    public void onDisable() {
        itemEntityList.clear();
        tileEntityList.clear();
    }
}
