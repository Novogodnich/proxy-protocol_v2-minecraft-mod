package name.modid.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import name.modid.mixin.ClientConnectionAccessor;
import net.minecraft.network.Connection;

import java.net.InetSocketAddress;

public class HAProxyHandler extends ChannelInboundHandlerAdapter {
    private final Connection connection;
    private boolean removed = false;

    public HAProxyHandler(Connection connection) {
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

                if (!removed) {
                    removed = true;
                    ctx.executor().execute(() -> {
                        var pipeline = ctx.pipeline();
                        try {
                            if (pipeline.get("haproxyDecoder") != null) {
                                pipeline.remove("haproxyDecoder");
                            }
                            if (pipeline.context(this) != null) {
                                pipeline.remove(this);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
