package ie.tcd.mantiqul.packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The class is the basis for packet contents of various types.
 */
public abstract class PacketContent {

  public static final int HELLO_PACKET = 100;

  public int type = 0;
  public int size;

  /**
   * Constructs an object out of a datagram packet.
   *
   * @param packet Packet to analyse.
   */
  public static PacketContent fromDatagramPacket(DatagramPacket packet) {
    PacketContent content = null;

    try {
      int type;

      byte[] data;
      ByteArrayInputStream bin;
      ObjectInputStream oin;

      data = packet.getData(); // use packet content as seed for stream
      bin = new ByteArrayInputStream(data);
      oin = new ObjectInputStream(bin);

      type = oin.readInt(); // read type from beginning of packet

      switch (type) { // depending on type create content object
        case HELLO_PACKET:
          content = new HelloPacketContent();
          break;
      }
      if (content != null)
        content.size = data.length;
      oin.close();
      bin.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return content;
  }

  /**
   * This method is used to transform content into an output stream.
   *
   * @param out Stream to write the content for the packet to.
   */
  protected abstract void toObjectOutputStream(ObjectOutputStream out);

  /**
   * Returns the content of the object as DatagramPacket.
   *
   * @return Returns the content of the object as DatagramPacket.
   */
  public DatagramPacket toDatagramPacket() {
    DatagramPacket packet = null;
    byte[] data = toByteArray();
    packet = new DatagramPacket(data, data.length); // create packet from byte array
    size = data.length;

    return packet;
  }

  /**
   * Returns the content of the packet as String.
   *
   * @return Returns the content of the packet as String.
   */
  public abstract String toString();

  /**
   * Returns the type of the packet.
   *
   * @return Returns the type of the packet.
   */
  public int getType() {
    return type;
  }

  /**
   * Returns the size of the packet.
   *
   * @return Returns the size of the packet.
   */
  public int getSize() {
    return size;
  }

  public byte[] toByteArray() {
    try {
      ByteArrayOutputStream bout;
      ObjectOutputStream oout;
      byte[] data;

      bout = new ByteArrayOutputStream();
      oout = new ObjectOutputStream(bout);

      oout.writeInt(type); // write type to stream
      toObjectOutputStream(oout); // write content to stream depending on type

      oout.flush();
      data = bout.toByteArray(); // convert content to byte array

      oout.close();
      bout.close();
      return data;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
