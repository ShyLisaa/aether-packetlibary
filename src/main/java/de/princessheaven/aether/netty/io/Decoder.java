package de.princessheaven.aether.netty.io;

import de.princessheaven.aether.netty.buffer.PacketBuffer;

public interface Decoder {

    void read(PacketBuffer buffer);
}
