package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UnknownDestinationPacketContent extends PacketContent {

  private String destination;

  /**
   * Default constructor
   */
  public UnknownDestinationPacketContent(String destination) {
    type = UNKNOWN_DESTINATION;
    this.destination = destination;
  }

  /**
   * Constructs an unknown destination packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected UnknownDestinationPacketContent(ObjectInputStream oin) {
    try {
      type = UNKNOWN_DESTINATION;
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
    return "UNKNOWN_DESTINATION: " + destination;
  }

  /**
   * Getter for destination
   *
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }
}
