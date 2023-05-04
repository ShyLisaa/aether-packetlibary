import de.princessheaven.aether.netty.bootstrap.client.NettyClient;
import de.princessheaven.aether.netty.event.EventRegistry;
import de.princessheaven.aether.netty.exception.PacketRegistrationException;
import de.princessheaven.aether.netty.registry.SimplePacketRegistry;
import org.tinylog.Logger;

import java.net.InetSocketAddress;

public class NettyClientBootstrap {
    public static void main(String[] args) throws PacketRegistrationException {
        NettyClient nettyClient = new NettyClient(new InetSocketAddress("127.0.0.1", 25565), new SimplePacketRegistry(), new EventRegistry(), future -> {
            if (future.isSuccess()) {
                Logger.info("Successfully connected to server!");
            } else {
                Logger.error("Failed to connect to server!");
                future.cause().printStackTrace();
            }
        });

        nettyClient.getPacketRegistry().registerPacket(TestPacket.class);

        nettyClient.getConnectedChanel().writeAndFlush(new TestPacket("Hello World!"));
    }
}
