
package de.princessheaven.aether.netty.event;

import de.princessheaven.aether.netty.Packet;
import de.princessheaven.aether.netty.io.Responder;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EventRegistry {

    private final List<RegisteredPacketSubscriber> subscribers = new LinkedList<>();

    public <T extends Packet> void registerEvents(PacketReceiveEvent<T> packetReceiveEvent) {
        subscribers.add(new RegisteredPacketSubscriber(packetReceiveEvent));
    }

    public void invoke(Packet packet, ChannelHandlerContext ctx) {
        try {
            for (RegisteredPacketSubscriber subscriber : subscribers) {
                subscriber.invoke(packet, ctx, Responder.forId(packet.getSessionId(), ctx));
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
