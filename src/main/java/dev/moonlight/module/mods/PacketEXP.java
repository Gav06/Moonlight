package dev.moonlight.module.mods;

import dev.moonlight.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BindSetting;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.settings.impl.ModeSetting;
import dev.moonlight.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Module.Info(name = "PacketEXP", category = Module.Category.Player, desc = "Throws Exp with packets.")
public class PacketEXP extends Module {

    public ModeSetting triggerMode = new ModeSetting("TriggerMode", TriggerMode.RightClick);
    public enum TriggerMode {RightClick, MiddleClick, Custom}
    public BindSetting customKey = new BindSetting("CustomKey", Keyboard.KEY_NONE, () -> triggerMode.getValueEnum().equals(TriggerMode.Custom));
    public FloatSetting packets = new FloatSetting("Packets", 2, 0, 10);
    public BoolSetting onlyInHand = new BoolSetting("OnlyInHand", false, false);

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if (triggerMode.getValueEnum().equals(TriggerMode.RightClick) && !mc.gameSettings.keyBindUseItem.isKeyDown())
            return;

        if (triggerMode.getValueEnum().equals(TriggerMode.MiddleClick) && !Mouse.isButtonDown(2))
            return;

        if (triggerMode.getValueEnum().equals(TriggerMode.Custom) && !Keyboard.isKeyDown(customKey.getBind()))
            return;

        if (onlyInHand.getValue() && !mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE))
            return;

        if (InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE) == -1)
            return;

        mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE)));

        for (int i = 0; i < packets.getValue(); i++) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }
}
