import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class is used to build the user interface
 *
 * @author Zixu (Shawn) Chen, Zixi Pan
 * @version 1.1
 * @date 2017-12-08
 */
public class Frame extends JFrame implements ActionListener {

  private Graph graph;
  private List<Edge> edges;
  private List<String> shortestPathResult;

  private MapCanvas map;
  private JPanel input;
  private JLabel originLabel;
  private JLabel destinationLabel;
  private JLabel distanceLabel;
  private JButton findPath;
  private JTextField origin;
  private JTextField destination;

  public Frame(String filename, String start, String end) {
    //Initialize GUI:
    init(filename, true);

    //Print the path:
    getAndPrintPath(start, end);

    //Use the data from the previous method to draw the map:
    map = new MapCanvas(graph, edges, shortestPathResult);
    //Add the map panel to the frame:
    add(map, BorderLayout.CENTER);
  }

  public Frame(String filename) {
    //Initialize GUI:
    init(filename, false);

    //Use the data from the previous method to draw the map:
    map = new MapCanvas(graph, edges);
    //Add the map panel to the frame:
    add(map, BorderLayout.CENTER);
  }

  private void init(String filename, boolean hasDirection) {
    //Initialize JFrame:
    setLayout(new BorderLayout());
    setTitle("Street Mapping - " + filename);
    setSize(800, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    if (hasDirection) {
      //Initialize all the components:
      originLabel = new JLabel("Enter your origin:");
      destinationLabel = new JLabel("Enter your destination:");
      distanceLabel = new JLabel("Distance: N/A");
      findPath = new JButton("Find Path");
      findPath.addActionListener(this);
      origin = new JTextField();
      destination = new JTextField();

      //Add all the components on the JPanel:
      input = new JPanel(new GridLayout(2, 3));
      input.add(originLabel);
      input.add(destinationLabel);
      input.add(distanceLabel);
      input.add(origin);
      input.add(destination);
      input.add(findPath);

      //Add the input panel to the frame:
      add(input, BorderLayout.SOUTH);
    }

    //Create the graph:
    createGraph(filename);
  }

  private void createGraph(String filename) {

    try {
      BufferedReader bReader = new BufferedReader(new FileReader(filename));
      String currentLine;

      //Create the graph:
      this.graph = new Graph();

      //Add vertices and edges to the graph:
      String[] input;
      while ((currentLine = bReader.readLine()) != null) {
        input = currentLine.split("\\s+");
        //It's a intersection:
        if (input[0].equals("i")) {
          graph.addVertex(input[1], Double.parseDouble(input[2]), Double.parseDouble(input[3]));
        }
        //It's a road:
        else {
          graph.addEdge(input[1], input[2], input[3]);
        }
      }
      this.edges = graph.getEdges();

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String starting = origin.getText();
    String ending = destination.getText();
    getAndPrintPath(starting, ending);

    //Remove the old map first:
    remove(map);

    //Then add a new map:
    map = new MapCanvas(graph, edges, shortestPathResult);
    add(map, BorderLayout.CENTER);
  }

  private void getAndPrintPath(String start, String end) {

    //Get and set shortest path:
    this.shortestPathResult = graph.getShortestPath(start, end);

    //Print the path route and the distance:
    if (shortestPathResult.isEmpty()) {
      System.out.println("Path route: not found.");
      System.out.println("Path distance: N/A");
      distanceLabel.setText("Distance: N/A");
    } else {
      //Initialize distance to 0:
      double distance = 0;
      //Print the start of the route and add its distance to the second intersection:
      shortestPathResult.add(0, graph.getVertices().get(start).getIntersection());
      System.out.print("Path route: " + shortestPathResult.get(0));
      distance += graph.getVertices().get(shortestPathResult.get(0)).getEdges()
          .get(shortestPathResult.get(1)).getDistance();
      for (int i = 1; i < shortestPathResult.size(); i++) {
        System.out.print(" -> " + shortestPathResult.get(i));
        if (i + 1 >= shortestPathResult.size()) {
          continue;
        }
        distance += graph.getVertices().get(shortestPathResult.get(i)).getEdges()
            .get(shortestPathResult.get(i + 1)).getDistance();
      }
      System.out.println();
      //Convert the distance from kilometer to mile and print:
      System.out.printf("Path distance: %.2f miles%n%n", distance / 1.6);
      distanceLabel.setText("Distance: " + distance / 1.6 + " miles");
    }
  }
}
