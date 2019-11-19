package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.PacketContent;
import ie.tcd.mantiqul.packet.PayloadPacketContent;
import java.net.DatagramPacket;
import java.net.SocketException;

public class EndNode extends Node{
  private static final String PREFIX = "> ";
  private final String DEFAULT_ROUTER;

  private Terminal terminal;

  EndNode(int listeningPort, String defaultRouter) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(getClass().getSimpleName());
    DEFAULT_ROUTER = defaultRouter;
  }

  public void start() {
    while (true) {
      String commandString = terminal.read(PREFIX);
    }
  }

  /**
   * Override method which handles the each type of packet received.
   *
   * @param packet The packet received
   */
  @Override
  public void onReceipt(DatagramPacket packet) {
    PacketContent packetContent = PacketContent.fromDatagramPacket(packet);
    switch (packetContent.type) {
      case PacketContent.PAYLOAD_PACKET:
        PayloadPacketContent payloadPacketContent = (PayloadPacketContent) packetContent;
        terminal.println(payloadPacketContent.toString());
        break;
      default:
        terminal.println("Unknown packet received");
    }
    terminal.println(packetContent.toString());
  }

  public static void main(String[] args) throws SocketException {
    if (args.length < 1) {
      System.out.println("Usage: java ie.tcd.mantiqul.EndNode <default router>");
      return;
    }
    (new EndNode(DEFAULT_PORT, args[0])).start();
  }
}
