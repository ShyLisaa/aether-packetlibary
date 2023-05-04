package de.princessheaven.aether.netty.bootstrap.server;

import de.princessheaven.aether.netty.event.EventRegistry;
import de.princessheaven.aether.netty.handler.PacketChannelInboundHandler;
import de.princessheaven.aether.netty.handler.PacketDecoder;
import de.princessheaven.aether.netty.handler.PacketEncoder;
import de.princessheaven.aether.netty.registry.IPacketRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class NettyServer extends ChannelInitializer<Channel> {

    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;

    private Channel connectedChannel;

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventRegistry eventRegistry;

    public NettyServer(InetSocketAddress socketAddress, IPacketRegistry packetRegistry, EventRegistry eventRegistry, Consumer<Future<? super Void>> doneCallback) {
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;
        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(this)
                .channel(NioServerSocketChannel.class);

        try {
            this.bootstrap.bind(socketAddress).awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() throws InterruptedException {
        this.parentGroup.shutdownGracefully().sync();
        this.workerGroup.shutdownGracefully().sync();
    }

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline()
                .addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry))
                .addLast(new ChannelHandler() {
                    @Override
                    public void handlerAdded(ChannelHandlerContext ctx) {

                    }

                    @Override
                    public void handlerRemoved(ChannelHandlerContext ctx) {

                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

                    }
                });
        this.connectedChannel = channel;
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public IPacketRegistry getPacketRegistry() {
        return packetRegistry;
    }

    public EventLoopGroup getParentGroup() {
        return parentGroup;
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public Channel getConnectedChannel() {
        return connectedChannel;
    }
}