package dev.moonlight.mixin.mixins;

import dev.moonlight.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacketMixin(Packet<?> packetIn, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.Send(packetIn))) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead0Mixin(ChannelHandlerContext channelContext, Packet<?> packet, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.Receive(packet))) {
            ci.cancel();
        }
    }
}