package de.princessheaven.aether.netty;

import de.princessheaven.aether.netty.io.Decoder;
import de.princessheaven.aether.netty.io.Encoder;
import io.netty.util.internal.ThreadLocalRandom;

public abstract class Packet implements Encoder, Decoder {

    private long sessionId = ThreadLocalRandom.current().nextLong();

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }
}
