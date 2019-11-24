package ie.tcd.mantiqul.command;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to tokenize strings using space as a delimiter. It will also tokenize quotes
 * similar to passing arguments to a Java program.
 */
public class Tokenizer {
  public final String[] tokens;

  public Tokenizer(String string) {
    tokens = tokenize(string);
  }

  /**
   * Tokenize a string using space as a delimiter.
   *
   * @param string string to tokenize
   * @return a token array
   */
  private String[] tokenize(String string) {
    String trimmed = string.trim();
    List<String> tokens = new LinkedList<>();
    Pattern pattern = Pattern.compile("\"(.*)\"|\\S+");
    Matcher matcher = pattern.matcher(trimmed);
    while (matcher.find()) {
      if (matcher.group(1) != null) tokens.add(matcher.group(1));
      else tokens.add(matcher.group());
    }
    return tokens.toArray(new String[0]);
  }

  /**
   * Splices an array and returns a sub-array
   *
   * @param start start index
   * @param end end index inclusive
   * @return the sub-array containing elements from the start to the end (inclusive) index
   */
  public String[] splice(int start, int end) {
    if (start > end) return null;
    else if (end == start) return tokens;
    String[] result = new String[end - start];
    System.arraycopy(tokens, start, result, 0, result.length);
    return result;
  }
}
