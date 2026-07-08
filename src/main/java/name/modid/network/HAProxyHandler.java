package name.modid.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import net.minecraft.network.ClientConnection;
import name.modid.mixin.ClientConnectionAccessor;

import java.net.InetSocketAddress;

public class HAProxyHandler extends ChannelInboundHandlerAdapter {
    private final ClientConnection connection;

    public HAProxyHandler(ClientConnection connection) {
        this.connection = connection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HAProxyMessage ipMessage) {
            try {
                String realIp = ipMessage.sourceAddress();
                int realPort = ipMessage.sourcePort();

                if (realIp != null) {
                    InetSocketAddress newAddress = new InetSocketAddress(realIp, realPort);
                    ((ClientConnectionAccessor) connection).setSocketAddress(newAddress);
                }
            } catch (Exception e) {
                e.printStackTrace();
} finally {
    ipMessage.release();
    if (ctx.pipeline().get("haproxyDecoder") != null) {
        //ctx.pipeline().remove("haproxyDecoder");
    }
    ctx.pipeline().remove(this);
}        } else {
            super.channelRead(ctx, msg);
        }
    }
}
