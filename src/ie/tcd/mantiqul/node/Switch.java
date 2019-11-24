package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.FeatureResultPacketContent;
import ie.tcd.mantiqul.packet.FlowModPacketContent;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import ie.tcd.mantiqul.packet.PacketInPacketContent;
import ie.tcd.mantiqul.packet.PayloadPacketContent;
import ie.tcd.mantiqul.packet.UnknownDestinationPacketContent;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Switch extends Node {

  private final String CONTROLLER = "controller";

  private Terminal terminal;
  private double versionNumber;
  private String name;
  private Map<String, String> flowTable;
  private List<String> connections;
  private BlockingQueue<PayloadPacketContent> packetBuffer;

  Switch(int listeningPort, String name, List<String> connections) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(name);
    this.name = name;
    versionNumber = 1.0;
    flowTable = new ConcurrentHashMap<>();
    this.connections = connections;
    packetBuffer = new LinkedBlockingQueue<>();
    latch = new CountDownLatch(1);
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
        if (receivedVersionNumber < versionNumber) {
          versionNumber = receivedVersionNumber;
        }
        terminal.println("Hello from " + packet.getAddress().getHostName());
        break;
      case PacketContent.PAYLOAD_PACKET:
        PayloadPacketContent payloadPacketContent = (PayloadPacketContent) packetContent;
        packetBuffer.add(payloadPacketContent);
        break;
      case PacketContent.FEATURE_REQUEST:
        int num_buffers = 11;
        int num_tables = 1;
        FeatureResultPacketContent specifications =
            new FeatureResultPacketContent(num_buffers, num_tables, name, connections);
        send(specifications, packet.getAddress(), packet.getPort());
        break;
      case PacketContent.FLOW_MOD_PACKET:
        FlowModPacketContent flowMod = (FlowModPacketContent) packetContent;
        flowTable.put(flowMod.getDestination(), flowMod.getNextHop());
        latch.countDown();
        break;
      case PacketContent.UNKNOWN_DESTINATION:
        UnknownDestinationPacketContent unknownDestinationPacketContent =
            (UnknownDestinationPacketContent) packetContent;
        terminal.println(
            "Destination '" + unknownDestinationPacketContent.getDestination() + "' not found");
        latch.countDown();
        break;
      default:
        terminal.println("Unknown packet received");
    }
//    terminal.println("---------------------------------------------------------------------------");
//    terminal.println(packetContent.toString());
//    terminal.println("---------------------------------------------------------------------------");
  }

  /**
   * Initialises the router
   */
  public void initialise() {
    send(new HelloPacketContent(versionNumber), new InetSocketAddress(CONTROLLER, DEFAULT_PORT));
    this.start();
  }

  /**
   * Handles the packet buffer. When a payload packet is received, if a table miss occurs, a packet
   * in is sent to the controller to get a flow mod packet. If the destination is present i.e. a
   * table miss does not occur, the packet is then forwarded to the correct destination.
   */
  @Override
  public void run() {
    try {
      while (true) {
        PayloadPacketContent toForward = packetBuffer.take();
        toForward.setSwitchName(name);
        String payloadDestination = toForward.getDestination();
        if (!payloadDestination.equals(name)) {
          boolean tableMiss = !flowTable.containsKey(payloadDestination);
          if (tableMiss) {
            InetSocketAddress destination = new InetSocketAddress(CONTROLLER, DEFAULT_PORT);
            send(new PacketInPacketContent(toForward), destination);
            terminal.println("Destination unknown, asking controller for next destination");
            //wait for a flow mod packet to arrive
            latch.await();
          }
          latch = new CountDownLatch(1);
          String nextNodeHop = flowTable.get(payloadDestination);
          InetSocketAddress nextHopAddress = new InetSocketAddress(nextNodeHop, DEFAULT_PORT);
          send(toForward, nextHopAddress);
          terminal.println("Forwarding to " + nextNodeHop);
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws SocketException {
    if (args.length < 2) {
      System.out
          .println("Usage: java ie.tcd.mantiqul.Switch <name> <connection 1> ... <connection N>");
      return;
    }
    (new Switch(DEFAULT_PORT, args[0], Arrays.asList(args).subList(1, args.length))).initialise();
  }
}
