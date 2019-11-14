package ie.tcd.mantiqul;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

/**
 * This class provides a terminal window that provides field for concurrent input from the user and
 * output to the user.
 */
public class Terminal {

  TerminalPanel panel;

  /**
   * This class provides the internal fields of the Terminal window, an output field, a label and an
   * input field. The output field can be used for the concurrent printing of Strings. The label is
   * used to print a prompt for the input expected in the input field. The input field can be used
   * to request a String as input from the user.
   */
  public class TerminalPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -4404241756143559030L;
    protected JTextField textField;
    protected JTextArea textArea;
    private static final String newline = "\n";
    private JLabel label;
    private String input;

    /** Create the panel. */
    public TerminalPanel() {
      super(new GridBagLayout());

      textField = new JTextField(30);
      textField.addActionListener(this);

      textArea = new JTextArea(10, 30);
      textArea.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(textArea);

      GridBagConstraints c1 = new GridBagConstraints();
      c1.gridx = 0;
      c1.gridy = 0;
      c1.gridwidth = GridBagConstraints.REMAINDER;
      c1.fill = GridBagConstraints.BOTH;
      c1.weightx = 1.0;
      c1.weighty = 1.0;
      add(scrollPane, c1);

      label = new JLabel("");
      GridBagConstraints c2 = new GridBagConstraints();
      c2.gridx = 0;
      c2.gridy = 1;
      add(label, c2);

      GridBagConstraints c3 = new GridBagConstraints();
      c3.fill = GridBagConstraints.HORIZONTAL;
      c3.gridy = 1;
      c3.gridx = 1;
      add(textField, c3);
    }

    public synchronized void actionPerformed(ActionEvent evt) {
      input = textField.getText();
      textField.selectAll();
      textField.setText("");
      textArea.setCaretPosition(textArea.getDocument().getLength());
      notify();
    }

    public void setPrompt(String prompt) {
      label.setText(prompt);
    }

    public void print(String output) {
      textArea.append(output);
    }

    public void println(String output) {
      textArea.append(output + newline);
    }

    public synchronized String read() {
      textField.setEditable(true);
      try {
        wait();
      } catch (Exception e) {
        e.printStackTrace();
      }
      textField.setEditable(false);
      return input;
    }

    public void clear() {
      textArea.setText("");
    }
  }

  /**
   * This constructor establishes a terminal window with a given name.
   *
   * @param name Name of the terminal window.
   */
  public Terminal(String name) {
    JFrame frame = new JFrame(name);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(panel = new TerminalPanel());
    frame.setVisible(true);
    frame.pack();
  }

  /**
   * This method prints a string to the output field.
   *
   * @param output String to print in the output field.
   */
  public void print(String output) {
    panel.print(output);
  }

  /**
   * This method prints a string to the output field, followed by a newline.
   *
   * @param output String to print in the output field.
   */
  public void println(String output) {
    panel.println(output);
  }

  /**
   * This method attempts to get an input from a user in the input field.
   *
   * @param prompt String to print before waiting for input
   * @return Input from the user
   */
  public synchronized String read(String prompt) {
    String input;
    panel.setPrompt(prompt);
    input = panel.read();
    panel.setPrompt("");
    return input;
  }

  /**
   * This method attempts to get an yes or no answer from a user
   *
   * @param prompt String to print before waiting for input
   * @return Input from the user
   */
  public synchronized boolean getBoolean(String prompt) {
    int reply = JOptionPane.showConfirmDialog(null, prompt, "Message", JOptionPane.YES_NO_OPTION);
    return reply == JOptionPane.YES_OPTION;
  }

  /**
   * This method clears the text from the terminal
   */
  public void clearText() {
    panel.clear();
  }
}
