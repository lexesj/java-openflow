package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Class for packet content that represents feature request packets */
public class FeatureRequestPacketContent extends PacketContent {

  /** Constructor which sets the packet type. */
  public FeatureRequestPacketContent() {
    type = FEATURE_REQUEST;
  }

  /**
   * Constructs an feature request packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected FeatureRequestPacketContent(ObjectInputStream oin) {
    try {
      type = FEATURE_REQUEST;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes the content into an ObjectOutputStream
   *
   * @param oout The object output stream to write to
   */
  protected void toObjectOutputStream(ObjectOutputStream oout) {}

  /**
   * Returns the content of the packet as String.
   *
   * @return Returns the content of the packet as String.
   */
  public String toString() {
    return "FEATURE_REQUEST";
  }
}
