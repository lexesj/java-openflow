package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.FeatureRequestPacketContent;
import ie.tcd.mantiqul.packet.FeatureResultPacketContent;
import ie.tcd.mantiqul.packet.FlowModPacketContent;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import ie.tcd.mantiqul.packet.PacketInPacketContent;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller extends Node {

  private Terminal terminal;
  private double versionNumber;
  private Map<String, Map<String, String>> flowTable;

  Controller(int listeningPort) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(getClass().getSimpleName());
    versionNumber = 1.0;
    initialiseFlowTable();
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
      case PacketContent.PACKET_IN_PACKET:
        PacketInPacketContent packetInPacketContent = (PacketInPacketContent) packetContent;
        String destination = packetInPacketContent.getDestination();
        String nextHop = flowTable.get(destination).get(packet.getAddress().getCanonicalHostName());
        FlowModPacketContent flowMod = new FlowModPacketContent(nextHop, destination);
        send(flowMod, packet.getAddress(), packet.getPort());
        break;
      default:
        terminal.println("Unknown packet received");
    }
//    terminal.println("---------------------------------------------------------------------------");
//    terminal.println(packetContent.toString());
//    terminal.println("---------------------------------------------------------------------------");
  }

  private void initialiseFlowTable() {
    flowTable = new ConcurrentHashMap<>();
    Map<String, String> endpoint1 = new ConcurrentHashMap<>();
    endpoint1.put("switch0.telecomms", "switch1.telecomms");
    endpoint1.put("switch1.telecomms", "switch2.telecomms");
    endpoint1.put("switch2.telecomms", "endpoint1.telecomms");
    flowTable.put("endpoint1.telecomms", endpoint1);
    Map<String, String> endpoint0 = new ConcurrentHashMap<>();
    endpoint0.put("switch2.telecomms", "switch1.telecomms");
    endpoint0.put("switch1.telecomms", "switch0.telecomms");
    endpoint0.put("switch0.telecomms", "endpoint0.telecomms");
    flowTable.put("endpoint0.telecomms", endpoint0);
  }

  public static void main(String[] args) throws SocketException {
    new Controller(DEFAULT_PORT);
  }
}
