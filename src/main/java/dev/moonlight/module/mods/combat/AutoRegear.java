package dev.moonlight.module.mods.combat;

import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.InventoryUtil;
import dev.moonlight.util.MessageUtil;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Module.Info(
        name = "AutoRegear",
        desc = "Like rusherhack.",
        category = Module.Category.Combat
)
public class AutoRegear extends Module {

    public BoolSetting save = new BoolSetting("Save", false, false);

    public HashMap<Integer, Item> slots = new HashMap<>();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(save.getValue()) {
            for (int i = 45; i > 0; --i) {
                slots.put(i, mc.player.inventory.getStackInSlot(i).getItem());
            }
            save.toggle();
            MessageUtil.sendMessage(slots.values().toString());
        }
        if(mc.currentScreen instanceof GuiShulkerBox) {
            for(int i = 36; i > 0; --i) {
                if(slots.containsKey(i) && mc.player.inventoryContainer.getSlot(i).getStack().getItem() == slots.get(i) && mc.player.inventoryContainer.getSlot(i).getStack().getItem() != Items.AIR) {
                    InventoryUtil.moveItemToSlot(i, mc.player.inventoryContainer.getSlot(i).getSlotIndex());
                }
            }
        }
    }
}
