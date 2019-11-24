package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.PacketContent;
import ie.tcd.mantiqul.packet.PayloadPacketContent;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class EndNode extends Node {
  private static final String PREFIX = "> ";
  private final String DEFAULT_SWITCH;

  private Terminal terminal;

  EndNode(int listeningPort, String name, String defaultRouter) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(name);
    DEFAULT_SWITCH = defaultRouter;
  }

  public void start() {
    while (true) {
      String commandString = terminal.read(PREFIX);
      String destination = terminal.read(PREFIX);
      send(
          new PayloadPacketContent(commandString, DEFAULT_SWITCH, destination),
          new InetSocketAddress(DEFAULT_SWITCH, DEFAULT_PORT));
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
        terminal.println(payloadPacketContent.getPayload());
        break;
      default:
        terminal.println("Unknown packet received");
    }
//    terminal.println("---------------------------------------------------------------------------");
//    terminal.println(packetContent.toString());
//    terminal.println("---------------------------------------------------------------------------");
  }

  public static void main(String[] args) throws SocketException {
    if (args.length < 2) {
      System.out.println("Usage: java ie.tcd.mantiqul.EndNode <name> <default router>");
      return;
    }
    (new EndNode(DEFAULT_PORT, args[0], args[1])).start();
  }
}
