package name.modid.mixin;

import io.netty.channel.Channel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import net.minecraft.network.ClientConnection;
import name.modid.network.HAProxyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.ServerNetworkIo$1")
public class ServerNetworkIoMixin {

    @Inject(method = "initChannel(Lio/netty/channel/Channel;)V", at = @At("TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        ClientConnection connection = channel.pipeline().get(ClientConnection.class);

        if (connection != null) {
            channel.pipeline().addFirst("haproxyDecoder", new HAProxyMessageDecoder());
            channel.pipeline().addAfter("haproxyDecoder", "mineguardProxyHandler", new HAProxyHandler(connection));
        }
    }
}