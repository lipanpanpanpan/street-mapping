import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents the intersections.
 *
 * @author Zixu (Shawn) Chen, Zixi Pan
 * @version 1.1
 * @date 2017-12-07
 */
public class Vertex implements Comparable<Vertex> {

  private String intersection;
  private double latitude;
  private double longitude;
  private boolean isVisited;
  private Vertex previous;
  private double distanceToStart;
  private final Map<String, Edge> edges;

  public Vertex(String intersection, double latitude, double longitude) {
    this.intersection = intersection;
    this.latitude = latitude;
    this.longitude = longitude;
    this.isVisited = false;
    this.previous = null;
    this.distanceToStart = Double.POSITIVE_INFINITY;
    this.edges = new HashMap<>();
  }

  public void addEdge(String name, Vertex vertex) {
    edges.put(vertex.getIntersection(), new Edge(name, this, vertex));
  }

  public String getIntersection() {
    return intersection;
  }

  public void setIntersection(String intersection) {
    this.intersection = intersection;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public boolean isVisited() {
    return isVisited;
  }

  public void setVisited(boolean visited) {
    isVisited = visited;
  }

  public Vertex getPrevious() {
    return previous;
  }

  public void setPrevious(Vertex previous) {
    this.previous = previous;
  }

  public double getDistanceToStart() {
    return distanceToStart;
  }

  public void setDistanceToStart(double distanceToStart) {
    this.distanceToStart = distanceToStart;
  }

  public Map<String, Edge> getEdges() {
    return edges;
  }

  @Override
  public int compareTo(Vertex o) {
    return (int) (this.distanceToStart - o.distanceToStart);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vertex vertex = (Vertex) o;
    return Double.compare(vertex.latitude, latitude) == 0 &&
        Double.compare(vertex.longitude, longitude) == 0 &&
        isVisited == vertex.isVisited &&
        Double.compare(vertex.distanceToStart, distanceToStart) == 0 &&
        Objects.equals(intersection, vertex.intersection) &&
        Objects.equals(previous, vertex.previous) &&
        Objects.equals(edges, vertex.edges);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(intersection, latitude, longitude, isVisited, previous, distanceToStart, edges);
  }
}
