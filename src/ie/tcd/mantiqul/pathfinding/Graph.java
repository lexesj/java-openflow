package ie.tcd.mantiqul.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * This class is used for path finding
 */
public class Graph {

  Map<String, Node> nodes;

  public Graph() {
    nodes = new HashMap<>();
  }

  /**
   * Gets a path from the start node to the end using a breadth first search algorithm
   *
   * @param start the starting node
   * @param end   the destination node
   * @return a list of nodes which indicates the path to take to reach destination node
   */
  public List<Node> getPathBFS(Node start, Node end) {
    Map<Node, Node> prev = new HashMap<>();
    Queue<Node> queue = new LinkedList<>();
    Set<Node> seen = new HashSet<>();
    queue.add(start);
    seen.add(start);
    while (!queue.isEmpty()) {
      Node curr = queue.poll();
      for (Node adjacent : curr.adjacentNodes) {
        if (!seen.contains(adjacent)) {
          prev.put(adjacent, curr);
          queue.add(adjacent);
          seen.add(adjacent);
        }
      }
    }
    return tracebackPath(end, prev);
  }

  /**
   * Gets the path to the destination
   *
   * @return the path a list
   */
  public List<Node> tracebackPath(Node end, Map<Node, Node> prevMap) {
    List<Node> path = new ArrayList<>();
    Node curr = end;
    while (curr != null) {
      path.add(curr);
      curr = prevMap.get(curr);
    }
    Collections.reverse(path);
    return path;
  }

  /**
   * Puts a node into the nodes hash map
   *
   * @param name the name of the node
   * @param node the node to put to the hash map
   */
  public void putNode(String name, Node node) {
    nodes.put(name, node);
  }

  /**
   * Gets the node with the associated name
   *
   * @param name the node's name
   * @return the node with the associated name
   */
  public Node getNode(String name) {
    return nodes.get(name);
  }


  /**
   * Gets the node if it exists else the default value
   *
   * @param name       the node's name
   * @param defaultVal the default node to return
   * @return the node with the associated name if it is in the hashmap else the default value
   */
  public Node getOrDefault(String name, Node defaultVal) {
    return nodes.getOrDefault(name, defaultVal);
  }

  /**
   * Class which represents nodes in a graph
   */
  public static class Node implements Comparable<Node> {

    List<Node> adjacentNodes;
    String name;

    public Node(String name) {
      this.name = name;
      adjacentNodes = new LinkedList<>();
    }

    /**
     * Add an adjacent node
     *
     * @param node node to add
     */
    public void adjacentAdd(Node node) {
      adjacentNodes.add(node);
    }

    /**
     * Tests for equality
     *
     * @param o the object to test for equality
     * @return true if this node is equal to o
     */
    @Override
    public boolean equals(Object o) {
      return this.compareTo((Node) o) == 0;
    }

    /**
     * Returns the name of the node
     *
     * @return the name of the node
     */
    public String getName() {
      return name;
    }

    /**
     * Compares two nodes
     *
     * @param node node to compare to
     * @return negative value if name is less than node to compare to, 0 if name is equal and
     * positive value if name is greater than node to compare to
     */
    @Override
    public int compareTo(Node node) {
      return name.compareTo(node.name);
    }

    @Override
    public String toString() {
      return name + adjacentNodes.toString();
    }
  }
}
