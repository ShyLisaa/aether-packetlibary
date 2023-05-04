import de.princessheaven.aether.netty.Packet;
import de.princessheaven.aether.netty.buffer.PacketBuffer;

public class TestPacket extends Packet {

    private String message;

    public TestPacket() {
    }

    public TestPacket(String message) {
        this.message = message;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.message = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUTF8(this.message);
    }

    public String getMessage() {
        return this.message;
    }
}
