package ie.tcd.mantiqul.node;

import ie.tcd.mantiqul.Terminal;
import ie.tcd.mantiqul.command.Tokenizer;
import ie.tcd.mantiqul.packet.PacketContent;
import ie.tcd.mantiqul.packet.PayloadPacketContent;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class EndNode extends Node {

  private static final String PREFIX = "> ";
  private static final String MESSAGE_USAGE = "Usage: message send <\"message\"> <destination>";
  private static final String HELP_MESSAGE = "The following commands are available:\n"
      + "    - clear - Clears the terminal screen\n"
      + "    - message send <\"message\"> <destination>\n";
  private final String DEFAULT_SWITCH;

  private Terminal terminal;

  EndNode(int listeningPort, String name, String defaultRouter) throws SocketException {
    super(listeningPort);
    terminal = new Terminal(name);
    DEFAULT_SWITCH = defaultRouter;
  }

  public void start() {
    terminal.println("Type \"help\" for a list of commands");
    while (true) {
      String commandString = terminal.read(PREFIX);
      terminal.println(PREFIX + commandString);
      String[] tokens = new Tokenizer(commandString).tokens;
      String command = tokens[0];
      String[] args = Tokenizer.splice(tokens, 1, tokens.length);
      executeCommand(command, args);
    }
  }

  /**
   * Override method which handles the each type of packet received.
   *
   * @param packet The packet received
   */
  @Override
  public void onReceipt(DatagramPacket packet) {
    PacketContent packetContent = PacketContent.fromDatagramPacket(packet);
    switch (packetContent.type) {
      case PacketContent.PAYLOAD_PACKET:
        PayloadPacketContent payloadPacketContent = (PayloadPacketContent) packetContent;
        terminal.println("Received message '" + payloadPacketContent.getPayload() + "'");
        break;
      default:
        terminal.println("Unknown packet received");
    }
//    terminal.println("---------------------------------------------------------------------------");
//    terminal.println(packetContent.toString());
//    terminal.println("---------------------------------------------------------------------------");
  }

  /**
   * Execute a command from the terminal
   *
   * @param command the command to run
   * @param args    the command's arguments
   */
  private void executeCommand(String command, String[] args) {
    switch (command) {
      case "message":
        if (args.length < 3) {
          terminal.println(MESSAGE_USAGE);
        } else if ("send".equals(args[0])) {
          String message = args[1];
          String destination = args[2];
          send(new PayloadPacketContent(message, DEFAULT_SWITCH, destination),
              new InetSocketAddress(DEFAULT_SWITCH, DEFAULT_PORT));
        }
        break;
      case "clear":
        terminal.clearText();
        break;
      case "help":
        terminal.println(HELP_MESSAGE);
        break;
      default:
        terminal.println("Command '" + command + "' was not found");
    }
  }

  public static void main(String[] args) throws SocketException {
    if (args.length < 2) {
      System.out.println("Usage: java ie.tcd.mantiqul.EndNode <name> <default router>");
      return;
    }
    (new EndNode(DEFAULT_PORT, args[0], args[1])).start();
  }
}
