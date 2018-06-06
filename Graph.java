import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This is the graph class.
 *
 * @author Zixu (Shawn) Chen, Zixi Pan
 * @version 1.1
 * @date 2017-12-07
 */
public class Graph {

  private final Map<String, Vertex> vertices;
  private final List<Edge> edges;
  private PriorityQueue<Vertex> pQueue;

  public Graph() {
    this.vertices = new HashMap<>();
    this.edges = new ArrayList<>();
  }

  public void addVertex(String intersection, double latitude, double longitude) {
    //Handle vertex already exists situation:
    if (vertices.containsKey(intersection)) {
      System.out.println("Vertex is already in the graph! Please enter a different one!");
      return;
    }
    vertices.put(intersection, new Vertex(intersection, latitude, longitude));
  }

  public void addEdge(String road, String intersectionOne, String intersectionTwo) {
    //Handle vertex does not exist situation:
    if (!vertices.containsKey(intersectionOne) || !vertices.containsKey(intersectionTwo)) {
      System.out.println("At least one of the vertex does not exist! Please check your input!");
      return;
    }
    Vertex vertexOne = vertices.get(intersectionOne);
    Vertex vertexTwo = vertices.get(intersectionTwo);
    vertexOne.addEdge(road, vertexTwo);
    vertexTwo.addEdge(road, vertexOne);
    edges.add(new Edge(road, vertexOne, vertexTwo));
  }

  public List<String> getShortestPath(String intersectionOne, String intersectionTwo) {
    List<String> result = new ArrayList<>();
    pQueue = new PriorityQueue<>();

    //Initialize all the distances to infinity and put all the vertices into the priority queue:
    for (String vertex : vertices.keySet()) {
      vertices.get(vertex).setDistanceToStart(Double.POSITIVE_INFINITY);
      vertices.get(vertex).setVisited(false);
      vertices.get(vertex).setPrevious(null);
    }

    //Change the starting point distance to zero:
    Vertex vertex = vertices.get(intersectionOne);
    vertex.setDistanceToStart(0);
    pQueue.add(vertex);

    //Mark starting point as visited:
    vertex.setVisited(true);

    //Build all the shortest paths:
    while (!pQueue.isEmpty()) {
      //Find the vertex that has the smallest distance to the starting point:
      Vertex minDistVertex = pQueue.poll();

      //Mark it as visited:
      minDistVertex.setVisited(true);
      Map<String, Edge> edges = minDistVertex.getEdges();
      for (String key : edges.keySet()) {
        relax(edges.get(key));
      }
    }

    //Put the results into the list:
    Vertex destination = vertices.get(intersectionTwo);
    while (destination.getPrevious() != null) {
      result.add(destination.getIntersection());
      destination = destination.getPrevious();
    }
    Collections.reverse(result);

    return result;
  }

  private void relax(Edge edge) {
    Vertex home = edge.getIntersectionOne();
    Vertex neighbor = edge.getIntersectionTwo();
    if (neighbor.getDistanceToStart() > home.getDistanceToStart() + edge.getDistance()) {
      neighbor.setDistanceToStart(home.getDistanceToStart() + edge.getDistance());
      neighbor.setPrevious(home);
      pQueue.remove(neighbor);
      pQueue.add(neighbor);
    }
    //If the vertex is not in the priority queue, add it to the priority queue:
    if (!neighbor.isVisited() && !pQueue.contains(neighbor)) {
      pQueue.add(neighbor);
    }
  }

  public Map<String, Vertex> getVertices() {
    return vertices;
  }

  public List<Edge> getEdges() {
    return edges;
  }
}
