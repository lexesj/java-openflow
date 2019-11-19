package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.FeatureRequestPacketContent;
import ie.tcd.mantiqul.packet.FeatureResultPacketContent;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Controller extends Node {

  private Terminal terminal;
  private double versionNumber;

  Controller(int listeningPort) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(getClass().getSimpleName());
    versionNumber = 1.0;
    terminal.println(this.toString());
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
        HelloPacketContent helloPacketContent = (HelloPacketContent) packetContent;
        double receivedVersionNumber = helloPacketContent.getVersionNumber();
        if (receivedVersionNumber < versionNumber) versionNumber = receivedVersionNumber;
        send(new HelloPacketContent(versionNumber), packet.getAddress(), packet.getPort());
        send(new FeatureRequestPacketContent(), packet.getAddress(), packet.getPort());
        break;
      case PacketContent.FEATURE_RESULT:
        FeatureResultPacketContent featureResultPacketContent =
            (FeatureResultPacketContent) packetContent;
        break;
      default:
        terminal.println("Unknown packet received");
    }
    terminal.println("---------------------------------------------------------------------------");
    terminal.println(packetContent.toString());
    terminal.println("---------------------------------------------------------------------------");
  }

  public static void main(String[] args) throws SocketException {
    new Controller(DEFAULT_PORT);
  }
}
