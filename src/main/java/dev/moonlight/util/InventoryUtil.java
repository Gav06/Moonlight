package dev.moonlight.util;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static int getItemSlot(Item item) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }

    public static int getItemHotbar(Item item) {
        int itemSlot = -1;
        for(int i = 9; i > 0; --i) {
            if(mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }

    public static void switchToSlot(int slot) {
        if (mc.player.inventory.currentItem == slot || slot == -1) {
            return;
        }
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static void silentSwitchToSlot(int slot) {
        if (mc.player.inventory.currentItem == slot || slot == -1) {
            return;
        }
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.playerController.updateController();
    }

    public static void moveItemToSlot(int startSlot, int endSlot) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }
}
