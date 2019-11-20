package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FlowModPacketContent extends PacketContent {

  private String nextHop;
  private String destination;

  public FlowModPacketContent(String nextHop, String destination) {
    type = FLOW_MOD_PACKET;
    this.nextHop = nextHop;
    this.destination = destination;
  }

  /**
   * Constructs an flow mod packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected FlowModPacketContent(ObjectInputStream oin) {
    try {
      type = FLOW_MOD_PACKET;
      nextHop = oin.readUTF();
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
      oout.writeUTF(nextHop);
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
    return "FLOW_MOD_PACKET next hop: " + nextHop + " destination: " + destination;
  }

  /**
   * Returns the next hop
   *
   * @return the next hop
   */
  public String getNextHop() {
    return nextHop;
  }

  /**
   * Returns the next destination
   *
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }
}
