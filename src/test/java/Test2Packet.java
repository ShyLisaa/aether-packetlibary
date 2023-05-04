import de.princessheaven.aether.netty.Packet;
import de.princessheaven.aether.netty.buffer.PacketBuffer;
import de.princessheaven.aether.netty.event.PacketEvent;
import de.princessheaven.aether.netty.event.PacketReceiveEvent;
import io.netty.channel.ChannelHandlerContext;

public class Test2Packet extends Packet implements PacketReceiveEvent<Test2Packet> {

    private String message;

    public Test2Packet() {
    }

    public Test2Packet(String message) {
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

    @PacketEvent
    public void onPacketReceive(Test2Packet packet, ChannelHandlerContext ctx) {
        System.out.println(packet.getMessage());
    }
}
