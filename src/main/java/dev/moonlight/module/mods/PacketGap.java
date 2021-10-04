package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BindSetting;
import dev.moonlight.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "PacketGap",
        desc = "Allows you to eat gaps when you are holding a bind.",
        category = Module.Category.Player
)
public class PacketGap extends Module {

    public BindSetting buttonToHold = new BindSetting("ButtonToHold", Keyboard.KEY_G);

    int originalSlot = -1;

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        int slot = InventoryUtil.getItemSlot(Items.GOLDEN_APPLE);
        boolean isHolding = false;
        if(Keyboard.isKeyDown(buttonToHold.getBind())) {
            originalSlot = mc.player.inventory.currentItem;
            isHolding = true;
            boolean hasSwitched = false;
            if (slot != -1) {
                InventoryUtil.silentSwitchToSlot(slot);
                hasSwitched = true;
            }
            if(hasSwitched) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
        if(!Keyboard.isKeyDown(buttonToHold.getBind()) && isHolding) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(originalSlot));
        }
    }
}
