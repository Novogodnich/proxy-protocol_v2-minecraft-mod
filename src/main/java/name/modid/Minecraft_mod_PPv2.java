package name.modid;

import io.netty.channel.Channel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import name.modid.mixin.ChannelAccessor;
import name.modid.mixin.ServerLoginPacketListenerImplAccessor;
import name.modid.network.HAProxyHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

public class Minecraft_mod_PPv2 implements ModInitializer {
	@Override
	public void onInitialize() {

		ServerLoginConnectionEvents.INIT.register((handler, server) -> {
			if (handler instanceof ServerLoginPacketListenerImpl targetHandler) {
				Connection connection = ((ServerLoginPacketListenerImplAccessor) targetHandler).getConnection();

				if (connection != null) {

					Channel channel = ((ChannelAccessor) connection).getChannel();

					if (channel != null) {
						var pipeline = channel.pipeline();

						pipeline.addFirst("haproxyDecoder", new HAProxyMessageDecoder());
						pipeline.addAfter("haproxyDecoder", "mineguardProxyHandler", new HAProxyHandler(connection));
					}
				}
			}
		});
	}
}
