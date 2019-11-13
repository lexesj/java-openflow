package ie.tcd.mantiqul.node;

import java.net.DatagramPacket;
import java.net.SocketException;

public class Controller extends Node {


  Controller(int listeningPort) throws SocketException {
    super(listeningPort);
  }

  /**
   * Override method which handles the each type of packet received.
   *
   * @param packet The packet received
   */
  @Override
  public void onReceipt(DatagramPacket packet) {

  }
}
