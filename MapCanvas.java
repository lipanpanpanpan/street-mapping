import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

/**
 * This class is used to draw the map
 *
 * @author Zixu (Shawn) Chen, Zixi Pan
 * @version 1.1
 * @date 2017-12-08
 */
public class MapCanvas extends JComponent {

  private Graph graph;
  private List<Edge> edges;
  private List<String> shortestPathResult;

  public MapCanvas(Graph graph, List<Edge> edges, List<String> shortestPathResult) {
    this.graph = graph;
    this.edges = edges;
    this.shortestPathResult = shortestPathResult;
  }

  public MapCanvas(Graph graph, List<Edge> edges) {
    this.graph = graph;
    this.edges = edges;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    //Get extreme coordinates:
    double[] coordinates = findExtremeCoordinates(graph);
    double upMost = coordinates[0];
    //double downMost = coordinates[1];
    double leftMost = coordinates[2];
    //double rightMost = coordinates[3];

    //Get the scale:
    double[] scales = getScale(coordinates);
    double yScale = scales[0];
    double xScale = scales[1];

    //Get the windows' width and height:
    double width = getWidth() - 25;
    double height = getHeight() - 25;

    //Graphics2D to enable change thickness by setting stroke:
    Graphics2D g2 = (Graphics2D) g;

    //Draw the graph:
    for (Edge edge : edges) {
      double y1 = (upMost - edge.getIntersectionOne().getLatitude()) / yScale * height;
      double x1 = (leftMost - edge.getIntersectionOne().getLongitude()) / xScale * width;
      double y2 = (upMost - edge.getIntersectionTwo().getLatitude()) / yScale * height;
      double x2 = (leftMost - edge.getIntersectionTwo().getLongitude()) / xScale * width;
      g2.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    if (shortestPathResult != null) {
      //Draw the shortest path:
      g2.setColor(Color.RED);
      g2.setStroke(new BasicStroke(3));

      for (int i = 0; i < shortestPathResult.size(); i++) {
        Map<String, Vertex> vertices = graph.getVertices();
        //Prevent going over the index:
        if ((i + 1) == shortestPathResult.size()) {
          break;
        }
        Vertex vertex1 = vertices.get(shortestPathResult.get(i));
        Vertex vertex2 = vertices.get(shortestPathResult.get(i + 1));
        double y1 = (upMost - vertex1.getLatitude()) / yScale * height;
        double x1 = (leftMost - vertex1.getLongitude()) / xScale * width;
        double y2 = (upMost - vertex2.getLatitude()) / yScale * height;
        double x2 = (leftMost - vertex2.getLongitude()) / xScale * width;
        g2.draw(new Line2D.Double(x1, y1, x2, y2));
      }
    }
  }

  private double[] getScale(double[] coordinates) {
    double yScale = coordinates[0] - coordinates[1];
    double xScale = coordinates[2] - coordinates[3];
    return new double[]{yScale, xScale};
  }

  private double[] findExtremeCoordinates(Graph graph) {
    Map<String, Vertex> vertices = graph.getVertices();
    //Latitude is associated with up / down. The higher the latitude is, the larger is its up value:
    double upMost = Double.NEGATIVE_INFINITY;
    double downMost = Double.POSITIVE_INFINITY;

    //Longitude is associated with left / right. In this case, we are in the west hemisphere and use
    //negative longitude. Therefore, the lefter the position is, the lower its longitude is.
    double leftMost = Double.POSITIVE_INFINITY;
    double rightMost = Double.NEGATIVE_INFINITY;

    //Array to contain the result:
    double[] coordinates = new double[4];

    //Compare to get the extreme coordinates:
    for (String key : vertices.keySet()) {
      Vertex vertex = vertices.get(key);
      if (vertex.getLatitude() > upMost) {
        upMost = vertex.getLatitude();
      }
      if (vertex.getLatitude() < downMost) {
        downMost = vertex.getLatitude();
      }
      if (vertex.getLongitude() < leftMost) {
        leftMost = vertex.getLongitude();
      }
      if (vertex.getLongitude() > rightMost) {
        rightMost = vertex.getLongitude();
      }
    }
    coordinates[0] = upMost;
    coordinates[1] = downMost;
    coordinates[2] = leftMost;
    coordinates[3] = rightMost;

    return coordinates;
  }

}
