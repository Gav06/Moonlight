package dev.moonlight.module.mods.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.events.PlayerUpdateEvent;
import dev.moonlight.friend.FriendManager;
import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.util.InventoryUtil;
import dev.moonlight.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

@Module.Info(
        name = "MiddleClick",
        desc = "When you middle click it does stuff.",
        category = Module.Category.Player
)
public class MiddleClick extends Module {

    public BoolSetting silent = new BoolSetting("Silent", true, false);
    public BoolSetting xp = new BoolSetting("XP", true, false);
    public BoolSetting footXP = new BoolSetting("FootXP", false, false, () -> xp.getValue());
    public BoolSetting verticalXP = new BoolSetting("VerticalXP", false, false, () -> xp.getValue());
    public BoolSetting ePearl = new BoolSetting("EPearl", false, false);
    public BoolSetting packetGap = new BoolSetting("PacketGap", true, false, () -> silent.getValue());
    public BoolSetting friend = new BoolSetting("Friend", true, false);

    boolean mouseHolding = false;
    boolean hasSwitched = false;
    boolean hasClicked = false;
    int originalSlot;

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == 2) {
            if (Mouse.getEventButtonState()) {
                originalSlot = mc.player.inventory.currentItem;
                mouseHolding = true;
                hasClicked = true;
            }else {
                mouseHolding = false;
                mc.player.connection.sendPacket(new CPacketHeldItemChange(originalSlot));
                if(!silent.getValue()) mc.player.inventory.currentItem = originalSlot;
                originalSlot = -1;
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        int slot = -1;
        if(xp.getValue()) {
            slot = InventoryUtil.getItemSlot(Items.EXPERIENCE_BOTTLE);
        }else if(ePearl.getValue()) {
            slot = InventoryUtil.getItemSlot(Items.ENDER_PEARL);
        }else if(packetGap.getValue()) {
            slot = InventoryUtil.getItemSlot(Items.GOLDEN_APPLE);
        }
        if(mouseHolding) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.EXPERIENCE_BOTTLE && xp.getValue() || mc.player.getHeldItemMainhand().getItem() != Items.ENDER_PEARL && ePearl.getValue() || mc.player.getHeldItemMainhand().getItem() != Items.GOLDEN_APPLE && packetGap.getValue()) {
                if (silent.getValue() && slot != -1) {
                    InventoryUtil.silentSwitchToSlot(slot);
                    hasSwitched = true;
                } else if (!silent.getValue() && slot != -1) {
                    InventoryUtil.switchToSlot(slot);
                }
            }
            if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL && hasClicked || hasSwitched && hasClicked || hasSwitched) {
                if (footXP.getValue() && xp.getValue())
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, 90f, true));
                if (verticalXP.getValue() && xp.getValue())
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90f, true));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                if(hasClicked = true) hasClicked = false;
            }
            if(hasClicked && friend.getValue() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                if(!moonlight.getFriendManager().isFriend(mc.objectMouseOver.entityHit.getName())) {
                    moonlight.getFriendManager().addFriend(mc.objectMouseOver.entityHit.getName());
                    MessageUtil.sendMessage(ChatFormatting.GREEN + "Added" + " " + ChatFormatting.GOLD + mc.objectMouseOver.entityHit.getName() + ChatFormatting.RESET + " to friends list.");
                }else {
                    moonlight.getFriendManager().delFriend(mc.objectMouseOver.entityHit.getName());
                    MessageUtil.sendMessage(ChatFormatting.RED + "Removed" + " " + ChatFormatting.GOLD + mc.objectMouseOver.entityHit.getName() + ChatFormatting.RESET + " from friends list.");
                }
                hasClicked = false;
            }
        }
    }

    @Override
    public void onDisable() {
        if(hasSwitched) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(originalSlot));
            hasSwitched = false;
        }
        mouseHolding = false;
        hasClicked = false;
    }
}
