package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents hello packets
 */
public class HelloPacketContent extends PacketContent {

  private double versionNumber;

  /**
   * Constructor that takes in information about an acknowledgement.
   */
  public HelloPacketContent() {
    type = HELLO_PACKET;
    versionNumber = 1.0;
  }

  /**
   * Constructs an ack packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected HelloPacketContent(ObjectInputStream oin) {
    try {
      type = HELLO_PACKET;
      versionNumber = oin.readDouble();
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
      oout.writeDouble(versionNumber);
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
    return "HELLO_PACKET ver: " + versionNumber;
  }

  /**
   * Returns the version number
   *
   * @return Returns the version number
   */
  public double getVersionNumber() {
    return versionNumber;
  }
}
