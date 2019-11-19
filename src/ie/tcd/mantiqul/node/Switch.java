package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.packet.HelloPacketContent;
import ie.tcd.mantiqul.packet.PacketContent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Switch extends Node {

  public final String CONTROLLER = "controller";

  Terminal terminal;

  Switch(int listeningPort) throws SocketException {
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
    (new Switch(DEFAULT_PORT)).start();
  }
}
