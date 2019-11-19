package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketInPacketContent extends PacketContent {
  private String destination;

  /**
   * Constructor which takes in a payload packet
   *
   * @param payload the payload to be sent
   */
  public PacketInPacketContent(PayloadPacketContent payload) {
    type = PACKET_IN_PACKET;
    destination = payload.getDestination();
  }

  /**
   * Constructs an packet in packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected PacketInPacketContent(ObjectInputStream oin) {
    try {
      type = PACKET_IN_PACKET;
      destination = oin.readUTF();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the content into an ObjectOutputStream
   *
   * @param oout The object output stream to write to
   */
  protected void toObjectOutputStream(ObjectOutputStream oout) {
    try {
      oout.writeUTF(destination);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the content of the packet as String.
   *
   * @return Returns the content of the packet as String.
   */
  public String toString() {
    return "PACKET_IN_PACKET: destination: " + destination;
  }

  /**
   * Returns the destination
   *
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }
}
