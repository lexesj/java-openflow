package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.packet.PacketContent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public abstract class Node {
  public static final int PACKETSIZE = 65536;

  protected DatagramSocket socket;
  protected Listener listener;
  protected CountDownLatch latch;

  Node(int listeningPort) throws SocketException {
    latch = new CountDownLatch(1);
    listener = new SimpleListener();
    listener.setDaemon(true);
    listener.start();
    socket = new DatagramSocket(listeningPort);
    listener.go();
  }

  public abstract void onReceipt(DatagramPacket packet);

  public void send(PacketContent packetContent, InetSocketAddress dstAddress) {
    send(packetContent, dstAddress.getAddress(), dstAddress.getPort());
  }

  public void send(PacketContent packetContent, InetAddress dstAddress, int port) {
    try {
      DatagramPacket packet = packetContent.toDatagramPacket();
      setDestination(packet, dstAddress, port);
      socket.send(packet);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void setDestination(DatagramPacket packet, InetAddress dstAddress, int port) {
    packet.setAddress(dstAddress);
    packet.setPort(port);
  }

  abstract class Listener extends Thread {
    /*
     *  Telling the listener that the socket has been initialized
     */
    public void go() {
      latch.countDown();
    }
  }

  /**
   * SimpleListener thread
   *
   * <p>Listens for incoming packets on a datagram socket and informs registered receivers about
   * incoming packets.
   */
  class SimpleListener extends Listener {

    /*
     * Listen for incoming packets and inform receivers
     */
    @Override
    public void run() {
      try {
        latch.await();
        // Endless loop: attempt to receive packet, notify receivers, etc
        while (true) {
          DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
          socket.receive(packet);
          onReceipt(packet);
        }
      } catch (Exception e) {
        if (!(e instanceof SocketException)) e.printStackTrace();
      }
    }
  }

  @Override
  public String toString() {
    try {
      String ipAddress = String.format("IP Address : %s\n", InetAddress.getLocalHost().toString());
      String port = String.format("Port : %s\n", socket.getLocalPort());
      char[] line = new char[ipAddress.length() * 2];
      Arrays.fill(line, '-');
      String lineString = new String(line);
      return lineString + "\n" + ipAddress + port + lineString;
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return null;
  }
}
