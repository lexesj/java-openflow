package ie.tcd.mantiqul.packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PayloadPacketContent extends PacketContent {

  private String payload;
  private String destination;

  /**
   * Constructor which takes in the payload and destination
   *
   * @param payload the payload to be sent
   * @param destination the destination of the payload
   */
  public PayloadPacketContent(String payload, String destination) {
    type = PAYLOAD_PACKET;
    this.payload = payload;
    this.destination = destination;
  }

  /**
   * Constructs an ack packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected PayloadPacketContent(ObjectInputStream oin) {
    try {
      type = PAYLOAD_PACKET;
      payload = oin.readUTF();
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
    return "PAYLOAD_PACKET payload: " + payload + ", destination: " + destination;
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
}
