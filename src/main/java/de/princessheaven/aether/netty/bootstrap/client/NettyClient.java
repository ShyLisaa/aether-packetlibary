package de.princessheaven.aether.netty.bootstrap.client;

import de.princessheaven.aether.netty.event.EventRegistry;
import de.princessheaven.aether.netty.handler.PacketChannelInboundHandler;
import de.princessheaven.aether.netty.handler.PacketDecoder;
import de.princessheaven.aether.netty.handler.PacketEncoder;
import de.princessheaven.aether.netty.registry.IPacketRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NettyClient extends ChannelInitializer<Channel> {

    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry;

    private Channel connectedChanel;

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(InetSocketAddress socketAddress, IPacketRegistry packetRegistry, EventRegistry eventRegistry, Consumer<Future<? super Void>> doneCallback) {
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

        try {
            this.bootstrap.connect(socketAddress)
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.workerGroup.schedule(this::shutdown, 5, TimeUnit.SECONDS);
    }

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline()
                .addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry));
        connectedChanel = channel;
    }

    public void shutdown() {
        try {
            this.workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public Channel getConnectedChanel() {
        return connectedChanel;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public IPacketRegistry getPacketRegistry() {
        return packetRegistry;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
}
