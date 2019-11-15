package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Router extends Node {

  public final String CONTROLLER = "controller";

  Terminal terminal;

  Router(int listeningPort) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(getClass().getName());
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
        System.out.println("hello");
        break;
    }
  }

  /**
   * Initialises the router
   */
  public void start() {
    try {
      send(new HelloPacketContent(),
          new InetSocketAddress(InetAddress.getByName(CONTROLLER), DEFAULT_PORT));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws SocketException {
    (new Router(DEFAULT_PORT)).start();
  }
}
