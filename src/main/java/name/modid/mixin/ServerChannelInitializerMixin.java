package name.modid.mixin;

import io.netty.channel.Channel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import name.modid.network.HAProxyHandler;
import net.minecraft.network.Connection;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.network.ServerConnectionListener$1")
public class ServerChannelInitializerMixin {

    @Inject(
            method = "initChannel(Lio/netty/channel/Channel;)V",
            at = @At("TAIL"),
            remap = true
    )
    private void onInitChannel(@NonNull Channel channel, CallbackInfo ci) {
        Connection connection = channel.pipeline().get(Connection.class);

        if (connection != null) {
            channel.pipeline().addFirst("haproxyDecoder", new HAProxyMessageDecoder());
            channel.pipeline().addAfter("haproxyDecoder", "proxyHandler", new HAProxyHandler(connection));
        }
    }
}
