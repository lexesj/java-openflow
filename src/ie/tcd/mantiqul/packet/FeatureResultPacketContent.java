package ie.tcd.mantiqul.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/** Class for packet content that represents feature request packets */
public class FeatureResultPacketContent extends PacketContent {
  int num_buffers;
  int num_tables;
  String switchName;
  List<String> connections;

  /**
   * Constructor which sets the packet type.
   *
   * @param num_buffers the max number of packets the router can queue
   * @param num_tables the number of tables a router has
   * @param connections ip addresses of the router's connections
   */
  public FeatureResultPacketContent(int num_buffers, int num_tables, String switchName, List<String> connections) {
    type = FEATURE_RESULT;
    this.num_buffers = num_buffers;
    this.num_tables = num_tables;
    this.switchName  = switchName;
    this.connections = connections;
  }

  /**
   * Constructs an feature result packet out of a datagram packet.
   *
   * @param oin The received packet as an object input stream
   */
  protected FeatureResultPacketContent(ObjectInputStream oin) {
    try {
      type = FEATURE_RESULT;
      num_buffers = oin.readInt();
      num_tables = oin.readInt();
      switchName = oin.readUTF();
      int num_connections = oin.readInt();
      connections = new ArrayList<>();
      for (int i = 0; i < num_connections; i++) {
        connections.add(oin.readUTF());
      }
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
      oout.writeInt(num_buffers);
      oout.writeInt(num_tables);
      oout.writeUTF(switchName);
      oout.writeInt(connections.size());
      for (String connection : connections) {
        oout.writeUTF(connection);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the content of the packet as String.
   *
   * @return Returns the content of the packet as String.
   */
  public String toString() {
    StringBuilder result =
        new StringBuilder(
            "FEATURE_RESULT:\nQueue Max: "
                + num_buffers
                + "\nNum Tables: "
                + num_tables
                + "\nSwitch Name: "
                + switchName
                + "\nConnections: ");
    for (String connection : connections) result.append(connection).append(" ");
    return result.toString();
  }

  /**
   * Returns switch name
   *
   * @return switch name
   */
  public String getSwitchName() {
    return switchName;
  }

  /**
   * Returns switch's connections
   *
   * @return the switch's connections
   */
  public List<String> getConnections() {
    return connections;
  }

}
