package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Controller extends Node {

  Terminal terminal;

  Controller(int listeningPort) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(getClass().getSimpleName());
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
      case PacketContent.HELLO_PACKET:
        send(new HelloPacketContent(), packet.getAddress(), packet.getPort());
        break;
    }
  }

  public static void main(String[] args) throws SocketException {
    new Controller(DEFAULT_PORT);
  }
}
