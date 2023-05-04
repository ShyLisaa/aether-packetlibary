import de.princessheaven.aether.netty.Packet;
import de.princessheaven.aether.netty.bootstrap.server.NettyServer;
import de.princessheaven.aether.netty.event.EventRegistry;
import de.princessheaven.aether.netty.event.PacketEvent;
import de.princessheaven.aether.netty.event.PacketReceiveEvent;
import de.princessheaven.aether.netty.exception.PacketRegistrationException;
import de.princessheaven.aether.netty.registry.SimplePacketRegistry;
import io.netty.channel.ChannelHandlerContext;
import org.tinylog.Logger;

import java.net.InetSocketAddress;

public class NettyServerBootstrap {

    public static void main(String[] args) throws PacketRegistrationException {
        NettyServer nettyServer = new NettyServer(new InetSocketAddress("127.0.0.1", 25565), new SimplePacketRegistry(), new EventRegistry(), future -> {
            if (future.isDone()) {
                Logger.info("Successfully started server!");
            } else {
                Logger.error("Failed to start server!");
            }
        });

        nettyServer.getPacketRegistry().registerPacket(TestPacket.class);
    }
}
