package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.FeatureRequestPacketContent;
import ie.tcd.mantiqul.packet.FeatureResultPacketContent;
import ie.tcd.mantiqul.packet.FlowModPacketContent;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import ie.tcd.mantiqul.packet.PacketInPacketContent;
import ie.tcd.mantiqul.pathfinding.Graph;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Controller extends Node {

  private Terminal terminal;
  private double versionNumber;
  private Map<String, Map<String, String>> flowTables;
  private Graph graph;

  Controller(int listeningPort) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(getClass().getSimpleName());
    versionNumber = 1.0;
    graph = new Graph();
    flowTables = new ConcurrentHashMap<>();
  }

  /**
   * Override method which handles the each type of packet received.
   *
   * @param packet The packet received
   */
  public void onReceipt(DatagramPacket packet) {
    PacketContent packetContent = PacketContent.fromDatagramPacket(packet);
    switch (packetContent.type) {
      case PacketContent.HELLO_PACKET:
        HelloPacketContent helloPacketContent = (HelloPacketContent) packetContent;
        double receivedVersionNumber = helloPacketContent.getVersionNumber();
        if (receivedVersionNumber < versionNumber) {
          versionNumber = receivedVersionNumber;
        }
        send(new HelloPacketContent(versionNumber), packet.getAddress(), packet.getPort());
        send(new FeatureRequestPacketContent(), packet.getAddress(), packet.getPort());
        terminal.println("Hello from " + packet.getAddress().getHostName());
        break;
      case PacketContent.FEATURE_RESULT:
        FeatureResultPacketContent featureResultPacketContent =
            (FeatureResultPacketContent) packetContent;
        String name = featureResultPacketContent.getSwitchName();
        List<String> connections = featureResultPacketContent.getConnections();
        Graph.Node node = graph.getOrDefault(name, new Graph.Node(name));
        for (String connection : connections) {
          Graph.Node adjacentNode = graph
              .getOrDefault(connection, new Graph.Node(connection));
          graph.putNode(connection, adjacentNode);
          node.adjacentAdd(adjacentNode);
        }
        graph.putNode(name, node);
        break;
      case PacketContent.PACKET_IN_PACKET:
        PacketInPacketContent packetInPacketContent = (PacketInPacketContent) packetContent;
        String destination = packetInPacketContent.getDestination();
        String switchName = packetInPacketContent.getSwitchName();
        boolean tableMiss = flowTables.get(destination) == null;
        if (tableMiss) {
          generatePath(switchName, destination);
        }
        String nextHop = flowTables.get(destination).get(switchName);
        FlowModPacketContent flowMod = new FlowModPacketContent(nextHop, destination);
        send(flowMod, packet.getAddress(), packet.getPort());
        terminal.println("Forwarding routing table to " + switchName);
        break;
      default:
        terminal.println("Unknown packet received");
    }
//    terminal.println("---------------------------------------------------------------------------");
//    terminal.println(packetContent.toString());
//    terminal.println("---------------------------------------------------------------------------");
  }

  private void generatePath(String start, String end) {
    Graph.Node startNode = graph.getNode(start);
    Graph.Node endNode = graph.getNode(end);
    List<Graph.Node> path = graph.getPathBFS(startNode, endNode);
    Map<String, String> destination = new ConcurrentHashMap<>();
    for (int i = 0; i < path.size() - 1; i++) {
      String current = path.get(i).getName();
      String nextHop = path.get(i + 1).getName();
      destination.put(current, nextHop);
    }
    flowTables.put(end, destination);
  }

  public static void main(String[] args) throws SocketException {
    new Controller(DEFAULT_PORT);
  }
}
