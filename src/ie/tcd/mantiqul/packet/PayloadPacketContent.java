package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PayloadPacketContent extends PacketContent {

  private String payload;
  private String switchName;
  private String destination;

  /**
   * Constructor which takes in the payload and destination
   *
   * @param payload the payload to be sent
   * @param switchName the name of the switch the payload was sent from
   * @param destination the destination of the payload
   */
  public PayloadPacketContent(String payload, String switchName, String destination) {
    type = PAYLOAD_PACKET;
    this.payload = payload;
    this.switchName = switchName;
    this.destination = destination;
  }

  /**
   * Constructs an payload packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected PayloadPacketContent(ObjectInputStream oin) {
    try {
      type = PAYLOAD_PACKET;
      payload = oin.readUTF();
      switchName = oin.readUTF();
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
      oout.writeUTF(payload);
      oout.writeUTF(switchName);
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
    return "PAYLOAD_PACKET payload: " + payload + ", switch name: " + switchName + ", destination: " + destination;
  }

  /**
   * Returns the payload string
   *
   * @return the payload string
   */
  public String getPayload() {
    return payload;
  }

  /**
   * Returns the destination
   *
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Returns the switch name
   *
   * @return the switch name
   */
  public String getSwitchName() {
    return switchName;
  }

  /**
   * Sets the switch name
   */
  public void setSwitchName(String switchName) {
    this.switchName = switchName;
  }

}
