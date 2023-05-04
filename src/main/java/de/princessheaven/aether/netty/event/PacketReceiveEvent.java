package de.princessheaven.aether.netty.event;

import com.sun.jdi.event.Event;
import de.princessheaven.aether.netty.Packet;
import io.netty.channel.ChannelHandlerContext;

public interface PacketReceiveEvent<T extends Packet> {

   void onPacketReceive(T packet, ChannelHandlerContext ctx);
}
