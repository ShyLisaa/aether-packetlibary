package de.princessheaven.aether.netty.io;

import de.princessheaven.aether.netty.buffer.PacketBuffer;

public interface Encoder {

    void write(PacketBuffer buffer);
}
