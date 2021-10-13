package dev.moonlight.module.mods.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.module.Module;
import dev.moonlight.util.InventoryUtil;
import dev.moonlight.util.MessageUtil;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(
        name = "AutoTotem",
        desc = "Moves a totem to your offhand.",
        category = Module.Category.Combat
)
public class AutoTotem extends Module {

    String autoTotemPrefix = ChatFormatting.GRAY + "[" + ChatFormatting.RED + "AutoTotem" + ChatFormatting.GRAY + "]" + ChatFormatting.RESET;

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            int slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
            if(slot != -1) {
                MessageUtil.sendRawMessage(autoTotemPrefix + " Switched to totem.");
                InventoryUtil.moveItemToSlot(slot, 45);
            }
        }
    }
}
