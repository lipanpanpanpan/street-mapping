import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This class is the main class of this program
 *
 * @author Zixu (Shawn) Chen, Zixi Pan
 * @version 1.1
 * @date 2017-12-08
 */
public class StreetMap {

  public static void main(String[] args) {

    if (args.length == 5 && args[1].equals("--show") && args[2].equals("--directions")) {
      Frame frame = new Frame(args[0], args[3], args[4]);
      frame.setVisible(true);
    } else if (args.length == 2 && args[1].equals("--show")) {
      Frame frame = new Frame(args[0]);
      frame.setVisible(true);
    } else if (args.length == 4 && args[1].equals("--directions")) {
      createGraph(args[0], args[2], args[3]);
    } else {
      System.out.println("Invalid input format! Program exited.");
    }
  }

  private static void createGraph(String filename, String start, String end) {
    try {
      BufferedReader bReader = new BufferedReader(new FileReader(filename));
      String currentLine;

      //Create the graph:
      Graph graph = new Graph();

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

      getAndPrintPath(graph, start, end);

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private static void getAndPrintPath(Graph graph, String start, String end) {

    //Get and set shortest path:
    List<String> shortestPathResult = graph.getShortestPath(start, end);

    //Print the path route and the distance:
    if (shortestPathResult.isEmpty()) {
      System.out.println("Path route: not found.");
      System.out.println("Path distance: N/A");
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
      System.out.printf("Path distance: %.2f miles%n", distance / 1.6);
    }
  }

}
