package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UnknownDestinationPacketContent extends PacketContent {

  /**
   * Default constructor
   */
  public UnknownDestinationPacketContent() {
    type = UNKNOWN_DESTINATION;
  }

  /**
   * Constructs an unknown destination packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected UnknownDestinationPacketContent(ObjectInputStream oin) {
    type = UNKNOWN_DESTINATION;
  }

  /**
   * Writes the content into an ObjectOutputStream
   *
   * @param oout The object output stream to write to
   */
  protected void toObjectOutputStream(ObjectOutputStream oout) {
  }

  /**
   * Returns the content of the packet as String.
   *
   * @return Returns the content of the packet as String.
   */
  public String toString() {
    return "UNKNOWN_DESTINATION";
  }
}
