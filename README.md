# Fabric PROXY v2 Base Wrapper (PPv2)

A lightweight Fabric server-side mod that adds native support for the HAProxy PROXY protocol v2. It injects custom handlers into the Netty pipeline to accurately forward real player IPs behind reverse proxies, CDNs, or DDoS protection layers (like HAProxy, Velocity, or cloud mitigation providers).

## Features

- **Native Netty Integration**: Dynamically updates the channel pipeline during the handshake phase.
- **Accurate IP Forwarding**: Restores real client IPs in Minecraft server logs, bans, and whitelist systems instead of showing 127.0.0.1 or the proxy's IP.
- **Production Ready**: Fixed common pipeline race conditions (NoSuchElementException) and safely shaded required Netty HAProxy codecs.

## Installation & Requirements

- Drop the compiled .jar into your server's `mods/` directory.
- Ensure your upstream proxy/load balancer has PROXY protocol v2 enabled for the Minecraft backend port (e.g., `send-proxy-v2` in HAProxy configuration).

**Compatibility**:
- Fabric Loader 0.18.4+
- Minecraft 1.21.11
