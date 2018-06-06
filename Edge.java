import java.util.Objects;

/**
 * This class represents the roads.
 *
 * @author Zixu (Shawn) Chen, Zixi Pan
 * @version 1.1
 * @date 2017-12-07
 */
public class Edge implements Comparable<Edge> {

  //Earth's radium in kilometers:
  private final double R = 6372.8;
  private String road;
  private Vertex intersectionOne;
  private Vertex intersectionTwo;
  private double distance;

  public Edge(String road, Vertex intersectionOne, Vertex intersectionTwo) {
    this.road = road;
    this.intersectionOne = intersectionOne;
    this.intersectionTwo = intersectionTwo;
    this.distance = calculateDistance(this.intersectionOne, this.intersectionTwo);
  }

  public String getRoad() {
    return road;
  }

  public void setRoad(String road) {
    this.road = road;
  }

  public Vertex getIntersectionOne() {
    return intersectionOne;
  }

  public void setIntersectionOne(Vertex intersectionOne) {
    this.intersectionOne = intersectionOne;
    //Update distance:
    this.distance = calculateDistance(this.intersectionOne, this.intersectionTwo);
  }

  public Vertex getIntersectionTwo() {
    return intersectionTwo;
  }

  public void setIntersectionTwo(Vertex intersectionTwo) {
    this.intersectionTwo = intersectionTwo;
    //Update distance:
    this.distance = calculateDistance(this.intersectionOne, this.intersectionTwo);
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  private double calculateDistance(Vertex intersectionOne, Vertex intersectionTwo) {
    //TODO: calculate the distance and scaling factor
    return haversine(intersectionOne.getLatitude(), intersectionOne.getLongitude(),
        intersectionTwo.getLatitude(), intersectionTwo.getLongitude());
  }

  /**
   * This method is used to get the distance between two points on the map
   * Taken from: https://rosettacode.org/wiki/Haversine_formula#Java
   *
   * @param lat1 point one's latitude
   * @param lon1 point one's longitude
   * @param lat2 point two's latitude
   * @param lon2 point two's longitude
   * @return the distance between two points
   */
  private double haversine(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    double a =
        Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math
            .cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return R * c;
  }

  @Override
  public int compareTo(Edge o) {
    return (int) (this.distance - o.distance);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Edge edge = (Edge) o;
    return Double.compare(edge.R, R) == 0 &&
        Double.compare(edge.distance, distance) == 0 &&
        Objects.equals(road, edge.road) &&
        Objects.equals(intersectionOne, edge.intersectionOne) &&
        Objects.equals(intersectionTwo, edge.intersectionTwo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(R, road, intersectionOne, intersectionTwo, distance);
  }
}
